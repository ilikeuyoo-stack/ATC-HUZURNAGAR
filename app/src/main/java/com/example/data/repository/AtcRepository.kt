package com.example.data.repository

import com.example.data.local.AtcDao
import com.example.data.local.DatabaseInitializer
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AtcRepository(private val dao: AtcDao) {

    suspend fun ensureSeeded() {
        val existingTrades = dao.getAllTrades().firstOrNull()
        if (existingTrades.isNullOrEmpty()) {
            DatabaseInitializer.seedDatabase(dao)
        }
    }

    // Trades & Batches
    fun getAllTrades(): Flow<List<Trade>> = dao.getAllTrades()
    fun getAllBatches(): Flow<List<Batch>> = dao.getAllBatches()
    fun getBatchesForTrade(tradeId: String): Flow<List<Batch>> = dao.getBatchesForTrade(tradeId)

    // Students & Instructors
    fun getAllStudents(): Flow<List<Student>> = dao.getAllStudents()
    fun getStudentsByBatch(batchId: String): Flow<List<Student>> = dao.getStudentsByBatch(batchId)
    suspend fun getStudentById(studentId: String): Student? = dao.getStudentById(studentId)
    suspend fun getStudentByAdmissionNo(admissionNo: String): Student? = dao.getStudentByAdmissionNo(admissionNo)
    fun getAllInstructors(): Flow<List<Instructor>> = dao.getAllInstructors()

    // Attendance
    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceRecord>> =
        dao.getAttendanceForStudent(studentId)

    fun getAttendanceForDate(date: String): Flow<List<AttendanceRecord>> =
        dao.getAttendanceForDate(date)

    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>> =
        dao.getAllAttendanceRecords()

    suspend fun submitBatchAttendance(
        records: List<AttendanceRecord>,
        auditLogs: List<AttendanceAuditLog> = emptyList()
    ) {
        dao.insertAttendance(records)
        auditLogs.forEach { dao.insertAuditLog(it) }
    }

    suspend fun updateAttendanceRecord(record: AttendanceRecord, auditLog: AttendanceAuditLog?) {
        dao.updateAttendance(record)
        auditLog?.let { dao.insertAuditLog(it) }
    }

    // Leave Requests
    fun getLeaveRequestsForStudent(studentId: String): Flow<List<LeaveRequest>> =
        dao.getLeaveRequestsForStudent(studentId)

    fun getAllLeaveRequests(): Flow<List<LeaveRequest>> =
        dao.getAllLeaveRequests()

    suspend fun submitLeaveRequest(request: LeaveRequest) {
        dao.insertLeaveRequest(request)
    }

    suspend fun reviewLeaveRequest(id: Long, status: LeaveStatus, notes: String) {
        dao.updateLeaveStatus(id, status, notes)
    }

    // Class Schedule & Plans
    fun getScheduleForBatchAndDay(batchId: String, dayOfWeek: Int): Flow<List<ClassSchedule>> =
        dao.getScheduleForBatchAndDay(batchId, dayOfWeek)

    fun getAllClassSchedules(): Flow<List<ClassSchedule>> =
        dao.getAllClassSchedules()

    fun getClassPlan(batchId: String, date: String): Flow<DailyClassPlan?> =
        dao.getClassPlan(batchId, date)

    fun getAllClassPlans(): Flow<List<DailyClassPlan>> =
        dao.getAllClassPlans()

    suspend fun postClassPlan(plan: DailyClassPlan) {
        dao.insertClassPlan(plan)
    }

    // Daily Routine
    fun getAllRoutineItems(): Flow<List<DailyRoutine>> =
        dao.getAllRoutineItems()

    // Study Materials
    fun getAllMaterials(): Flow<List<StudyMaterial>> = dao.getAllMaterials()
    fun getMaterialsForTrade(tradeId: String): Flow<List<StudyMaterial>> = dao.getMaterialsForTrade(tradeId)

    suspend fun toggleDownload(id: String, isDownloaded: Boolean) {
        dao.setDownloaded(id, isDownloaded)
    }

    suspend fun toggleBookmark(id: String, isBookmarked: Boolean) {
        dao.setBookmarked(id, isBookmarked)
    }

    suspend fun uploadMaterial(material: StudyMaterial) {
        dao.insertMaterial(material)
    }

    // Quiz Questions
    fun getQuizQuestionsForTrade(tradeId: String): Flow<List<QuizQuestion>> =
        dao.getQuizQuestionsForTrade(tradeId)

    fun getAllQuizQuestions(): Flow<List<QuizQuestion>> =
        dao.getAllQuizQuestions()

    // Announcements
    fun getAllAnnouncements(): Flow<List<Announcement>> =
        dao.getAllAnnouncements()

    suspend fun postAnnouncement(announcement: Announcement) {
        dao.insertAnnouncement(announcement)
    }

    // Audit Logs
    fun getAllAuditLogs(): Flow<List<AttendanceAuditLog>> =
        dao.getAllAuditLogs()
}
