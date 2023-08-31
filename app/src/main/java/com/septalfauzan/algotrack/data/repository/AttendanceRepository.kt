package com.septalfauzan.algotrack.data.repository

import android.util.Log
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.PendingAttendanceEntity
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val apiService: AlgoTrackApiInterfaces,
    private val appDatabase: MainDatabase,
    private val dataStorePreference: DataStorePreference,
) : IAttendanceRepository {
    override suspend fun getHistory(): Flow<List<AttendanceEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHistory(date: String): Flow<List<AttendanceEntity>> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val response = apiService.getHistory(authToken = token)

            Log.d(this::class.java.simpleName, "getHistory: $response")

            val apiData = response.data.map { it.toAttendanceEntity() }

            coroutineScope {
                launch(Dispatchers.IO) {
                    appDatabase.attendanceDao().deleteAll()
                    saveBatchToLocalDB(apiData)
                }
            }
            return flowOf(appDatabase.attendanceDao().getByDate(date))
        }catch (e: java.lang.Exception){
            throw e
        }

    }

    override suspend fun getDetail(id: String): Flow<AttendanceEntity> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val response = apiService.getDetailHistory(authToken = token, id)
            return flowOf(response.data.toAttendanceEntity())
        }catch (e: java.lang.Exception){
            throw e
        }

    }

    override suspend fun updateAttendance(data: AttendanceEntity): Flow<AttendanceResponse> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val response = apiService.postAttendance(authToken = token, attendance = data)
            return flowOf(response)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun saveToLocalDB(data: AttendanceEntity): Flow<Long> {
        return flowOf(appDatabase.attendanceDao().insert(data))
    }

    override suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>): Flow<List<Long>> {
        return flowOf(appDatabase.attendanceDao().insertBatch(listData))
    }

    override suspend fun createNewBlankAttendance(data: PendingAttendanceEntity): Flow<Long> {
        return flowOf(appDatabase.pendingAttendanceDao().insert(data))
    }
}