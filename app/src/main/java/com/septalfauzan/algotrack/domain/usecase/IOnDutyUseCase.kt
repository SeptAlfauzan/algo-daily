package com.septalfauzan.algotrack.domain.usecase

import kotlinx.coroutines.flow.Flow

interface IOnDutyUseCase {
    suspend fun getOnDutyStatus() : Flow<Boolean>
    suspend fun setOnDutyStatus(status: Boolean)
}