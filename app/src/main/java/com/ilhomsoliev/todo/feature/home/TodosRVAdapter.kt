package com.ilhomsoliev.todo.feature.home

import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ilhomsoliev.todo.R
import com.ilhomsoliev.todo.core.formatDate
import com.ilhomsoliev.todo.data.models.TodoItemModel
import com.ilhomsoliev.todo.data.models.TodoPriority

class TodosRVAdapter(
    private val onClick: (TodoItemModel) -> Unit,
    private val onUpdate: (TodoItemModel) -> Unit,
    private val onAddClick: () -> Unit,
) : ListAdapter<TodoItemModel, RecyclerView.ViewHolder>(TodoDiffCallback()) {

    companion object {
        private const val ITEM_TYPE_TODO = 0
        private const val ITEM_TYPE_ADD = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) ITEM_TYPE_ADD else ITEM_TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_TODO) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
            TodoViewHolder(view, onClick, onUpdate)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add, parent, false)
            AddViewHolder(view, onAddClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    class TodoViewHolder(
        itemView: View,
        private val onClick: (TodoItemModel) -> Unit,
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
            checkBox.isChecked = todo.isCompleted
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
            // Apply the tint based on API level
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                checkBox.buttonTintList = colorStateList
            } else {
                CompoundButtonCompat.setButtonTintList(checkBox, colorStateList)
            }

        }
    }

    class AddViewHolder(
        itemView: View,
        private val onAddClick: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onAddClick()
            }
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