package com.septalfauzan.algotrack

import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.septalfauzan.algotrack.data.ui.BottomBarMenu
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.LoginScreen
import com.septalfauzan.algotrack.ui.RegisterScreen
import com.septalfauzan.algotrack.ui.component.BottomBar
import com.septalfauzan.algotrack.ui.screen.*
import com.septalfauzan.algotrack.presentation.*


val permission33APIBelow = listOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

val permission33APIAbove = listOf(
    android.Manifest.permission.POST_NOTIFICATIONS,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlgoDailyApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    isLogged: Boolean,
    timerViewModel: TimerViewModel,
    themeViewModel: ThemeViewModel,
    notificationViewModel: NotificationViewModel,
    profileViewModel: ProfileViewModel,
    historyAttendanceViewModel: HistoryAttendanceViewModel,
    attendanceViewModel: AttendanceViewModel,
) {
    val systemUiController = rememberSystemUiController()
    val bottomBarMenuItems = listOf(
        BottomBarMenu(screen = Screen.Home, icon = Icons.Default.Home),
        BottomBarMenu(screen = Screen.History, icon = Icons.Default.History),
        BottomBarMenu(screen = Screen.Map, icon = Icons.Default.Map),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val permissionsState =
        rememberMultiplePermissionsState(permissions = if (Build.VERSION.SDK_INT > 33) permission33APIAbove else permission33APIBelow)

    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
        if(!permissionsState.allPermissionsGranted) showDialog = true
    }


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
        PermissionsRequired(
            multiplePermissionsState = permissionsState,
            permissionsNotGrantedContent = {
//                AlertModalDialog(
//                    isShowed = showDialog,
//                    title = "Permission perlu untuk ditambahkan!",
//                    text = "Aplikasi ini memerlukan beberapa permission tersebut untuk dapat menjalankan fitur-fitur di dalamnya",
//                    onStateChange = { showDialog = it }
//                )
            },
            permissionsNotAvailableContent = { /*TODO*/ }) {
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
                            registerViewModel.registerUser(
                                userData,
                                onSuccess = { navigateToLogin() })
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
                        setOnDuty = { value -> attendanceViewModel.setOnDutyValue(value) },
                        onDutyValue = attendanceViewModel.onDutyStatus
                    )
                }
                composable(Screen.Map.route) {
                    MapScreen(navController)
                }
                composable(
                    route = Screen.Attendance.route,
                    deepLinks = listOf(navDeepLink {
                        uriPattern = "https://algodaily/attendance/{id}"
                    }),
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) {
                    val id = it.arguments?.getString("id") ?: ""
                    AttendanceScreen(id = id, navController = navController, attendanceViewModel)
                }
                composable(Screen.Success.route) {
                    systemUiController.setSystemBarsColor(
                        color = MaterialTheme.colors.primary
                    )
                    SuccessScreen(navController = navController)
                }
                composable(Screen.History.route) {
                    AttendanceHistoryScreen(
                        navController,
                        getHistory = { date -> historyAttendanceViewModel.getHistory(date) },
                        reloadHistory = { historyAttendanceViewModel.reloadHistory() },
                        historyUiState = historyAttendanceViewModel.result
                    )
                }
                composable(
                    route = Screen.Profile.route,
                ) {
                    fun navigateToLogin() = navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                    systemUiController.setSystemBarsColor(
                        color = MaterialTheme.colors.background
                    )
                    ProfileScreen(
                        profileUiState = profileViewModel.profile,
                        getProfile = { profileViewModel.getProfile() },
                        setOnDuty = {value -> attendanceViewModel.setOnDutyValue(value)},
                        onDutyState = attendanceViewModel.onDutyStatus,
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
                    arguments = listOf(navArgument("id") { type = NavType.StringType }
                    )) {
                    val id = it.arguments?.getString("id") ?: ""
                    DetailScreen(
                        attendanceId = id,
                        navController = navController,
                        detailStateUi = historyAttendanceViewModel.detail,
                        loadDetail = { id -> historyAttendanceViewModel.getDetail(id) },
                        reloadDetail = { historyAttendanceViewModel.reloadDetail() }
                    )
                }
                composable(route = Screen.ChangePassword.route) {
                    ChangePasswordScreen()
                }
            }
        }
    }
}