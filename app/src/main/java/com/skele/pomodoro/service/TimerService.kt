package com.skele.pomodoro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CombinedVibration
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.skele.pomodoro.MainActivity
import com.skele.pomodoro.R
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.timer.state.TaskTimerState
import com.skele.pomodoro.ui.timer.state.TaskTimerStateManager
import com.skele.pomodoro.ui.timer.state.TimerState
import com.skele.pomodoro.util.toMinuteFormatString
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimerService : LifecycleService() {
    private var isForegroundActive = false

    private val CHANNEL_ID = "foreground_timer"
    private val NOTIFICATION_ID = 99

    private var notificationBuilder: NotificationCompat.Builder? = null

    private lateinit var activityIntent: Intent
    private lateinit var activityPendingIntent: PendingIntent
    private lateinit var startPendingIntent: PendingIntent
    private lateinit var pausePendingIntent: PendingIntent
    private lateinit var stopPendingIntent: PendingIntent

    @Inject
    lateinit var taskTimerStateManager: TaskTimerStateManager

    fun startForegroundService() {
        if (taskTimerStateManager.taskTimerState.value is TaskTimerState.HasTask) {
            val state = taskTimerStateManager.taskTimerState.value as TaskTimerState.HasTask
            val notification = createNotification(state.taskInfo.task, state.timerState)
            startForeground(NOTIFICATION_ID, notification)
            isForegroundActive = true
        }
    }

    fun stopForegroundService() {
        if (isForegroundActive) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isForegroundActive = false
        }
    }

    private fun stopService() {
        stopForegroundService()
        this.stopSelf()
    }

    private fun createNotification(
        task: Task,
        timerState: TimerState,
    ): Notification {
        if (notificationBuilder == null) {
            notificationBuilder =
                NotificationCompat.Builder(this, CHANNEL_ID)
        }

        val notification =
            notificationBuilder!!
                .apply {
                    setOnlyAlertOnce(true)
                    setOngoing(true)
                    setSmallIcon(R.drawable.ic_launcher_foreground)
                    setContentText(timerState.time.toMinuteFormatString())
                    clearActions()
                    if (timerState is TimerState.Ready || timerState is TimerState.Paused) {
                        addAction(
                            NotificationCompat.Action(
                                android.R.drawable.ic_media_play,
                                getString(R.string.start_button),
                                startPendingIntent,
                            ),
                        )
                        addAction(
                            NotificationCompat.Action(
                                android.R.drawable.ic_menu_close_clear_cancel,
                                getString(R.string.stop_button),
                                stopPendingIntent,
                            ),
                        )
                    } else {
                        addAction(
                            NotificationCompat.Action(
                                android.R.drawable.ic_media_pause,
                                "Pause",
                                pausePendingIntent,
                            ),
                        )
                    }
                    setContentIntent(activityPendingIntent)
                    setStyle(null)
                }.build()
        return notification
    }

    private fun updateNotification(timerState: TimerState): Notification {
        if (notificationBuilder == null) {
            notificationBuilder =
                NotificationCompat.Builder(this, CHANNEL_ID)
        }

        val notification =
            notificationBuilder!!
                .apply {
                    setContentText(timerState.time.toMinuteFormatString())
                }.build()
        return notification
    }

    private fun onTimerFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibrator = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val effect = VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE)
            val combined = CombinedVibration.createParallel(effect)
            vibrator.vibrate(combined)
        } else {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

            val effect = VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
    }

    private fun init() {
        activityIntent = Intent(this, MainActivity::class.java)
        activityPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE,
            )
        val notificationStartIntent =
            Intent(this, TimerService::class.java).apply {
                action = CustomActions.START
            }
        val notificationPauseIntent =
            Intent(this, TimerService::class.java).apply {
                action = CustomActions.PAUSE
            }
        val notificationStopIntent =
            Intent(this, TimerService::class.java).apply {
                action = CustomActions.STOP
            }
        startPendingIntent =
            PendingIntent.getService(this, 0, notificationStartIntent, PendingIntent.FLAG_IMMUTABLE)
        pausePendingIntent =
            PendingIntent.getService(this, 0, notificationPauseIntent, PendingIntent.FLAG_IMMUTABLE)
        stopPendingIntent =
            PendingIntent.getService(this, 0, notificationStopIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createChannel() {
        val serviceChannel =
            NotificationChannel(
                CHANNEL_ID,
                "Timer Notification",
                NotificationManager.IMPORTANCE_LOW,
            )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)
    }

    private fun updateForegroundService() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        lifecycleScope.launch {
            taskTimerStateManager.taskTimerState
                .takeWhile { taskTimerState -> taskTimerState is TaskTimerState.HasTask }
                .collect { taskTimerState ->
                    when (val timerState = (taskTimerState as TaskTimerState.HasTask).timerState) {
                        is TimerState.Running -> updateNotification(timerState)
                        is TimerState.Finished -> onTimerFinish()
                        else -> createNotification(taskTimerState.taskInfo.task, timerState)
                    }
                }
        }
    }

    override fun onCreate() {
        super.onCreate()
        init()
        createChannel()
        updateForegroundService()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        // Log.d("TAG", "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            CustomActions.START -> {
                taskTimerStateManager.startTimer()
            }

            CustomActions.PAUSE -> {
                taskTimerStateManager.pauseTimer()
            }

            CustomActions.STOP -> {
                stopService()
            }
        }
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return TimerServiceBinder()
    }

    inner class TimerServiceBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }
}
