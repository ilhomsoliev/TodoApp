package com.ilhomsoliev.todo.feature.add.model

import com.ilhomsoliev.todo.data.models.TodoPriority

sealed class AddEvent {
    data class EnterScreen(val id: String?) : AddEvent()
    data object Add : AddEvent()
    data class TextChange(val text: String) : AddEvent()
    data class PriorityChange(val priority: TodoPriority) : AddEvent()
    data class DeadlineChange(val date: Long?) : AddEvent()
    data object Delete : AddEvent()
    data object OnBack : AddEvent()
    data class DateDialogIsActiveChange(val value: Boolean) : AddEvent()
    data class OnDateChange(val selectedDateMillis: Long) : AddEvent()
}