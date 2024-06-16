package com.ilhomsoliev.todo.feature.home.models

sealed class HomeEvent {
    data class DeleteItem(val id: String) : HomeEvent()
    data class MarkItem(val id: String) : HomeEvent()
}