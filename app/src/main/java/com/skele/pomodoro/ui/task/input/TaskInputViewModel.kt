package com.skele.pomodoro.ui.task.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.data.repository.TaskRepository
import com.skele.pomodoro.ui.timer.state.UserAction
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskInputViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    fun onAction(action: UserAction){
        when(action){
            is TaskInputAction.Submit -> saveTask(action.task)
        }
    }

    private fun saveTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task)
        }
    }
}