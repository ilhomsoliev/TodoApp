package com.ilhomsoliev.todo.feature.add.model

sealed class AddAction {
    data object NavigateBack : AddAction()
    data class ShowSnackbar(val text: String) : AddAction()
}