package com.ilhomsoliev.todo.feature.home.models

sealed class HomeEvent {
    data class DeleteItem(val id: String) : HomeEvent()
    data class MarkItem(val id: String) : HomeEvent()
    data class ItemClick(val id: String) : HomeEvent()
    data object AddClick : HomeEvent()
    data object OpenInfo : HomeEvent()

    data object ToggleIsCompletedVisible : HomeEvent()
    data object Refresh : HomeEvent()
}