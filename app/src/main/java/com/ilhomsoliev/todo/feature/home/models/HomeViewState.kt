package com.ilhomsoliev.todo.feature.home.models

import com.ilhomsoliev.todo.data.models.TodoItemModel

data class HomeViewState(
    val todos: List<TodoItemModel> = emptyList(),
    val isShowCompletedEnabled: Boolean = false,
    val completedCount: Int = 0,
)