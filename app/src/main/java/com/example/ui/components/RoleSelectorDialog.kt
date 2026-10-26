package com.example.ui.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Instructor
import com.example.data.model.Student
import com.example.data.model.UserRole
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings

@Composable
fun RoleSelectorDialog(
    currentRole: UserRole,
    currentStudent: Student?,
    currentInstructor: Instructor?,
    allStudents: List<Student>,
    instructors: List<Instructor>,
    language: AppLanguage,
    onRoleSelected: (UserRole) -> Unit,
    onStudentSelected: (Student) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Quick Switch, 1: Phone/Admission Login

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SwitchAccount,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AppStrings.switchRole.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text(if (language == AppLanguage.TELUGU) "త్వరిత ఎంపిక" else "Quick Switch", fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text(if (language == AppLanguage.TELUGU) "ఫోన్ / OTP" else "Phone / OTP", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedTab == 0) {
                    // Quick Switch Options
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // 1. Student Card
                        RoleOptionCard(
                            title = AppStrings.roleStudent.get(language),
                            subtitle = currentStudent?.let { "${it.name} (${it.admissionNo})" } ?: "Sample Student",
                            icon = Icons.Default.School,
                            isSelected = currentRole == UserRole.STUDENT,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            onClick = {
                                onRoleSelected(UserRole.STUDENT)
                                onDismiss()
                            }
                        )

                        // 2. Instructor Card
                        RoleOptionCard(
                            title = AppStrings.roleInstructor.get(language),
                            subtitle = currentInstructor?.let { "${it.name} (${it.designation})" } ?: "K. Ramesh Babu (CNC Lead)",
                            icon = Icons.Default.Engineering,
                            isSelected = currentRole == UserRole.INSTRUCTOR,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            onClick = {
                                onRoleSelected(UserRole.INSTRUCTOR)
                                onDismiss()
                            }
                        )

                        // 3. Admin Card
                        RoleOptionCard(
                            title = AppStrings.roleAdmin.get(language),
                            subtitle = if (language == AppLanguage.TELUGU) "ప్రిన్సిపాల్ కార్యాలయం, ATC హుజూర్‌నగర్" else "Principal Office, ATC Huzurnagar",
                            icon = Icons.Default.AdminPanelSettings,
                            isSelected = currentRole == UserRole.ADMIN,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            onClick = {
                                onRoleSelected(UserRole.ADMIN)
                                onDismiss()
                            }
                        )

                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        // Switch Active Student Dropdown preview
                        Text(
                            text = if (language == AppLanguage.TELUGU) "విద్యార్థిని మార్చండి (టెస్టింగ్ కోసం):" else "Switch Active Student (for testing):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        LazyColumn(modifier = Modifier.height(110.dp)) {
                            items(allStudents.take(6)) { student ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (student.id == currentStudent?.id) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onStudentSelected(student)
                                            onRoleSelected(UserRole.STUDENT)
                                            onDismiss()
                                        }
                                        .padding(vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.TELUGU) student.nameTe else student.name,
                                            fontSize = 12.sp,
                                            fontWeight = if (student.id == currentStudent?.id) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = student.admissionNo,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Phone / Admission No login simulation
                    var phoneOrAdm by remember { mutableStateOf("") }
                    var pinOrOtp by remember { mutableStateOf("") }
                    var loginMsg by remember { mutableStateOf<String?>(null) }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = phoneOrAdm,
                            onValueChange = { phoneOrAdm = it },
                            label = { Text(if (language == AppLanguage.TELUGU) "ఫోన్ / అడ్మిషన్ నంబర్" else "Phone / Admission No", fontSize = 12.sp) },
                            placeholder = { Text("e.g. 9848011221 or ATC-24-CNC01", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = pinOrOtp,
                            onValueChange = { pinOrOtp = it },
                            label = { Text(if (language == AppLanguage.TELUGU) "PIN లేదా OTP" else "PIN or OTP", fontSize = 12.sp) },
                            placeholder = { Text("Default PIN: 1234", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (phoneOrAdm.contains("EMP") || phoneOrAdm.contains("9848")) {
                                    onRoleSelected(UserRole.INSTRUCTOR)
                                    onDismiss()
                                } else if (phoneOrAdm.contains("ADMIN") || phoneOrAdm.contains("PRI")) {
                                    onRoleSelected(UserRole.ADMIN)
                                    onDismiss()
                                } else {
                                    onRoleSelected(UserRole.STUDENT)
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(AppStrings.simulateOtp.get(language), fontSize = 13.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(AppStrings.close.get(language))
            }
        }
    )
}

@Composable
fun RoleOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    containerColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) containerColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
