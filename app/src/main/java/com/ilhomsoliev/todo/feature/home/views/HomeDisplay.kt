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
    /*val context = LocalContext.current

    val motionScene = remember {
        """
        {
          ConstraintSets: {
            start: {
              topBar: {
                width: "spread",
                height: 100,
                start: ['parent', 'start', 16],
                end: ['parent', 'end', 16],
                top: ['parent', 'top', 16]
              }
            },
            end: {
              topBar: {
                width: "spread",
                height: 56,
                start: ['parent', 'start', 16],
                end: ['parent', 'end', 16],
                top: ['parent', 'top', 16]
              }
            }
          },
          Transitions: {
            default: {
              from: 'start',
              to: 'end',
              pathMotionArc: 'none',
              KeyFrames: {
                KeyAttributes: [
                  {
                    target: ['topBar'],
                    frames: [25, 50, 75, 100],
                    alpha: [1, 0.75, 0.5, 0]
                  }
                ]
              }
            }
          }
        }
        """
    }
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        val scrollOffset = listState.firstVisibleItemScrollOffset
        progress = (scrollOffset / 600f).coerceIn(0f, 1f)
    }*/
    Scaffold(
        containerColor = AppTheme.colorScheme.backPrimary,
        topBar = {
            HomeTopBar(completedItemsCount = state.completedCount, state.isShowCompletedEnabled) {
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
            itemsIndexed(state.todos, key = { index, item -> item.id }) { index, item ->
                TodoItem(item, onCheckedChange = {
                    callback(HomeEvent.MarkItem(item.id))
                }, isFirst = index == 0, onClick = {
                    callback(HomeEvent.ItemClick(item.id))
                })
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