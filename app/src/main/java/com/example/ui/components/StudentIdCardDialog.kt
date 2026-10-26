package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Student
import com.example.data.model.Trade
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.AppStrings
import com.example.ui.theme.IndustrialBluePrimary
import com.example.ui.theme.SafetyOrangeSecondary

@Composable
fun StudentIdCardDialog(
    student: Student?,
    trades: List<Trade>,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val trade = trades.firstOrNull { it.id == student?.tradeId }
    val tradeName = if (language == AppLanguage.TELUGU) trade?.nameTe ?: trade?.nameEn else trade?.nameEn

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, IndustrialBluePrimary, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Band
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(IndustrialBluePrimary)
                            .padding(vertical = 10.dp, horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "GOVT. OF TELANGANA • DET",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "ADVANCED TRAINING CENTRE (ATC) ITI",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Huzurnagar, Suryapet District",
                                color = SafetyOrangeSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Photo Avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(3.dp, SafetyOrangeSecondary, CircleShape)
                            .background(IndustrialBluePrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Student Photo",
                            tint = IndustrialBluePrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Student Name & Admission No
                    Text(
                        text = student?.name ?: "Student Name",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = if (language == AppLanguage.TELUGU) student?.nameTe ?: "" else "",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IndustrialBluePrimary.copy(alpha = 0.12f),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = student?.admissionNo ?: "ATC-2024",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IndustrialBluePrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    // Details Grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IdFieldRow(label = AppStrings.trade.get(language), value = tradeName ?: "Advanced ITI")
                        IdFieldRow(label = AppStrings.batch.get(language), value = "2024-2026 (1st Year)")
                        IdFieldRow(label = AppStrings.bloodGroup.get(language), value = student?.bloodGroup ?: "O+")
                        IdFieldRow(label = AppStrings.studentPhone.get(language), value = student?.phone ?: "98480xxxxx")
                        IdFieldRow(label = AppStrings.guardianPhone.get(language), value = student?.guardianPhone ?: "94400xxxxx")
                        IdFieldRow(label = AppStrings.validTill.get(language), value = "July 2026")
                    }

                    Divider(modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    // Barcode / QR Simulation
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                repeat(26) { i ->
                                    Box(
                                        modifier = Modifier
                                            .width(if (i % 3 == 0) 3.dp else 1.5.dp)
                                            .height(26.dp)
                                            .background(Color.Black)
                                    )
                                }
                            }
                            Text(
                                text = student?.admissionNo ?: "ATC-2024",
                                fontSize = 8.sp,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                color = Color.Gray
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "ISSUED BY",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = "Principal, ATC",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = IndustrialBluePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(AppStrings.close.get(language))
            }
        }
    )
}

@Composable
fun IdFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
