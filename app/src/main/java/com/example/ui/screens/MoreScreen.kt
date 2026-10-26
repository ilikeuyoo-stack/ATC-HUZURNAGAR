package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.AtcViewModel

enum class MoreSubSection {
    MENU,
    DAILY_ROUTINE,
    SAFETY_CHECKLIST,
    QUIZ_PRACTICE,
    ACADEMIC_CALENDAR,
    ANNOUNCEMENTS
}

@Composable
fun MoreScreen(
    viewModel: AtcViewModel,
    onOpenIdCard: () -> Unit,
    onOpenRoleSelector: () -> Unit
) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val routines by viewModel.dailyRoutines.collectAsState()
    val quizQuestions by viewModel.allQuizQuestions.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val trades by viewModel.trades.collectAsState()

    var activeSection by remember { mutableStateOf(MoreSubSection.MENU) }

    when (activeSection) {
        MoreSubSection.MENU -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = if (language == AppLanguage.TELUGU) "మరిన్ని సేవలు & సెట్టింగ్‌లు" else "More Services & Utilities",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // 1. Digital ID Card
                item {
                    MoreMenuRow(
                        title = AppStrings.digitalIdCard.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "ఫోటో, QR కోడ్ మరియు బ్లడ్ గ్రూప్‌తో ID కార్డు" else "Official digital student ID with photo & QR",
                        icon = Icons.Default.Badge,
                        iconColor = IndustrialBluePrimary,
                        onClick = onOpenIdCard
                    )
                }

                // 2. Daily Routine Timeline
                item {
                    MoreMenuRow(
                        title = AppStrings.dailyRoutine.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "అసెంబ్లీ, థియరీ, లంచ్, 5S వర్క్‌షాప్ క్లీనింగ్ షెడ్యూల్" else "Assembly, theory, practical, breaks & 5S timeline",
                        icon = Icons.Default.Timeline,
                        iconColor = SafetyOrangeSecondary,
                        onClick = { activeSection = MoreSubSection.DAILY_ROUTINE }
                    )
                }

                // 3. Safety Checklist
                item {
                    MoreMenuRow(
                        title = AppStrings.safetyChecklist.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "సేఫ్టీ బూట్లు, అప్రాన్, సేఫ్టీ గాగుల్స్ తనిఖీ" else "Daily safety gear check before entering workshop",
                        icon = Icons.Default.HealthAndSafety,
                        iconColor = PrecisionTeal,
                        onClick = { activeSection = MoreSubSection.SAFETY_CHECKLIST }
                    )
                }

                // 4. CBT Practice Quiz
                item {
                    MoreMenuRow(
                        title = AppStrings.mcqQuiz.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "NCVT CBT ఆన్‌లైన్ పరీక్షల కోసం ప్రాక్టీస్ ప్రశ్నలు" else "Mock multiple choice tests with instant answers & Telugu explanation",
                        icon = Icons.Default.Quiz,
                        iconColor = Color(0xFF6A1B9A),
                        onClick = { activeSection = MoreSubSection.QUIZ_PRACTICE }
                    )
                }

                // 5. Academic Calendar
                item {
                    MoreMenuRow(
                        title = AppStrings.academicCalendar.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "NCVT పరీక్షల తేదీలు, సెలవులు మరియు అసెస్‌మెంట్లు" else "NCVT exam timetable, state holidays & assessment schedule",
                        icon = Icons.Default.CalendarMonth,
                        iconColor = Color(0xFF00838F),
                        onClick = { activeSection = MoreSubSection.ACADEMIC_CALENDAR }
                    )
                }

                // 6. Announcements Board
                item {
                    MoreMenuRow(
                        title = AppStrings.announcements.get(language),
                        subtitle = if (language == AppLanguage.TELUGU) "ప్రిన్సిపాల్ మరియు ట్రైనర్ల అధికారిక నోటీసులు" else "College notices, emergency circulars & event updates",
                        icon = Icons.Default.Campaign,
                        iconColor = Color(0xFFD84315),
                        onClick = { activeSection = MoreSubSection.ANNOUNCEMENTS }
                    )
                }

                item {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                }

                // 7. Role Switcher
                item {
                    MoreMenuRow(
                        title = AppStrings.switchRole.get(language),
                        subtitle = "Current: ${when (role) {
                            UserRole.STUDENT -> AppStrings.roleStudent.get(language)
                            UserRole.INSTRUCTOR -> AppStrings.roleInstructor.get(language)
                            UserRole.ADMIN -> AppStrings.roleAdmin.get(language)
                        }}",
                        icon = Icons.Default.SwitchAccount,
                        iconColor = MaterialTheme.colorScheme.primary,
                        onClick = onOpenRoleSelector
                    )
                }

                // 8. Language Toggle
                item {
                    MoreMenuRow(
                        title = if (language == AppLanguage.ENGLISH) "భాష మార్చండి: తెలుగు" else "Change Language: English",
                        subtitle = if (language == AppLanguage.ENGLISH) "Switch app display to Telugu (తెలుగు)" else "యాప్‌ను ఆంగ్ల భాషలో చూడండి",
                        icon = Icons.Default.Translate,
                        iconColor = SafetyOrangeSecondary,
                        onClick = { viewModel.toggleLanguage() }
                    )
                }

                // App Info Footer
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ATC Huzurnagar Student Hub v1.0",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Advanced Training Centre ITI, Huzurnagar, Telangana",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        MoreSubSection.DAILY_ROUTINE -> {
            DailyRoutineView(
                routines = routines,
                language = language,
                onBack = { activeSection = MoreSubSection.MENU }
            )
        }

        MoreSubSection.SAFETY_CHECKLIST -> {
            SafetyChecklistView(
                language = language,
                onBack = { activeSection = MoreSubSection.MENU }
            )
        }

        MoreSubSection.QUIZ_PRACTICE -> {
            QuizPracticeView(
                quizQuestions = quizQuestions,
                trades = trades,
                language = language,
                onBack = { activeSection = MoreSubSection.MENU }
            )
        }

        MoreSubSection.ACADEMIC_CALENDAR -> {
            AcademicCalendarView(
                language = language,
                onBack = { activeSection = MoreSubSection.MENU }
            )
        }

        MoreSubSection.ANNOUNCEMENTS -> {
            AnnouncementsFullView(
                announcements = announcements,
                language = language,
                role = role,
                onPostNotice = { tradeId, titleEn, titleTe, contentEn, contentTe, isUrgent ->
                    viewModel.postNotice(tradeId, titleEn, titleTe, contentEn, contentTe, isUrgent)
                },
                onBack = { activeSection = MoreSubSection.MENU }
            )
        }
    }
}

@Composable
fun MoreMenuRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

// ---------------- SUB VIEWS ----------------

@Composable
fun DailyRoutineView(
    routines: List<DailyRoutine>,
    language: AppLanguage,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = AppStrings.dailyRoutine.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = SafetyOrangeSecondary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.TELUGU) "ATC హుజూర్‌నగర్ దినచర్య సమయపాలన తప్పనిసరి. ఉదయం 9:00 గంటలకు అసెంబ్లీ ప్రారంభమవుతుంది." else "Strict punctuality is observed at ATC Huzurnagar. Morning assembly begins sharp at 09:00 AM.",
                        fontSize = 11.sp
                    )
                }
            }
        }

        items(routines) { item ->
            val isCurrentTimeSlot = item.orderIndex == 4 // Highlights practical session as example active period
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isCurrentTimeSlot) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                border = if (isCurrentTimeSlot) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(90.dp)
                    ) {
                        Text(
                            text = item.timeSlot.substringBefore("-").trim(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = item.timeSlot.substringAfter("-").trim(),
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        if (isCurrentTimeSlot) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.TELUGU) item.titleTe else item.titleEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == AppLanguage.TELUGU) item.descriptionTe else item.descriptionEn,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SafetyChecklistView(
    language: AppLanguage,
    onBack: () -> Unit
) {
    var checkShoes by remember { mutableStateOf(true) }
    var checkDrawing by remember { mutableStateOf(true) }
    var checkGoggles by remember { mutableStateOf(false) }
    var checkEarPlugs by remember { mutableStateOf(false) }
    var checkIdCard by remember { mutableStateOf(true) }

    val checkedCount = listOf(checkShoes, checkDrawing, checkGoggles, checkEarPlugs, checkIdCard).count { it }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = AppStrings.safetyChecklist.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (checkedCount == 5) StatusPresentContainer else SafetyOrangeContainer.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (checkedCount == 5) "✓ Workshop Ready: All safety protocols verified!" else "Safety Gear Status: $checkedCount / 5 items ready",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (checkedCount == 5) StatusPresent else SafetyOrangeSecondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.TELUGU) "మెషినరీ ల్యాబ్స్ మరియు EV వర్క్‌షాప్‌లలోకి ప్రవేశించే ముందు రక్షణ పరికరాలను ధరించండి." else "Never enter CNC machine shop, EV high voltage bay, or robotics cell without verified PPE.",
                        fontSize = 11.sp
                    )
                }
            }
        }

        item {
            SafetyCheckItem(
                title = AppStrings.wearSafetyShoes.get(language),
                subtitle = "ISI steel-toe safety shoes protect against heavy tooling drop",
                isChecked = checkShoes,
                onCheckedChange = { checkShoes = it }
            )
        }
        item {
            SafetyCheckItem(
                title = AppStrings.idCardCheck.get(language),
                subtitle = "Must be worn visibly around neck at all times inside campus",
                isChecked = checkIdCard,
                onCheckedChange = { checkIdCard = it }
            )
        }
        item {
            SafetyCheckItem(
                title = AppStrings.wearSafetyGlasses.get(language),
                subtitle = "Required during CNC milling, lathe turning, and welding",
                isChecked = checkGoggles,
                onCheckedChange = { checkGoggles = it }
            )
        }
        item {
            SafetyCheckItem(
                title = AppStrings.bringDrawingInstruments.get(language),
                subtitle = "Mini drafter, compass, set squares, and drawing sheets",
                isChecked = checkDrawing,
                onCheckedChange = { checkDrawing = it }
            )
        }
        item {
            SafetyCheckItem(
                title = AppStrings.hairEarProtection.get(language),
                subtitle = "Tie long hair securely; wear ear defenders in high decibel plant areas",
                isChecked = checkEarPlugs,
                onCheckedChange = { checkEarPlugs = it }
            )
        }
    }
}

@Composable
fun SafetyCheckItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun QuizPracticeView(
    quizQuestions: List<QuizQuestion>,
    trades: List<Trade>,
    language: AppLanguage,
    onBack: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableStateOf(0) }
    var hasAnswered by remember { mutableStateOf(false) }

    val currentQ = quizQuestions.getOrNull(currentQuestionIndex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = AppStrings.mcqQuiz.get(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Score: $score",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        if (currentQ == null) {
            item {
                Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Practice Complete!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Your final score is $score out of ${quizQuestions.size}", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            currentQuestionIndex = 0
                            score = 0
                            selectedOptionIndex = null
                            hasAnswered = false
                        }) {
                            Text("Restart Practice")
                        }
                    }
                }
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${quizQuestions.size}",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = currentQ.subject,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.TELUGU) currentQ.questionTe else currentQ.questionEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        val options = listOf(currentQ.optionA, currentQ.optionB, currentQ.optionC, currentQ.optionD)
                        options.forEachIndexed { optIndex, optionText ->
                            val isChosen = selectedOptionIndex == optIndex
                            val isCorrect = currentQ.correctIndex == optIndex

                            val buttonBg = when {
                                !hasAnswered -> if (isChosen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                isCorrect -> StatusPresentContainer
                                isChosen && !isCorrect -> StatusAbsentContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }

                            val textColor = when {
                                !hasAnswered -> if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                isCorrect -> StatusPresent
                                isChosen && !isCorrect -> StatusAbsent
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = buttonBg,
                                border = if (isChosen) androidx.compose.foundation.BorderStroke(1.5.dp, textColor) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable(enabled = !hasAnswered) {
                                        selectedOptionIndex = optIndex
                                        hasAnswered = true
                                        if (optIndex == currentQ.correctIndex) {
                                            score += 1
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${('A' + optIndex)}. ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = textColor
                                    )
                                    Text(
                                        text = optionText,
                                        fontSize = 13.sp,
                                        fontWeight = if (isChosen || (hasAnswered && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                        color = textColor
                                    )
                                }
                            }
                        }

                        if (hasAnswered) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.TELUGU) "వివరణ (Explanation):" else "Explanation:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = IndustrialBluePrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (language == AppLanguage.TELUGU) currentQ.explanationTe else currentQ.explanationEn,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    currentQuestionIndex += 1
                                    selectedOptionIndex = null
                                    hasAnswered = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Next Question ➔")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AcademicCalendarView(
    language: AppLanguage,
    onBack: () -> Unit
) {
    val events = listOf(
        Triple("2026-11-14", "Formative Assessment 1 (FA-1)", "All Trades practical job inspection & theory test"),
        Triple("2026-12-24", "National Energy Conservation Day Seminar", "EV & Automation special projects demonstration"),
        Triple("2027-01-14", "Pongal / Sankranti Vacation", "College closed for festival"),
        Triple("2027-02-18", "Pre-NCVT Mock CBT Online Test", "CBT exam pattern practice at computer lab"),
        Triple("2027-03-20", "Campus Placement Drive (Tata Motors, Schneider, Cyient)", "Final year interview round at ATC Auditorium"),
        Triple("2027-07-10", "All India Trade Test (AITT) NCVT CBT Exams", "Official national certification examinations")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = AppStrings.academicCalendar.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        items(events) { (date, title, desc) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.width(74.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = date.substring(5),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(text = date.substring(0, 4), fontSize = 10.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementsFullView(
    announcements: List<Announcement>,
    language: AppLanguage,
    role: UserRole,
    onPostNotice: (String, String, String, String, String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    var showPostNoticeDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        text = AppStrings.announcements.get(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                if (role != UserRole.STUDENT) {
                    Button(
                        onClick = { showPostNoticeDialog = true },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.postNotice.get(language), fontSize = 11.sp)
                    }
                }
            }
        }

        items(announcements) { notice ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (notice.isUrgent) SafetyOrangeContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (notice.isUrgent) {
                            Surface(shape = RoundedCornerShape(4.dp), color = SafetyOrangeSecondary) {
                                Text(
                                    text = AppStrings.urgentNotice.get(language),
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        } else {
                            Text(text = notice.date, fontSize = 11.sp, color = Color.Gray)
                        }

                        Text(
                            text = "From: ${notice.authorRole}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.TELUGU) notice.titleTe else notice.titleEn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (language == AppLanguage.TELUGU) notice.contentTe else notice.contentEn,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    if (showPostNoticeDialog) {
        var noticeTitleEn by remember { mutableStateOf("") }
        var noticeTitleTe by remember { mutableStateOf("") }
        var noticeContentEn by remember { mutableStateOf("") }
        var noticeContentTe by remember { mutableStateOf("") }
        var isUrgent by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showPostNoticeDialog = false },
            title = { Text(AppStrings.postNotice.get(language), fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = noticeTitleEn,
                        onValueChange = { noticeTitleEn = it },
                        label = { Text("Title (English)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noticeTitleTe,
                        onValueChange = { noticeTitleTe = it },
                        label = { Text("Title (Telugu / తెలుగు)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noticeContentEn,
                        onValueChange = { noticeContentEn = it },
                        label = { Text("Notice Content", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isUrgent, onCheckedChange = { isUrgent = it })
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(AppStrings.urgentNotice.get(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (noticeTitleEn.isNotBlank()) {
                            onPostNotice(
                                "ALL",
                                noticeTitleEn,
                                if (noticeTitleTe.isBlank()) noticeTitleEn else noticeTitleTe,
                                noticeContentEn,
                                if (noticeContentTe.isBlank()) noticeContentEn else noticeContentTe,
                                isUrgent
                            )
                            showPostNoticeDialog = false
                        }
                    }
                ) {
                    Text(AppStrings.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPostNoticeDialog = false }) {
                    Text(AppStrings.cancel.get(language))
                }
            }
        )
    }
}
