package com.septalfauzan.algotrack.ui.component

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.septalfauzan.algotrack.data.ui.BottomBarMenu
import com.septalfauzan.algotrack.navigation.Screen



private val dummyMenu = listOf(
    BottomBarMenu(screen = Screen.Home, icon = Icons.Default.Home),
    BottomBarMenu(screen = Screen.History, icon = Icons.Default.History),
    BottomBarMenu(screen = Screen.Map, icon = Icons.Default.Map),
)

@Composable
fun BottomBar(
    bottomBarMenu: List<BottomBarMenu>,
    navHostController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
){
    BottomNavigation(modifier, backgroundColor = MaterialTheme.colors.background, elevation = 4.dp) {
        bottomBarMenu.forEach {menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = menu.screen.route) },
                label = { Text(text = menu.screen.route) },
                selected = currentDestination?.route == menu.screen.route,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                onClick = {
                    navHostController.navigate(menu.screen.route){
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}