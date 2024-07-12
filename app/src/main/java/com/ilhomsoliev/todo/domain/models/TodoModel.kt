package com.ilhomsoliev.todo.domain.models

import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.data.models.getTodoPriorityFromString
import com.ilhomsoliev.todo.data.source.local.dto.TodoEntity
import com.ilhomsoliev.todo.data.source.remote.models.request.TodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

data class TodoModel(
    val changedAt: Long = 0, // TODO
    val color: String,
    val createdAt: Long,
    val deadline: Long,
    val done: Boolean,
    val id: String,
    val priority: TodoPriority,
    val lastUpdatedBy: String,
    val text: String
) {
    fun mapToRequest() = TodoRequest(
        changed_at = changedAt,
        color = color,
        created_at = createdAt,
        deadline = deadline,
        done = done,
        id = id,
        importance = priority.nameServer,
        last_updated_by = lastUpdatedBy,
        text = text,
    )

    fun mapToEntity() = TodoEntity(
        changedAt = changedAt,
        color = color,
        createdAt = createdAt,
        deadline = deadline,
        done = done,
        id = id,
        importance = priority.name,
        lastUpdatedBy = lastUpdatedBy,
        text = text,
    )

    companion object {
        val demo = TodoModel(
            changedAt = 12312312,
            color = "",
            createdAt = 213123,
            deadline = 12312312,
            done = false,
            id = "1",
            priority = TodoPriority.LOW,
            lastUpdatedBy = "",
            text = "Some text for my todo",
        )
        val demos = listOf(
            demo,
            demo.copy(id = "2", deadline = 123123123),
            demo.copy(id = "3", done = true),
            demo.copy(id = "4"),
        )
    }

}

fun TodoEntity.map() = TodoModel(
    changedAt = changedAt,
    color = color,
    createdAt = createdAt,
    deadline = deadline,
    done = done,
    id = id,
    priority = TodoPriority.valueOf(importance),
    lastUpdatedBy = lastUpdatedBy,
    text = text,
)

fun TodoResponse.map() = TodoModel(
    changedAt = changed_at,
    color = color,
    createdAt = created_at,
    deadline = deadline,
    done = done,
    id = id,
    priority = getTodoPriorityFromString(importance),
    lastUpdatedBy = last_updated_by,
    text = text,
)

