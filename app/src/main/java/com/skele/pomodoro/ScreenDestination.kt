package com.skele.pomodoro

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

//enum class ScreenDestination(
//    val route: String,
//    val hideBottomBar: Boolean,
//    val icon: ImageVector?
//){
//    Timer("timer", false, Icons.Default.PlayArrow),
//    List("list", false, Icons.Default.List),
//    Record("record", false, Icons.Default.DateRange),
//    TaskInput("task-input", true, null)
//}

interface ScreenDestinations{
    val route: String
    val hideBottomBar: Boolean
}
interface ScreenDestinationsWithArgument : ScreenDestinations{
    val typeArg: String
    val routeWithArgs: String
    val arguments: List<NamedNavArgument>
}
interface BottomNavTarget{
    val icon: ImageVector
}
interface BottomNavDestinations : ScreenDestinations, BottomNavTarget

val bottomNavDestinations : List<BottomNavDestinations> = listOf(
    TimerDestination, ListDestination, RecordDestination
)
object TimerDestination : BottomNavDestinations{
    override val icon: ImageVector
        get() = Icons.Default.PlayArrow
    override val route: String
        get() = "timer"
    override val hideBottomBar: Boolean
        get() = false
}
object ListDestination : BottomNavDestinations{
    override val icon: ImageVector
        get() = Icons.Default.List
    override val route: String
        get() = "list"
    override val hideBottomBar: Boolean
        get() = false
}
object RecordDestination : BottomNavDestinations{
    override val icon: ImageVector
        get() = Icons.Default.DateRange
    override val route: String
        get() = "record"
    override val hideBottomBar: Boolean
        get() = false
}
object TaskInputDestination : ScreenDestinationsWithArgument{
    override val route: String
        get() = "task_input"
    override val hideBottomBar: Boolean
        get() = false

    override val typeArg = "task_id"
    override val routeWithArgs = "${this.route}/{$typeArg}"
    override val arguments = listOf(
        navArgument(name = typeArg) { type = NavType.LongType }
    )
}