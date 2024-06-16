package com.ilhomsoliev.todo.feature.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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
import com.ilhomsoliev.todo.feature.home.repository
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
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToViewState()
        subscribeToActions()
        setupListeners()
    }

    private fun subscribeToViewState() {
        lifecycleScope.launch {
            viewModel.viewStates().collectLatest { state: AddViewState ->
                with(binding) {
                    textViewPriority.text = state.priority.nameRu

                    if (state.deadline == null) {
                        textViewDeadline.visibility = View.INVISIBLE
                    } else {
                        textViewDeadline.visibility = View.VISIBLE
                        textViewDeadline.text = formatDate(state.deadline)
                    }
                }
            }
        }
    }

    private fun subscribeToActions() {
        lifecycleScope.launch {
            viewModel.viewActions().collectLatest { action ->
                action.printToLog("Here 1r")

                when (action) {
                    is AddAction.NavigateBack -> popBack()
                    is AddAction.SetTodoDescription -> {
                        action.text.printToLog("Here")
                        binding.editTextDescription.setText(action.text)
                    }

                    null -> {}
                }
            }
        }
    }

    private fun setupListeners() {
        with(binding) {
            textViewSave.setOnClickListener {
                viewModel.obtainEvent(AddEvent.Add(editTextDescription.text.toString()))
            }

            iconClose.setOnClickListener {
                popBack()
            }

            layoutRemoveAddTodo.setOnClickListener {
                popBack()
            }

            layoutDeadlinePicker.setOnClickListener {
                showDatePickerDialog { timeInMillis ->
                    viewModel.obtainEvent(AddEvent.DeadlineChange(timeInMillis))
                }
            }
            switchDeadline.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showDatePickerDialog { timeInMillis ->
                        viewModel.obtainEvent(AddEvent.DeadlineChange(timeInMillis))
                    }
                    viewModel.obtainEvent(AddEvent.DeadlineSwitchClick(true))
                } else {
                    viewModel.obtainEvent(AddEvent.DeadlineSwitchClick(false))
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
        }

    }

    private fun showDatePickerDialog(onDateSet: (currentTimeMillis: Long) -> Unit) {
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
                day
            )

        datePickerDialog.show()
    }

    private fun popBack() {
        (activity as MainActivity).navigateToHomeFragment()
    }

}