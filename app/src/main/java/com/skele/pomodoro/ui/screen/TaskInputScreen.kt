package com.skele.pomodoro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.theme.PomodoroTheme
import kotlin.time.Duration.Companion.minutes

@Composable
fun TaskInputScreen(
    modifier: Modifier = Modifier,
    task: Task? = null,
    onCancel: () -> Unit = {},
    onSubmit: (Task) -> Unit = {}
) {
    val name = rememberInputFieldState(task?.description ?: "")

    val workTime = rememberInputFieldState(((task?.workTime?.inWholeMinutes?.rem(60)) ?: 25).toString())
    val breakTime = rememberInputFieldState(((task?.breakTime?.inWholeMinutes?.rem(60)) ?: 25).toString())
    val longBreak = rememberInputFieldState(((task?.longBreakTime?.inWholeMinutes?.rem(60)) ?: 25).toString())

    val dailyGoal = rememberInputFieldState((task?.dailyGoal ?: 5).toString())

    val red = rememberInputFieldState((task?.color?.red?.toInt() ?: 255).toString())
    val green = rememberInputFieldState((task?.color?.green?.toInt() ?: 255).toString())
    val blue = rememberInputFieldState((task?.color?.blue?.toInt() ?: 255).toString())

    fun inputTask() : Task {
        return task?.copy(
            description = name.text.text,
            workTimeInMillisec = workTime.text.text.toInt().minutes.inWholeMilliseconds,
            breakTimeInMillisec = breakTime.text.text.toInt().minutes.inWholeMilliseconds,
            longBreakTimeInMillisec = longBreak.text.text.toInt().minutes.inWholeMilliseconds,
            dailyGoal = dailyGoal.text.text.toInt(),
            colorNum = Color(red.text.text.toInt(), green.text.text.toInt(), blue.text.text.toInt()).toArgb()
        ) ?: Task(
            description = name.text.text,
            workTime = workTime.text.text.toInt().minutes,
            breakTime = breakTime.text.text.toInt().minutes,
            longBreakTime = longBreak.text.text.toInt().minutes,
            dailyGoal = dailyGoal.text.text.toInt(),
            color = Color(red.text.text.toInt(), green.text.text.toInt(), blue.text.text.toInt())
        )
    }

    Column(
        modifier = modifier
    ) {
        MultiLineInputField(
            title = "작업 이름",
            hint = listOf("이름"),
            state = listOf(name),
        )
        MultiLineInputField(
            title = "시간 입력",
            hint = listOf("작업", "휴식", "긴 휴식"),
            state = listOf(workTime, breakTime, longBreak),
            keyboardType = KeyboardType.Number
        )
        MultiLineInputField(
            title = "일일 목표",
            hint = listOf("목표 작업량"),
            state = listOf(dailyGoal),
            keyboardType = KeyboardType.Number
        )
        MultiLineInputField(
            title = "색상",
            hint = listOf("R", "G", "B"),
            state = listOf(red, green, blue),
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Button(onClick = onCancel) {
                Text("취소")
            }
            Button(onClick = {
                onSubmit(inputTask())
            }){
                Text("확인")
            }
        }
    }
}

class InputFieldState(initalValue: String){
    var text by mutableStateOf(TextFieldValue(initalValue))

    companion object{
        val Saver : Saver<InputFieldState, Any> = mapSaver(
            save = { mapOf("text" to it.text.text) },
            restore = {
                InputFieldState(it["text"] as String)
            }
        )
    }
}

@Composable
fun rememberInputFieldState(initalValue: String) : InputFieldState =
    rememberSaveable(initalValue, saver = InputFieldState.Saver) {
        InputFieldState(initalValue)
    }

@Composable
fun MultiLineInputField(
    modifier: Modifier = Modifier,
    title: String,
    hint: List<String>,
    state: List<InputFieldState>,
    keyboardType: KeyboardType = KeyboardType.Text
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            title,
            fontSize = 16.sp
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for(i in state.indices) TextField(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(64.dp),
                singleLine = true,
                value = state[i].text,
                textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                onValueChange = {input ->
                    // 숫자 입력일 경우, 숫자 이외의 입력 무시
                    if(keyboardType != KeyboardType.Number || input.text.all(Char::isDigit)){
                        state[i].text = input
                    }
                },
                placeholder = { Text(if(i >= hint.size) hint.last() else hint[i], fontSize = 20.sp, textAlign = TextAlign.Center) },
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        }
    }
}

@Preview
@Composable
fun SingleLineInputFieldPreview(){
    PomodoroTheme {
        val nameInputState = rememberSaveable {
            InputFieldState("")
        }

        MultiLineInputField(
            title = "작업 이름",
            hint = listOf("이름"),
            state = listOf(nameInputState),
        )
    }
}