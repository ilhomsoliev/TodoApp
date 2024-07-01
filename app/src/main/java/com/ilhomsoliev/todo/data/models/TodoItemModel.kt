package com.ilhomsoliev.todo.data.models

data class TodoItemModel(
    val id: String,
    val text: String,
    val priority: TodoPriority,
    val deadline: Long? = null,
    val isCompleted: Boolean,
    val createdDate: Long,
    val editedDate: Long? = null,
) {
    companion object {
        val demo = TodoItemModel(
            id = "1",
            text = "Lorem pusama amfasdf",
            priority = TodoPriority.HIGH,
            deadline = null,
            isCompleted = false,
            createdDate = 0,
            editedDate = null,
        )
        val demos = listOf(
            demo,
            demo.copy(id = "2", deadline = 123123123),
            demo.copy(id = "3", isCompleted = true),
            demo.copy(id = "4"),
        )
    }

}

enum class TodoPriority(val nameRu: String) {
    LOW("Низкий"), USUAL("Нет"), HIGH("!! Высокий");
}