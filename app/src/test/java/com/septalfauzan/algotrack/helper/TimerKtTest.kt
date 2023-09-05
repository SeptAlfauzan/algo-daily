package com.septalfauzan.algotrack.helper

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Calendar

@RunWith(JUnit4::class)
internal class TimerKtTest {

    @Test
    fun `format millisecond into mm colon ss format`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 50)
        calendar.set(Calendar.SECOND, 0)

        val expected = "50:00"
        assertEquals(expected, calendar.getMilliSecFromMinutesSecond().formatMilliseconds())
    }

    @Test
    fun `get millisecond from calendar minute and second`() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 50)
        calendar.set(Calendar.SECOND, 0)

        val expected: Long = 50*60*1000
        assertEquals(expected, calendar.getMilliSecFromMinutesSecond())
    }

    @Test
    fun `format calendar date`() {
        val rawTimeStamp = "29/08/2023"
        val expected = "Tuesday, 29 Aug 2023"
        assertEquals(expected, rawTimeStamp.formatCalendarDate())
    }


    @Test
    fun `format date to used in database sorting`() {
        val rawTimeStamp = "Tuesday, 29 Aug 2023"
        val expected = "29/08/2023"
        assertEquals(expected, rawTimeStamp.reverseFormatCalendarDate())
    }

    @Test
    fun `format time from timestamp datasource`() {
        val rawTimeStamp = "2023-08-29T05:55:21.071Z"
        val expected = "Tuesday, 05:55 AM, 29 Aug 2023"
        assertEquals(expected, rawTimeStamp.formatTimeStampDatasource())
    }
    @Test
    fun `cant parse format time from timestamp datasource`() {
        val rawTimeStamp = "11:04 AM. 29 Aug 2023"
        val expected = "11:04 AM. 29 Aug 2023"
        assertEquals(expected, rawTimeStamp.formatTimeStampDatasource())
    }

}