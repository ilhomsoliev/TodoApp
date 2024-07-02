package com.ilhomsoliev.todo.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.ilhomsoliev.todo.domain.repository.TodoRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SyncDataWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    @Inject
    lateinit var repository: TodoRepository

    override suspend fun doWork(): Result {
        repository.getTodos()
        return Result.success()
    }
}

fun scheduleDataSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val syncDataWorkRequest = PeriodicWorkRequestBuilder<SyncDataWorker>(8, TimeUnit.HOURS)
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "SyncDataWork",
        ExistingPeriodicWorkPolicy.KEEP,
        syncDataWorkRequest
    )
}