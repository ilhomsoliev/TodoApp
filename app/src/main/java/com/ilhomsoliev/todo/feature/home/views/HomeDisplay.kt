package com.ilhomsoliev.todo.feature.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState
import com.ilhomsoliev.todo.shared.SwipeableItem
import com.ilhomsoliev.todo.shared.theme.AppTheme
import com.ilhomsoliev.todo.shared.theme.TodoTheme

@Composable
@Preview
fun HomeDisplayPreview() {
    TodoTheme {
        HomeDisplay(state = HomeViewState(todos = TodoItemModel.demos)) {}
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeDisplay(
    state: HomeViewState,
    callback: (HomeEvent) -> Unit,
) {
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = AppTheme.colorScheme.backPrimary,
        topBar = {
            HomeTopBar(
                completedItemsCount = state.completedCount,
                showCompleted = state.isShowCompletedEnabled
            ) {
                callback(HomeEvent.ToggleIsCompletedVisible)
            }
        },
        floatingActionButton = {
            FloatingActionButton(containerColor = AppTheme.colorScheme.blue, onClick = {
                callback(HomeEvent.AddClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_icon),
                    tint = Color.White
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(horizontal = 12.dp),
            state = listState,
        ) {
            itemsIndexed(state.todos, key = { _, item -> item.id }) { index, item ->
                SwipeableItem(
                    Modifier.clip(
                        if (index == 0) RoundedCornerShape(
                            topEnd = 12.dp,
                            topStart = 12.dp
                        ) else RoundedCornerShape(0.dp)
                    ), onSwipeLeft = {
                        callback(HomeEvent.DeleteItem(item.id))
                    }, onSwipeRight = {
                        callback(HomeEvent.MarkItem(item.id))
                    }) {
                    TodoItem(item, onCheckedChange = {
                        callback(HomeEvent.MarkItem(item.id))
                    }, isFirst = index == 0, onClick = {
                        callback(HomeEvent.ItemClick(item.id))
                    })
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 12.dp,
                                bottomStart = 12.dp
                            )
                        )
                        .background(AppTheme.colorScheme.backSecondary)
                        .clickable { callback(HomeEvent.AddClick) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        modifier = Modifier.alpha(0f),
                        checked = false, onCheckedChange = {}
                    )
                    Text(
                        text = stringResource(R.string.new_one),
                        color = AppTheme.colorScheme.labelTertiary
                    )
                }
            }
        }
    }

}