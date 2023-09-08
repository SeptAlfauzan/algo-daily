package com.septalfauzan.algotrack.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.sqlite.db.SimpleSQLiteQuery
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceStatus
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.domain.model.AttendanceRequestBody
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.formatTimeToGMT
import com.septalfauzan.algotrack.data.source.remote.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import com.septalfauzan.algotrack.helper.RequestError.getErrorMessage
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

    override suspend fun getHistory(date: String): Flow<List<AttendanceEntity>> {
        val token = dataStorePreference.getAuthToken().first()
        val sortBy = dataStorePreference.getSortBy().first().lowercase()
        val sortType = when (SortBy.valueOf(sortBy.uppercase())) {
            SortBy.CREATED_AT -> dataStorePreference.getSortByTimeType().first()
            SortBy.STATUS -> dataStorePreference.getSortByStatusType().first()
        }

        val query = SimpleSQLiteQuery("SELECT * FROM attendance WHERE strftime('%d/%m/%Y', datetime(timestamp, 'utc'))  = '$date' ORDER BY $sortBy $sortType")

        try {
            val response = apiService.getHistory(authToken = token)

            if(!response.isSuccessful) {
                val errorJson = response.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: response.message())
            }

            val dataFormattedToGMT = response.body()!!.data.map { it.formatTimeToGMT() }
            val apiData = dataFormattedToGMT.map { it.toAttendanceEntity() }
            appDatabase.attendanceDao().deleteAll()
            saveBatchToLocalDB(apiData)

            val sorted = appDatabase.attendanceDao().getHistory(query)
            Log.d("TAG", "getHistory: $query")
            return flowOf(sorted)
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun getDetail(id: String): Flow<AttendanceEntity> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val response = apiService.getDetailHistory(authToken = token, id)

            if(!response.isSuccessful) {
                val errorJson = response.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: response.message())
            }

            return flowOf(response.body()!!.data.formatTimeToGMT().toAttendanceEntity())
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    override suspend fun updateAttendance(
        id: String,
        data: AttendanceRequestBody
    ): Flow<AttendanceResponse> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val response = apiService.putAttendance(authToken = token, attendance = data, id = id)

            if(!response.isSuccessful) {
                val errorJson = response.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: response.message())
            }
            return flowOf(response.body()!!)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createNewBlankAttendance(): Flow<AttendanceResponse> {
        try {
            val token = dataStorePreference.getAuthToken().first()
            val isOnDuty = dataStorePreference.getOnDutyValue().first()
//            val currentTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            val blankAttendance = AttendanceRequestBody(
                status = if (isOnDuty) AttendanceStatus.NOT_FILLED else AttendanceStatus.OFF_DUTY,
                reason = null,
                latitude = null,
                longitude = null,
            )
            val response =
                apiService.postAttendance(authToken = token, attendance = blankAttendance)

            if(!response.isSuccessful) {
                val errorJson = response.errorBody()?.string()
                val errorResponse = errorJson?.getErrorMessage()
                throw Exception(errorResponse?.errors ?: response.message())
            }

            return flowOf(response.body()!!)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun setSortByAndType(column: SortBy, sortType: SortType) {
        when (column) {
            SortBy.CREATED_AT -> {
                dataStorePreference.setSortBy(column)
                dataStorePreference.setSortByTimestampType(sortType)
            }
            SortBy.STATUS -> {
                dataStorePreference.setSortBy(column)
                dataStorePreference.setSortByStatusType(sortType)
            }
        }
    }

    override suspend fun getSortByTimestampValue(): Flow<SortType> =
        flowOf(SortType.valueOf(dataStorePreference.getSortByTimeType().first()))

    override suspend fun getSortByStatusValue(): Flow<SortType> =
        flowOf(SortType.valueOf(dataStorePreference.getSortByStatusType().first()))

    override suspend fun deleteLocalAttendanceHistoryRecord(): Flow<Int> {
       return flowOf(appDatabase.attendanceDao().deleteAll())
    }
}