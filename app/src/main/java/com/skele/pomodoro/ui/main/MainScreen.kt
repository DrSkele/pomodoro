package com.skele.pomodoro.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.skele.pomodoro.navigation.MainNavigation

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    MainNavigation(
        navController = navController
    )
}
