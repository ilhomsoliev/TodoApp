package com.ilhomsoliev.todo.feature.home.models

import com.ilhomsoliev.todo.feature.add.model.AddAction

sealed class HomeAction {
    data class ShowSnackbar(val text: String) : HomeAction()

//    data object UpdateItem : HomeAction()
}