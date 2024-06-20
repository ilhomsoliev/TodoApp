package com.ilhomsoliev.todo.feature.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


open class SwipeToDeleteCallback(
    private val adapter: TodosRVAdapter,
    private val onSwipeLeft: (String) -> Unit,
    private val onSwipeRight: (String) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deletePaint = Paint().apply {
        color = Color.RED
    }

    private val donePaint = Paint().apply {
        color = Color.GREEN
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if(viewHolder is TodosRVAdapter.AddViewHolder) return false
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(viewHolder is TodosRVAdapter.AddViewHolder) return

        val position = viewHolder.layoutPosition
        val id = adapter.currentList[position].id
        if (direction == ItemTouchHelper.LEFT) {
            onSwipeLeft(id)
        } else if (direction == ItemTouchHelper.RIGHT) {
            onSwipeRight(id)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        if(viewHolder is TodosRVAdapter.AddViewHolder) return

        if (dX > 0) { // Swiping to the right
            val limitedDX =
                dX.coerceAtMost(itemView.width / 4f)
            c.drawRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left.toFloat() + limitedDX,
                itemView.bottom.toFloat(),
                donePaint
            )

            itemView.translationX = limitedDX
        } else if (dX < 0) { // Swiping to the left
            c.drawRect(
                itemView.right.toFloat() + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                deletePaint
            )
            itemView.translationX = dX
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        if(viewHolder is TodosRVAdapter.AddViewHolder) return super.getSwipeThreshold(viewHolder)
        return if (viewHolder.itemView.translationX > 0) 10f else super.getSwipeThreshold(viewHolder)
    }
}