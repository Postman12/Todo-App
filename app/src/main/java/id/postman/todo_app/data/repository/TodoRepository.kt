package id.postman.todo_app.data.repository

import androidx.lifecycle.LiveData
import id.postman.todo_app.data.TodoDAO
import id.postman.todo_app.data.model.TodoData

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllData: LiveData<List<TodoData>> = todoDAO.getAllData()
    val sortByHighPriority: LiveData<List<TodoData>> = todoDAO.sortByHighPriority()
    val sortByLowPriority: LiveData<List<TodoData>> = todoDAO.sortByLowPriority()

    suspend fun insertData(todoData: TodoData) {
        todoDAO.insertData(todoData)
    }

    suspend fun updateData(todoData: TodoData) {
        todoDAO.updateData(todoData)
    }

    fun searchData(searchQuery: String): LiveData<List<TodoData>> {
        return todoDAO.searchData(searchQuery)
    }

    suspend fun deleteData(todoData: TodoData) {
        todoDAO.deleteData(todoData)
    }

    suspend fun deleteAll() {
        todoDAO.deleteAll()
    }
}