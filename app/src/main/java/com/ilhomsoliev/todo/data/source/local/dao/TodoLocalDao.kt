package com.ilhomsoliev.todo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ilhomsoliev.todo.data.source.local.dto.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoLocalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(todos: List<TodoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Query("DELETE FROM todo_table WHERE :todoId = id")
    suspend fun deleteById(todoId: String)

    @Query("SELECT * FROM todo_table ORDER BY createdAt ASC")
    fun getTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE :todoId = id")
    fun getTodoById(todoId: String): TodoEntity?

    @Query("DELETE FROM todo_table")
    suspend fun clearAll(): Int
}