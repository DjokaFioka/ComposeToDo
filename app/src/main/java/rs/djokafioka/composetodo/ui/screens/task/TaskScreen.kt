package rs.djokafioka.composetodo.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.data.models.Priority
import rs.djokafioka.composetodo.data.models.ToDoTask
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel
import rs.djokafioka.composetodo.util.Action

/**
 * Created by Djordje on 9.4.2024..
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    val title: String = sharedViewModel.title
    val description: String = sharedViewModel.description
    val priority: Priority = sharedViewModel.priority

    val context = LocalContext.current

    //This was a custom BackHandler
//    BackHandler(onBackPressed = { navigateToListScreen(Action.NO_ACTION)})

    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(
                                context = context,
                                resId = R.string.title_description_empty_error
                            )
                        }
                    }
                }
//                navigateToListScreen = navigateToListScreen
            )
        },
        content = { padding ->
            TaskContent(
                modifier = Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding()
                    ),
                title = title,
                onTitleChange = {
                    sharedViewModel.updateTitle(newTitle = it)
                },
                description = description,
                onDescriptionChange = {
                    sharedViewModel.updateDescription(newDescription = it)
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.updatePriority(newPriority = it)
                }
            )
        }
    )
}

fun displayToast(
    context: Context,
    resId: Int
) {
    Toast.makeText(
        context,
        resId,
        Toast.LENGTH_SHORT
    ).show()
}

//There is an official BackHandler function that handles all this
//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher? =
//        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//) {
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallback = remember {
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//                Log.d("BackHandler", "handleOnBackPressed: Triggered")
//            }
//        }
//    }
//
//    //Disposable effect is used when there is a need to dispose smth when key changes
//    //or when a composable function leaves the composition
//    DisposableEffect(key1 = backDispatcher) {
//        backDispatcher?.addCallback(backCallback)
//
//        onDispose {
//            backCallback.remove()
//        }
//    }
//}