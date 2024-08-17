package com.skele.pomodoro.ui.timer.state

import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskTimerStateManager
    @Inject
    constructor(
        private val timerStateManager: TimerStateManager,
        private val taskRepository: TaskRepository,
    ) {
        private val _taskTimerState = MutableStateFlow<TaskTimerState>(TaskTimerState.NoTask)
        val taskTimerState: StateFlow<TaskTimerState> = _taskTimerState.asStateFlow()

        private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        private var taskCollectorJob: Job? = null

        init {
            coroutineScope.launch {
                timerStateManager.timerState
                    .takeWhile { _taskTimerState.value is TaskTimerState.HasTask }
                    .collect { timerState ->
                        val state = _taskTimerState.value as TaskTimerState.HasTask
                        _taskTimerState.value =
                            when (timerState) {
                                // When timer is finished, update pomodoro state
                                is TimerState.Finished ->
                                    state.copy(
                                        timerState = timerState,
                                        pomodoroState = state.pomodoroState.getNext(state.taskInfo.task.breakInterval),
                                    )

                                else -> state.copy(timerState = timerState)
                            }
                    }
            }
        }

        fun setTask(taskInfo: TaskWithDailyRecord) {
            timerStateManager.setTimer(taskInfo.task.workTime)
            _taskTimerState.value =
                TaskTimerState.HasTask(
                    taskInfo,
                    timerStateManager.timerState.value,
                    PomodoroState.Working(0),
                )
            taskCollectorJob?.cancel()
            taskCollectorJob =
                coroutineScope.launch {
                    taskRepository
                        .getTaskWithDailyRecord(taskInfo.task.id)
                        .takeWhile { _taskTimerState.value is TaskTimerState.HasTask }
                        .collect { taskInfo ->
                            if (taskInfo != null) {
                                _taskTimerState.value =
                                    (_taskTimerState.value as TaskTimerState.HasTask).copy(taskInfo = taskInfo)
                            } else {
                                _taskTimerState.value = TaskTimerState.NoTask
                            }
                        }
                }
        }

        fun startTimer() {
            timerStateManager.start()
        }

        fun pauseTimer() {
            timerStateManager.pause()
        }

        fun stopTimer() {
            timerStateManager.stop()
        }

        fun onDestory() {
            coroutineScope.cancel()
        }

        private fun saveRecord() {
        }
    }
