package com.ilhomsoliev.todo.core


import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilhomsoliev.todo.shared.snackbar.model.SnackbarMessage
import com.ilhomsoliev.todo.shared.snackbar.model.UserMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseSharedViewModel<State : Any, Action, Event>(initialState: State) :
    ViewModel() {

    private val _viewStates = MutableStateFlow(initialState)

    private val _viewActions =
        MutableSharedFlow<Action?>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    fun viewStates(): WrappedStateFlow<State> = WrappedStateFlow(_viewStates.asStateFlow())

    fun viewActions(): WrappedSharedFlow<Action?> =
        WrappedSharedFlow(_viewActions.asSharedFlow())

    protected var viewState: State
        get() = _viewStates.value
        set(value) {
            _viewStates.value = value
        }

    protected var viewAction: Action?
        get() = _viewActions.replayCache.last()
        set(value) {
            _viewActions.tryEmit(value)
        }

    abstract fun obtainEvent(viewEvent: Event)

    /**
     * Convenient method to perform work in [viewModelScope] scope.
     */
    protected fun withViewModelScope(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context = context, block = block)
    }

    fun clear() {
        //coroutineTags.forEach { it.value.cancel() }
        onCleared()
    }

    // Snackbar
    private val _snackbarMessage = MutableStateFlow<SnackbarMessage?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    internal fun showSnackbarMessage(
        message: String,
        actionLabel: String? = null,
        onActionPerformed: () -> Unit = {},
    ) {
        _snackbarMessage.value = SnackbarMessage.from(
            userMessage = UserMessage.from(message),
            actionLabelMessage = if (actionLabel != null) UserMessage.from(actionLabel) else null,
            withDismissAction = true,
            onSnackbarResult = {
                if (it == SnackbarResult.ActionPerformed)
                    onActionPerformed()
            }
        )
    }

    fun dismissSnackbar() = run { _snackbarMessage.value = null }

    fun showSnackbarMessage(message: String) {
        _snackbarMessage.value = SnackbarMessage.from(
            userMessage = UserMessage.from(message),
        )
    }

}

public class WrappedStateFlow<T : Any>(private val origin: StateFlow<T>) : StateFlow<T> by origin {
    public fun watch(block: (T) -> Unit): Closeable = watchFlow(block)
}

public class WrappedSharedFlow<T : Any?>(private val origin: SharedFlow<T>) :
    SharedFlow<T> by origin {
    public fun watch(block: (T) -> Unit): Closeable = watchFlow(block)
}

private fun <T> Flow<T>.watchFlow(block: (T) -> Unit): Closeable {
    val context = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    onEach(block).launchIn(context)

    return object : Closeable {
        override fun close() {
            context.cancel()
        }
    }
}

public interface Closeable {
    public fun close()
}