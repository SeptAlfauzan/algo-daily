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
import com.septalfauzan.algotrack.domain.model.ui.BottomBarMenu
import com.septalfauzan.algotrack.helper.navigation.Screen


@Composable
fun BottomBar(
    bottomBarMenu: List<BottomBarMenu>,
    navHostController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
){
    BottomNavigation(modifier, backgroundColor = MaterialTheme.colors.surface, elevation = 4.dp) {
        bottomBarMenu.forEach {menu ->
            BottomNavigationItem(
                icon = { Icon(imageVector = menu.icon, contentDescription = menu.screen.route) },
                label = { Text(text = menu.screen.route) },
                selected = currentDestination?.route == menu.screen.route,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                onClick = {
                    navHostController.navigate(menu.screen.route){
                        popUpTo(navHostController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}