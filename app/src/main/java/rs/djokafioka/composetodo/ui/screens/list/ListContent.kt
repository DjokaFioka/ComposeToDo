package rs.djokafioka.composetodo.ui.screens.list

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.data.models.ToDoTask
import rs.djokafioka.composetodo.ui.theme.*
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.RequestState
import rs.djokafioka.composetodo.util.SearchAppBarState

/**
 * Created by Djordje on 9.4.2024..
 */

@ExperimentalAnimationApi
@Composable
fun ListContent(
    modifier: Modifier = Modifier,
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortingState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Log.d("MyTAG", "ListContent: Composition")
    Log.d("MyTAG", "ListContent: searchAppBarState = " + searchAppBarState)
    if (sortingState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchedTasks is RequestState.Success) {
                    HandleListContent(
                        modifier = modifier,
                        tasks = searchedTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortingState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        modifier = modifier,
                        tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
            sortingState.data == Priority.LOW -> {
                HandleListContent(
                    modifier = modifier,
                    tasks = lowPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
            sortingState.data == Priority.HIGH -> {
                HandleListContent(
                    modifier = modifier,
                    tasks = highPriorityTasks,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
    }

//    if (allTasks is RequestState.Success) {
//        if (allTasks.data.isEmpty()) {
//            EmptyContent()
//        } else {
//            DisplayTasks(
//                tasks = allTasks.data,
//                navigateToTaskScreen = navigateToTaskScreen
//            )
//        }
//    }

    //This was before we wrapped the response into RequestState
//    if (tasks.isEmpty()) {
//        EmptyContent()
//    } else {
//        DisplayTasks(
//            tasks = tasks,
//            navigateToTaskScreen = navigateToTaskScreen
//        )
//    }
}

@ExperimentalAnimationApi
@Composable
fun HandleListContent(
    modifier: Modifier = Modifier,
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasks(
            modifier = modifier,
            tasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun DisplayTasks(
    modifier: Modifier = Modifier,
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = tasks,
            key = { task ->
                task.id
            }
        ) { task ->
            val dismissState = rememberSwipeToDismissBoxState()
            val dismisDirection = dismissState.dismissDirection
            val isDismissed = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
                    && dismissState.progress == 1f

            if (isDismissed && dismisDirection == SwipeToDismissBoxValue.EndToStart) {
                val scope = rememberCoroutineScope()
                SideEffect {
                    scope.launch {
                        delay(300)
                        onSwipeToDelete(Action.DELETE, task)
                    }
                }
            }

            val degrees by animateFloatAsState(
//                if (dismissState.targetValue == DismissValue.Default)
                if (dismissState.progress in 0f..0.5f)
                0f
                else
                    -45f, label = ""
            )

            var itemAppeared by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }

            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                SwipeToDismissBox(
                    state = dismissState,
//                    directions = setOf(DismissDirection.EndToStart),
//                    dismissThresholds = {
//                        FractionalThreshold(fraction = 0.2f)
//                    },
                    backgroundContent = {
                        RedBackground(degrees = degrees)
                    }
                ) {
                    TaskItem(
                        toDoTask = task,
                        navigateToTaskScreen = navigateToTaskScreen
                    )
                }
            }
        }
    }
}

@Composable
fun RedBackground(
    degrees: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriority)
            .padding(horizontal = LARGEST_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White
        )
    }
}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        //color = MaterialTheme.colorScheme.taskItemBackgroundColor,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row() {
                Text(
                    modifier = Modifier
                        .weight(8f),
                    text = toDoTask.title,
                    //color = MaterialTheme.colorScheme.taskItemTextColor,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE)
                    ) {
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = toDoTask.description,
                //color = MaterialTheme.colorScheme.taskItemTextColor,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun TaskItemPreview() {
    TaskItem(
        toDoTask = ToDoTask(
            1,
            "Groceries",
            "Go to the supermarket and buy groceries",
            Priority.HIGH
        ),
        navigateToTaskScreen = {}
    )
}