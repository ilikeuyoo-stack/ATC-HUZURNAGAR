package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AtcDao {
    // Trades
    @Query("SELECT * FROM trades")
    fun getAllTrades(): Flow<List<Trade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrades(trades: List<Trade>)

    // Batches
    @Query("SELECT * FROM batches")
    fun getAllBatches(): Flow<List<Batch>>

    @Query("SELECT * FROM batches WHERE tradeId = :tradeId")
    fun getBatchesForTrade(tradeId: String): Flow<List<Batch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatches(batches: List<Batch>)

    // Students
    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE batchId = :batchId")
    fun getStudentsByBatch(batchId: String): Flow<List<Student>>

    @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
    suspend fun getStudentById(studentId: String): Student?

    @Query("SELECT * FROM students WHERE admissionNo = :admissionNo LIMIT 1")
    suspend fun getStudentByAdmissionNo(admissionNo: String): Student?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<Student>)

    // Instructors
    @Query("SELECT * FROM instructors")
    fun getAllInstructors(): Flow<List<Instructor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructors(instructors: List<Instructor>)

    // Attendance Records
    @Query("SELECT * FROM attendance_records WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE date = :date")
    fun getAttendanceForDate(date: String): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records")
    fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(records: List<AttendanceRecord>)

    @Update
    suspend fun updateAttendance(record: AttendanceRecord)

    // Leave Requests
    @Query("SELECT * FROM leave_requests WHERE studentId = :studentId ORDER BY requestedOn DESC")
    fun getLeaveRequestsForStudent(studentId: String): Flow<List<LeaveRequest>>

    @Query("SELECT * FROM leave_requests ORDER BY requestedOn DESC")
    fun getAllLeaveRequests(): Flow<List<LeaveRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaveRequest(request: LeaveRequest)

    @Query("UPDATE leave_requests SET status = :status, reviewNotes = :notes WHERE id = :id")
    suspend fun updateLeaveStatus(id: Long, status: LeaveStatus, notes: String)

    // Schedules
    @Query("SELECT * FROM class_schedules WHERE batchId = :batchId AND dayOfWeek = :dayOfWeek")
    fun getScheduleForBatchAndDay(batchId: String, dayOfWeek: Int): Flow<List<ClassSchedule>>

    @Query("SELECT * FROM class_schedules")
    fun getAllClassSchedules(): Flow<List<ClassSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ClassSchedule>)

    // Daily Class Plan
    @Query("SELECT * FROM daily_class_plans WHERE batchId = :batchId AND date = :date LIMIT 1")
    fun getClassPlan(batchId: String, date: String): Flow<DailyClassPlan?>

    @Query("SELECT * FROM daily_class_plans ORDER BY date DESC")
    fun getAllClassPlans(): Flow<List<DailyClassPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClassPlan(plan: DailyClassPlan)

    // Daily Routine
    @Query("SELECT * FROM daily_routines ORDER BY orderIndex ASC")
    fun getAllRoutineItems(): Flow<List<DailyRoutine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutines(routines: List<DailyRoutine>)

    // Study Materials
    @Query("SELECT * FROM study_materials")
    fun getAllMaterials(): Flow<List<StudyMaterial>>

    @Query("SELECT * FROM study_materials WHERE tradeId = :tradeId")
    fun getMaterialsForTrade(tradeId: String): Flow<List<StudyMaterial>>

    @Query("UPDATE study_materials SET isDownloaded = :isDownloaded WHERE id = :id")
    suspend fun setDownloaded(id: String, isDownloaded: Boolean)

    @Query("UPDATE study_materials SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun setBookmarked(id: String, isBookmarked: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: StudyMaterial)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<StudyMaterial>)

    // Quiz Questions
    @Query("SELECT * FROM quiz_questions WHERE tradeId = :tradeId")
    fun getQuizQuestionsForTrade(tradeId: String): Flow<List<QuizQuestion>>

    @Query("SELECT * FROM quiz_questions")
    fun getAllQuizQuestions(): Flow<List<QuizQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestion>)

    // Announcements
    @Query("SELECT * FROM announcements ORDER BY isUrgent DESC, date DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<Announcement>)

    // Audit Logs
    @Query("SELECT * FROM attendance_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AttendanceAuditLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AttendanceAuditLog)
}
