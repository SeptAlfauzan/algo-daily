package com.septalfauzan.algotrack.domain.repository

import kotlinx.coroutines.flow.Flow

interface IThemeRepository {
    suspend fun setDarkThemeValue(value: Boolean) : Unit
    fun getDarkThemeValueFlow() : Flow<Boolean>
}