package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AtcViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: AtcViewModel = viewModel()) {
    val language by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val student by viewModel.currentStudent.collectAsState()
    val instructor by viewModel.currentInstructor.collectAsState()
    val trades by viewModel.trades.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val instructors by viewModel.instructors.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    var currentTab by remember { mutableStateOf(NavigationTab.HOME) }
    var showRoleDialog by remember { mutableStateOf(false) }
    var showIdCardDialog by remember { mutableStateOf(false) }
    var showQrModal by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle toast messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearToast()
            }
        }
    }

    // Handle Android system back button
    BackHandler(enabled = currentTab != NavigationTab.HOME) {
        currentTab = NavigationTab.HOME
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AtcTopAppBar(
                currentLanguage = language,
                currentRole = role,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onRoleClick = { showRoleDialog = true }
            )
        },
        bottomBar = {
            AtcBottomNavBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it },
                language = language
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                NavigationTab.HOME -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateTab = { currentTab = it },
                        onOpenIdCard = { showIdCardDialog = true },
                        onOpenQuiz = { currentTab = NavigationTab.MORE },
                        onOpenRoutine = { currentTab = NavigationTab.MORE },
                        onOpenQrModal = { showQrModal = true }
                    )
                }

                NavigationTab.CLASSES -> {
                    ClassesScreen(viewModel = viewModel)
                }

                NavigationTab.ATTENDANCE -> {
                    AttendanceScreen(
                        viewModel = viewModel,
                        onOpenQrModal = { showQrModal = true }
                    )
                }

                NavigationTab.MATERIALS -> {
                    MaterialsScreen(viewModel = viewModel)
                }

                NavigationTab.MORE -> {
                    MoreScreen(
                        viewModel = viewModel,
                        onOpenIdCard = { showIdCardDialog = true },
                        onOpenRoleSelector = { showRoleDialog = true }
                    )
                }
            }
        }
    }

    // Role Switcher Dialog
    if (showRoleDialog) {
        RoleSelectorDialog(
            currentRole = role,
            currentStudent = student,
            currentInstructor = instructor,
            allStudents = allStudents,
            instructors = instructors,
            language = language,
            onRoleSelected = { newRole -> viewModel.setRole(newRole) },
            onStudentSelected = { newStudent -> viewModel.setCurrentStudent(newStudent) },
            onDismiss = { showRoleDialog = false }
        )
    }

    // Digital ID Card Dialog
    if (showIdCardDialog) {
        StudentIdCardDialog(
            student = student,
            trades = trades,
            language = language,
            onDismiss = { showIdCardDialog = false }
        )
    }

    // Attendance QR Code Dialog
    if (showQrModal) {
        AttendanceQrModal(
            role = role,
            studentId = student?.id,
            language = language,
            onStudentCheckIn = { studentId -> viewModel.submitQrCheckIn(studentId) },
            onDismiss = { showQrModal = false }
        )
    }
}
