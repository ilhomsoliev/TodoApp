package com.ilhomsoliev.todo.app.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.data.repository.TodoItemsRepositoryImpl
import com.ilhomsoliev.todo.feature.add.AddScreen
import com.ilhomsoliev.todo.feature.add.AddViewModel
import com.ilhomsoliev.todo.feature.home.HomeScreen
import com.ilhomsoliev.todo.feature.home.HomeViewModel

val repository: TodoItemsRepository = TodoItemsRepositoryImpl()

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Scaffold(
        modifier = Modifier.padding(WindowInsets.ime.asPaddingValues()),
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screens.Home.route
        ) {
            composable(Screens.Home.route) {
                val viewModel = remember { HomeViewModel(repository) }
                HomeScreen(vm = viewModel, goAddTodo = {
                    navController.navigate(Screens.Add.buildRoute(it))
                })
            }
            composable(Screens.Add.route) {
                val viewModel = remember { AddViewModel(repository) }
                val id = Screens.Add.getId(it)

                AddScreen(vm = viewModel, id = id, onBack = {
                    navController.popBackStack()
                })
            }
        }
    }
}