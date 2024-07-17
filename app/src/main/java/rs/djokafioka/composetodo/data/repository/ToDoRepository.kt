package rs.djokafioka.composetodo.data.repository

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import rs.djokafioka.composetodo.data.ToDoDao
import rs.djokafioka.composetodo.data.models.ToDoTask
import javax.inject.Inject

/**
 * Created by Djordje on 6.4.2024..
 */
@ViewModelScoped
class ToDoRepository @Inject constructor(
    private val toDoDao: ToDoDao
) {
    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTasks()
    val sortByLowPriority = toDoDao.sortByLowPriority()
    val sortByHighPriority = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask> {
        return toDoDao.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDao.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        toDoDao.updateTask(toDoTask)
    }

    suspend fun deleteTask(toDoTask: ToDoTask) {
        toDoDao.deleteTask(toDoTask)
    }

    suspend fun deleteAllTasks() {
        toDoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String) =
        toDoDao.searchDatabase(searchQuery)
}