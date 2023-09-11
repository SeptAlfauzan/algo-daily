package com.septalfauzan.algotrack.helper

fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,4})+\$")
    return emailRegex.matches(this)
}