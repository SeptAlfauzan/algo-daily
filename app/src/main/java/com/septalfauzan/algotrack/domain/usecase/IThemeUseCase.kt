package com.septalfauzan.algotrack.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IThemeUseCase {
    suspend fun setTheme(value: Boolean) : Unit

    suspend fun getTheme() : Boolean
}