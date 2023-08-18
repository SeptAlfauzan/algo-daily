package com.septalfauzan.algotrack.data.event

sealed class MyEvent {
    data class MessageEvent(val message: String): MyEvent()
}