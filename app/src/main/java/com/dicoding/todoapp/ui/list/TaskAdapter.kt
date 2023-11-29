package com.dicoding.todoapp.ui.list

import android.content.*
import android.view.*
import android.widget.*
import androidx.paging.*
import androidx.recyclerview.widget.*
import com.dicoding.todoapp.*
import com.dicoding.todoapp.data.*
import com.dicoding.todoapp.ui.detail.*
import com.dicoding.todoapp.utils.*

@Suppress("UNREACHABLE_CODE")
class TaskAdapter(
    private val onCheckedChange: (Task, Boolean) -> Unit
) : PagedListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    //TODO 8 : Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
        throw NotImplementedError("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position) as Task
        //TODO 9 : Bind data to ViewHolder (You can run app to check)
        holder.bind(task)
        when {
            //TODO 10 : Display title based on status using TitleTextView
            task.isCompleted -> {
                //DONE
                holder.cbComplete.isChecked = true
                holder.tvTitle.state = 1
            }

            task.dueDateMillis < System.currentTimeMillis() -> {
                //OVERDUE
                holder.cbComplete.isChecked = false
                holder.tvTitle.state = 2
            }

            else -> {
                //NORMAL
                holder.cbComplete.isChecked = false
                holder.tvTitle.state = 0
            }
        }
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TaskTitleView = itemView.findViewById(R.id.item_tv_title)
        val cbComplete: CheckBox = itemView.findViewById(R.id.item_checkbox)
        private val tvDueDate: TextView = itemView.findViewById(R.id.item_tv_date)

        lateinit var getTask: Task

        fun bind(task: Task) {
            getTask = task
            tvTitle.text = task.title
            tvDueDate.text = DateConverter.convertMillisToString(task.dueDateMillis)
            itemView.setOnClickListener {
                val detailIntent = Intent(itemView.context, DetailTaskActivity::class.java)
                detailIntent.putExtra(TASK_ID, task.id)
                itemView.context.startActivity(detailIntent)
            }
            cbComplete.setOnClickListener {
                onCheckedChange(task, !task.isCompleted)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }
        }
    }
}