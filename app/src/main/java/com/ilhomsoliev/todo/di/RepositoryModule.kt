package com.ilhomsoliev.todo.di

import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.data.repository.TodoItemsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(): TodoItemsRepository {
        return TodoItemsRepositoryImpl()
    }

}