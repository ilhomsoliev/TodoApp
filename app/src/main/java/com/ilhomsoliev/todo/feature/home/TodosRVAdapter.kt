package com.ilhomsoliev.todo.feature.home

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.core.formatDate
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority
import com.ilhomsoliev.todo.feature.home.TodosRVAdapter.TodoViewHolder

class TodosRVAdapter(
    private val onClick: (TodoItemModel) -> Unit,
    private val onDelete: (TodoItemModel) -> Unit,
    private val onUpdate: (TodoItemModel) -> Unit,
) : ListAdapter<TodoItemModel, TodoViewHolder>(TodoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false),
            onClick,
            onDelete,
            onUpdate,
        )

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TodoViewHolder(
        itemView: View,
        private val onClick: (TodoItemModel) -> Unit,
        private val onDelete: (TodoItemModel) -> Unit,
        private val onUpdate: (TodoItemModel) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val taskTextView: TextView = itemView.findViewById(R.id.textViewTaskText)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        private val priorityIconImageView: ImageView =
            itemView.findViewById(R.id.imageViewTodoPriority)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        private var currentTodo: TodoItemModel? = null

        init {
            itemView.setOnClickListener {
                currentTodo?.let {
                    onClick(it)
                }
            }
            itemView.setOnLongClickListener { view ->
                currentTodo?.let {
                    showPopupMenu(view, it)
                }
                true
            }

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                currentTodo?.let {
                    onUpdate(it.copy(isCompleted = isChecked))
                }
            }
        }

        fun bind(todo: TodoItemModel) {
            currentTodo = todo
            taskTextView.text = todo.text
            if (todo.editedDate != null) {
                dateTextView.text = formatDate(todo.editedDate)
                dateTextView.visibility = View.VISIBLE
            } else {
                dateTextView.visibility = View.GONE
            }
            if (todo.isCompleted) {
                taskTextView.paintFlags = taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                taskTextView.setTextColor(
                    itemView.context.resources.getColor(
                        R.color.labelPrimary,
                        null
                    )
                )
            } else {
                taskTextView.paintFlags =
                    taskTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                taskTextView.setTextColor(
                    itemView.context.resources.getColor(
                        R.color.labelPrimary,
                        null
                    )
                )
            }
            when (todo.priority) {
                TodoPriority.HIGH -> {
                    priorityIconImageView.visibility = View.VISIBLE
                    priorityIconImageView.setImageResource(R.drawable.high_priority)
                }

                TodoPriority.LOW -> {
                    priorityIconImageView.visibility = View.VISIBLE
                    priorityIconImageView.setImageResource(R.drawable.low_priority)
                }

                else -> {
                    priorityIconImageView.visibility = View.GONE
                }
            }

            // CheckBox
            val uncheckedColorHighPriority = ContextCompat.getColor(itemView.context, R.color.red)
            val uncheckedColor = ContextCompat.getColor(itemView.context, R.color.supportSeparator)
            val checkedColor = ContextCompat.getColor(itemView.context, R.color.green)
            val colorStateList = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked), // unchecked state
                    intArrayOf(android.R.attr.state_checked)  // checked state
                ),
                intArrayOf(
                    if (todo.priority == TodoPriority.HIGH) uncheckedColorHighPriority else uncheckedColor, // unchecked color
                    checkedColor // checked color
                )
            )
            checkBox.buttonTintList = colorStateList
            checkBox.isChecked = todo.isCompleted
        }

        private fun showPopupMenu(view: View, item: TodoItemModel) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.todo_item_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_item_edit -> {
                        onClick(item)
                        true
                    }

                    R.id.menu_item_delete -> {
                        onDelete(item)
                        true
                    }

                    R.id.menu_item_mark_as_done -> {
                        onUpdate(item)
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    class TodoDiffCallback : DiffUtil.ItemCallback<TodoItemModel>() {

        override fun areItemsTheSame(oldItem: TodoItemModel, newItem: TodoItemModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItemModel, newItem: TodoItemModel): Boolean {
            return oldItem == newItem
        }
    }
}