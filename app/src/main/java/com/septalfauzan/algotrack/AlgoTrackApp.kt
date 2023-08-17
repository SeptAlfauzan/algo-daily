package com.septalfauzan.algotrack

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.septalfauzan.algotrack.navigation.Screen
import com.septalfauzan.algotrack.ui.HomeScreen
import com.septalfauzan.algotrack.ui.LoginScreen
import com.septalfauzan.algotrack.ui.RegisterScreen
import com.septalfauzan.algotrack.viewmodels.AuthViewModel

@Composable
fun AlgoTrackApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    registerViewModel: AuthViewModel,
    isLogged: Boolean,
    modifier: Modifier = Modifier,
) {


    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isLogged) Screen.Home.route else Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                fun navigateToHome() = navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }

                LoginScreen(
                    navController = navController,
                    updateEmail = {authViewModel.updateEmail(it)},
                    updatePassword = {authViewModel.updatePassword(it)},
                    formUIStateFlow = authViewModel.formUiState,
                    loginAction = { authViewModel.login(onSuccess = { navigateToHome() }) })
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                fun navigateToLogin() = navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) {
                        inclusive = true
                    }
                }

                HomeScreen(
                    logout = {
                        authViewModel.logout(onSuccess = { navigateToLogin() })
                    })
            }
        }
    }
}