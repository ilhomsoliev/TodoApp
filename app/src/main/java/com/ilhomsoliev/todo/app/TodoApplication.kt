package com.ilhomsoliev.todo.app

import android.app.Application
import com.ilhomsoliev.todo.workers.scheduleDataSync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        scheduleDataSync(this)
    }

}

