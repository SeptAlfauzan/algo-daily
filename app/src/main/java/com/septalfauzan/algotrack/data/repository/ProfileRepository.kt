package com.septalfauzan.algotrack.data.repository

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.data.ui.UiState
import com.septalfauzan.algotrack.domain.model.apiResponse.GetProfileResponse
import com.septalfauzan.algotrack.domain.repository.IProfileRepository
import com.septalfauzan.algotrack.helper.NetworkCall
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val apiService: AlgoTrackApiInterfaces,
    private val dataStorePreference: DataStorePreference
) : IProfileRepository {

    override suspend fun getUserProfile(): Flow<GetProfileResponse> {
        val token = dataStorePreference.getAuthToken().first()
        try {
            return flowOf(apiService.getUserProfile(token))
        } catch (e: Exception){
            throw e
        }
    }
}