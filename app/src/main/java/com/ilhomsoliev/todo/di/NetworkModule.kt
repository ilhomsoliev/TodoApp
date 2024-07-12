package com.ilhomsoliev.todo.di

import com.ilhomsoliev.todo.data.source.remote.KtorSource
import com.ilhomsoliev.todo.data.source.remote.TodoNetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideKtorSource(): KtorSource {
        return KtorSource()
    }

    @Provides
    @Singleton
    fun provideTodoManager(): TodoNetworkManager {
        return TodoNetworkManager(provideKtorSource())
    }

}