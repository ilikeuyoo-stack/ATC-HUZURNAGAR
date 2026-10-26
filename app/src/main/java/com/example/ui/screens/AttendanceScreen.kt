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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AtcViewModel

@Composable
fun AttendanceScreen(
    viewModel: AtcViewModel,
    onOpenQrModal: () -> Unit
) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val batches by viewModel.batches.collectAsState()
    val trades by viewModel.trades.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val allAttendance by viewModel.allAttendance.collectAsState()
    val draftMap by viewModel.attendanceDraftMap.collectAsState()
    val selectedBatchId by viewModel.selectedBatchId.collectAsState()
    val leaveRequests by viewModel.allLeaveRequests.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()

    var showLeaveDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showAuditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (role) {
            UserRole.STUDENT -> {
                // Today's Status Card
                item {
                    val summary = student?.let { viewModel.getStudentAttendanceSummary(it.id) }
                    val todayRecord = allAttendance.firstOrNull { it.studentId == student?.id && it.date == "2026-10-26" }

                    StudentAttendanceDetailCard(
                        student = student,
                        todayStatus = todayRecord?.status ?: AttendanceStatus.PRESENT,
                        summary = summary,
                        language = language,
                        onQrCheckIn = onOpenQrModal,
                        onRequestLeave = { showLeaveDialog = true }
                    )
                }

                // Monthly Calendar View
                item {
                    StudentMonthlyCalendarCard(
                        studentId = student?.id ?: "",
                        allAttendance = allAttendance,
                        language = language
                    )
                }

                // Student's Leave Requests List
                item {
                    val myLeaves = leaveRequests.filter { it.studentId == student?.id }
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = AppStrings.requestLeave.get(language),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                TextButton(onClick = { showLeaveDialog = true }) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.requestLeave.get(language), fontSize = 12.sp)
                                }
                            }

                            if (myLeaves.isEmpty()) {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "ఎటువంటి సెలవు దరఖాస్తులు లేవు." else "No pending or past leave requests.",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            } else {
                                myLeaves.forEach { req ->
                                    val (statusColor, statusText) = when (req.status) {
                                        LeaveStatus.APPROVED -> Pair(StatusPresent, AppStrings.statusApproved.get(language))
                                        LeaveStatus.REJECTED -> Pair(StatusAbsent, AppStrings.statusRejected.get(language))
                                        LeaveStatus.PENDING -> Pair(StatusLate, AppStrings.statusPending.get(language))
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(text = "${req.startDate} to ${req.endDate}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                Text(text = req.reason, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = statusColor.copy(alpha = 0.15f)
                                            ) {
                                                Text(
                                                    text = statusText,
                                                    color = statusColor,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            UserRole.INSTRUCTOR -> {
                // Batch & Date Selector
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.TELUGU) "బ్యాచ్ హాజరు నమోదు (ఒకే ట్యాప్‌తో)" else "Daily Batch Attendance Entry (One-Tap)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (batches.isNotEmpty()) {
                                val selectedIndex = batches.indexOfFirst { it.id == selectedBatchId }.let {
                                    if (it >= 0) it else 0
                                }.coerceIn(0, batches.size - 1)

                                ScrollableTabRow(
                                    selectedTabIndex = selectedIndex,
                                    edgePadding = 0.dp
                                ) {
                                    batches.forEach { batch ->
                                        val isSelected = batch.id == selectedBatchId
                                        Tab(
                                            selected = isSelected,
                                            onClick = { viewModel.selectBatch(batch.id) },
                                            text = {
                                                Text(
                                                    text = batch.name,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.markAllDraft(AttendanceStatus.PRESENT) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.markAllPresent.get(language), fontSize = 11.sp, maxLines = 1)
                                }

                                OutlinedButton(
                                    onClick = onOpenQrModal,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(AppStrings.displayQrCode.get(language).take(12), fontSize = 11.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                // Student Roster with 4 big toggle buttons per student
                val batchStudents = allStudents.filter { it.batchId == selectedBatchId }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (language == AppLanguage.TELUGU) "విద్యార్థుల జాబితా" else "Student Roster"} (${batchStudents.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Date: 2026-10-26",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(batchStudents) { st ->
                    val status = draftMap[st.id] ?: AttendanceStatus.PRESENT
                    InstructorStudentAttendanceRow(
                        student = st,
                        currentStatus = status,
                        language = language,
                        onStatusChange = { newStatus ->
                            viewModel.setStudentDraftStatus(st.id, newStatus)
                        }
                    )
                }

                item {
                    Button(
                        onClick = { viewModel.submitAttendance("2026-10-26") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.submitAttendance.get(language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Instructor Leave Requests Review
                item {
                    val pendingLeaves = leaveRequests.filter { it.status == LeaveStatus.PENDING }
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.TELUGU) "పరిశీలించాల్సిన సెలవు దరఖాస్తులు (${pendingLeaves.size})" else "Pending Leave Requests (${pendingLeaves.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (pendingLeaves.isEmpty()) {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "సెలవు దరఖాస్తులు ఏవీ పెండింగ్‌లో లేవు." else "No pending leave requests from students.",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            } else {
                                pendingLeaves.forEach { req ->
                                    val reqStudent = allStudents.firstOrNull { it.id == req.studentId }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = reqStudent?.name ?: "Student",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    text = "${req.startDate} to ${req.endDate}",
                                                    fontSize = 11.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            Text(text = "Reason: ${req.reason}", fontSize = 11.sp, modifier = Modifier.padding(vertical = 4.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                TextButton(
                                                    onClick = { viewModel.reviewLeave(req.id, LeaveStatus.REJECTED, "Not approved by instructor") }
                                                ) {
                                                    Text(AppStrings.statusRejected.get(language), color = StatusAbsent, fontSize = 11.sp)
                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Button(
                                                    onClick = { viewModel.reviewLeave(req.id, LeaveStatus.APPROVED, "Approved by instructor") },
                                                    colors = ButtonDefaults.buttonColors(containerColor = StatusPresent),
                                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                                ) {
                                                    Text(AppStrings.statusApproved.get(language), fontSize = 11.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            UserRole.ADMIN -> {
                // Admin Overview
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = if (language == AppLanguage.TELUGU) "అడ్మిన్ హాజరు నివేదికలు & తనిఖీ" else "Admin Attendance Management & Reports",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { showExportDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(AppStrings.exportReport.get(language).take(13), fontSize = 11.sp, maxLines = 1)
                                }

                                OutlinedButton(
                                    onClick = { showAuditDialog = true },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(AppStrings.editPastAttendance.get(language).take(13), fontSize = 11.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                item {
                    AdminLowAttendanceAlertsCard(
                        allStudents = allStudents,
                        viewModel = viewModel,
                        language = language
                    )
                }

                // Recent Audit Trail
                item {
                    Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "హాజరు మార్పుల ఆడిట్ లాగ్" else "Attendance Audit Log (Recent)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                TextButton(onClick = { showAuditDialog = true }) {
                                    Text(AppStrings.viewDetails.get(language), fontSize = 11.sp)
                                }
                            }

                            auditLogs.take(3).forEach { log ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(text = log.studentName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(
                                            text = "${log.oldStatus} ➔ ${log.newStatus} • by ${log.modifiedBy}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(text = "Reason: ${log.reason}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Leave Request Dialog Form
    if (showLeaveDialog) {
        var startDate by remember { mutableStateOf("2026-10-27") }
        var endDate by remember { mutableStateOf("2026-10-28") }
        var reason by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = {
                Text(
                    text = AppStrings.requestLeave.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text(AppStrings.startDate.get(language), fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text(AppStrings.endDate.get(language), fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text(AppStrings.reason.get(language), fontSize = 11.sp) },
                        placeholder = { Text("e.g. Medical illness or family function", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reason.isNotBlank()) {
                            viewModel.submitLeaveRequest(startDate, endDate, reason)
                            showLeaveDialog = false
                        }
                    }
                ) {
                    Text(AppStrings.submitAttendance.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text(AppStrings.cancel.get(language))
                }
            }
        )
    }

    // Export Report Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text(
                    text = AppStrings.exportReport.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Advanced Training Centre (ATC) ITI, Huzurnagar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Month: October 2026 | Total Students: 60 | Total Batches: 6",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(text = "• Basic Designer (BDVV): 92.5% Attendance", fontSize = 11.sp)
                    Text(text = "• Artisan Advanced Tools (AUT): 89.0% Attendance", fontSize = 11.sp)
                    Text(text = "• CNC Machining (ACNCMT): 91.2% Attendance", fontSize = 11.sp)
                    Text(text = "• Industrial Robotics (IRDMT): 88.4% Attendance", fontSize = 11.sp)
                    Text(text = "• Process Control (MPCA): 87.8% Attendance", fontSize = 11.sp)
                    Text(text = "• Mechanic EV (MEV): 94.0% Attendance", fontSize = 11.sp)
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        text = "Format: NCVT Standard Form 44 Compliance PDF & Excel format generated for State Directorate of Employment & Training.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showExportDialog = false }) {
                    Text(AppStrings.downloadOffline.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text(AppStrings.close.get(language))
                }
            }
        )
    }

    // Audit Log Full Dialog
    if (showAuditDialog) {
        AlertDialog(
            onDismissRequest = { showAuditDialog = false },
            title = {
                Text(
                    text = AppStrings.editPastAttendance.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                LazyColumn(modifier = Modifier.height(260.dp)) {
                    items(auditLogs) { log ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = log.studentName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(
                                    text = "Date: ${log.date} | ${log.oldStatus} ➔ ${log.newStatus}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(text = "By: ${log.modifiedBy}", fontSize = 10.sp)
                                Text(text = "Reason: ${log.reason}", fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAuditDialog = false }) {
                    Text(AppStrings.close.get(language))
                }
            }
        )
    }
}

@Composable
fun StudentAttendanceDetailCard(
    student: Student?,
    todayStatus: AttendanceStatus,
    summary: com.example.ui.viewmodel.AttendanceSummary?,
    language: AppLanguage,
    onQrCheckIn: () -> Unit,
    onRequestLeave: () -> Unit
) {
    val (statusColor, statusText) = when (todayStatus) {
        AttendanceStatus.PRESENT -> Pair(StatusPresent, AppStrings.statusPresent.get(language))
        AttendanceStatus.ABSENT -> Pair(StatusAbsent, AppStrings.statusAbsent.get(language))
        AttendanceStatus.LATE -> Pair(StatusLate, AppStrings.statusLate.get(language))
        AttendanceStatus.LEAVE -> Pair(StatusLeave, AppStrings.statusLeave.get(language))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = AppStrings.todayAttendance.get(language),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "2026-10-26 (Today)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Attendance Counts Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AttendanceMetric(title = AppStrings.statusPresent.get(language), count = summary?.presentDays ?: 18, color = StatusPresent)
                AttendanceMetric(title = AppStrings.statusAbsent.get(language), count = summary?.absentDays ?: 2, color = StatusAbsent)
                AttendanceMetric(title = AppStrings.statusLate.get(language), count = summary?.lateDays ?: 1, color = StatusLate)
                AttendanceMetric(title = AppStrings.statusLeave.get(language), count = summary?.leaveDays ?: 1, color = StatusLeave)
            }

            Spacer(modifier = Modifier.height(14.dp))

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
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.qrCheckIn.get(language), fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onRequestLeave,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.EventBusy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(AppStrings.requestLeave.get(language), fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun AttendanceMetric(title: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$count", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = color)
        Text(text = title, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun StudentMonthlyCalendarCard(
    studentId: String,
    allAttendance: List<AttendanceRecord>,
    language: AppLanguage
) {
    val myRecords = allAttendance.filter { it.studentId == studentId }
    val recordMap = myRecords.associateBy { it.date }

    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${AppStrings.monthlyCalendar.get(language)} - October 2026",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Calendar days row (sample of last 7 working days)
            val sampleDays = listOf(
                "2026-10-20" to "Tue 20",
                "2026-10-21" to "Wed 21",
                "2026-10-22" to "Thu 22",
                "2026-10-23" to "Fri 23",
                "2026-10-24" to "Sat 24",
                "2026-10-25" to "Sun 25",
                "2026-10-26" to "Mon 26"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                sampleDays.forEach { (dateStr, label) ->
                    val rec = recordMap[dateStr]
                    val (dotColor, symbol) = when (rec?.status) {
                        AttendanceStatus.PRESENT -> Pair(StatusPresent, "P")
                        AttendanceStatus.ABSENT -> Pair(StatusAbsent, "A")
                        AttendanceStatus.LATE -> Pair(StatusLate, "L")
                        AttendanceStatus.LEAVE -> Pair(StatusLeave, "Lv")
                        null -> if (dateStr.endsWith("25")) Pair(Color.LightGray, "H") else Pair(StatusPresent, "P")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = label.substringBefore(" "), fontSize = 10.sp, color = Color.Gray)
                        Text(text = label.substringAfter(" "), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(dotColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = symbol,
                                color = dotColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalendarLegendItem(color = StatusPresent, label = AppStrings.statusPresent.get(language))
                CalendarLegendItem(color = StatusAbsent, label = AppStrings.statusAbsent.get(language))
                CalendarLegendItem(color = StatusLate, label = AppStrings.statusLate.get(language))
                CalendarLegendItem(color = StatusLeave, label = AppStrings.statusLeave.get(language))
            }
        }
    }
}

@Composable
fun CalendarLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 9.sp, color = Color.Gray)
    }
}

@Composable
fun InstructorStudentAttendanceRow(
    student: Student,
    currentStatus: AttendanceStatus,
    language: AppLanguage,
    onStatusChange: (AttendanceStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.TELUGU) student.nameTe else student.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = student.admissionNo,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 4 Toggle Chips (Present, Absent, Late, Leave)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                StatusToggleButton(
                    label = "P",
                    isSelected = currentStatus == AttendanceStatus.PRESENT,
                    activeColor = StatusPresent,
                    onClick = { onStatusChange(AttendanceStatus.PRESENT) }
                )
                StatusToggleButton(
                    label = "A",
                    isSelected = currentStatus == AttendanceStatus.ABSENT,
                    activeColor = StatusAbsent,
                    onClick = { onStatusChange(AttendanceStatus.ABSENT) }
                )
                StatusToggleButton(
                    label = "L",
                    isSelected = currentStatus == AttendanceStatus.LATE,
                    activeColor = StatusLate,
                    onClick = { onStatusChange(AttendanceStatus.LATE) }
                )
                StatusToggleButton(
                    label = "Lv",
                    isSelected = currentStatus == AttendanceStatus.LEAVE,
                    activeColor = StatusLeave,
                    onClick = { onStatusChange(AttendanceStatus.LEAVE) }
                )
            }
        }
    }
}

@Composable
fun StatusToggleButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) activeColor else MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
