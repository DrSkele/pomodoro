package com.skele.pomodoro

interface NavDestination {
    val route: String
}

object HomeDestination: NavDestination{
    override val route: String
        get() = "home"
}