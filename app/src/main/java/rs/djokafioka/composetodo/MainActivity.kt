package rs.djokafioka.composetodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import rs.djokafioka.composetodo.navigation.SetUpNavigation
import rs.djokafioka.composetodo.ui.theme.ComposeToDoTheme
import rs.djokafioka.composetodo.ui.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        //Fixing status bar... there is more in Theme.kt
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ComposeToDoTheme {
                navController = rememberNavController()
                SetUpNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}

