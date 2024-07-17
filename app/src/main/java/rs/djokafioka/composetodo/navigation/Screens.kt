package rs.djokafioka.composetodo.navigation

import androidx.navigation.NavHostController
import rs.djokafioka.composetodo.util.Action
import rs.djokafioka.composetodo.util.Constants.LIST_SCREEN
import rs.djokafioka.composetodo.util.Constants.SPLASH_SCREEN

/**
 * Created by Djordje on 6.4.2024..
 */
class Screens(navController: NavHostController) {
    //We removed when we added Splash API dependency
//    val splash: () -> Unit = {
//        navController.navigate(route = "list/${Action.NO_ACTION}") {
//            popUpTo(SPLASH_SCREEN) { inclusive = true}
//        }
//    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }

    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) { inclusive = true }
        }
    }
}