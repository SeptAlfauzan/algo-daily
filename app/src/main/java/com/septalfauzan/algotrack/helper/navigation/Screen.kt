package com.septalfauzan.algotrack.helper.navigation

sealed class Screen(val route: String){
    object Login : Screen("login")
    object Register : Screen( "register" )
    object Home : Screen( "home" )
    object Profile : Screen("profile")
    object Detail : Screen("detail/{id}"){
        fun createRoute(id: String) = "detail/$id"
    }
    object ChangePassword : Screen("change_password")
    object Map : Screen( "map" )
    object History : Screen("history")
    object Attendance : Screen("attendance/{id}/{createdAt}"){
        fun createRoute(id: String, createdAt: String) = "attendance/$id/$createdAt"
    }
    object UploadProfilePic : Screen("profile-change")
    object Success : Screen("success/{title}/{desc}"){
        fun createRoute(title: String, desc: String) = "success/$title/$desc"
    }
}
