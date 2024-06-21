package com.skele.pomodoro.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.skele.pomodoro.ListDestination
import com.skele.pomodoro.MainViewModel
import com.skele.pomodoro.RecordDestination
import com.skele.pomodoro.TaskInputDestination
import com.skele.pomodoro.TimerDestination
import com.skele.pomodoro.bottomNavDestinations
import com.skele.pomodoro.data.TaskRepository
import com.skele.pomodoro.data.model.Task

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(null) {
        viewModel.loadHighestPriority()
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if(bottomNavDestinations.any{it.route == currentDestination?.route}){
                //material3 bottom nav bar
                NavigationBar {
                    bottomNavDestinations.forEach{ dest ->
                        NavigationBarItem(
                            selected = currentDestination?.hierarchy?.any{ it.route == dest.route } == true,
                            onClick = { navController.navigateWithSaveState(dest.route) },
                            icon = { Icon(dest.icon, dest.route) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = TimerDestination.route
        ){
            composable(TimerDestination.route){
                if(viewModel.isServiceReady && viewModel.currentTask != null){
                    TimerScreen(
                        timer = viewModel.timerService!!.timerState,
                        taskData = viewModel.currentTask!!,
                        onChangeTask = { navController.navigateWithSaveState(ListDestination.route) },
                        onSettingsClick = { navController.navigateSingleTop(TaskInputDestination.route) }
                    )
                } else {
                    LoadingScreen()
                }
            }
            composable(ListDestination.route){
                ListScreen(
                    onAdd = { navController.navigateSingleTop(TaskInputDestination.route) },
                    onTaskSelect = { task -> navController.navigateSingleTop("${TaskInputDestination.route}/{${task.id}}") }
                )
            }
            composable(RecordDestination.route){
                RecordScreen()
            }
            composable(
                route = TaskInputDestination.route
            ){navBackStackEntry ->
                TaskInputScreen(
                    onCancel = { navController.popBackStack() },
                    onSubmit = { viewModel.insertNewTask(it) }
                )
            }
            composable(
                route = TaskInputDestination.routeWithArgs,
                arguments = TaskInputDestination.arguments
            ){navBackStackEntry ->
                val taskId = navBackStackEntry.arguments?.getLong(TaskInputDestination.typeArg)

                if(taskId == null){
                    TaskInputScreen(
                        onCancel = { navController.popBackStack() },
                        onSubmit = { viewModel.insertNewTask(it) }
                    )
                } else {
                    val task: Task? by rememberSaveable {
                        mutableStateOf(null)
                    }
                    LaunchedEffect(key1 = taskId) {
                        TaskRepository.instance.selectTaskWithId(taskId)
                    }

                    if(task != null){
                        TaskInputScreen(
                            task = task,
                            onCancel = { navController.popBackStack() },
                            onSubmit = { viewModel.insertNewTask(it) }
                        )
                    } else {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
fun NavHostController.navigateSingleTop(route: String) {
    navigate(route){
        launchSingleTop = true
    }
}
fun NavHostController.navigateWithSaveState(route: String) {
    navigate(route){
        popUpTo(graph.findStartDestination().id){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}