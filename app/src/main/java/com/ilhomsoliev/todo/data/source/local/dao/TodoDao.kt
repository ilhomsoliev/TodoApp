package com.ilhomsoliev.todo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ilhomsoliev.todo.data.source.local.dto.TodoEntity

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(beers: List<TodoEntity>)

    @Query("SELECT * FROM todo_table")
    fun getTodos(): List<TodoEntity>

    @Query("DELETE FROM todo_table")
    suspend fun clearAll(): Int
}