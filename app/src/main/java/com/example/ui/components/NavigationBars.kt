package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AtcTopAppBar(
    currentLanguage: AppLanguage,
    currentRole: UserRole,
    onToggleLanguage: () -> Unit,
    onRoleClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                // College Emblem / Icon badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PrecisionManufacturing,
                        contentDescription = "ATC Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = AppStrings.appTitle.get(currentLanguage),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = AppStrings.appSubtitle.get(currentLanguage),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                        maxLines = 1
                    )
                }
            }
        },
        actions = {
            // Role Badge (clickable to switch role)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onRoleClick() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val (roleName, roleIcon) = when (currentRole) {
                        UserRole.STUDENT -> Pair(AppStrings.roleStudent.get(currentLanguage), Icons.Default.School)
                        UserRole.INSTRUCTOR -> Pair(AppStrings.roleInstructor.get(currentLanguage), Icons.Default.Engineering)
                        UserRole.ADMIN -> Pair(AppStrings.roleAdmin.get(currentLanguage), Icons.Default.AdminPanelSettings)
                    }
                    Icon(
                        imageVector = roleIcon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = roleName.take(9),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Language Toggle Button (One-tap EN <-> తె)
            Button(
                onClick = onToggleLanguage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(34.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (currentLanguage == AppLanguage.ENGLISH) "తెలుగు" else "English",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    )
}

enum class NavigationTab {
    HOME,
    CLASSES,
    ATTENDANCE,
    MATERIALS,
    MORE
}

@Composable
fun AtcBottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    language: AppLanguage
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        val items = listOf(
            Triple(NavigationTab.HOME, AppStrings.tabHome.get(language), Icons.Filled.Home to Icons.Outlined.Home),
            Triple(NavigationTab.CLASSES, AppStrings.tabClasses.get(language), Icons.Filled.Schedule to Icons.Outlined.Schedule),
            Triple(NavigationTab.ATTENDANCE, AppStrings.tabAttendance.get(language), Icons.Filled.FactCheck to Icons.Outlined.FactCheck),
            Triple(NavigationTab.MATERIALS, AppStrings.tabMaterials.get(language), Icons.Filled.MenuBook to Icons.Outlined.MenuBook),
            Triple(NavigationTab.MORE, AppStrings.tabMore.get(language), Icons.Filled.Dashboard to Icons.Outlined.Dashboard)
        )

        items.forEach { (tab, label, icons) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
