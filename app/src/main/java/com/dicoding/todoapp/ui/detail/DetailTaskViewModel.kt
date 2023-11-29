package com.dicoding.todoapp.ui.detail

import androidx.lifecycle.*
import com.dicoding.todoapp.data.*
import kotlinx.coroutines.*

class DetailTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val _taskId = MutableLiveData<Int>()

    private val _task = _taskId.switchMap { id ->
        taskRepository.getTaskById(id)
    }
    val task: LiveData<Task> = _task

    fun setTaskId(taskId: Int?) {
        if (taskId == _taskId.value) {
            return
        }
        _taskId.value = taskId!!
    }

    fun deleteTask() {
        viewModelScope.launch {
            _task.value?.let { taskRepository.deleteTask(it) }
        }
    }
}