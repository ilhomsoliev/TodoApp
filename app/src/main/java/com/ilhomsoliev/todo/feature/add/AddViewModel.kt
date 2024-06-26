package com.ilhomsoliev.todo.feature.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ilhomsoliev.todo.core.BaseSharedViewModel
import com.ilhomsoliev.todo.core.generateRandomString
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.feature.add.model.AddAction
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.model.AddViewState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddViewModel(
    private val repository: TodoItemsRepository
) : BaseSharedViewModel<AddViewState, AddAction, AddEvent>(AddViewState()) {

    override fun obtainEvent(viewEvent: AddEvent) {
        when (viewEvent) {
            is AddEvent.Add -> {
                val response = repository.insertTodo(
                    TodoItemModel(
                        id = viewState.id ?: generateRandomString(),
                        text = viewState.text,
                        priority = viewState.priority,
                        deadline = viewState.deadline,
                        isCompleted = false,
                        createdDate = System.currentTimeMillis(),
                        editedDate = null,
                    )
                )
                viewAction = if (response) {
                    AddAction.NavigateBack
                } else {
                    AddAction.ShowSnackbar("Заполните поля")
                }
            }

            is AddEvent.DeadlineChange -> {
                viewState = viewState.copy(deadline = viewEvent.date)
            }

            is AddEvent.PriorityChange -> {
                viewState = viewState.copy(priority = viewEvent.priority)
            }

            is AddEvent.TextChange -> {
                viewState = viewState.copy(text = viewEvent.text)
            }

            is AddEvent.EnterScreen -> {
                viewEvent.id?.let {
                    repository.getTodoById(it)?.let { todo ->
                        viewState = viewState.copy(
                            id = todo.id,
                            text = todo.text,
                            priority = todo.priority,
                            deadline = todo.deadline,
                        )
                    }
                }
            }

            AddEvent.Delete -> {
                viewState.id?.let {
                    repository.deleteTodo(it)
                }
                viewAction = AddAction.NavigateBack
            }

            is AddEvent.DateDialogIsActiveChange -> {
                viewState = viewState.copy(dateDialogActive = viewEvent.value)
            }

            is AddEvent.OnDateChange -> {
                val selectedDate = Calendar.getInstance().apply {
                    timeInMillis = viewEvent.selectedDateMillis
                }
                val dateFormatter = SimpleDateFormat("dd.MM.yyyy")
                viewState = viewState.copy(
                    date = dateFormatter.format(selectedDate.time),
                    deadline = viewEvent.selectedDateMillis,
                )
            }

            else -> {}
        }
    }
}