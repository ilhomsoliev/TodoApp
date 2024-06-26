package com.ilhomsoliev.todo.feature.home.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState

@Composable
fun HomeDisplay(
    state: HomeViewState,
    callback: (HomeEvent) -> Unit,
) {
    Scaffold(
        topBar = {}
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
        ) {
            itemsIndexed(state.todos, key = { index, item -> item.id }) { index, item ->
                TodoItem(item, onCheckedChange = {
                    callback(HomeEvent.MarkItem(item.id))
                }, onClick = {
                    callback(HomeEvent.ItemClick(item.id))
                })
            }
            item {
                Text(text = "Noveye")
            }
        }
    }

}