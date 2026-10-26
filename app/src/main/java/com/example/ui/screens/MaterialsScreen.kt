package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun MaterialsScreen(
    viewModel: AtcViewModel
) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val trades by viewModel.trades.collectAsState()
    val allMaterials by viewModel.studyMaterials.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedTradeId by remember(student) {
        mutableStateOf(student?.tradeId ?: "ALL")
    }
    var selectedType by remember { mutableStateOf<MaterialType?>(null) }
    var selectedMaterialForDetail by remember { mutableStateOf<StudyMaterial?>(null) }
    var showUploadDialog by remember { mutableStateOf(false) }

    val filteredMaterials = allMaterials.filter { mat ->
        val matchesTrade = selectedTradeId == "ALL" || mat.tradeId == selectedTradeId
        val matchesType = selectedType == null || mat.type == selectedType
        val matchesQuery = searchQuery.isBlank() ||
                mat.titleEn.contains(searchQuery, ignoreCase = true) ||
                mat.titleTe.contains(searchQuery, ignoreCase = true) ||
                mat.subject.contains(searchQuery, ignoreCase = true) ||
                mat.contentSnippet.contains(searchQuery, ignoreCase = true)
        matchesTrade && matchesType && matchesQuery
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Search & Upload Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(AppStrings.searchMaterials.get(language), fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                )

                if (role != UserRole.STUDENT) {
                    IconButton(
                        onClick = { showUploadDialog = true },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Upload", tint = Color.White)
                    }
                }
            }
        }

        // Trade Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedTradeId == "ALL",
                        onClick = { selectedTradeId = "ALL" },
                        label = { Text(AppStrings.filterByTrade.get(language), fontSize = 11.sp) }
                    )
                }
                items(trades) { tr ->
                    FilterChip(
                        selected = selectedTradeId == tr.id,
                        onClick = { selectedTradeId = tr.id },
                        label = { Text(tr.code, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }

        // Type Filter Chips (PDF, Job Sheet, Diagram, Video)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val types = listOf(
                    null to (if (language == AppLanguage.TELUGU) "అన్నీ" else "All"),
                    MaterialType.PDF to AppStrings.notes.get(language).take(8),
                    MaterialType.JOB_SHEET to AppStrings.jobSheets.get(language).take(9),
                    MaterialType.DIAGRAM to (if (language == AppLanguage.TELUGU) "డయాగ్రమ్స్" else "Diagrams")
                )

                types.forEach { (type, label) ->
                    val isSelected = selectedType == type
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedType = type }
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 6.dp),
                            maxLines = 1,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // Count header
        item {
            Text(
                text = "${AppStrings.studyMaterials.get(language)} (${filteredMaterials.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        // Materials List
        if (filteredMaterials.isEmpty()) {
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
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (language == AppLanguage.TELUGU) "మెటీరియల్స్ ఏవీ కనుగొనబడలేదు." else "No study materials match your search or filter.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(filteredMaterials) { mat ->
                StudyMaterialCard(
                    material = mat,
                    language = language,
                    onToggleDownload = { viewModel.toggleMaterialDownload(mat.id, mat.isDownloaded) },
                    onToggleBookmark = { viewModel.toggleMaterialBookmark(mat.id, mat.isBookmarked) },
                    onOpenDetail = { selectedMaterialForDetail = mat }
                )
            }
        }
    }

    // Material Detail Dialog (Preview and Snippet Reading)
    selectedMaterialForDetail?.let { mat ->
        AlertDialog(
            onDismissRequest = { selectedMaterialForDetail = null },
            title = {
                Text(
                    text = if (language == AppLanguage.TELUGU) mat.titleTe else mat.titleEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = mat.subject,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = if (mat.isDownloaded) AppStrings.downloaded.get(language) else "${mat.sizeMb} MB",
                            fontSize = 11.sp,
                            color = if (mat.isDownloaded) StatusPresent else Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = if (language == AppLanguage.TELUGU) mat.summaryTe else mat.summaryEn,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        text = if (language == AppLanguage.TELUGU) "ముఖ్యమైన నోట్స్ & సూత్రాలు:" else "Key Reference Notes & Snippet:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IndustrialBluePrimary
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = mat.contentSnippet,
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            modifier = Modifier.padding(10.dp),
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.toggleMaterialDownload(mat.id, mat.isDownloaded)
                        selectedMaterialForDetail = null
                    }
                ) {
                    Icon(
                        imageVector = if (mat.isDownloaded) Icons.Default.Delete else Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (mat.isDownloaded) (if (language == AppLanguage.TELUGU) "తొలగించు" else "Remove Offline") else AppStrings.downloadOffline.get(language)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedMaterialForDetail = null }) {
                    Text(AppStrings.close.get(language))
                }
            }
        )
    }

    // Upload Material Dialog
    if (showUploadDialog) {
        var newTitleEn by remember { mutableStateOf("") }
        var newTitleTe by remember { mutableStateOf("") }
        var newSubject by remember { mutableStateOf("Workshop Practice") }
        var newContent by remember { mutableStateOf("") }
        var newTradeId by remember { mutableStateOf(trades.firstOrNull()?.id ?: "trade_cnc") }

        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = {
                Text(
                    text = AppStrings.uploadMaterial.get(language),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTitleEn,
                        onValueChange = { newTitleEn = it },
                        label = { Text("Title (English)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTitleTe,
                        onValueChange = { newTitleTe = it },
                        label = { Text("Title (Telugu / తెలుగు)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newSubject,
                        onValueChange = { newSubject = it },
                        label = { Text("Subject", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Key Formulae / G-code / Content Snippet", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitleEn.isNotBlank()) {
                            viewModel.uploadStudyMaterial(
                                tradeId = newTradeId,
                                year = 1,
                                subject = newSubject,
                                titleEn = newTitleEn,
                                titleTe = if (newTitleTe.isBlank()) newTitleEn else newTitleTe,
                                type = MaterialType.PDF,
                                summaryEn = newTitleEn,
                                contentSnippet = newContent
                            )
                            showUploadDialog = false
                        }
                    }
                ) {
                    Text(AppStrings.save.get(language))
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) {
                    Text(AppStrings.cancel.get(language))
                }
            }
        )
    }
}

@Composable
fun StudyMaterialCard(
    material: StudyMaterial,
    language: AppLanguage,
    onToggleDownload: () -> Unit,
    onToggleBookmark: () -> Unit,
    onOpenDetail: () -> Unit
) {
    val (typeIcon, typeLabel, typeColor) = when (material.type) {
        MaterialType.PDF -> Triple(Icons.Default.PictureAsPdf, "PDF NOTES", Color(0xFFC62828))
        MaterialType.JOB_SHEET -> Triple(Icons.Default.Assignment, "JOB SHEET", IndustrialBluePrimary)
        MaterialType.DIAGRAM -> Triple(Icons.Default.Image, "DIAGRAM", PrecisionTeal)
        MaterialType.VIDEO -> Triple(Icons.Default.VideoLibrary, "VIDEO", SafetyOrangeSecondary)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = typeColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = typeIcon, contentDescription = null, tint = typeColor, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = typeLabel, color = typeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (material.isDownloaded) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = StatusPresentContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = StatusPresent, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "ఆఫ్‌లైన్" else "Offline",
                                    color = StatusPresent,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (material.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (material.isBookmarked) SafetyOrangeSecondary else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.TELUGU) material.titleTe else material.titleEn,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.TELUGU) material.summaryTe else material.summaryEn,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${material.subject} • ${material.sizeMb} MB",
                    fontSize = 10.sp,
                    color = Color.Gray
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TextButton(
                        onClick = onToggleDownload,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = if (material.isDownloaded) Icons.Default.OfflinePin else Icons.Default.Download,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (material.isDownloaded) StatusPresent else MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (material.isDownloaded) AppStrings.downloaded.get(language).take(10) else AppStrings.downloadOffline.get(language).take(8),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
