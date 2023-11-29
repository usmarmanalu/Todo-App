package com.dicoding.todoapp.ui.add

import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import com.dicoding.todoapp.*
import com.dicoding.todoapp.data.*
import com.dicoding.todoapp.ui.*
import com.dicoding.todoapp.ui.list.*
import com.dicoding.todoapp.utils.*
import com.google.android.material.textfield.*
import java.text.*
import java.util.*

class AddTaskActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        supportActionBar?.title = getString(R.string.add_task)

        val factory = ViewModelFactory.getInstance(this)
        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                //TODO 12 : Create AddTaskViewModel and insert new task to database

                val edTitle = findViewById<TextInputEditText>(R.id.add_ed_title).text.toString()
                val edDescription =
                    findViewById<TextInputEditText>(R.id.add_ed_description).text.toString()
                val tvDueDate = findViewById<TextView>(R.id.add_tv_due_date).text

                when {
                    edTitle.isEmpty() -> false
                    edDescription.isEmpty() -> false
                    tvDueDate == getString(R.string.due_date) -> false
                    else -> {
                        taskViewModel.insertTask(Task(0, edTitle, edDescription, dueDateMillis))
                        finish()
                        true
                    }
                }
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}