package com.example

import com.example.data.model.AttendanceStatus
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testBilingualStrings() {
        val enTitle = AppStrings.appTitle.get(AppLanguage.ENGLISH)
        val teTitle = AppStrings.appTitle.get(AppLanguage.TELUGU)
        assertEquals("ATC Huzurnagar", enTitle)
        assertEquals("ఎటిసి హుజూర్‌నగర్", teTitle)

        assertEquals("Present", AppStrings.statusPresent.get(AppLanguage.ENGLISH))
        assertEquals("హాజరు", AppStrings.statusPresent.get(AppLanguage.TELUGU))
    }

    @Test
    fun testAttendanceCalculationLogic() {
        val presentCount = 18
        val absentCount = 2
        val total = presentCount + absentCount
        val percentage = (presentCount.toDouble() / total.toDouble()) * 100.0
        assertEquals(90.0, percentage, 0.01)
        assertTrue(percentage >= 75.0)
    }

    @Test
    fun testAttendanceStatusValues() {
        val statuses = AttendanceStatus.values()
        assertTrue(statuses.contains(AttendanceStatus.PRESENT))
        assertTrue(statuses.contains(AttendanceStatus.ABSENT))
        assertTrue(statuses.contains(AttendanceStatus.LATE))
        assertTrue(statuses.contains(AttendanceStatus.LEAVE))
    }
}
