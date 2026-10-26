package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.example.data.model.UserRole
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.IndustrialBluePrimary
import com.example.ui.theme.SafetyOrangeSecondary
import com.example.ui.theme.StatusPresent

@Composable
fun AttendanceQrModal(
    role: UserRole,
    studentId: String?,
    language: AppLanguage,
    onStudentCheckIn: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var checkInDone by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = IndustrialBluePrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (role == UserRole.STUDENT) AppStrings.qrCheckIn.get(language) else AppStrings.displayQrCode.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (role == UserRole.STUDENT) {
                    if (checkInDone) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusPresent,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Geo-Verified! Attendance Marked Present.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = StatusPresent,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ATC Huzurnagar Classroom Hub • 2026-10-26",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    } else {
                        // Simulated QR Scanner Viewfinder
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(3.dp, SafetyOrangeSecondary, RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.85f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = SafetyOrangeSecondary,
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ALIGN QR IN FRAME",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Campus Geofence Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusPresent.copy(alpha = 0.12f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = StatusPresent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Inside ATC Huzurnagar Geofence (100m radius)",
                                    fontSize = 10.sp,
                                    color = StatusPresent,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                studentId?.let { onStudentCheckIn(it) }
                                checkInDone = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = if (language == AppLanguage.TELUGU) "హాజరును నిర్ధారించండి" else "Verify & Mark Present",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    // Instructor QR Display
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // High Contrast Simulated 2D Matrix (QR Grid)
                                Box(
                                    modifier = Modifier
                                        .size(150.dp)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        repeat(9) { row ->
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                repeat(9) { col ->
                                                    val isCorner = (row < 3 && col < 3) || (row < 3 && col > 5) || (row > 5 && col < 3)
                                                    val isBlack = isCorner || ((row + col * 2) % 3 == 0)
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .background(if (isBlack) Color.Black else Color.White)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "SESSION: ATC-DAILY-2026-10-26",
                                    fontSize = 9.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = AppStrings.qrWindowNotice.get(language),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyOrangeSecondary
                        )
                        Text(
                            text = "Expires in: 14m 32s • Requires student to be in campus",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(AppStrings.close.get(language))
            }
        }
    )
}
