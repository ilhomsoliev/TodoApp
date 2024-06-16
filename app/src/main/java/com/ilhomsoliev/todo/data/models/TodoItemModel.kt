package com.ilhomsoliev.todo.data.models

data class TodoItemModel(
    val id: String,
    val text: String,
    val priority: TodoPriority,
    val deadline: Long? = null,
    val isCompleted: Boolean,
    val createdDate: Long,
    val editedDate: Long? = null,
)

enum class TodoPriority(val nameRu: String) {
    LOW("Низкий"), USUAL("Нет"), HIGH("!! Высокий");
}