package com.example.assignment_5

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_5.databinding.TaskItemBinding


class TasksAdapter(private val items: List<TaskItem>) : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTask: ImageView = itemView.findViewById(R.id.imgtask)
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textViewDueDate: TextView = itemView.findViewById(R.id.textViewDueDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]


        holder.textCategory.text = item.category
        holder.textTitle.text = item.title
        holder.textViewDueDate.text = item.date

        if (item.imageByteArray != null) {
            val bitmap = TaskItem.byteArrayToBitmap(item.imageByteArray)
            holder.imageTask.setImageBitmap(bitmap)
        } else {
            holder.imageTask.setImageResource(R.drawable.briefcase)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}