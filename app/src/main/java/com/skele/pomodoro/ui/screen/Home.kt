package com.skele.pomodoro.ui.screen

import android.util.Log
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.skele.pomodoro.data.model.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if(bottomNavDestinations.any{it.route == currentDestination?.route}){
        currentDestination?.route?.let {
            viewModel.bottomNavDestination = it
        }
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
            startDestination = viewModel.bottomNavDestination
        ){
            composable(TimerDestination.route){

                LaunchedEffect(key1 = viewModel.isServiceReady) {
                    viewModel.loadCurrentTask()
                }

                if(viewModel.isServiceReady){
                    TimerScreen(
                        timer = viewModel.getTimerState(),
                        taskData = viewModel.getCurrentTask(),
                        onShowList = { navController.navigateWithSaveState(ListDestination.route) },
                        onSettingsClick = { task -> navController.navigateSingleTop("${TaskInputDestination.route}/${task.id}") }
                    )
                } else {
                    LoadingScreen()
                }
            }
            composable(ListDestination.route){

                val taskList by viewModel.taskList.collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )

                ListScreen(
                    list = taskList,
                    onAdd = { navController.navigateSingleTop(TaskInputDestination.route) },
                    onTaskSelect = { task ->
                        //navController.navigateSingleTop("${TaskInputDestination.route}/${task.id}")
                        navController.navigateWithSaveState(TimerDestination.route)
                        viewModel.setAsCurrentTask(task.id)
                    }
                )
            }
            composable(RecordDestination.route){
                RecordScreen()
            }
            composable(
                route = TaskInputDestination.routeWithArgs,
                arguments = TaskInputDestination.arguments
            ){navBackStackEntry ->
                val taskId = navBackStackEntry.arguments?.getLong(TaskInputDestination.typeArg)

                Log.d("TAG", "Home: $taskId")

                if(taskId == null){
                    TaskInputScreen(
                        onCancel = { navController.popBackStack() },
                        onSubmit = {
                            viewModel.insertTask(it)
                            navController.popBackStack()
                        }
                    )
                } else {
                    var task: Task? by remember {
                        mutableStateOf(null)
                    }
                    LaunchedEffect(key1 = taskId) {
                        task = viewModel.selectTaskWithId(taskId)
                    }

                    if(task != null){
                        TaskInputScreen(
                            task = task,
                            onCancel = { navController.popBackStack() },
                            onSubmit = {
                                viewModel.updateTask(it)
                                navController.popBackStack()
                            }
                        )
                    } else {
                        LoadingScreen()
                    }
                }
            }
            composable(
                route = TaskInputDestination.route
            ){navBackStackEntry ->
                TaskInputScreen(
                    onCancel = { navController.popBackStack() },
                    onSubmit = {
                        viewModel.insertTask(it)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
    var indicatorState by remember{
        mutableFloatStateOf(0.0f)
    }

    LaunchedEffect(key1 = null) {
        launch {
            while (true){
                indicatorState = (indicatorState + 0.1f) % 1f
                delay(100)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            progress = indicatorState,
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