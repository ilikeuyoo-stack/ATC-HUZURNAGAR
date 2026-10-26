package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}

enum class AttendanceStatus {
    PRESENT,
    ABSENT,
    LATE,
    LEAVE
}

enum class LeaveStatus {
    PENDING,
    APPROVED,
    REJECTED
}

enum class ClassType {
    THEORY,
    PRACTICAL,
    WORKSHOP,
    EMPLOYABILITY_SKILLS,
    ENGG_DRAWING,
    WORKSHOP_CALC
}

enum class MaterialType {
    PDF,
    VIDEO,
    JOB_SHEET,
    DIAGRAM
}

@Entity(tableName = "trades")
data class Trade(
    @PrimaryKey val id: String,
    val code: String,
    val nameEn: String,
    val nameTe: String,
    val durationYears: Int,
    val focusEn: String,
    val focusTe: String,
    val iconKey: String
)

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey val id: String,
    val tradeId: String,
    val name: String,
    val admissionYear: Int,
    val currentYear: Int, // 1 or 2
    val totalStudents: Int
)

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val id: String,
    val admissionNo: String,
    val name: String,
    val nameTe: String,
    val tradeId: String,
    val batchId: String,
    val phone: String,
    val guardianPhone: String,
    val bloodGroup: String,
    val pin: String = "1234"
)

@Entity(tableName = "instructors")
data class Instructor(
    @PrimaryKey val id: String,
    val employeeId: String,
    val name: String,
    val nameTe: String,
    val tradeId: String,
    val phone: String,
    val designation: String
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: String,
    val date: String, // YYYY-MM-DD
    val session: String = "FULL_DAY",
    val status: AttendanceStatus,
    val markedBy: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "leave_requests")
data class LeaveRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: LeaveStatus = LeaveStatus.PENDING,
    val requestedOn: String,
    val reviewNotes: String = ""
)

@Entity(tableName = "class_schedules")
data class ClassSchedule(
    @PrimaryKey val id: String,
    val tradeId: String,
    val batchId: String,
    val dayOfWeek: Int, // 1=Mon, 2=Tue, 3=Wed, 4=Thu, 5=Fri, 6=Sat
    val timeSlot: String,
    val subjectEn: String,
    val subjectTe: String,
    val instructorName: String,
    val roomWorkshop: String,
    val classType: ClassType
)

@Entity(tableName = "daily_class_plans")
data class DailyClassPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tradeId: String,
    val batchId: String,
    val date: String,
    val topicCovered: String,
    val homeworkPrep: String,
    val instructorName: String
)

@Entity(tableName = "daily_routines")
data class DailyRoutine(
    @PrimaryKey val id: String,
    val orderIndex: Int,
    val timeSlot: String,
    val titleEn: String,
    val titleTe: String,
    val descriptionEn: String,
    val descriptionTe: String,
    val iconKey: String
)

@Entity(tableName = "study_materials")
data class StudyMaterial(
    @PrimaryKey val id: String,
    val tradeId: String,
    val year: Int,
    val subject: String,
    val titleEn: String,
    val titleTe: String,
    val type: MaterialType,
    val sizeMb: Double,
    val isDownloaded: Boolean = false,
    val isBookmarked: Boolean = false,
    val summaryEn: String,
    val summaryTe: String,
    val contentSnippet: String
)

@Entity(tableName = "quiz_questions")
data class QuizQuestion(
    @PrimaryKey val id: String,
    val tradeId: String,
    val subject: String,
    val questionEn: String,
    val questionTe: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int, // 0..3
    val explanationEn: String,
    val explanationTe: String
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val tradeId: String, // "ALL" or specific tradeId
    val titleEn: String,
    val titleTe: String,
    val contentEn: String,
    val contentTe: String,
    val date: String,
    val authorRole: String,
    val isUrgent: Boolean = false,
    val readCount: Int = 42
)

@Entity(tableName = "attendance_audit_logs")
data class AttendanceAuditLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentName: String,
    val date: String,
    val oldStatus: String,
    val newStatus: String,
    val modifiedBy: String,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
