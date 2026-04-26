package com.riramzy.biomedtrack.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashVm: SplashVm = hiltViewModel()
) {
    val navEvent by splashVm.navigationEvent.collectAsStateWithLifecycle()

    SplashScreenContent(
        navController = navController,
        navEvent = navEvent
    )
}

@Composable
fun SplashScreenContent(
    navController: NavHostController,
    navEvent: SplashNavEvent?
) {
    LaunchedEffect(navEvent) {
        if (navEvent is SplashNavEvent.NavigateToMain) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else if (navEvent is SplashNavEvent.NavigateToLogin) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondary
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.biomedtrack),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(70.dp),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.primary
                )
            )

            Text(
                text = "BioMed Track",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                fontSize = 38.sp,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun SplashScreenPreview() {
    BioMedTheme {
        SplashScreenContent(
            navController = NavHostController(LocalContext.current),
            navEvent = null
        )
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SplashScreenDarkPreview() {
    BioMedTheme {
        SplashScreenContent(
            navController = NavHostController(LocalContext.current),
            navEvent = null
        )
    }
}