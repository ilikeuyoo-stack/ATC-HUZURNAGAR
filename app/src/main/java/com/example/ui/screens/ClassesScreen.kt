package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun ClassesScreen(
    viewModel: AtcViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val batches by viewModel.batches.collectAsState()
    val trades by viewModel.trades.collectAsState()
    val schedules by viewModel.allSchedules.collectAsState()
    val classPlans by viewModel.allClassPlans.collectAsState()

    var selectedDay by remember { mutableStateOf(1) } // 1=Mon, 2=Tue, 3=Wed, 4=Thu, 5=Fri, 6=Sat
    var selectedBatchId by remember(student, batches) {
        mutableStateOf(student?.batchId ?: batches.firstOrNull()?.id ?: "batch_cnc_2024")
    }
    var showPostPlanDialog by remember { mutableStateOf(false) }

    val currentBatch = batches.firstOrNull { it.id == selectedBatchId }
    val currentTrade = trades.firstOrNull { it.id == currentBatch?.tradeId }
    val daySchedules = schedules.filter { it.batchId == selectedBatchId && it.dayOfWeek == selectedDay }
    val latestPlan = classPlans.firstOrNull { it.batchId == selectedBatchId }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Trade & Batch Selector Bar
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (language == AppLanguage.TELUGU) "ట్రేడ్ & బ్యాచ్ టైమ్‌టేబుల్" else "Trade & Batch Timetable",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

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
                                    onClick = { selectedBatchId = batch.id },
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
                }
            }
        }

        // Days of week tabs (Mon to Sat)
        item {
            val days = listOf(
                Pair(1, AppStrings.mon.get(language)),
                Pair(2, AppStrings.tue.get(language)),
                Pair(3, AppStrings.wed.get(language)),
                Pair(4, AppStrings.thu.get(language)),
                Pair(5, AppStrings.fri.get(language)),
                Pair(6, AppStrings.sat.get(language))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                days.forEach { (dayIndex, dayLabel) ->
                    val isSelected = selectedDay == dayIndex
                    Button(
                        onClick = { selectedDay = dayIndex },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = dayLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Today's Topic Covered & Homework Prep Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = SafetyOrangeSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.topicsCovered.get(language),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        if (role != UserRole.STUDENT) {
                            IconButton(
                                onClick = { showPostPlanDialog = true },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Class Plan", tint = SafetyOrangeSecondary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = latestPlan?.topicCovered ?: "Parametric G-Code Loops, Tool Offsets, and Safety Interlocks.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = IndustrialBluePrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.homeworkPrep.get(language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = IndustrialBluePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = latestPlan?.homeworkPrep ?: "Bring Engineering Drawing Instruments and wear steel-toe safety shoes for CNC practical.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Schedule List Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${AppStrings.todayClasses.get(language)} (${daySchedules.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                if (role != UserRole.STUDENT) {
                    TextButton(onClick = { showPostPlanDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.postClassUpdate.get(language), fontSize = 12.sp)
                    }
                }
            }
        }

        // List of Classes
        if (daySchedules.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == AppLanguage.TELUGU) "ఈ రోజు ప్రత్యేక తరగతులు లేవు / ప్రాక్టికల్ వర్క్‌షాప్" else "No scheduled theory classes on this day / Full Workshop Practice",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(daySchedules) { schedule ->
                ClassScheduleCard(schedule = schedule, language = language)
            }
        }
    }

    // Post Plan Dialog
    if (showPostPlanDialog) {
        var topicInput by remember { mutableStateOf(latestPlan?.topicCovered ?: "") }
        var homeworkInput by remember { mutableStateOf(latestPlan?.homeworkPrep ?: "") }

        AlertDialog(
            onDismissRequest = { showPostPlanDialog = false },
            title = {
                Text(
                    text = AppStrings.postClassUpdate.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Batch: ${currentBatch?.name ?: ""}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text(AppStrings.topicsCovered.get(language), fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = homeworkInput,
                        onValueChange = { homeworkInput = it },
                        label = { Text(AppStrings.homeworkPrep.get(language), fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.postClassPlan(selectedBatchId, topicInput, homeworkInput)
                        showPostPlanDialog = false
                    }
                ) {
                    Text(AppStrings.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostPlanDialog = false }) {
                    Text(AppStrings.cancel.get(language))
                }
            }
        )
    }
}

@Composable
fun ClassScheduleCard(
    schedule: ClassSchedule,
    language: AppLanguage
) {
    val (typeColor, typeLabel) = when (schedule.classType) {
        ClassType.THEORY -> Pair(IndustrialBluePrimary, if (language == AppLanguage.TELUGU) "థియరీ" else "THEORY")
        ClassType.PRACTICAL -> Pair(SafetyOrangeSecondary, if (language == AppLanguage.TELUGU) "ప్రాక్టికల్" else "PRACTICAL")
        ClassType.WORKSHOP -> Pair(PrecisionTeal, if (language == AppLanguage.TELUGU) "వర్క్‌షాప్" else "WORKSHOP")
        ClassType.EMPLOYABILITY_SKILLS -> Pair(Color(0xFF6A1B9A), if (language == AppLanguage.TELUGU) "స్కిల్స్" else "EMPLOYABILITY")
        ClassType.ENGG_DRAWING -> Pair(Color(0xFF00838F), if (language == AppLanguage.TELUGU) "డ్రాయింగ్" else "DRAWING")
        ClassType.WORKSHOP_CALC -> Pair(Color(0xFFD84315), if (language == AppLanguage.TELUGU) "కాలిక్యులేషన్" else "W/S CALC")
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time Strip
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(80.dp)
            ) {
                Text(
                    text = schedule.timeSlot.substringBefore("-").trim(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = schedule.timeSlot.substringAfter("-").trim(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = typeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = typeLabel,
                        color = typeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.TELUGU) schedule.subjectTe else schedule.subjectEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Room,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.roomWorkshop,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = schedule.instructorName,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
