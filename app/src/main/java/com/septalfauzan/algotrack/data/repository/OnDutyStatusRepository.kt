package com.septalfauzan.algotrack.data.repository

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.domain.repository.IOnDutyStatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnDutyStatusRepository @Inject constructor(private val dataStorePreference: DataStorePreference) : IOnDutyStatusRepository {
    override suspend fun getOnDutyStatus(): Flow<Boolean> {
        return dataStorePreference.getOnDutyValue()
    }

    override suspend fun setOnDutyStatus(status: Boolean) {
        dataStorePreference.setOnDuty(status)
    }
}