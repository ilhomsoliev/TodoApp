package com.ilhomsoliev.todo.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.app.MainActivity
import com.ilhomsoliev.todo.data.repository.TodoItemsRepository
import com.ilhomsoliev.todo.data.repository.TodoItemsRepositoryImpl
import com.ilhomsoliev.todo.databinding.FragmentHomeBinding
import com.ilhomsoliev.todo.feature.home.models.HomeAction
import com.ilhomsoliev.todo.feature.home.models.HomeEvent
import com.ilhomsoliev.todo.feature.home.models.HomeViewState
import com.ilhomsoliev.todo.shared.showCustomSnackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

val repository: TodoItemsRepository = TodoItemsRepositoryImpl()

/*interface NavProvider {
    val navigator: MainActivity.Navigator
}


@Singleton
class NavProviderImpl: NavProvider {
    override lateinit var navigator: MainActivity.Navigator
        private set
    fun set(nav: MainActivity.Navigator) {
        navigator = nav
    }
}*/

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(repository) }

    private lateinit var todoAdapter: TodosRVAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        subscribeToViewState()
        subscribeToActions(view)
        setupListeners()
    }

    private fun initViews() {
        todoAdapter = TodosRVAdapter(
            onClick = { todo ->
                val id = todo.id
                (activity as MainActivity).navigator.navigateToAddFragment(id)
            },
            onUpdate = { todo ->
                viewModel.obtainEvent(HomeEvent.MarkItem(todo.id))
            },
            onDelete = {
                viewModel.obtainEvent(HomeEvent.DeleteItem(it.id))
            }
        )

        binding.recyclerViewTodos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }

        val swipeHandler = object : SwipeToDeleteCallback(
            adapter = todoAdapter,
            onSwipeLeft = { id ->
                viewModel.obtainEvent(HomeEvent.DeleteItem(id))
            },
            onSwipeRight = { id ->
                viewModel.obtainEvent(HomeEvent.MarkItem(id))
            }
        ) {}

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTodos)
    }

    private fun subscribeToViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewStates().collectLatest { state: HomeViewState ->
                with(binding) {
                    todoAdapter.submitList(state.todos)
                    textViewCompletedTodosCount.text = "Выполнено — ${state.completedCount}"
                    iconIsCompletedVisible.setImageResource(
                        if (state.isShowCompletedEnabled) R.drawable.eye_off else R.drawable.eye_active
                    )
                }
            }
        }
    }

    private fun subscribeToActions(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewActions().onEach { action ->
                when (action) {
                    is HomeAction.ShowSnackbar -> showCustomSnackbar(view, action.text)
                    null -> {}
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            floatingActionButtonAdd.setOnClickListener {
                (activity as MainActivity).navigator.navigateToAddFragment("-1")
            }

            iconIsCompletedVisible.setOnClickListener {
                viewModel.obtainEvent(HomeEvent.ToggleIsCompletedVisible)
            }
            layoutAddNewTodo.root.setOnClickListener {
                (activity as MainActivity).navigator.navigateToAddFragment("-1")
            }
        }
    }
}