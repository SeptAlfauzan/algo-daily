package com.septalfauzan.algotrack.helper

import java.text.SimpleDateFormat
import java.util.*

fun Long.formatMilliseconds(): String{
    val format = SimpleDateFormat("mm:ss")
    return format.format(Date(this))
}