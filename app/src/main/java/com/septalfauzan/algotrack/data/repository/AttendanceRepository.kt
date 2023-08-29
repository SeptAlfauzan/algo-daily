package com.septalfauzan.algotrack.data.repository

import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceHistoryResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponse
import com.septalfauzan.algotrack.domain.model.apiResponse.AttendanceResponseData
import com.septalfauzan.algotrack.domain.model.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


private val dummy = listOf<AttendanceResponseData>(
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-25T05:55:21.071Z",
        createdAt = "2023-08-25T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-25T05:55:21.071Z",
        createdAt = "2023-08-25T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-25T05:55:21.071Z",
        createdAt = "2023-08-25T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-25T05:55:21.071Z",
        createdAt = "2023-08-25T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-29T05:55:21.071Z",
        createdAt = "2023-08-29T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-29T05:55:21.071Z",
        createdAt = "2023-08-29T05:55:21.071Z",
    ),
    AttendanceResponseData(
        id = UUID.randomUUID().toString(),
        reason = null,
        status = AttendanceStatus.ON_DUTY.toString(),
        latitude = 0.0,
        longitude = 0.0,
        timestamp = "2023-08-29T05:55:21.071Z",
        createdAt = "2023-08-29T05:55:21.071Z",
    ),
)

class AttendanceRepository @Inject constructor(
    private val apiService: AlgoTrackApiInterfaces,
    private val appDatabase: MainDatabase,
    private val dataStorePreference: DataStorePreference,
) : IAttendanceRepository {
    override suspend fun getHistory(): Flow<List<AttendanceEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getHistory(date: String): Flow<List<AttendanceEntity>> {
        // TODO: change to actual data from api
        try {
            val dummyResponse = AttendanceHistoryResponse(data = dummy)
            val dummyData = dummyResponse.data.map { it.toAttendanceEntity() }

            coroutineScope {
                launch(Dispatchers.IO) {
                    appDatabase.attendanceDao().deleteAll()
                    saveBatchToLocalDB(dummyData)
                }
            }
            return flowOf(appDatabase.attendanceDao().getByDate(date))
        }catch (e: java.lang.Exception){
            throw e
        }

    }

    override suspend fun updateAttendance(data: AttendanceEntity): Flow<AttendanceResponse> {
        return flowOf(AttendanceResponse(data = dummy[0]))
    }

    override suspend fun saveToLocalDB(data: AttendanceEntity): Flow<Long> {
        return flowOf(appDatabase.attendanceDao().insert(data))
    }

    override suspend fun saveBatchToLocalDB(listData: List<AttendanceEntity>): Flow<List<Long>> {
        return flowOf(appDatabase.attendanceDao().insertBatch(listData))
    }
}