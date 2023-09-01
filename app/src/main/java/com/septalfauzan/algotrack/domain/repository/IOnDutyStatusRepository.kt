package com.septalfauzan.algotrack.domain.repository

import kotlinx.coroutines.flow.Flow

interface IOnDutyStatusRepository {
    suspend fun getOnDutyStatus() : Flow<Boolean>
    suspend fun setOnDutyStatus(status: Boolean)
}