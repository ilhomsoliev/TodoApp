package com.ilhomsoliev.todo.domain.models

import com.ilhomsoliev.todo.data.source.local.dto.TodoEntity
import com.ilhomsoliev.todo.data.source.remote.models.request.TodoRequest
import com.ilhomsoliev.todo.data.source.remote.models.response.list.TodoResponse

data class TodoModel(
    val changedAt: Long,
    val color: String,
    val createdAt: Long,
    val deadline: Long,
    val done: Boolean,
    val id: String,
    val importance: String,
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
        importance = importance,
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
        importance = importance,
        lastUpdatedBy = lastUpdatedBy,
        text = text,
    )
}

fun TodoEntity.map() = TodoModel(
    changedAt = changedAt,
    color = color,
    createdAt = createdAt,
    deadline = deadline,
    done = done,
    id = id,
    importance = importance,
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
    importance = importance,
    lastUpdatedBy = last_updated_by,
    text = text,
)

