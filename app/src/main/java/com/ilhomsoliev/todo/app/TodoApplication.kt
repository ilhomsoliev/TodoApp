package com.ilhomsoliev.todo.app

import android.app.Application
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.data.repository.TodoItemsRepositoryImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {

    val repository: TodoItemsRepository = TodoItemsRepositoryImpl()

}