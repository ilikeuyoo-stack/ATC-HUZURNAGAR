package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.NavigationTab
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AtcViewModel

@Composable
fun HomeScreen(
    viewModel: AtcViewModel,
    onNavigateTab: (NavigationTab) -> Unit,
    onOpenIdCard: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenRoutine: () -> Unit,
    onOpenQrModal: () -> Unit
) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val instructor by viewModel.currentInstructor.collectAsState()
    val trades by viewModel.trades.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val schedules by viewModel.allSchedules.collectAsState()
    val allAttendance by viewModel.allAttendance.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (role) {
            UserRole.STUDENT -> {
                item {
                    StudentHeaderCard(
                        student = student,
                        trades = trades,
                        language = language,
                        onOpenIdCard = onOpenIdCard
                    )
                }

                item {
                    val summary = student?.let { viewModel.getStudentAttendanceSummary(it.id) }
                    StudentAttendanceOverviewCard(
                        summary = summary,
                        language = language,
                        onNavigateAttendance = { onNavigateTab(NavigationTab.ATTENDANCE) },
                        onQrCheckIn = onOpenQrModal
                    )
                }

                item {
                    StudentNextClassCard(
                        student = student,
                        schedules = schedules,
                        language = language,
                        onNavigateClasses = { onNavigateTab(NavigationTab.CLASSES) }
                    )
                }

                item {
                    StudentQuickActionsRow(
                        language = language,
                        onOpenIdCard = onOpenIdCard,
                        onOpenRoutine = onOpenRoutine,
                        onOpenQuiz = onOpenQuiz,
                        onOpenMaterials = { onNavigateTab(NavigationTab.MATERIALS) }
                    )
                }

                item {
                    AnnouncementsSection(
                        announcements = announcements,
                        language = language
                    )
                }
            }

            UserRole.INSTRUCTOR -> {
                item {
                    InstructorHeaderCard(
                        instructor = instructor,
                        trades = trades,
                        language = language
                    )
                }

                item {
                    InstructorActionShortcutsCard(
                        language = language,
                        onMarkAttendance = { onNavigateTab(NavigationTab.ATTENDANCE) },
                        onShowQr = onOpenQrModal,
                        onViewClasses = { onNavigateTab(NavigationTab.CLASSES) }
                    )
                }

                item {
                    InstructorBatchesCard(
                        batches = viewModel.batches.collectAsState().value,
                        trades = trades,
                        language = language,
                        onSelectBatch = { batchId ->
                            viewModel.selectBatch(batchId)
                            onNavigateTab(NavigationTab.ATTENDANCE)
                        }
                    )
                }

                item {
                    AnnouncementsSection(
                        announcements = announcements,
                        language = language
                    )
                }
            }

            UserRole.ADMIN -> {
                item {
                    AdminCampusStatsCard(
                        totalStudents = allStudents.size,
                        totalTrades = trades.size,
                        allAttendance = allAttendance,
                        language = language
                    )
                }

                item {
                    AdminTradeAttendanceCard(
                        trades = trades,
                        allStudents = allStudents,
                        allAttendance = allAttendance,
                        language = language
                    )
                }

                item {
                    AdminLowAttendanceAlertsCard(
                        allStudents = allStudents,
                        viewModel = viewModel,
                        language = language
                    )
                }

                item {
                    AnnouncementsSection(
                        announcements = announcements,
                        language = language
                    )
                }
            }
        }
    }
}

// ---------------- STUDENT COMPONENTS ----------------

@Composable
fun StudentHeaderCard(
    student: Student?,
    trades: List<Trade>,
    language: AppLanguage,
    onOpenIdCard: () -> Unit
) {
    val trade = trades.firstOrNull { it.id == student?.tradeId }
    val tradeName = if (language == AppLanguage.TELUGU) trade?.nameTe ?: trade?.nameEn else trade?.nameEn

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Student Avatar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = student?.name?.take(1) ?: "S",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.TELUGU) student?.nameTe ?: "" else student?.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Adm No: ${student?.admissionNo ?: "ATC-2024"}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = tradeName ?: "Advanced ITI Trade",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = onOpenIdCard,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "View ID Card",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StudentAttendanceOverviewCard(
    summary: com.example.ui.viewmodel.AttendanceSummary?,
    language: AppLanguage,
    onNavigateAttendance: () -> Unit,
    onQrCheckIn: () -> Unit
) {
    val percentage = summary?.percentage ?: 85.0
    val isWarning = percentage < 75.0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isWarning) StatusAbsentContainer.copy(alpha = 0.6f) else StatusPresentContainer.copy(alpha = 0.5f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (isWarning) StatusAbsent else StatusPresent,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.overallAttendance.get(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                // Percentage Tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isWarning) StatusAbsent else StatusPresent
                ) {
                    Text(
                        text = "${String.format("%.1f", percentage)}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (percentage / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isWarning) StatusAbsent else StatusPresent,
                trackColor = Color.White.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isWarning) AppStrings.attendanceWarning.get(language) else AppStrings.attendanceGood.get(language),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isWarning) StatusAbsent else Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onQrCheckIn,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.qrCheckIn.get(language), fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onNavigateAttendance,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(AppStrings.monthlyCalendar.get(language).take(14), fontSize = 11.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun StudentNextClassCard(
    student: Student?,
    schedules: List<ClassSchedule>,
    language: AppLanguage,
    onNavigateClasses: () -> Unit
) {
    val studentSchedule = schedules.filter { it.batchId == student?.batchId }
    val nextClass = studentSchedule.firstOrNull()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigateClasses() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = AppStrings.nextClass.get(language),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = nextClass?.timeSlot ?: "09:30 AM",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.TELUGU) nextClass?.subjectTe ?: "CNC ప్రోగ్రామింగ్" else nextClass?.subjectEn ?: "CNC Milling Programming",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Room, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = nextClass?.roomWorkshop ?: "Workshop Lab 2",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = nextClass?.instructorName ?: "Trainer",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StudentQuickActionsRow(
    language: AppLanguage,
    onOpenIdCard: () -> Unit,
    onOpenRoutine: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenMaterials: () -> Unit
) {
    Column {
        Text(
            text = if (language == AppLanguage.TELUGU) "త్వరిత సేవలు" else "Quick Actions",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickActionButton(
                title = AppStrings.digitalIdCard.get(language),
                icon = Icons.Default.Badge,
                color = IndustrialBluePrimary,
                modifier = Modifier.weight(1f),
                onClick = onOpenIdCard
            )
            QuickActionButton(
                title = AppStrings.dailyRoutine.get(language),
                icon = Icons.Default.Timeline,
                color = SafetyOrangeSecondary,
                modifier = Modifier.weight(1f),
                onClick = onOpenRoutine
            )
            QuickActionButton(
                title = AppStrings.mcqQuiz.get(language),
                icon = Icons.Default.Quiz,
                color = PrecisionTeal,
                modifier = Modifier.weight(1f),
                onClick = onOpenQuiz
            )
            QuickActionButton(
                title = AppStrings.studyMaterials.get(language),
                icon = Icons.Default.LibraryBooks,
                color = Color(0xFF6A1B9A),
                modifier = Modifier.weight(1f),
                onClick = onOpenMaterials
            )
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }
    }
}

// ---------------- INSTRUCTOR COMPONENTS ----------------

@Composable
fun InstructorHeaderCard(
    instructor: Instructor?,
    trades: List<Trade>,
    language: AppLanguage
) {
    val trade = trades.firstOrNull { it.id == instructor?.tradeId }
    val tradeName = if (language == AppLanguage.TELUGU) trade?.nameTe ?: trade?.nameEn else trade?.nameEn

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Engineering, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.TELUGU) instructor?.nameTe ?: "" else instructor?.name ?: "ATC Instructor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = instructor?.designation ?: "Senior Trade Instructor",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = tradeName ?: "Advanced ITI Trade",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun InstructorActionShortcutsCard(
    language: AppLanguage,
    onMarkAttendance: () -> Unit,
    onShowQr: () -> Unit,
    onViewClasses: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.TELUGU) "నేటి విధులు" else "Today's Instructor Tasks",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onMarkAttendance,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.FactCheck, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.markAllPresent.get(language) + " / " + AppStrings.tabAttendance.get(language), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onShowQr,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.displayQrCode.get(language).take(12), fontSize = 11.sp, maxLines = 1)
                }

                OutlinedButton(
                    onClick = onViewClasses,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.EditCalendar, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.postClassUpdate.get(language).take(12), fontSize = 11.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun InstructorBatchesCard(
    batches: List<Batch>,
    trades: List<Trade>,
    language: AppLanguage,
    onSelectBatch: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.TELUGU) "మీ బ్యాచ్‌ల జాబితా" else "Active Batches (Tap to Mark Attendance)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            batches.forEach { batch ->
                val trade = trades.firstOrNull { it.id == batch.tradeId }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectBatch(batch.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = batch.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = "${trade?.code ?: "ATC"} • ${batch.totalStudents} Students Enrolled",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

// ---------------- ADMIN COMPONENTS ----------------

@Composable
fun AdminCampusStatsCard(
    totalStudents: Int,
    totalTrades: Int,
    allAttendance: List<AttendanceRecord>,
    language: AppLanguage
) {
    val todayRecords = allAttendance.filter { it.date == "2026-10-26" }
    val presentCount = todayRecords.count { it.status == AttendanceStatus.PRESENT }
    val todayPercent = if (todayRecords.isNotEmpty()) (presentCount.toDouble() / todayRecords.size) * 100.0 else 91.5

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = AppStrings.adminDashboard.get(language),
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = if (language == AppLanguage.TELUGU) "క్యాంపస్ హాజరు మరియు విశ్లేషణ" else "Campus Operations & Attendance",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AdminStatItem(
                    title = AppStrings.totalStudents.get(language),
                    value = "$totalStudents",
                    icon = Icons.Default.Groups
                )
                AdminStatItem(
                    title = AppStrings.totalTrades.get(language),
                    value = "$totalTrades",
                    icon = Icons.Default.Category
                )
                AdminStatItem(
                    title = AppStrings.todayOverallAttendance.get(language),
                    value = "${String.format("%.0f", todayPercent)}%",
                    icon = Icons.Default.Analytics
                )
            }
        }
    }
}

@Composable
fun AdminStatItem(title: String, value: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(text = title.take(12), color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
    }
}

@Composable
fun AdminTradeAttendanceCard(
    trades: List<Trade>,
    allStudents: List<Student>,
    allAttendance: List<AttendanceRecord>,
    language: AppLanguage
) {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.TELUGU) "ట్రేడ్ వారీగా నేటి హాజరు శాతం" else "Trade-wise Attendance Percentage",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            trades.forEach { trade ->
                val tradeStudents = allStudents.filter { it.tradeId == trade.id }
                val tradeStudentIds = tradeStudents.map { it.id }.toSet()
                val tradeRecords = allAttendance.filter { it.studentId in tradeStudentIds }
                val present = tradeRecords.count { it.status == AttendanceStatus.PRESENT }
                val pct = if (tradeRecords.isNotEmpty()) (present.toDouble() / tradeRecords.size) * 100.0 else 88.0

                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${trade.code} - ${if (language == AppLanguage.TELUGU) trade.nameTe.take(18) else trade.nameEn.take(22)}...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${String.format("%.0f", pct)}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pct < 75.0) StatusAbsent else StatusPresent
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (pct / 100.0).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (pct < 75.0) StatusAbsent else StatusPresent
                    )
                }
            }
        }
    }
}

@Composable
fun AdminLowAttendanceAlertsCard(
    allStudents: List<Student>,
    viewModel: AtcViewModel,
    language: AppLanguage
) {
    val lowAttendanceStudents = remember(allStudents) {
        allStudents.filter { st ->
            val summary = viewModel.getStudentAttendanceSummary(st.id)
            summary.percentage < 75.0
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = StatusAbsentContainer.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusAbsent)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.lowAttendanceAlerts.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = StatusAbsent
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (lowAttendanceStudents.isEmpty()) {
                Text(
                    text = "All students currently have >= 75% attendance.",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            } else {
                lowAttendanceStudents.take(4).forEach { st ->
                    val summary = viewModel.getStudentAttendanceSummary(st.id)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) st.nameTe else st.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${st.admissionNo} • Guardian: ${st.guardianPhone}",
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "${String.format("%.0f", summary.percentage)}%",
                                color = StatusAbsent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { viewModel.sendAbsenceAlert(st) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sms,
                                    contentDescription = "Send SMS",
                                    tint = SafetyOrangeSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------- SHARED ANNOUNCEMENTS ----------------

@Composable
fun AnnouncementsSection(
    announcements: List<Announcement>,
    language: AppLanguage
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = AppStrings.announcements.get(language),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Icon(Icons.Default.Campaign, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        announcements.take(2).forEach { notice ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (notice.isUrgent) SafetyOrangeContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (notice.isUrgent) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SafetyOrangeSecondary
                            ) {
                                Text(
                                    text = AppStrings.urgentNotice.get(language),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        } else {
                            Text(text = notice.date, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(text = notice.authorRole, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.TELUGU) notice.titleTe else notice.titleEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.TELUGU) notice.contentTe else notice.contentEn,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
