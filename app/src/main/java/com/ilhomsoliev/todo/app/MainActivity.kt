package com.ilhomsoliev.todo.app

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.app.navigation.Navigation
import com.ilhomsoliev.todo.shared.snackbar.ProvideSnackbarController
import com.ilhomsoliev.todo.shared.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.backPrimary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.backPrimary)
        setContent {
            TodoTheme {
                val navController: NavHostController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                ProvideSnackbarController(
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope
                ) {
                    Navigation(
                        modifier = Modifier,
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }
    fun onBackButtonClick() {

    }
}
