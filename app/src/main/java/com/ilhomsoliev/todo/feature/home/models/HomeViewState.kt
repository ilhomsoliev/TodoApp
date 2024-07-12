package com.ilhomsoliev.todo.feature.home.models

import com.ilhomsoliev.todo.domain.models.TodoModel

data class HomeViewState(
    val todos: List<TodoModel> = emptyList(),
    val isShowCompletedEnabled: Boolean = false,
    val completedCount: Int = 0,
)