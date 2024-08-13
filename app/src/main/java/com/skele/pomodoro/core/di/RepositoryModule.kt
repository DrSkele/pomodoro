package com.skele.pomodoro.core.di

import com.skele.pomodoro.data.repository.TaskRepository
import com.skele.pomodoro.data.repositoryimpl.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}
