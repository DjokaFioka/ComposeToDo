package rs.djokafioka.composetodo.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import rs.djokafioka.composetodo.R
import rs.djokafioka.composetodo.ui.theme.ComposeToDoTheme
import rs.djokafioka.composetodo.ui.theme.SPLASH_LOGO_HIGHT
import rs.djokafioka.composetodo.util.Constants.SPLASH_SCREEN_DELAY

/**
 * Created by Djordje on 16.4.2024..
 */

//@Composable
//fun SplashScreen(
//    navigateToListScreen: () -> Unit
//) {
//    var startAnimation by remember {
//        mutableStateOf(false)
//    }
//
//    val offsetState by animateDpAsState(
//        targetValue = if (startAnimation) 0.dp else 100.dp,
//        animationSpec = tween(
//            durationMillis = 1000
//        )
//    )
//
//    val alphaState by animateFloatAsState(
//        targetValue = if (startAnimation) 1f else 0f,
//        animationSpec = tween(
//            durationMillis = 1000
//        )
//    )
//
//    LaunchedEffect(key1 = true) {
//        startAnimation = true
//        delay(SPLASH_SCREEN_DELAY)
//        navigateToListScreen()
//    }
//
//    Splash(
//        offsetState = offsetState,
//        alphaState = alphaState
//    )
//}
//
//@Composable
//fun Splash(
//    offsetState: Dp,
//    alphaState: Float
//) {
//    Box(modifier = Modifier
//        .fillMaxSize(),
////        .background(MaterialTheme.colors.splashScreenBackground),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            modifier = Modifier
//                .size(SPLASH_LOGO_HIGHT)
//                .offset(y = offsetState)
//                .alpha(alpha = alphaState),
//            painter = painterResource(id = getLogo()),
//            contentDescription = stringResource(id = R.string.logo_description)
//        )
//    }
//}
//
//@Composable
//fun getLogo(): Int {
//    return if (isSystemInDarkTheme()) {
//        R.drawable.ic_logo_dark
//    } else {
//        R.drawable.ic_logo_light
//    }
//
//
//}
//
//@Preview
//@Composable
//fun SplashScreenPreview() {
//    Splash(offsetState = 0.dp, alphaState = 1f)
//}
//
//@Preview
//@Composable
//fun SplashScreenPreview2() {
//    ComposeToDoTheme(darkTheme = true) {
//        Splash(offsetState = 0.dp, alphaState = 1f)
//    }
//}