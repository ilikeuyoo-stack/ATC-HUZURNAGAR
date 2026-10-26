package com.example.ui.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TELUGU("te", "Telugu", "తెలుగు")
}

data class LocalizedText(
    val en: String,
    val te: String
) {
    fun get(lang: AppLanguage): String = if (lang == AppLanguage.TELUGU) te else en
}

object AppStrings {
    val appTitle = LocalizedText("ATC Huzurnagar", "ఎటిసి హుజూర్‌నగర్")
    val appSubtitle = LocalizedText(
        "Student Hub • Advanced Training Centre ITI",
        "స్టూడెంట్ హబ్ • అడ్వాన్స్‌డ్ ట్రైనింగ్ సెంటర్ ఐటిఐ"
    )

    // Roles
    val roleStudent = LocalizedText("Student", "విద్యార్థి")
    val roleInstructor = LocalizedText("Instructor / Trainer", "ట్రైనర్ / ఇన్స్ట్రక్టర్")
    val roleAdmin = LocalizedText("Admin / Principal", "ప్రిన్సిపాల్ / అడ్మిన్")
    val switchRole = LocalizedText("Switch Role", "రోల్ మార్చండి")
    val currentRole = LocalizedText("Current Role", "ప్రస్తుత రోల్")

    // Navigation Tabs
    val tabHome = LocalizedText("Home", "హోమ్")
    val tabClasses = LocalizedText("Classes", "తరగతులు")
    val tabAttendance = LocalizedText("Attendance", "హాజరు")
    val tabMaterials = LocalizedText("Materials", "మెటీరియల్స్")
    val tabMore = LocalizedText("More", "మరిన్ని")

    // Attendance Statuses
    val statusPresent = LocalizedText("Present", "హాజరు")
    val statusAbsent = LocalizedText("Absent", "గైర్హాజరు")
    val statusLate = LocalizedText("Late", "ఆలస్యం")
    val statusLeave = LocalizedText("Leave", "సెలవు")
    val statusPending = LocalizedText("Pending", "పెండింగ్")
    val statusApproved = LocalizedText("Approved", "ఆమోదించబడింది")
    val statusRejected = LocalizedText("Rejected", "తిరస్కరించబడింది")

    // Attendance Screen
    val todayAttendance = LocalizedText("Today's Attendance", "నేటి హాజరు")
    val monthlyCalendar = LocalizedText("Monthly Attendance Calendar", "నెలవారీ హాజరు క్యాలెండర్")
    val overallAttendance = LocalizedText("Overall Attendance", "మొత్తం హాజరు")
    val attendanceWarning = LocalizedText(
        "⚠️ Warning: Attendance is below 75%! You may not be permitted for NCVT exams.",
        "⚠️ హెచ్చరిక: హాజరు 75% కంటే తక్కువగా ఉంది! NCVT పరీక్షలకు అనుమతించబడకపోవచ్చు."
    )
    val attendanceGood = LocalizedText(
        "✓ Great! Your attendance is above 75%. Keep it up!",
        "✓ అద్భుతం! మీ హాజరు 75% కంటే ఎక్కువగా ఉంది. ఇలాగే కొనసాగించండి!"
    )
    val markAllPresent = LocalizedText("Mark All Present", "అందరికీ హాజరు వేయండి")
    val submitAttendance = LocalizedText("Submit Attendance", "హాజరును సమర్పించండి")
    val attendanceSubmittedSuccess = LocalizedText("Attendance submitted successfully!", "హాజరు విజయవంతంగా నమోదైంది!")
    val editPastAttendance = LocalizedText("Audit Log & Past Edits", "ఆడిట్ లాగ్ & మార్పులు")
    val exportReport = LocalizedText("Export Report (PDF/Excel)", "నివేదిక ఎగుమతి (PDF/Excel)")
    val requestLeave = LocalizedText("Request Leave", "సెలవు దరఖాస్తు")
    val reason = LocalizedText("Reason for Leave", "సెలవుకు కారణం")
    val startDate = LocalizedText("From Date", "ప్రారంభ తేదీ")
    val endDate = LocalizedText("To Date", "ముగింపు తేదీ")
    val qrCheckIn = LocalizedText("QR Self Check-in", "QR కోడ్ చెక్-ఇన్")
    val scanQrCode = LocalizedText("Scan Classroom QR Code", "తరగతి గది QR కోడ్ స్కాన్ చేయండి")
    val displayQrCode = LocalizedText("Show Class Check-in QR", "క్లాస్ చెక్-ఇన్ QR కోడ్ చూపండి")
    val qrWindowNotice = LocalizedText("Valid for 15 mins inside ATC campus", "ATC క్యాంపస్ పరిధిలో 15 నిమిషాలు చెల్లుబాటు")

    // Classes Screen
    val todayClasses = LocalizedText("Today's Classes", "నేటి తరగతులు")
    val nextClass = LocalizedText("NEXT CLASS", "తదుపరి తరగతి")
    val roomWorkshop = LocalizedText("Room / Workshop", "గది / వర్క్‌షాప్")
    val instructor = LocalizedText("Instructor", "ఇన్స్ట్రక్టర్")
    val timeSlot = LocalizedText("Time", "సమయం")
    val topicsCovered = LocalizedText("Today's Topic Covered", "ఈరోజు బోధించిన అంశం")
    val homeworkPrep = LocalizedText("Homework / Preparation", "హోమ్‌వర్క్ / తదుపరి తరగతి ప్రిపరేషన్")
    val postClassUpdate = LocalizedText("Post Daily Class Plan", "రోజువారీ క్లాస్ ప్లాన్ నమోదు చేయండి")
    val allDays = LocalizedText("Weekly Timetable", "వారపు టైమ్‌టేబుల్")
    val mon = LocalizedText("Mon", "సోమ")
    val tue = LocalizedText("Tue", "మంగళ")
    val wed = LocalizedText("Wed", "బుధ")
    val thu = LocalizedText("Thu", "గురు")
    val fri = LocalizedText("Fri", "శుక్ర")
    val sat = LocalizedText("Sat", "శని")

    // Routine & Checklist
    val dailyRoutine = LocalizedText("ATC Daily Routine", "ATC రోజువారీ దినచర్య")
    val safetyChecklist = LocalizedText("Workshop Safety Checklist", "వర్క్‌షాప్ సేఫ్టీ చెక్‌లిస్ట్")
    val academicCalendar = LocalizedText("Academic & Exam Calendar", "విద్యా & పరీక్షల క్యాలెండర్")
    val wearSafetyShoes = LocalizedText("Safety Shoes & Workshop Apron", "సేఫ్టీ బూట్లు & వర్క్‌షాప్ అప్రాన్")
    val bringDrawingInstruments = LocalizedText("Engineering Drawing Instruments", "ఇంజనీరింగ్ డ్రాయింగ్ పరికరాలు")
    val wearSafetyGlasses = LocalizedText("Eye Safety Goggles for Machining", "మెషినింగ్ సేఫ్టీ గాగుల్స్")
    val hairEarProtection = LocalizedText("Ear Plugs & Hair Net / Tied Hair", "చెవుల రక్షణ & జుట్టు కట్టుకోవడం")
    val idCardCheck = LocalizedText("ATC Student ID Card with Lanyard", "ATC స్టూడెంట్ ఐడి కార్డు")

    // Study Materials
    val studyMaterials = LocalizedText("Study Materials", "స్టడీ మెటీరియల్స్")
    val filterByTrade = LocalizedText("Filter by Trade", "ట్రేడ్ వారీగా ఎంచుకోండి")
    val searchMaterials = LocalizedText("Search notes, topics, PDFs...", "నోట్స్, టాపిక్స్, PDFలను వెతకండి...")
    val year1 = LocalizedText("1st Year", "1వ సంవత్సరం")
    val year2 = LocalizedText("2nd Year", "2వ సంవత్సరం")
    val downloadOffline = LocalizedText("Download for Offline Use", "ఆఫ్‌లైన్ కోసం డౌన్‌లోడ్ చేసుకోండి")
    val downloaded = LocalizedText("Downloaded (Offline Ready)", "డౌన్‌లోడ్ అయింది (ఆఫ్‌లైన్)")
    val uploadMaterial = LocalizedText("Upload Study Material", "స్టడీ మెటీరియల్ అప్‌లోడ్ చేయండి")
    val notes = LocalizedText("Notes & PDFs", "నోట్స్ & PDFలు")
    val videoLectures = LocalizedText("Video Tutorials", "వీడియో పాఠాలు")
    val jobSheets = LocalizedText("Job Sheets & Manuals", "జాబ్ షీట్లు & మాన్యువల్స్")
    val mcqQuiz = LocalizedText("NCVT CBT Practice Quiz", "NCVT CBT ప్రాక్టీస్ క్విజ్")

    // Profile & ID
    val studentProfile = LocalizedText("Student Profile", "విద్యార్థి ప్రొఫైల్")
    val digitalIdCard = LocalizedText("Digital ID Card", "డిజిటల్ ఐడి కార్డు")
    val admissionNo = LocalizedText("Admission No", "అడ్మిషన్ నంబర్")
    val trade = LocalizedText("Trade", "ట్రేడ్")
    val batch = LocalizedText("Batch", "బ్యాచ్")
    val studentPhone = LocalizedText("Student Phone", "విద్యార్థి ఫోన్")
    val guardianPhone = LocalizedText("Guardian Phone", "తల్లిదండ్రుల / సంరక్షకుని ఫోన్")
    val bloodGroup = LocalizedText("Blood Group", "బ్లడ్ గ్రూప్")
    val validTill = LocalizedText("Valid Till", "చెల్లుబాటు కాలం")

    // Announcements
    val announcements = LocalizedText("Announcements", "నోటీసులు & ప్రకటనలు")
    val postNotice = LocalizedText("Post New Notice", "కొత్త నోటీసును పోస్ట్ చేయండి")
    val urgentNotice = LocalizedText("URGENT NOTICE", "అత్యవసర నోటీసు")
    val readReceipts = LocalizedText("Read Receipts", "చదివిన విద్యార్థుల సంఖ్య")

    // Admin Dashboard
    val adminDashboard = LocalizedText("Admin Overview", "అడ్మిన్ డ్యాష్‌బోర్డ్")
    val totalStudents = LocalizedText("Total Enrolled Students", "మొత్తం విద్యార్థులు")
    val totalTrades = LocalizedText("Active Advanced Trades", "యాక్టివ్ ట్రేడులు")
    val todayOverallAttendance = LocalizedText("Today's Campus Attendance", "నేటి మొత్తం క్యాంపస్ హాజరు")
    val lowAttendanceAlerts = LocalizedText("Low Attendance (<75%) Alert List", "తక్కువ హాజరు (<75%) జాబితా")
    val sendSmsAlert = LocalizedText("Send Absence SMS Alert", "గైర్హాజరు SMS పంపండి")
    val alertSentSuccess = LocalizedText("SMS & Push Alert dispatched to Guardian!", "సంరక్షకునికి SMS & నోటిఫికేషన్ పంపబడింది!")

    // Actions
    val close = LocalizedText("Close", "మూసివేయి")
    val cancel = LocalizedText("Cancel", "రద్దు")
    val save = LocalizedText("Save", "సేవ్ చేయి")
    val viewDetails = LocalizedText("View Details", "వివరాలు చూడండి")
    val simulateOtp = LocalizedText("Use Demo Quick Login", "డెమో త్వరిత లాగిన్")
}
