package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        Trade::class,
        Batch::class,
        Student::class,
        Instructor::class,
        AttendanceRecord::class,
        LeaveRequest::class,
        ClassSchedule::class,
        DailyClassPlan::class,
        DailyRoutine::class,
        StudyMaterial::class,
        QuizQuestion::class,
        Announcement::class,
        AttendanceAuditLog::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun atcDao(): AtcDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "atc_huzurnagar.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
