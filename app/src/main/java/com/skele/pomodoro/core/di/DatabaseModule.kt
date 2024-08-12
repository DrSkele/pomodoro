package com.skele.pomodoro.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.skele.pomodoro.data.TaskDatabase
import com.skele.pomodoro.data.model.Task
import com.skele.pomodoro.ui.theme.Primary
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(
        @ApplicationContext context: Context,
    ): TaskDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "pomodoro.db",
            ).addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val taskDao = provideTaskDatabase(context).taskDao()
                            val defaultTask = Task("Default", 25.minutes, 5.minutes, 15.minutes, 5, Primary)
                            taskDao.insertNewTask(defaultTask)
                        }
                    }
                },
            ).build()
}
