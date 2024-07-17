package rs.djokafioka.composetodo.ui.screens.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.SearchAppBarState

/**
 * Created by Djordje on 7.4.2024..
 */

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    //Removing this LaunchedEffect because it is triggered on screen rotation and it shouldn't be
//    LaunchedEffect(key1 = true) {
////        Log.d("ListScreen", "LaunchedEffect triggered")
//        sharedViewModel.getAllTasks()
//        sharedViewModel.readSortingState()
//    }
    Log.d("MyTAG", "ListScreen: Composition")
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseActions(action = action)
    }

    //val action by sharedViewModel.action //we moved it to ListComposable to observe it

    val allTasks by sharedViewModel.allTasks.collectAsState() //returns list
    val searchedTasks by sharedViewModel.searchTasks.collectAsState() //returns list
//    val allTasks = sharedViewModel.allTasks.collectAsState() //returns state
//    for (task in allTasks.value) {
//        Log.d("ListScreen", task.title)
//    }

    val sortingState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

//    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    DisplaySnackbar(
        snackbarHostState = snackBarHostState,
        onComplete = {
//            sharedViewModel.action.value = it
            sharedViewModel.updateAction(it)
        },
        handleDatabaseActions = {
            sharedViewModel.handleDatabaseActions(action = action)
        },
        onUndoClicked = {
//            sharedViewModel.action.value = it
            sharedViewModel.updateAction(it)
        },
//        taskTitle = sharedViewModel.title.value,
        taskTitle = sharedViewModel.title,
        action = action
    )

//    sharedViewModel.handleDatabaseActions(action = action)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        },
//        content = { padding ->
//            ListContent(
//                modifier = Modifier
//                    .padding(
//                        top = padding.calculateTopPadding(),
//                        bottom = padding.calculateBottomPadding()
//                    ),
//                allTasks = allTasks,
//                searchedTasks = searchedTasks,
//                lowPriorityTasks = lowPriorityTasks,
//                highPriorityTasks = highPriorityTasks,
//                sortingState = sortingState,
//                searchAppBarState = searchAppBarState,
//                onSwipeToDelete = { action, task ->
////                    sharedViewModel.action.value = action
//                    sharedViewModel.updateAction(action)
//                    sharedViewModel.updateTaskFields(task = task)
////                    snackBarHostState.snackbarHostState.currentSnackbarData?.dismiss()
//                    snackBarHostState.currentSnackbarData?.dismiss()
//                },
//                navigateToTaskScreen = navigateToTaskScreen
//            )
//        },
        content = { padding ->
            ListContent(
                modifier = Modifier.padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortingState = sortingState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(task = task)
                    snackBarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
//        shape = CircleShape,
//        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_new_task),
//            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackbar(
    snackbarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    //handleDatabaseActions() //bug, since it is outside the LaunchedEffect

    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            val snackBarResult = snackbarHostState
                .showSnackbar(
                    message = setMessage(
                        action = action,
                        taskTitle = taskTitle
                    ),
                    actionLabel = setActionLabel(action),
                    duration = SnackbarDuration.Short
                )
            if (snackBarResult == SnackbarResult.ActionPerformed
                && action == Action.DELETE) {
                onUndoClicked(Action.UNDO)
            } else if (snackBarResult == SnackbarResult.Dismissed ||
                action != Action.DELETE) {
                onComplete(Action.NO_ACTION)
            }
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String
): String {
    return when (action) {
        Action.DELETE_ALL -> "All tasks removed."
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == Action.DELETE.name) {
        "UNDO"
    } else {
        "OK"
    }
}

//@Preview
//@Composable
//fun ListScreenPreview() {
//    ListScreen(navigateToTaskScreen = {})
//}