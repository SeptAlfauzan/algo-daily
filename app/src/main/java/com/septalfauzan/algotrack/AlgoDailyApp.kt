package com.septalfauzan.algotrack

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.septalfauzan.algotrack.domain.model.UserAbsen
import com.septalfauzan.algotrack.data.ui.BottomBarMenu
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.HomeScreen
import com.septalfauzan.algotrack.ui.LoginScreen
import com.septalfauzan.algotrack.ui.RegisterScreen
import com.septalfauzan.algotrack.ui.component.BottomBar
import com.septalfauzan.algotrack.ui.screen.*
import com.septalfauzan.algotrack.presentation.*
import java.util.*

@Composable
fun AlgoDailyApp(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    isLogged: Boolean,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel,
    themeViewModel: ThemeViewModel,
    notificationViewModel: NotificationViewModel,
    profileViewModel: ProfileViewModel,
) {
    val systemUiController = rememberSystemUiController()
    val bottomBarMenuItems = listOf<BottomBarMenu>(
        BottomBarMenu(screen = Screen.Home, icon = Icons.Default.Home),
        BottomBarMenu(screen = Screen.History, icon = Icons.Default.History),
        BottomBarMenu(screen = Screen.Map, icon = Icons.Default.Map),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (bottomBarMenuItems.map { it.screen.route }
                    .contains(currentDestination?.route)) BottomBar(
                bottomBarMenu = bottomBarMenuItems,
                navHostController = navController,
                currentDestination = currentDestination
            )
        }
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
            composable(Screen.Map.route) {
                MapScreen(navController)
            }
            composable(Screen.Attendance.route){
                AttendanceScreen(navController = navController)
            }
            composable(Screen.Success.route){
                SuccessScreen(navController = navController)
            }
            composable(Screen.History.route) {
                val historyList = listOf(
                    UserAbsen(Date(), true),
                    UserAbsen(Date(), false),
                    UserAbsen(Date(), true)
                )

                HistoryScreen(navController, historyList)
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
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.background
                )
                ProfileScreen(
                    userId = id,
                    profileUiState = profileViewModel.profile,
                    getProfile = { profileViewModel.getProfile() },
                    navController = navController,
                    isNotificationReminderActive = notificationViewModel.isNotificationReminderActive.collectAsState().value,
                    setNotificationReminder = { notificationViewModel.setNotificationReminder() },
                    cancelNotificationReminder = { notificationViewModel.cancelNotificationReminder() },
                    toggleTheme = { themeViewModel.toggleDarkTheme() },
                    isDarkMode = themeViewModel.isDarkTheme.collectAsState().value,
                    logout = {
                        authViewModel.logout(onSuccess = { navigateToLogin() })
                    },
                )
            }
            composable(
                route = Screen.Detail.route,
                deepLinks = listOf(navDeepLink { uriPattern = "https://algodaily/detail/{id}" }),
                arguments = listOf(navArgument("id"){ type= NavType.StringType }
            )){
                val id = it.arguments?.getString("id") ?: ""
                DetailScreen(attendanceId = id, navController = navController)
            }
            composable(route = Screen.ChangePassword.route){
                ChangePasswordScreen()
            }
        }
    }
}