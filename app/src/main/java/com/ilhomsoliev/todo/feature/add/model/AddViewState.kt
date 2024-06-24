package com.ilhomsoliev.todo.feature.add.model

import com.ilhomsoliev.todo.data.models.TodoPriority

data class AddViewState(
    val id: String? = null,
    val text: String = "",
    val priority: TodoPriority = TodoPriority.USUAL,
    val deadline: Long? = null,
) {
    fun isDeleteActive() = text.isNotEmpty()
}