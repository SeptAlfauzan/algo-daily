package com.septalfauzan.algotrack

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.septalfauzan.algotrack.navigation.Screen
import com.septalfauzan.algotrack.ui.HomeScreen
import com.septalfauzan.algotrack.ui.LoginScreen
import com.septalfauzan.algotrack.ui.RegisterScreen
import com.septalfauzan.algotrack.ui.screen.ProfileScreen
import com.septalfauzan.algotrack.viewmodels.AuthViewModel
import com.septalfauzan.algotrack.viewmodels.RegisterViewModel
import com.septalfauzan.algotrack.viewmodels.TimerViewModel

@Composable
fun AlgoTrackApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    isLogged: Boolean,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
) {
    val systemUiController = rememberSystemUiController()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLogged) Screen.Home.route else Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.secondary
                )
                fun navigateToHome() = navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }

                LoginScreen(
                    updateEmail = { authViewModel.updateEmail(it) },
                    updatePassword = { authViewModel.updatePassword(it) },
                    formUIStateFlow = authViewModel.formUiState,
                    eventMessage = authViewModel.eventFlow,
                    loginAction = { authViewModel.login(onSuccess = { navigateToHome() }) },
                    navController = navController
                )
            }
            composable(Screen.Register.route) {
                fun navigateToLogin() = navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Register.route) {
                        inclusive = true
                    }
                }

                RegisterScreen(
                    RegisterAction = { userData ->
                        registerViewModel.registerUser(userData, onSuccess = { navigateToLogin() })
                    },
                    LoginAction = { navigateToLogin() },
                    eventMessage = registerViewModel.eventFlow,
                )
            }
            composable(Screen.Home.route) {
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )
                HomeScreen(
                    timerState = timerViewModel.timerState,
                    navHostController = navController,
                    userId = "1"
                )
            }
            composable(
                route = Screen.Profile.route,
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType }
                )
            ) {
                val id = it.arguments?.getString("id") ?: ""
                fun navigateToLogin() = navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }
                ProfileScreen(
                    userId = id,
                    logout = {
                        authViewModel.logout(onSuccess = { navigateToLogin() })
                    },
                )
            }
        }
    }
}