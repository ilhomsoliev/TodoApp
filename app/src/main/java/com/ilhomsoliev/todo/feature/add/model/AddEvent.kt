package com.ilhomsoliev.todo.feature.add.model

import com.ilhomsoliev.todo.data.models.TodoPriority

sealed class AddEvent {
    data class EnterScreen(val id: String) : AddEvent()
    data class Add(val text: String) : AddEvent()
    data class TextChange(val text: String) : AddEvent()
    data class PriorityChange(val priority: TodoPriority) : AddEvent()
    data class DeadlineChange(val date: Long?) : AddEvent()
    data class DeadlineSwitchClick(val value: Boolean) : AddEvent()
}