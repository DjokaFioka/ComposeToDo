package rs.djokafioka.composetodo.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.data.models.ToDoTask
import rs.djokafioka.composetodo.data.repository.DataStoreRepository
import rs.djokafioka.composetodo.data.repository.ToDoRepository
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.Constants.MAX_TITLE_LENGTH
import rs.djokafioka.composetodo.util.RequestState
import rs.djokafioka.composetodo.util.SearchAppBarState
import javax.inject.Inject

/**
 * Created by Djordje on 6.4.2024..
 */
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

//    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)
    var action by mutableStateOf(Action.NO_ACTION)
        private set

//    val id: MutableState<Int> = mutableStateOf(0)
    var id by mutableIntStateOf(0)
        private set
//    val title: MutableState<String> = mutableStateOf("")
    var title by mutableStateOf("")
        private set
//    val description: MutableState<String> = mutableStateOf("")
    var description by mutableStateOf("")
        private set
//    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    var priority by mutableStateOf(Priority.LOW)
        private set

//    val searchAppBarState: MutableState<SearchAppBarState> =
//        mutableStateOf(SearchAppBarState.CLOSED)
    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    //    val searchTextState: MutableState<String> = mutableStateOf("")
    var searchTextState by mutableStateOf("")
        private set

    private val _searchTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTasks = _searchTasks.asStateFlow()

    private val _sortingState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortingState

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    //Changed because we always displayed a sad face even for a half a second
    // before displaying the list of task, no matter if the list was empty or not
    //    private val _allTasks = MutableStateFlow<List<ToDoTask>>(emptyList())
    val allTasks = _allTasks.asStateFlow()
    //isto sto i ovo
    //val allTasks: StateFlow<List<ToDoTask>> = _allTasks

    init {
        getAllTasks()
        readSortingState()
    }

    fun searchTasks(searchQuery: String) {
        _searchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = searchQuery)
                    .collect { searchedTasks ->
                        _searchTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchTasks.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    private fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect { tasks ->
                    _allTasks.value = RequestState.Success(tasks)
                    //Chenged when we wrapped the result in RequestState
//                _allTasks.value = tasks
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> =
        MutableStateFlow(null)
    val selectedTask = _selectedTask.asStateFlow()

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repository.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private fun readSortingState() {
        _sortingState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortingState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect {
                        _sortingState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortingState.value = RequestState.Error(e)
        }
    }

    fun saveSortingState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSortingState(priority)
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(toDoTask = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        Log.d("SharedViewModel", "handleDatabaseActions: Triggered")
        when (action) {
            Action.ADD -> {
                addTask()
//                updateAction(Action.NO_ACTION)
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {

            }
        }
        //we don't need this after we moved handleDatabaseActions from the ListScreen
        //this.action.value = Action.NO_ACTION
    }

    fun updateTaskFields(task: ToDoTask?) {
        if (task != null) {
            id = task.id
            title = task.title
            description = task.description
            priority = task.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String) {
        if (newTitle.length < MAX_TITLE_LENGTH) {
            title = newTitle
        }
    }

    fun updateAction(newAction: Action) {
        action = newAction
    }

    fun updateDescription(newDescription: String) {
        description = newDescription
    }

    fun updatePriority(newPriority: Priority) {
        priority = newPriority
    }

    fun updateAppBarState(newState: SearchAppBarState) {
        Log.d("MyTAG", "updateAppBarState: Clicked")
        searchAppBarState = newState
    }

    fun updateSearchTextState(newText: String) {
        searchTextState = newText
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }
}