package com.septalfauzan.algotrack.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.septalfauzan.algotrack.data.datastore.DataStorePreference
import com.septalfauzan.algotrack.data.source.local.MainDatabase
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceDao
import com.septalfauzan.algotrack.data.source.remote.apiInterfaces.AlgoTrackApiInterfaces
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceHistoryResponse
import com.septalfauzan.algotrack.data.source.remote.apiResponse.AttendanceResponseData
import com.septalfauzan.algotrack.data.source.remote.apiResponse.toAttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

val dummyData = AttendanceHistoryResponse(
    data = listOf(
        AttendanceResponseData(
            id = "1f60bb-b0df-4542-a21b-6049d3d8bfd3",
            status = "ON_DUTY",
            timestamp = "2023-09-11T03:22:57.959Z",
            latitude = -7.966443,
            longitude = 112.6146,
            reason = null,
            createdAt = "2023-09-11T02:06:22.242Z"
        )
    )
)

@RunWith(MockitoJUnitRunner::class)
internal class AttendanceRepositoryTest {


    @Mock
    lateinit var mockAttendanceRepository: IAttendanceRepository

    @Mock
    private lateinit var apiService: AlgoTrackApiInterfaces

    @Mock
    private lateinit var appDatabase: MainDatabase

    @Mock
    private lateinit var dataStorePreference: DataStorePreference

    @Before
    fun setUp() {
        dataStorePreference = mock(DataStorePreference::class.java)
        val mockAttendanceDao = mock(AttendanceDao::class.java)
        `when`(appDatabase.attendanceDao()).thenReturn(mockAttendanceDao)
        mockAttendanceRepository =
            AttendanceRepository(apiService, appDatabase, dataStorePreference)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getHistory() = runBlocking {
        `when`(dataStorePreference.getAuthToken()).thenReturn(flowOf("-"))
        `when`(dataStorePreference.getSortBy()).thenReturn(flowOf("CREATED_AT".lowercase()))
        `when`(dataStorePreference.getSortByTimeType()).thenReturn(flowOf("ASC"))
        `when`(apiService.getHistory(authToken = "-")).thenReturn(Response.success(dummyData))

        val listEntities = dummyData.data.map { it.toAttendanceEntity() }
        val sortBy = dataStorePreference.getSortBy().first()
        val sortType = dataStorePreference.getSortByTimeType().first()
        val date = "09/11/2023"
        val query = SimpleSQLiteQuery("SELECT * FROM attendance WHERE strftime('%d/%m/%Y', datetime(timestamp, 'utc'))  = '$date' ORDER BY $sortBy $sortType")
        `when`(appDatabase.attendanceDao().getHistory(query)).thenReturn(listEntities)
        `when`(mockAttendanceRepository.getHistory(date)).thenReturn(flowOf(listEntities))

        val response = mockAttendanceRepository.getHistory(date)
        assertEquals(listEntities, response.first())
    }

    @Test
    fun getDetail() {
    }

    @Test
    fun updateAttendance() {
    }

    @Test
    fun saveToLocalDB() {
    }

    @Test
    fun saveBatchToLocalDB() {
    }

    @Test
    fun createNewBlankAttendance() {
    }

    @Test
    fun setSortByAndType() {
    }

    @Test
    fun getSortByTimestampValue() {
    }

    @Test
    fun getSortByStatusValue() {
    }

    @Test
    fun deleteLocalAttendanceHistoryRecord() {
    }
}