package com.skele.pomodoro.ui.task.input.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skele.pomodoro.ui.theme.PomodoroTheme


@Composable
fun MultiLineInputField(
    modifier: Modifier = Modifier,
    title: String,
    hint: List<String>,
    state: List<TextFieldState>,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            title,
            fontSize = 16.sp,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for (i in state.indices) {
                TextField(
                    modifier =
                    Modifier
                        .weight(1f)
                        .heightIn(64.dp),
                    singleLine = true,
                    value = state[i].text.toString(),
                    textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                    onValueChange = { input ->
                        // 숫자 입력일 경우, 숫자 이외의 입력 무시
                        if (keyboardType != KeyboardType.Number || input.all(Char::isDigit)) {
                            state[i].setTextAndPlaceCursorAtEnd(input)
                        }
                    },
                    placeholder = {
                        Text(
                            if (i >= hint.size) hint.last() else hint[i],
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    },
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                )
            }
        }
    }
}

@Preview
@Composable
fun SingleLineInputFieldPreview() {
    PomodoroTheme {
        val nameInputState =
            rememberTextFieldState("")

        MultiLineInputField(
            title = "작업 이름",
            hint = listOf("이름"),
            state = listOf(nameInputState),
        )
    }
}