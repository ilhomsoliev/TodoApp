package com.ilhomsoliev.todo.app.navigation

sealed class Screens(val route:String) {
    data object Home : Screens("Home")
    data object Add : Screens("Add")
}