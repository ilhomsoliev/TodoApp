package com.ilhomsoliev.todo.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ilhomsoliev.todo.feature.add.AddScreen
import com.ilhomsoliev.todo.feature.divkit_screen.InfoScreen
import com.ilhomsoliev.todo.feature.home.HomeScreen


@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screens.Home.route
        ) {
            composable(Screens.Home.route) {
                HomeScreen(vm = hiltViewModel(), goAddTodo = {
                    navController.navigate(Screens.Add.buildRoute(it))
                }, goInfo = {
                    navController.navigate(Screens.Info.route)
                })
            }

            composable(Screens.Info.route) {
                InfoScreen(onBack = {
                    navController.navigateUp()
                })
            }
            composable(Screens.Add.route) {
                val id = Screens.Add.getId(it)
                AddScreen(vm = hiltViewModel(), id = id, onBack = {
                    navController.navigateUp()
                })
            }
        }
    }
}