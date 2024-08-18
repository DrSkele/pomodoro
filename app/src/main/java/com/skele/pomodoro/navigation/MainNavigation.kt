package com.skele.pomodoro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skele.pomodoro.ui.task.input.TaskInputScreen
import com.skele.pomodoro.ui.timer.TimerScreen

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    NavHost(navController = navController, startDestination = TaskTimer){
        composable<TaskTimer>{
            TimerScreen()
        }
        composable<TaskInput> { backStackEntry ->
            val taskInput = backStackEntry.toRoute<TaskInput>()
            TaskInputScreen(taskId = taskInput.taskId)
        }
    }
}