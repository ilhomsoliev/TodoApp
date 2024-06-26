package com.ilhomsoliev.todo.app

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.app.navigation.Navigation


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.backPrimary)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.backPrimary)
        setContent {
            val navController: NavHostController = rememberNavController()
            Navigation(
                modifier = Modifier,
                navController = navController,
            )
        }
    }
}
