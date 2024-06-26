package com.ilhomsoliev.todo.feature.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.app.MainActivity
import com.ilhomsoliev.todo.core.formatDate
import com.ilhomsoliev.todo.core.printToLog
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.databinding.FragmentAddBinding
import com.ilhomsoliev.todo.feature.add.model.AddAction
import com.ilhomsoliev.todo.feature.add.model.AddEvent
import com.ilhomsoliev.todo.feature.add.model.AddViewState
import com.ilhomsoliev.todo.feature.add.views.AddDisplay
import com.ilhomsoliev.todo.feature.home.repository
import com.ilhomsoliev.todo.shared.showCustomSnackbar
import com.ilhomsoliev.todo.shared.theme.TodoTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


class AddFragment : Fragment() {


    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(id: String): AddFragment {
            val fragment = AddFragment()
            val args = Bundle()
            args.putString(ARG_ID, id)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: AddViewModel by viewModels { AddViewModelFactory(repository) }

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val id = arguments?.getString(ARG_ID)
        id?.let {
            viewModel.obtainEvent(AddEvent.EnterScreen(id))
        }
        return ComposeView(requireContext()).apply {
            setContent {
                TodoTheme {
                    AddScreen()
                }
            }
        }
    }

    @Composable
    fun AddScreen() {
        val state by viewModel.viewStates().collectAsState()
        val actions by viewModel.viewActions().collectAsState(initial = null)

        LaunchedEffect(key1 = actions) {
            when (val action = actions) {
                is AddAction.NavigateBack -> popBack()
                is AddAction.ShowSnackbar -> {
                    view?.let { showCustomSnackbar(it, action.text) }
                }

                null -> {}
            }
        }

        AddDisplay(
            state = state,
            callback = {
                when (it) {
                    is AddEvent.OnBack -> popBack()
                    else -> viewModel.obtainEvent(it)
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*subscribeToViewState()
        subscribeToActions(view)
        setupListeners()*/
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            viewModel.viewStates().collectLatest { state: AddViewState ->
                with(binding) {
                    editTextDescription.setText(state.text)
                    textViewPriority.text = state.priority.nameRu
                    if (state.deadline == null) {
                        textViewDeadline.visibility = View.INVISIBLE
                    } else {
                        textViewDeadline.visibility = View.VISIBLE
                        textViewDeadline.text = formatDate(state.deadline)
                    }
                    if (state.deadline != null) {
                        switchDeadline.isChecked = true
                    } else {
                        switchDeadline.isChecked = false
                    }
                }
            }
        }
    }

    private fun subscribeToActions(view: View) {
        lifecycleScope.launch {
            viewModel.viewActions().collectLatest { action ->

            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            textViewSave.setOnClickListener {
                viewModel.obtainEvent(
                    AddEvent.Add
                )
            }

            iconClose.setOnClickListener {
                popBack()
            }

            layoutRemoveAddTodo.setOnClickListener {
                viewModel.obtainEvent(AddEvent.Delete)
            }

            switchDeadline.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showDatePickerDialog({ timeInMillis ->
                        viewModel.obtainEvent(AddEvent.DeadlineChange(timeInMillis))
                    }) {
//                        if()
                    }
                } else {
                    viewModel.obtainEvent(AddEvent.DeadlineChange(null))
                }
            }

            layoutPriorityPicker.setOnClickListener {
                val popupMenu = PopupMenu(root.context, layoutPriorityPicker)
                popupMenu.menuInflater.inflate(R.menu.priority_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    val priority = when (menuItem.itemId) {
                        R.id.menu_item_usual_priority -> TodoPriority.USUAL
                        R.id.menu_item_low_priority -> TodoPriority.LOW
                        R.id.menu_item_high_priority -> TodoPriority.HIGH
                        else -> TodoPriority.USUAL
                    }
                    viewModel.obtainEvent(AddEvent.PriorityChange(priority))
                    true
                }
                popupMenu.show()
            }

            editTextDescription.onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    hasFocus.printToLog("Hello ")
                    if (!hasFocus) {
                        viewModel.obtainEvent(AddEvent.TextChange(editTextDescription.text.toString()))
                    }
                }
        }
    }

    private fun showDatePickerDialog(
        onDateSet: (currentTimeMillis: Long) -> Unit,
        onCancelled: () -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(
                binding.root.context,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    val selectedDateCalendar = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDayOfMonth)
                    }
                    val timeInMillis = selectedDateCalendar.timeInMillis
                    onDateSet(timeInMillis)
                },
                year,
                month,
                day,
            )
        datePickerDialog.setOnCancelListener {
            onCancelled()
        }
        datePickerDialog.show()
    }

    private fun popBack() {
        (activity as MainActivity).navigator.navigateToHomeFragment()
    }

}