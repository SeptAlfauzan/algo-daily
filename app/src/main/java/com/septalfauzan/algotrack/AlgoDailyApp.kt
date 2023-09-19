package com.septalfauzan.algotrack

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.septalfauzan.algotrack.domain.model.ui.BottomBarMenu
import com.septalfauzan.algotrack.helper.navigation.Screen
import com.septalfauzan.algotrack.ui.component.BottomBar
import com.septalfauzan.algotrack.ui.screen.*
import com.septalfauzan.algotrack.presentation.*
import com.septalfauzan.algotrack.ui.component.AlertModalDialog
import com.septalfauzan.algotrack.util.Notification

val permission33APIBelow = listOf(
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
val permission33APIAbove = listOf(
    android.Manifest.permission.POST_NOTIFICATIONS,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.ACCESS_FINE_LOCATION,
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlgoDailyApp(
    openNotificationSetting: (Context) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    isLogged: Boolean,
    timerViewModel: TimerViewModel,
    themeViewModel: ThemeViewModel,
    profileViewModel: ProfileViewModel,
    historyAttendanceViewModel: HistoryAttendanceViewModel,
    attendanceViewModel: AttendanceViewModel,
) {
    val bottomBarMenuItems = listOf(
        BottomBarMenu(screen = Screen.Home, icon = Icons.Default.Home),
        BottomBarMenu(screen = Screen.History, icon = Icons.Default.History),
        BottomBarMenu(screen = Screen.Map, icon = Icons.Default.Map),
        BottomBarMenu(screen = Screen.Profile, icon = Icons.Default.AccountCircle),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val context = LocalContext.current

    val permissionsState =
        rememberMultiplePermissionsState(permissions = if (Build.VERSION.SDK_INT >= 33) permission33APIAbove else permission33APIBelow)

    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
        if (!permissionsState.allPermissionsGranted) {
            showDialog = true
            openNotificationSetting(context)
        }
    }

    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        bottomBar = {
            if (bottomBarMenuItems.map { it.screen.route }
                    .contains(currentDestination?.route) && currentDestination?.route != Screen.Profile.route) BottomBar(
                bottomBarMenu = bottomBarMenuItems,
                navHostController = navController,
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        PermissionsRequired(
            multiplePermissionsState = permissionsState,
            permissionsNotGrantedContent = {
                AlertModalDialog(
                    isShowed = showDialog,
                    title = stringResource(id = R.string.permissions_need_to_be_added),
                    text = stringResource(id = R.string.need_permission),
                    onStateChange = { showDialog = it }
                )
            },
            permissionsNotAvailableContent = { }) {
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
                        registerFormUiStateFlow = registerViewModel.registerFormUiState,
                        updateName = { registerViewModel.updateName(it) },
                        updateEmail = { registerViewModel.updateEmail(it) },
                        updatePassword = { registerViewModel.updatePassword(it) },
                        eventMessage = registerViewModel.eventFlow,
                    )
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        timerState = timerViewModel.timerState,
                        navHostController = navController,
                        getHomeStateFlow = { profileViewModel.getProfileWithStats() },
                        homeData = profileViewModel.homeData,
                        reloadHomeData = { profileViewModel.reloadProfile() },
                        setOnDuty = { value -> attendanceViewModel.setOnDutyValue(value) },
                        onDutyValue = attendanceViewModel.onDutyStatus
                    )
                }

                composable(Screen.Map.route) {
                    MapScreen()
                }

                composable(
                    route = Screen.Attendance.route,
                    deepLinks = listOf(navDeepLink {
                        uriPattern = "https://algodaily/attendance/{id}/{createdAt}"
                    }),
                    arguments = listOf(
                        navArgument("id") { type = NavType.StringType },
                        navArgument("createdAt") { type = NavType.StringType })
                ) {
                    val id = it.arguments?.getString("id") ?: ""
                    val createdAt = it.arguments?.getString("createdAt") ?: ""
                    AttendanceScreen(
                        id = id,
                        createdAt = createdAt,
                        navController = navController,
                        viewModel = attendanceViewModel
                    )
                }

                composable(
                    route = Screen.Success.route,
                    arguments = listOf(
                        navArgument("title") { type = NavType.StringType },
                        navArgument("desc") { type = NavType.StringType })
                ) {
                    val title = it.arguments?.getString("title")
                    val desc = it.arguments?.getString("desc")
                    SuccessScreen(navController = navController, title = title, desc = desc)
                }

                composable(Screen.History.route) {
                    AttendanceHistoryScreen(
                        navController,
                        getHistory = { date -> historyAttendanceViewModel.getHistory(date) },
                        reloadHistory = { historyAttendanceViewModel.reloadHistory() },
                        historyUiState = historyAttendanceViewModel.result,
                        setSortingBy = { column, sortType ->
                            historyAttendanceViewModel.sortBy(
                                column,
                                sortType
                            )
                        },
                        statusSortType = historyAttendanceViewModel.statusSortType,
                        timestampSortType = historyAttendanceViewModel.timestampSortType,
                    )
                }

                composable(
                    route = Screen.Profile.route,
                ) {
                    fun navigateToLogin() = navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    ProfileScreen(
                        profileUiState = profileViewModel.profile,
                        getProfile = { profileViewModel.getProfile() },
                        setOnDuty = { value -> attendanceViewModel.setOnDutyValue(value) },
                        onDutyState = attendanceViewModel.onDutyStatus,
                        navController = navController,
                        toggleTheme = { themeViewModel.toggleDarkTheme() },
                        isDarkMode = themeViewModel.isDarkTheme.collectAsState().value,
                        logout = { authViewModel.logout(onSuccess = { navigateToLogin() }) },
                        eventMessage = authViewModel.eventFlow,
                        reloadProfile = { profileViewModel.reloadProfile() }
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

                composable(route = Screen.UploadProfilePic.route) {
                    fun navigateToHome() = navController.navigate(
                        Screen.Success.createRoute(
                            context.getString(
                                R.string.success_title_change_profile_pic
                            ), context.getString(
                                R.string.success_desc_change_profile_pic
                            )
                        )
                    ) {
                        popUpTo(Screen.UploadProfilePic.route) { inclusive = true }
                    }
                    UserChangeProfilePicScreen(
                        profileStateFlow = profileViewModel.profile,
                        getProfile = { profileViewModel.getProfile() },
                        reloadProfile = { profileViewModel.reloadProfile() },
                        updatePP = { selectedImageFile ->
                            if (selectedImageFile != null) {
                                profileViewModel.updatePP(
                                    selectedImageFile,
                                    onSuccess = { navigateToHome() })
                            }
                        },
                        navController = navController,
                        eventMessage = profileViewModel.eventFlow
                    )
                }

                composable(route = Screen.ChangePassword.route) {
                    fun navigateToHome() = navController.navigate(
                        Screen.Success.createRoute(
                            context.getString(R.string.success_change_title_password),
                            context.getString(R.string.success_change_desc_password)
                        )
                    ) {
                        popUpTo(Screen.ChangePassword.route) { inclusive = true }
                    }
                    ChangePasswordScreen(
                        navController = navController,
                        eventMessage = authViewModel.eventFlow,
                        changePassword = { userNewPassword ->
                            authViewModel.changePassword(
                                userNewPassword,
                                onSuccess = { navigateToHome() })
                        }
                    )
                }
            }
        }
    }
}