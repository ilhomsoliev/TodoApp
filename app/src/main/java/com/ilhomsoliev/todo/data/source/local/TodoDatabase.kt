package com.ilhomsoliev.todo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilhomsoliev.todo.data.source.local.dao.TodoDao
import com.ilhomsoliev.todo.data.source.local.dto.TodoEntity

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao
}