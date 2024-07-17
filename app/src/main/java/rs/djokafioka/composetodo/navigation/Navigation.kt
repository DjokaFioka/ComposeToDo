package rs.djokafioka.composetodo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import rs.djokafioka.composetodo.navigation.destinations.listComposable
import rs.djokafioka.composetodo.navigation.destinations.taskComposable
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel
import rs.djokafioka.composetodo.util.Constants.LIST_SCREEN

/**
 * Created by Djordje on 6.4.2024..
 */

@ExperimentalAnimationApi
@Composable
fun SetUpNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
//        startDestination = SPLASH_SCREEN
        startDestination = LIST_SCREEN
    ) {
//        splashComposable(
//            navigateToListScreen = screen.splash
//        )
        listComposable(
            sharedViewModel = sharedViewModel,
            navigateToTaskScreen = screen.list
        )
        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToListScreen = screen.task
        )
    }
}