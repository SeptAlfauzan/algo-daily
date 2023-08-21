package com.septalfauzan.algotrack.navigation

sealed class Screen(val route: String){
    object Login : Screen("login")
    object Register : Screen( "register" )
    object Home : Screen( "home" )
    object Profile : Screen("profile/{id}"){
        fun createRoute(id: String) = "profile/$id"
    }
    object Map : Screen( "map" )
    object History : Screen("history")
}
