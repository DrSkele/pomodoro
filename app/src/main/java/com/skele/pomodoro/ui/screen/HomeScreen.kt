package com.skele.pomodoro.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import com.skele.pomodoro.TimerState
import com.skele.pomodoro.data.model.TaskWithDailyRecord
import com.skele.pomodoro.service.TimerService
import com.skele.pomodoro.ui.screen.list.ListScreen
import com.skele.pomodoro.ui.screen.record.RecordScreen
import com.skele.pomodoro.ui.screen.timer.TimerScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    timerService: TimerService,
    currentTask: TaskWithDailyRecord
){
    val pagerState = rememberPagerState { 3 }

    HorizontalPager(
        state = pagerState
    ) {page ->
        when(page){
            0 -> TimerScreen(
                timer = timerService.timerState,
                taskData = currentTask
            ) {

            }
            1 -> ListScreen()
            2 -> RecordScreen()
        }

    }
}
