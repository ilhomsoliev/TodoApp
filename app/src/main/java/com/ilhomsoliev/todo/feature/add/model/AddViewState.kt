package com.ilhomsoliev.todo.feature.add.model

import com.ilhomsoliev.todo.data.models.TodoPriority

data class AddViewState(
    val id: String? = null,
    val text: String = "",
    val dateDialogActive: Boolean = false,
    val priority: TodoPriority = TodoPriority.USUAL,
    val deadline: Long? = null,
    val date: String = "",
) {
    fun isDeleteActive() = text.isNotEmpty()
}