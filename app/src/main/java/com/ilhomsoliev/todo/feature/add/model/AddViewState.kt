package com.ilhomsoliev.todo.feature.add.model

import com.ilhomsoliev.todo.data.models.TodoPriority

data class AddViewState(
    val text: String = "",
    val priority: TodoPriority = TodoPriority.LOW,
    val deadline: Long? = null,
    val isDeadlineTimeActivated: Boolean = false,
) {
    fun isDeleteActive() = text.isNotEmpty()
}