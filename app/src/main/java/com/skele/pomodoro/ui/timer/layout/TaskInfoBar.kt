package com.skele.pomodoro.ui.timer.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.theme.PomodoroTheme

@Composable
fun TaskInfoBar(
    modifier: Modifier = Modifier,
    task: Task?,
    onClick: () -> Unit = {},
    onSettingsClick: (Task) -> Unit = {},
) {
    Surface(
        modifier =
            modifier
                .shadow(10.dp, RoundedCornerShape(4.dp))
                .height(48.dp)
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (task != null) {
                Surface(
                    color = task.color,
                    modifier =
                        Modifier
                            .width(8.dp)
                            .fillMaxHeight(),
                ) {}
                Text(
                    text = task.description,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onSettingsClick(task) }) {
                    Icon(
                        Icons.Rounded.Settings,
                        "task settings",
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            } else {
                Text(
                    text = "등록된 작업이 없습니다.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun TaskInfoBarPreview() {
    PomodoroTheme {
        TaskInfoBar(task = Task.sampleTask)
    }
}
