package rs.djokafioka.composetodo.navigation.destinations

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import rs.djokafioka.composetodo.ui.screens.list.ListScreen
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.Constants.LIST_ARGUMENT_KEY
import rs.djokafioka.composetodo.util.Constants.LIST_SCREEN
import rs.djokafioka.composetodo.util.toAction

/**
 * Created by Djordje on 7.4.2024..
 */

@ExperimentalAnimationApi
fun NavGraphBuilder.listComposable(
    sharedViewModel: SharedViewModel,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        })
    ) { navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction) {
            if (action != myAction) {
                Log.d("MyTAG", "updateAction: = " + action)
                myAction = action
//                sharedViewModel.action.value = action
                sharedViewModel.updateAction(action)
            }
        }

        val databaseAction = sharedViewModel.action

        Log.d("ListComposable", "listComposable: action = " + databaseAction)

        ListScreen(
            action = databaseAction,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}