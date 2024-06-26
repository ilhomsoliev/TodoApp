package com.ilhomsoliev.todo.app.navigation

import androidx.navigation.NavBackStackEntry

sealed class Screens(val route: String) {
    data object Home : Screens("Home")
    data object Add : Screens("Add/{$TODO_ID_ARG_KEY}") {
        fun buildRoute(id: String?): String =
            "Add/${id}"

        fun getId(entry: NavBackStackEntry): String? =
            entry.arguments?.getString(TODO_ID_ARG_KEY)
    }

    companion object {
        const val TODO_ID_ARG_KEY = "TODO_ID_ARG_KEY"
    }
}