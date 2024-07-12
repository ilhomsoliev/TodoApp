package com.ilhomsoliev.todo.di

import com.ilhomsoliev.todo.domain.repository.TodoRepository
import com.ilhomsoliev.todo.domain.repository.TodoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideRepository(todoRepositoryImpl: TodoRepositoryImpl): TodoRepository

}