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
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.skele.pomodoro.MainActivity
import com.skele.pomodoro.R
import com.skele.pomodoro.TaskState
import com.skele.pomodoro.TimerState
import com.skele.pomodoro.util.toMinuteFormatString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.time.Duration

class TimerService : LifecycleService() {

    private var isForegroundActive = false

    private val CHANNEL_ID = "foreground_timer"
    private val NOTIFICATION_ID = 99

    private var notificationBuilder : NotificationCompat.Builder? = null

    private lateinit var activityIntent: Intent
    private lateinit var activityPendingIntent: PendingIntent
    private lateinit var startPendingIntent: PendingIntent
    private lateinit var pausePendingIntent: PendingIntent
    private lateinit var stopPendingIntent: PendingIntent

    val taskState = TaskState()
    val timerState = TimerState(Duration.ZERO)

    fun changeTimerTask(taskId : Long){
        CoroutineScope(Dispatchers.IO).launch{
            taskState.setAsCurrentTask(taskId)
            taskState.currentTask?.let {
                timerState.setDuration(it.task.getTimeOfType(taskState.currentTimerType))
            }
        }
    }
    fun loadTimerTask(){
        CoroutineScope(Dispatchers.IO).launch {
            taskState.loadCurrentTask()
            taskState.currentTask?.let {
                timerState.setDuration(it.task.getTimeOfType(taskState.currentTimerType))
            }
        }
    }
    fun startForegroundService(){
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Timer Notification",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        isForegroundActive = true
    }
    fun stopForegroundService(){
        if(isForegroundActive) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            isForegroundActive = false
        }
    }
    private fun stopService(){
        stopForegroundService()
        this.stopSelf()
    }
    private fun createNotification() : Notification {
        if(notificationBuilder == null) notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)

        val notification = notificationBuilder!!
            .apply {
                setOnlyAlertOnce(true)
                setOngoing(true)
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentText(timerState.timeFlow.value.toMinuteFormatString())
                clearActions()
                addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_play,
                        "Start",
                        startPendingIntent
                    )
                )
                addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_menu_close_clear_cancel,
                        "Stop",
                        stopPendingIntent
                    )
                )
                setContentIntent(activityPendingIntent)
                setStyle(null)
            }.build()
        return notification
    }
    private fun updateNotification() : Notification {
        val notification = notificationBuilder!!.apply {
            //setContentTitle(timerState.type.value.text)
            setContentText(timerState.timeFlow.value.toMinuteFormatString())
            clearActions()
            if(timerState.isPaused.value){
                addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_play,
                        "Start",
                        startPendingIntent
                    )
                )
                addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_menu_close_clear_cancel,
                        "Stop",
                        stopPendingIntent
                    )
                )
            } else {
                addAction(
                    NotificationCompat.Action(
                        android.R.drawable.ic_media_pause,
                        "Pause",
                        pausePendingIntent
                    )
                )
            }
        }.build()
        return notification
    }
    private fun onTimerFinish(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val vibrator = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val effect = VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE)
            val combined = CombinedVibration.createParallel(effect)
            vibrator.vibrate(combined)
        } else {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

            val effect = VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        }
        taskState.proceedToNextTimer()
    }
    private fun init(){
        activityIntent = Intent(this, MainActivity::class.java)
        activityPendingIntent = PendingIntent.getActivity(this, 0, activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationStartIntent = Intent(this, TimerService::class.java).apply {
            action = CustomActions.START
        }
        val notificationPauseIntent = Intent(this, TimerService::class.java).apply {
            action = CustomActions.PAUSE
        }
        val notificationStopIntent = Intent(this, TimerService::class.java).apply {
            action = CustomActions.STOP
        }
        startPendingIntent = PendingIntent.getService(this, 0, notificationStartIntent,PendingIntent.FLAG_IMMUTABLE)
        pausePendingIntent = PendingIntent.getService(this, 0, notificationPauseIntent,PendingIntent.FLAG_IMMUTABLE)
        stopPendingIntent = PendingIntent.getService(this, 0, notificationStopIntent, PendingIntent.FLAG_IMMUTABLE)
    }
    private fun updateForegroundService(){
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        lifecycleScope.launch {
            launch {
                timerState.timeFlow.filter { isForegroundActive } .collectLatest {
                    manager.notify(NOTIFICATION_ID, updateNotification())
                }
            }
            launch {
                timerState.isPaused.filter { isForegroundActive }.collectLatest {
                    manager.notify(NOTIFICATION_ID, updateNotification())
                }
            }
        }
    }
    private fun setListener(){
        CoroutineScope(Dispatchers.Main).launch {
            timerState.timerFinishEvent.collectLatest {
                onTimerFinish()
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        init()
        updateForegroundService()
        setListener()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG", "onStartCommand: ${intent?.action}")
        when(intent?.action){
            CustomActions.START -> {
                timerState.start()
            }
            CustomActions.PAUSE -> {
                timerState.pause()
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

    inner class TimerServiceBinder : Binder(){
        fun getService() : TimerService = this@TimerService
    }
}