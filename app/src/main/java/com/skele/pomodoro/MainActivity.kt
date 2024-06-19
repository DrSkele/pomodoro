package com.skele.pomodoro

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skele.pomodoro.service.CustomActions
import com.skele.pomodoro.service.TimerService
import com.skele.pomodoro.ui.screen.HomeScreen
import com.skele.pomodoro.ui.theme.PomodoroTheme

class MainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            viewModel.isServiceReady = true
            val binder = service as TimerService.TimerServiceBinder
            viewModel.timerService = binder.getService()
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.isServiceReady = false
        }
    }
    fun startTimerService(){
        val intent = Intent(this, TimerService::class.java).apply {
            action = CustomActions.CREATE
        }
        startService(intent)
    }
    fun bindTimerService(){
        val intent = Intent(this, TimerService::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }
    fun unbindTimerService(){
        if(viewModel.isServiceReady) unbindService(serviceConnection)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PomodoroApp(
                viewModel = viewModel
            )
        }
    }
    override fun onStart() {
        super.onStart()
        bindTimerService()
    }

    override fun onStop() {
        super.onStop()
        unbindTimerService()
    }
}

@Composable
fun PomodoroApp(
    viewModel: MainViewModel
){
    val navController = rememberNavController()
    PomodoroTheme {
        HomeNavHost(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun HomeNavHost(
    navController: NavHostController,
    viewModel: MainViewModel
){

    LaunchedEffect(key1 = viewModel.currentId) {

    }

    NavHost(
        navController = navController,
        startDestination = HomeDestination.route
    ){
        composable(route = HomeDestination.route){
            if(viewModel.currentTask != null && viewModel.timerService != null){
                HomeScreen(
                    timerService = viewModel.timerService!!,
                    currentTask = viewModel.currentTask!!
                )
            }
        }
    }
}