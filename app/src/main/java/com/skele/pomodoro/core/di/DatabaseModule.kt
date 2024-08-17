package com.skele.pomodoro.core.di

import android.content.Context
import androidx.room.Room
import com.skele.pomodoro.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
            ).build()
//            ).addCallback(
//                object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        CoroutineScope(Dispatchers.IO).launch {
//                            val taskDao = provideTaskDatabase(context).taskDao()
//                            val defaultTask =
//                                Task("Default", 25.minutes, 5.minutes, 15.minutes, 4, 5, Primary)
//                            taskDao.insertNewTask(defaultTask)
//                        }
//                    }
//                },
//            ).build()
}
