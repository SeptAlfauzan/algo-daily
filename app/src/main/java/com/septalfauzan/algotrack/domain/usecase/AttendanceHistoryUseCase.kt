package com.septalfauzan.algotrack.domain.usecase

import com.septalfauzan.algotrack.data.datastore.SortBy
import com.septalfauzan.algotrack.data.datastore.SortType
import com.septalfauzan.algotrack.data.source.local.dao.AttendanceEntity
import com.septalfauzan.algotrack.domain.repository.IAttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceHistoryUseCase @Inject constructor(private val attendanceRepository: IAttendanceRepository) : IAttendanceHistoryUseCase  {
    override suspend fun getHistory(date: String): Flow<List<AttendanceEntity>> = attendanceRepository.getHistory(date)
    override suspend fun getDetail(id: String): Flow<AttendanceEntity> = attendanceRepository.getDetail(id)
    override suspend fun setSortByAndType(column: SortBy, sortType: SortType) = attendanceRepository.setSortByAndType(column, sortType)
    override suspend fun getSortByTimestampValue(): Flow<SortType> = attendanceRepository.getSortByTimestampValue()
    override suspend fun getSortByStatusValue(): Flow<SortType> = attendanceRepository.getSortByStatusValue()
    override suspend fun deleteLocalHistoryAttendances(): Flow<Int> = attendanceRepository.deleteLocalAttendanceHistoryRecord()

}