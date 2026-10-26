package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.AtcRepository
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class AttendanceSummary(
    val totalDays: Int,
    val presentDays: Int,
    val absentDays: Int,
    val lateDays: Int,
    val leaveDays: Int,
    val percentage: Double
)

class AtcViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AtcRepository = AtcRepository(
        AppDatabase.getInstance(application).atcDao()
    )

    // Language
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    // Roles & Current User
    private val _currentRole = MutableStateFlow(UserRole.STUDENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    private val _currentInstructor = MutableStateFlow<Instructor?>(null)
    val currentInstructor: StateFlow<Instructor?> = _currentInstructor.asStateFlow()

    // Data lists
    val trades: StateFlow<List<Trade>> = repository.getAllTrades()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val batches: StateFlow<List<Batch>> = repository.getAllBatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStudents: StateFlow<List<Student>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val instructors: StateFlow<List<Instructor>> = repository.getAllInstructors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val announcements: StateFlow<List<Announcement>> = repository.getAllAnnouncements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyRoutines: StateFlow<List<DailyRoutine>> = repository.getAllRoutineItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyMaterials: StateFlow<List<StudyMaterial>> = repository.getAllMaterials()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allQuizQuestions: StateFlow<List<QuizQuestion>> = repository.getAllQuizQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AttendanceAuditLog>> = repository.getAllAuditLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAttendance: StateFlow<List<AttendanceRecord>> = repository.getAllAttendanceRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSchedules: StateFlow<List<ClassSchedule>> = repository.getAllClassSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allClassPlans: StateFlow<List<DailyClassPlan>> = repository.getAllClassPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLeaveRequests: StateFlow<List<LeaveRequest>> = repository.getAllLeaveRequests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Instructor marking state
    private val _selectedBatchId = MutableStateFlow<String>("batch_cnc_2024")
    val selectedBatchId: StateFlow<String> = _selectedBatchId.asStateFlow()

    private val _attendanceDraftMap = MutableStateFlow<Map<String, AttendanceStatus>>(emptyMap())
    val attendanceDraftMap: StateFlow<Map<String, AttendanceStatus>> = _attendanceDraftMap.asStateFlow()

    // Feedback message
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            // Set default student and instructor after seeding
            allStudents.collectLatest { list ->
                if (list.isNotEmpty() && _currentStudent.value == null) {
                    _currentStudent.value = list.first()
                }
            }
        }
        viewModelScope.launch {
            instructors.collectLatest { list ->
                if (list.isNotEmpty() && _currentInstructor.value == null) {
                    _currentInstructor.value = list.first()
                }
            }
        }
        viewModelScope.launch {
            batches.collectLatest { list ->
                if (list.isNotEmpty() && list.none { it.id == _selectedBatchId.value }) {
                    _selectedBatchId.value = list.first().id
                }
            }
        }
        // Initialize draft map when batch changes or students load
        viewModelScope.launch {
            combine(allStudents, selectedBatchId) { students, batchId ->
                val batchStudents = students.filter { it.batchId == batchId }
                batchStudents.associate { it.id to AttendanceStatus.PRESENT }
            }.collectLatest { map ->
                if (_attendanceDraftMap.value.isEmpty()) {
                    _attendanceDraftMap.value = map
                }
            }
        }
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ENGLISH) {
            AppLanguage.TELUGU
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _currentLanguage.value = lang
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setCurrentStudent(student: Student) {
        _currentStudent.value = student
    }

    fun selectBatch(batchId: String) {
        _selectedBatchId.value = batchId
        val batchStudents = allStudents.value.filter { it.batchId == batchId }
        _attendanceDraftMap.value = batchStudents.associate { it.id to AttendanceStatus.PRESENT }
    }

    fun setStudentDraftStatus(studentId: String, status: AttendanceStatus) {
        _attendanceDraftMap.value = _attendanceDraftMap.value.toMutableMap().apply {
            put(studentId, status)
        }
    }

    fun markAllDraft(status: AttendanceStatus) {
        val current = _attendanceDraftMap.value
        _attendanceDraftMap.value = current.keys.associateWith { status }
    }

    fun submitAttendance(date: String = getTodayDate()) {
        viewModelScope.launch {
            val instructorName = _currentInstructor.value?.name ?: "ATC Instructor"
            val records = _attendanceDraftMap.value.map { (studentId, status) ->
                AttendanceRecord(
                    studentId = studentId,
                    date = date,
                    session = "FULL_DAY",
                    status = status,
                    markedBy = instructorName
                )
            }
            repository.submitBatchAttendance(records)
            _toastMessage.value = "Attendance for $date successfully saved!"
        }
    }

    fun submitQrCheckIn(studentId: String) {
        viewModelScope.launch {
            val today = getTodayDate()
            val record = AttendanceRecord(
                studentId = studentId,
                date = today,
                session = "FULL_DAY",
                status = AttendanceStatus.PRESENT,
                markedBy = "QR Geo-Verified Self Check-in"
            )
            repository.submitBatchAttendance(listOf(record))
            _toastMessage.value = "QR Check-in Verified! Marked Present for today."
        }
    }

    fun submitLeaveRequest(startDate: String, endDate: String, reason: String) {
        val student = _currentStudent.value ?: return
        viewModelScope.launch {
            val req = LeaveRequest(
                studentId = student.id,
                startDate = startDate,
                endDate = endDate,
                reason = reason,
                status = LeaveStatus.PENDING,
                requestedOn = getTodayDate()
            )
            repository.submitLeaveRequest(req)
            _toastMessage.value = "Leave request submitted to Trade Instructor."
        }
    }

    fun reviewLeave(requestId: Long, status: LeaveStatus, notes: String) {
        viewModelScope.launch {
            repository.reviewLeaveRequest(requestId, status, notes)
            _toastMessage.value = "Leave request updated to ${status.name}."
        }
    }

    fun postClassPlan(batchId: String, topic: String, homework: String) {
        val instructorName = _currentInstructor.value?.name ?: "Trade Instructor"
        viewModelScope.launch {
            val batch = batches.value.firstOrNull { it.id == batchId }
            val tradeId = batch?.tradeId ?: "trade_cnc"
            val plan = DailyClassPlan(
                tradeId = tradeId,
                batchId = batchId,
                date = getTodayDate(),
                topicCovered = topic,
                homeworkPrep = homework,
                instructorName = instructorName
            )
            repository.postClassPlan(plan)
            _toastMessage.value = "Class plan and homework posted to students!"
        }
    }

    fun postNotice(tradeId: String, titleEn: String, titleTe: String, contentEn: String, contentTe: String, isUrgent: Boolean) {
        viewModelScope.launch {
            val announcement = Announcement(
                id = "ann_${System.currentTimeMillis()}",
                tradeId = tradeId,
                titleEn = titleEn,
                titleTe = titleTe,
                contentEn = contentEn,
                contentTe = contentTe,
                date = getTodayDate(),
                authorRole = if (_currentRole.value == UserRole.ADMIN) "Principal Office" else "Trade Instructor",
                isUrgent = isUrgent,
                readCount = 0
            )
            repository.postAnnouncement(announcement)
            _toastMessage.value = "Announcement posted successfully."
        }
    }

    fun uploadStudyMaterial(
        tradeId: String,
        year: Int,
        subject: String,
        titleEn: String,
        titleTe: String,
        type: MaterialType,
        summaryEn: String,
        contentSnippet: String
    ) {
        viewModelScope.launch {
            val mat = StudyMaterial(
                id = "mat_${System.currentTimeMillis()}",
                tradeId = tradeId,
                year = year,
                subject = subject,
                titleEn = titleEn,
                titleTe = titleTe,
                type = type,
                sizeMb = 1.9,
                isDownloaded = true,
                isBookmarked = false,
                summaryEn = summaryEn,
                summaryTe = titleTe,
                contentSnippet = contentSnippet
            )
            repository.uploadMaterial(mat)
            _toastMessage.value = "Study material uploaded and available offline!"
        }
    }

    fun toggleMaterialDownload(id: String, currentState: Boolean) {
        viewModelScope.launch {
            repository.toggleDownload(id, !currentState)
            _toastMessage.value = if (!currentState) "Saved offline on device." else "Removed from offline storage."
        }
    }

    fun toggleMaterialBookmark(id: String, currentState: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(id, !currentState)
        }
    }

    fun sendAbsenceAlert(student: Student) {
        viewModelScope.launch {
            // Simulate automated SMS/Push alert dispatch to student & guardian
            _toastMessage.value = "SMS Alert dispatched to Guardian (${student.guardianPhone}): ${student.name} marked absent today."
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Helper: calculate student attendance stats
    fun getStudentAttendanceSummary(studentId: String): AttendanceSummary {
        val records = allAttendance.value.filter { it.studentId == studentId }
        val total = records.size
        if (total == 0) return AttendanceSummary(0, 0, 0, 0, 0, 100.0)
        val present = records.count { it.status == AttendanceStatus.PRESENT }
        val absent = records.count { it.status == AttendanceStatus.ABSENT }
        val late = records.count { it.status == AttendanceStatus.LATE }
        val leave = records.count { it.status == AttendanceStatus.LEAVE }
        // Present + Leave count as attended / excused in typical ITI calculations
        val percentage = ((present.toDouble() + (leave * 0.8) + (late * 0.7)) / total.toDouble()) * 100.0
        return AttendanceSummary(total, present, absent, late, leave, percentage.coerceIn(0.0, 100.0))
    }

    // Helper: get today date YYYY-MM-DD
    fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
