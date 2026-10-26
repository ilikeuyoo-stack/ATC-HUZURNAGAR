package com.example.data.local

import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {

    suspend fun seedDatabase(dao: AtcDao) = withContext(Dispatchers.IO) {
        // 1. Trades
        val trades = listOf(
            Trade(
                id = "trade_designer",
                code = "BDVV",
                nameEn = "Basic Designer & Virtual Verifier (Mechanical)",
                nameTe = "బేసిక్ డిజైనర్ & వర్చువల్ వెరిఫైయర్ (మెకానికల్)",
                durationYears = 2,
                focusEn = "CAD/CAM modeling, 3D simulation, FEA basics, digital prototyping",
                focusTe = "CAD/CAM మోడలింగ్, 3D సిమ్యులేషన్, డిజిటల్ ప్రోటోటైపింగ్",
                iconKey = "design"
            ),
            Trade(
                id = "trade_artisan",
                code = "AUT",
                nameEn = "Artisan Using Advanced Tools",
                nameTe = "ఆర్టిసన్ యూజింగ్ అడ్వాన్స్‌డ్ టూల్స్",
                durationYears = 1,
                focusEn = "Precision manufacturing, digital hand & machine tools, metrology",
                focusTe = "ఖచ్చితమైన తయారీ, డిజిటల్ హ్యాండ్ & మెషిన్ టూల్స్, మెట్రాలజీ",
                iconKey = "artisan"
            ),
            Trade(
                id = "trade_cnc",
                code = "ACNCMT",
                nameEn = "Advanced CNC Machining Technician",
                nameTe = "అడ్వాన్స్‌డ్ CNC మెషినింగ్ టెక్నీషియన్",
                durationYears = 2,
                focusEn = "CNC milling & lathe programming (G-Code/M-Code), 5-axis setup, maintenance",
                focusTe = "CNC మిల్లింగ్ & లేత్ ప్రోగ్రామింగ్ (G-కోడ్/M-కోడ్), నిర్వహణ",
                iconKey = "cnc"
            ),
            Trade(
                id = "trade_robotics",
                code = "IRDMT",
                nameEn = "Industrial Robotics & Digital Manufacturing",
                nameTe = "ఇండస్ట్రియల్ రోబోటిక్స్ & డిజిటల్ మాన్యుఫ్యాక్చరింగ్",
                durationYears = 1,
                focusEn = "6-axis industrial robots, teach pendant programming, smart factory workflows",
                focusTe = "పారిశ్రామిక రోబోట్లు, టీచ్ పెండెంట్ ప్రోగ్రామింగ్, స్మార్ట్ ఫ్యాక్టరీ",
                iconKey = "robotics"
            ),
            Trade(
                id = "trade_automation",
                code = "MPCA",
                nameEn = "Manufacturing Process Control & Automation",
                nameTe = "మాన్యుఫ్యాక్చరింగ్ ప్రాసెస్ కంట్రోల్ & ఆటోమేషన్",
                durationYears = 1,
                focusEn = "PLC programming (Ladder logic), SCADA, industrial sensors, pneumatics",
                focusTe = "PLC ప్రోగ్రామింగ్, సెన్సార్లు, పారిశ్రామిక ఆటోమేషన్",
                iconKey = "automation"
            ),
            Trade(
                id = "trade_ev",
                code = "MEV",
                nameEn = "Mechanic Electric Vehicle (NSQF)",
                nameTe = "మెకానిక్ ఎలక్ట్రిక్ వెహికల్ (NSQF)",
                durationYears = 2,
                focusEn = "EV Li-ion battery packs, BMS, BLDC traction motors, powertrain diagnostics",
                focusTe = "EV బ్యాటరీ మేనేజ్‌మెంట్, ఎలక్ట్రిక్ మోటార్లు, డయాగ్నోస్టిక్స్",
                iconKey = "ev"
            )
        )
        dao.insertTrades(trades)

        // 2. Batches
        val batches = listOf(
            Batch("batch_ds_2024", "trade_designer", "BDVV-2024 (1st Year)", 2024, 1, 10),
            Batch("batch_ar_2024", "trade_artisan", "AUT-2024 (1st Year)", 2024, 1, 10),
            Batch("batch_cnc_2024", "trade_cnc", "CNC-2024 (1st Year)", 2024, 1, 10),
            Batch("batch_rob_2024", "trade_robotics", "ROB-2024 (1st Year)", 2024, 1, 10),
            Batch("batch_auto_2024", "trade_automation", "AUTO-2024 (1st Year)", 2024, 1, 10),
            Batch("batch_ev_2024", "trade_ev", "EV-2024 (1st Year)", 2024, 1, 10)
        )
        dao.insertBatches(batches)

        // 3. Instructors
        val instructors = listOf(
            Instructor("inst_1", "EMP-101", "K. Ramesh Babu", "కె. రమేష్ బాబు", "trade_cnc", "9848011221", "Senior CNC Instructor"),
            Instructor("inst_2", "EMP-102", "P. Srilatha", "పి. శ్రీలత", "trade_designer", "9848022332", "CAD/CAM Specialist Trainer"),
            Instructor("inst_3", "EMP-103", "M. Venkateshwarlu", "ఎం. వెంకటేశ్వర్లు", "trade_ev", "9848033443", "EV Powertrain Trainer"),
            Instructor("inst_4", "EMP-104", "G. Naresh", "జి. నరేష్", "trade_robotics", "9848044554", "Robotics & Automation Lead"),
            Instructor("inst_5", "EMP-105", "B. Anjaneyulu", "బి. ఆంజనేయులు", "trade_artisan", "9848055665", "Advanced Tooling Instructor"),
            Instructor("inst_6", "EMP-106", "S. Radhika", "ఎస్. రాధిక", "trade_automation", "9848066776", "PLC & Sensors Trainer")
        )
        dao.insertInstructors(instructors)

        // 4. Students (10 sample students per batch = 60 students)
        val students = mutableListOf<Student>()
        val studentNames = listOf(
            Pair("Shiva Kumar Reddy", "శివ కుమార్ రెడ్డి"),
            Pair("Anitha Vemula", "అనిత వేముల"),
            Pair("Sai Kiran Guguloth", "సాయి కిరణ్ గుగులోత్"),
            Pair("Rajeshwari Kotla", "రాజేశ్వరి కోట్ల"),
            Pair("Venkatesh Banoth", "వెంకటేష్ బానోత్"),
            Pair("Pranay Teja", "ప్రణయ్ తేజ"),
            Pair("Mounika Goud", "మౌనిక గౌడ్"),
            Pair("Suresh Nayak", "సురేష్ నాయక్"),
            Pair("Bhavani Sunkari", "భవాని సుంకరి"),
            Pair("Tarun Chandra", "తరుణ్ చంద్ర")
        )

        batches.forEachIndexed { bIdx, b ->
            val prefix = when (b.tradeId) {
                "trade_designer" -> "DS"
                "trade_artisan" -> "AR"
                "trade_cnc" -> "CNC"
                "trade_robotics" -> "ROB"
                "trade_automation" -> "PA"
                else -> "EV"
            }
            studentNames.forEachIndexed { sIdx, (en, te) ->
                val num = String.format("%02d", sIdx + 1)
                students.add(
                    Student(
                        id = "std_${prefix}_$num",
                        admissionNo = "ATC-24-$prefix$num",
                        name = "$en ($prefix)",
                        nameTe = "$te ($prefix)",
                        tradeId = b.tradeId,
                        batchId = b.id,
                        phone = "9849" + String.format("%06d", bIdx * 100 + sIdx * 11),
                        guardianPhone = "9440" + String.format("%06d", bIdx * 100 + sIdx * 11),
                        bloodGroup = listOf("O+", "B+", "A+", "AB+")[sIdx % 4],
                        pin = "1234"
                    )
                )
            }
        }
        dao.insertStudents(students)

        // 5. Seed Attendance records for past days + today
        val sampleDates = listOf("2026-10-22", "2026-10-23", "2026-10-24", "2026-10-25", "2026-10-26")
        val attendanceList = mutableListOf<AttendanceRecord>()

        students.forEachIndexed { idx, st ->
            sampleDates.forEachIndexed { dIdx, date ->
                // Ensure student 0 (Shiva Kumar Reddy) has good attendance, student 2 and 7 have lower attendance (<75%)
                val status = when {
                    st.id.endsWith("03") && (dIdx == 1 || dIdx == 3) -> AttendanceStatus.ABSENT
                    st.id.endsWith("08") && (dIdx == 0 || dIdx == 2 || dIdx == 4) -> AttendanceStatus.ABSENT
                    st.id.endsWith("05") && dIdx == 2 -> AttendanceStatus.LATE
                    st.id.endsWith("07") && dIdx == 1 -> AttendanceStatus.LEAVE
                    else -> AttendanceStatus.PRESENT
                }
                attendanceList.add(
                    AttendanceRecord(
                        studentId = st.id,
                        date = date,
                        session = "FULL_DAY",
                        status = status,
                        markedBy = "K. Ramesh Babu (EMP-101)"
                    )
                )
            }
        }
        dao.insertAttendance(attendanceList)

        // 6. Daily Routine
        val routines = listOf(
            DailyRoutine("rt_1", 1, "09:00 - 09:30 AM", "Morning Assembly & ATC Briefing", "ఉదయం అసెంబ్లీ & ప్రార్థన", "National Anthem, safety pledge, daily announcements", "జాతీయ గీతం, సేఫ్టీ ప్రతిజ్ఞ, నేటి ప్రకటనలు", "assembly"),
            DailyRoutine("rt_2", 2, "09:30 - 11:00 AM", "Trade Theory Lecture", "ట్రేడ్ థియరీ క్లాస్", "Core technical fundamentals, diagrams, engineering standards", "ప్రాథమిక సాంకేతిక అంశాలు, డయాగ్రమ్స్", "theory"),
            DailyRoutine("rt_3", 3, "11:00 - 11:15 AM", "Tea & Refreshment Break", "టీ & అల్పాహార విరామం", "15 minutes break", "15 నిమిషాల విరామం", "break"),
            DailyRoutine("rt_4", 4, "11:15 - 01:00 PM", "Hands-on Practical Session 1", "వర్క్‌షాప్ ప్రాక్టికల్ సెషన్ 1", "Machine setup, CNC simulation, CAD modeling, wiring", "మెషిన్ సెటప్, సిమ్యులేషన్, వైరింగ్", "practical"),
            DailyRoutine("rt_5", 5, "01:00 - 01:45 PM", "Lunch Break", "భోజన విరామం", "ATC cafeteria & student dining hall", "భోజన విరామ సమయం", "lunch"),
            DailyRoutine("rt_6", 6, "01:45 - 04:30 PM", "Workshop Practice & Job Fabrication", "వర్క్‌షాప్ ప్రాక్టీస్ & జాబ్ మేకింగ్", "Real component machining, robot path programming, EV testing", "కాంపోనెంట్ మెషినింగ్, రోబోట్ ప్రోగ్రామింగ్", "workshop"),
            DailyRoutine("rt_7", 7, "04:30 - 04:45 PM", "5S Japanese Workplace Cleaning", "5S వర్క్‌షాప్ క్లీనింగ్", "Sort, set in order, shine tools, return measuring equipment", "టూల్స్ శుభ్రపరచడం, భద్రపరచడం", "cleaning"),
            DailyRoutine("rt_8", 8, "04:45 - 05:00 PM", "Daily Log & Dismissal", "రోజువారీ లాగ్ & ముగింపు", "Submission of job sheets, day wrap up", "జాబ్ షీట్ల పరిశీలన, ముగింపు", "closing")
        )
        dao.insertRoutines(routines)

        // 7. Class Schedules (Monday to Saturday)
        val schedules = listOf(
            ClassSchedule("cs_cnc_1", "trade_cnc", "batch_cnc_2024", 1, "09:30 - 11:00 AM", "CNC Milling Programming (G-Code)", "CNC మిల్లింగ్ ప్రోగ్రామింగ్ (G-కోడ్)", "K. Ramesh Babu", "Lab 2 / CNC Room", ClassType.THEORY),
            ClassSchedule("cs_cnc_2", "trade_cnc", "batch_cnc_2024", 1, "11:15 - 01:00 PM", "5-Axis Machine Tool Setup", "5-యాక్సిస్ మెషిన్ సెటప్", "K. Ramesh Babu", "Main Machine Shop", ClassType.PRACTICAL),
            ClassSchedule("cs_cnc_3", "trade_cnc", "batch_cnc_2024", 1, "01:45 - 03:15 PM", "Workshop Calculation & Science", "వర్క్‌షాప్ కాలిక్యులేషన్ & సైన్స్", "P. Srilatha", "Classroom 4", ClassType.WORKSHOP_CALC),
            ClassSchedule("cs_cnc_4", "trade_cnc", "batch_cnc_2024", 1, "03:15 - 04:30 PM", "Employability Skills (English & Digital)", "ఎంప్లాయబిలిటీ స్కిల్స్", "S. Radhika", "Computer Lab 1", ClassType.EMPLOYABILITY_SKILLS),

            // Designer
            ClassSchedule("cs_ds_1", "trade_designer", "batch_ds_2024", 1, "09:30 - 11:00 AM", "3D CAD Modeling & SolidWorks", "3D CAD మోడలింగ్", "P. Srilatha", "CAD Lab 3", ClassType.THEORY),
            ClassSchedule("cs_ds_2", "trade_designer", "batch_ds_2024", 1, "11:15 - 01:00 PM", "Engineering Drawing & GD&T", "ఇంజనీరింగ్ డ్రాయింగ్ & GD&T", "P. Srilatha", "Drawing Hall", ClassType.ENGG_DRAWING),
            ClassSchedule("cs_ds_3", "trade_designer", "batch_ds_2024", 1, "01:45 - 04:30 PM", "Virtual Assembly Simulation", "వర్చువల్ అసెంబ్లీ సిమ్యులేషన్", "P. Srilatha", "CAD Lab 3", ClassType.PRACTICAL),

            // EV
            ClassSchedule("cs_ev_1", "trade_ev", "batch_ev_2024", 1, "09:30 - 11:00 AM", "Li-ion Battery Pack & BMS Systems", "Li-ion బ్యాటరీ & BMS సిస్టమ్స్", "M. Venkateshwarlu", "EV Center 1", ClassType.THEORY),
            ClassSchedule("cs_ev_2", "trade_ev", "batch_ev_2024", 1, "11:15 - 01:00 PM", "BLDC Motor & Inverter Diagnostics", "BLDC మోటార్ & ఇన్వర్టర్ టెస్టింగ్", "M. Venkateshwarlu", "EV Workshop", ClassType.PRACTICAL),
            ClassSchedule("cs_ev_3", "trade_ev", "batch_ev_2024", 1, "01:45 - 04:30 PM", "Regenerative Braking Lab", "రీజెనరేటివ్ బ్రేకింగ్ ల్యాబ్", "M. Venkateshwarlu", "EV Workshop", ClassType.WORKSHOP),

            // Robotics
            ClassSchedule("cs_rob_1", "trade_robotics", "batch_rob_2024", 1, "09:30 - 11:00 AM", "Robot Kinematics & Coordinates", "రోబోట్ కైనమాటిక్స్ & కోఆర్డినేట్స్", "G. Naresh", "Robotics Cell", ClassType.THEORY),
            ClassSchedule("cs_rob_2", "trade_robotics", "batch_rob_2024", 1, "11:15 - 01:00 PM", "Teach Pendant Path Programming", "టీచ్ పెండెంట్ ప్రోగ్రామింగ్", "G. Naresh", "ABB/KUKA Station", ClassType.PRACTICAL),

            // Automation
            ClassSchedule("cs_auto_1", "trade_automation", "batch_auto_2024", 1, "09:30 - 11:00 AM", "PLC Ladder Logic Programming", "PLC లాడర్ లాజిక్ ప్రోగ్రామింగ్", "S. Radhika", "Automation Lab", ClassType.THEORY),
            ClassSchedule("cs_auto_2", "trade_automation", "batch_auto_2024", 1, "11:15 - 01:00 PM", "Pneumatics & Sensor Actuation", "న్యూమాటిక్స్ & సెన్సార్ల అనుసంధానం", "S. Radhika", "Pneumatics Bench", ClassType.PRACTICAL),

            // Artisan
            ClassSchedule("cs_art_1", "trade_artisan", "batch_art_2024", 1, "09:30 - 11:00 AM", "Precision Metrology & Digital Calipers", "డిజిటల్ కాలిపర్స్ & గేజ్‌ల వాడకం", "B. Anjaneyulu", "Tool Room", ClassType.THEORY),
            ClassSchedule("cs_art_2", "trade_artisan", "batch_art_2024", 1, "11:15 - 01:00 PM", "Automated Hand Tool Operations", "ఆటోమేటెడ్ హ్యాండ్ టూల్స్ ప్రాక్టీస్", "B. Anjaneyulu", "Tool Room Bench", ClassType.PRACTICAL)
        )
        dao.insertSchedules(schedules)

        // 8. Daily Class Plans
        val classPlans = listOf(
            DailyClassPlan(
                tradeId = "trade_cnc",
                batchId = "batch_cnc_2024",
                date = "2026-10-26",
                topicCovered = "Canned Cycles (G81 drilling, G83 peck drilling) and tool offset compensation (G43, H01).",
                homeworkPrep = "Write complete G-Code program for 4-hole pitch circle diameter on 100mm plate for tomorrow's practical.",
                instructorName = "K. Ramesh Babu"
            ),
            DailyClassPlan(
                tradeId = "trade_designer",
                batchId = "batch_ds_2024",
                date = "2026-10-26",
                topicCovered = "Parametric Part Modeling, Extrude Boss/Cut, Lofting features in CAD.",
                homeworkPrep = "Study Sheet metal bending allowances and bring drawing instrument set.",
                instructorName = "P. Srilatha"
            ),
            DailyClassPlan(
                tradeId = "trade_ev",
                batchId = "batch_ev_2024",
                date = "2026-10-26",
                topicCovered = "Battery Management System (BMS) cell balancing: Active vs Passive balancing.",
                homeworkPrep = "Read Chapter 4 on High Voltage safety interlock loop (HVIL) before workshop.",
                instructorName = "M. Venkateshwarlu"
            )
        )
        classPlans.forEach { dao.insertClassPlan(it) }

        // 9. Study Materials
        val materials = listOf(
            StudyMaterial(
                id = "mat_cnc_1",
                tradeId = "trade_cnc",
                year = 1,
                subject = "CNC Milling",
                titleEn = "Standard G-Code & M-Code Reference Handbook",
                titleTe = "G-కోడ్ & M-కోడ్ రిఫరెన్స్ హ్యాండ్‌బుక్",
                type = MaterialType.PDF,
                sizeMb = 2.4,
                isDownloaded = true,
                isBookmarked = true,
                summaryEn = "Quick reference guide for FANUC & Siemens 828D CNC controllers with syntax examples.",
                summaryTe = "FANUC & సిమెన్స్ CNC కంట్రోలర్ల కోసం సమగ్ర ప్రోగ్రామింగ్ గైడ్.",
                contentSnippet = "G00: Rapid positioning\nG01: Linear interpolation with feedrate F\nG02: Circular interpolation clockwise\nG03: Circular interpolation counter-clockwise\nG28: Return to machine zero\nG40/G41/G42: Cutter radius compensation\nG90: Absolute programming\nG91: Incremental programming\nM03: Spindle ON clockwise\nM05: Spindle STOP\nM08: Coolant ON\nM30: End of program and rewind"
            ),
            StudyMaterial(
                id = "mat_designer_1",
                tradeId = "trade_designer",
                year = 1,
                subject = "CAD/CAM",
                titleEn = "Geometric Dimensioning & Tolerancing (GD&T) Guide",
                titleTe = "GD&T ఇంజనీరింగ్ టాలరెన్స్ గైడ్",
                type = MaterialType.PDF,
                sizeMb = 3.1,
                isDownloaded = false,
                isBookmarked = true,
                summaryEn = "Mastering flatness, cylindricity, perpendicularity, position tolerances per ISO 1101.",
                summaryTe = "ఇంజనీరింగ్ డ్రాయింగ్స్ కోసం GD&T ప్రమాణాలు మరియు ఉదాహరణలు.",
                contentSnippet = "Symbols Covered:\n- Form: Straightness, Flatness, Circularity, Cylindricity\n- Orientation: Parallelism, Perpendicularity, Angularity\n- Location: True Position, Concentricity, Symmetry\n- Runout: Circular Runout, Total Runout\nDatums A, B, C setup for 3D inspection."
            ),
            StudyMaterial(
                id = "mat_ev_1",
                tradeId = "trade_ev",
                year = 1,
                subject = "EV Powertrains",
                titleEn = "Li-ion Battery Pack Wiring & Cell Diagnostics",
                titleTe = "Li-ion బ్యాటరీ ప్యాక్ వైరింగ్ & డయాగ్నోస్టిక్స్",
                type = MaterialType.DIAGRAM,
                sizeMb = 1.8,
                isDownloaded = true,
                isBookmarked = false,
                summaryEn = "16S4P battery configuration, thermal runaway sensors, NTC thermistor placement.",
                summaryTe = "ఎలక్ట్రిక్ వాహనాల బ్యాటరీ ప్యాక్ వైరింగ్, థర్మల్ సెన్సార్లు మరియు భద్రత.",
                contentSnippet = "Nominal Voltage: 3.7V per cell (NMC)\nFull Charge: 4.2V | Cutoff: 2.8V\nPack Voltage (16S): 59.2V Nominal\nSafety Protocols:\n1. Use insulated 1000V rated tools\n2. Wear Class 0 electrical safety gloves\n3. Discharge capacitors before service"
            ),
            StudyMaterial(
                id = "mat_rob_1",
                tradeId = "trade_robotics",
                year = 1,
                subject = "Robotics",
                titleEn = "ABB IRC5 Robot Programming & Safety Interlocks",
                titleTe = "రోబోట్ ప్రోగ్రామింగ్ & సేఫ్టీ ఇంటర్‌లాక్స్",
                type = MaterialType.JOB_SHEET,
                sizeMb = 4.0,
                isDownloaded = false,
                isBookmarked = false,
                summaryEn = "Teach pendant RAPID programming instructions: MoveJ, MoveL, SetDO, WaitDI.",
                summaryTe = "రోబోటిక్స్ టీచ్ పెండెంట్ ప్రోగ్రామింగ్ మరియు ల్యాబ్ జాబ్ షీట్.",
                contentSnippet = "MODULE MainModule\n  CONST robtarget pHome := [...];\n  CONST robtarget pPick := [...];\n  PROC main()\n    MoveJ pHome, v1000, fine, tool0;\n    MoveL pPick, v500, fine, tool0;\n    SetDO doGripper, 1;\n    WaitTime 0.5;\n  ENDPROC\nENDMODULE"
            ),
            StudyMaterial(
                id = "mat_auto_1",
                tradeId = "trade_automation",
                year = 1,
                subject = "Industrial Automation",
                titleEn = "Siemens S7-1200 PLC Ladder Logic Cheat Sheet",
                titleTe = "PLC లాడర్ లాజిక్ చీట్ షీట్",
                type = MaterialType.PDF,
                sizeMb = 1.5,
                isDownloaded = true,
                isBookmarked = true,
                summaryEn = "NO/NC contacts, TON/TOF timers, CTU counters, seal-in circuits for motor control.",
                summaryTe = "మోటార్ స్టార్టర్, టైమర్లు మరియు సెన్సార్ కంట్రోల్ కోసం PLC ప్రోగ్రామింగ్.",
                contentSnippet = "Standard DOL Starter Logic:\n--[ I0.0 (Stop NC) ]--[ I0.1 (Start NO) ]-------( Q0.0 Motor )--\n                      |                        |\n                      +-------[ Q0.0 ]---------+"
            ),
            StudyMaterial(
                id = "mat_artisan_1",
                tradeId = "trade_artisan",
                year = 1,
                subject = "Precision Metrology",
                titleEn = "Digital Micrometer & Vernier Height Gauge Manual",
                titleTe = "డిజిటల్ మైక్రోమీటర్ & వెర్నియర్ గేజ్ మాన్యువల్",
                type = MaterialType.JOB_SHEET,
                sizeMb = 2.1,
                isDownloaded = false,
                isBookmarked = false,
                summaryEn = "Zero error calibration, ratchet stop technique, 0.001mm least count measurement.",
                summaryTe = "టూల్ రూమ్ కొలతలు, జీరో ఎర్రర్ సర్దుబాటు మరియు ప్రాక్టీస్ జాబ్ షీట్.",
                contentSnippet = "Least count: 0.001 mm\nSteps:\n1. Clean anvil and spindle faces with lint-free cloth.\n2. Rotate thimble until ratchet slips 3 clicks.\n3. Check for zero error and press ZERO button.\n4. Take 3 readings at 120-degree intervals to detect out-of-roundness."
            )
        )
        dao.insertMaterials(materials)

        // 10. CBT Practice Quiz Questions (NCVT Preparation)
        val questions = listOf(
            QuizQuestion(
                id = "q_cnc_1",
                tradeId = "trade_cnc",
                subject = "CNC Machining",
                questionEn = "Which G-Code is used for circular interpolation in clockwise direction?",
                questionTe = "సవ్యదిశలో (Clockwise) సర్క్యులర్ ఇంటర్‌పోలేషన్ కోసం ఏ G-కోడ్ ఉపయోగించబడుతుంది?",
                optionA = "G01",
                optionB = "G02",
                optionC = "G03",
                optionD = "G00",
                correctIndex = 1,
                explanationEn = "G02 executes circular interpolation in clockwise direction, while G03 is for counter-clockwise direction.",
                explanationTe = "G02 అనేది క్లాక్‌వైజ్ ఆర్క్ కోసం, G03 అనేది యాంటీ-క్లాక్‌వైజ్ ఆర్క్ కోసం ఉపయోగిస్తారు."
            ),
            QuizQuestion(
                id = "q_cnc_2",
                tradeId = "trade_cnc",
                subject = "CNC Machining",
                questionEn = "What does the M-Code M30 indicate in a CNC part program?",
                questionTe = "CNC ప్రోగ్రామ్‌లో M30 కోడ్ దేనిని సూచిస్తుంది?",
                optionA = "Spindle start clockwise",
                optionB = "Coolant on",
                optionC = "Program end and rewind to start",
                optionD = "Optional stop",
                correctIndex = 2,
                explanationEn = "M30 terminates the program and rewinds the cursor back to the program beginning.",
                explanationTe = "M30 అనేది ప్రోగ్రామ్ ముగింపు మరియు తిరిగి మొదటి లైన్‌కు రీవైండ్ చేయడాన్ని సూచిస్తుంది."
            ),
            QuizQuestion(
                id = "q_ev_1",
                tradeId = "trade_ev",
                subject = "Electric Vehicles",
                questionEn = "What is the typical nominal voltage of a single Lithium-ion (NMC) battery cell?",
                questionTe = "సింగిల్ లిథియం-అయాన్ (NMC) బ్యాటరీ సెల్ యొక్క సాధారణ నామినల్ వోల్టేజ్ ఎంత?",
                optionA = "1.5 Volts",
                optionB = "2.0 Volts",
                optionC = "3.7 Volts",
                optionD = "12.0 Volts",
                correctIndex = 2,
                explanationEn = "NMC lithium-ion cells have a nominal operating voltage of ~3.6 to 3.7V, with max charge at 4.2V.",
                explanationTe = "NMC లిథియం-అయాన్ సెల్ సాధారణ వోల్టేజ్ 3.7V గా ఉంటుంది. ఫుల్ ఛార్జింగ్ వద్ద 4.2V ఉంటుంది."
            ),
            QuizQuestion(
                id = "q_ev_2",
                tradeId = "trade_ev",
                subject = "Electric Vehicles",
                questionEn = "Which safety component automatically disconnects the high voltage battery in case of emergency or maintenance?",
                questionTe = "ఎమర్జెన్సీ లేదా రిపేర్ సమయంలో హై వోల్టేజ్ బ్యాటరీని డిస్‌కనెక్ట్ చేయడానికి ఏ సేఫ్టీ పరికరం వాడతారు?",
                optionA = "Service Plug / Manual Service Disconnect (MSD)",
                optionB = "Alternator",
                optionC = "Spark plug",
                optionD = "Fuel filter",
                correctIndex = 0,
                explanationEn = "The Manual Service Disconnect (MSD) physically breaks the high-voltage circuit to ensure technician safety.",
                explanationTe = "మాన్యువల్ సర్వీస్ డిస్‌కనెక్ట్ (MSD) హై వోల్టేజ్ సర్క్యూట్‌ను సురక్షితంగా బ్రేక్ చేస్తుంది."
            ),
            QuizQuestion(
                id = "q_ds_1",
                tradeId = "trade_designer",
                subject = "Engineering Drawing & CAD",
                questionEn = "What does the abbreviation GD&T stand for in mechanical design?",
                questionTe = "మెకానికల్ డిజైన్‌లో GD&T అంటే ఏమిటి?",
                optionA = "General Drafting and Tooling",
                optionB = "Geometric Dimensioning and Tolerancing",
                optionC = "Graphics Display and Texturing",
                optionD = "Ground Diameter and Thickness",
                correctIndex = 1,
                explanationEn = "GD&T stands for Geometric Dimensioning and Tolerancing, a universal engineering language defining allowable variations in form, orientation, and location.",
                explanationTe = "GD&T అంటే Geometric Dimensioning and Tolerancing. ఇది ఇంజనీరింగ్ భాగాల కొలతల ఖచ్చితత్వాన్ని నిర్దేశిస్తుంది."
            ),
            QuizQuestion(
                id = "q_rob_1",
                tradeId = "trade_robotics",
                subject = "Industrial Robotics",
                questionEn = "How many degrees of freedom (DoF) does an articulated industrial arm robot typically possess?",
                questionTe = "ఆర్టిక్యులేటెడ్ పారిశ్రామిక రోబోట్ సాధారణంగా ఎన్ని డిగ్రీస్ ఆఫ్ ఫ్రీడమ్ (DoF) కలిగి ఉంటుంది?",
                optionA = "2 axes",
                optionB = "4 axes",
                optionC = "6 axes",
                optionD = "10 axes",
                correctIndex = 2,
                explanationEn = "Standard articulated industrial robots have 6 axes (6 DoF), allowing complete position (X,Y,Z) and orientation (Roll, Pitch, Yaw) in 3D workspace.",
                explanationTe = "పారిశ్రామిక రోబోట్లు సాధారణంగా 6 యాక్సిస్‌లు కలిగి ఉంటాయి, ఇవి 3D స్పేస్‌లో ఏ స్థానానికైనా కదలడానికి సహాయపడతాయి."
            )
        )
        dao.insertQuestions(questions)

        // 11. Announcements
        val announcements = listOf(
            Announcement(
                id = "ann_1",
                tradeId = "ALL",
                titleEn = "Mandatory 75% Attendance for NCVT CBT Exam 2026",
                titleTe = "NCVT CBT పరీక్షల కోసం 75% తప్పనిసరి హాజరు నిబంధన",
                contentEn = "Dear students, DGT guidelines require a minimum 75% biometric/daily attendance to generate CBT examination hall tickets. Students with less than 75% attendance must meet the Principal immediately.",
                contentTe = "ప్రియమైన విద్యార్థులకు సూచన: NCVT CBT పరీక్ష హాల్ టికెట్లు జారీ చేయడానికి కనీసం 75% హాజరు తప్పనిసరి. 75% కన్నా తక్కువ ఉన్నవారు వెంటనే ప్రిన్సిపాల్ గారిని సంప్రదించాలి.",
                date = "2026-10-25",
                authorRole = "Principal, ATC Huzurnagar",
                isUrgent = true,
                readCount = 58
            ),
            Announcement(
                id = "ann_2",
                tradeId = "ALL",
                titleEn = "Workshop Safety Shoes & Apron Inspection Tomorrow",
                titleTe = "రేపు వర్క్‌షాప్ సేఫ్టీ బూట్లు & అప్రాన్ తనిఖీ",
                contentEn = "Strict safety compliance is enforced at ATC Huzurnagar. Every student entering Machine Shop, EV Lab, or Automation Cell must wear ISI-marked steel-toe safety shoes and ATC blue apron.",
                contentTe = "ATC హుజూర్‌నగర్ వర్క్‌షాప్‌లలోకి ప్రవేశించే విద్యార్థులందరూ తప్పనిసరిగా స్టీల్-టో సేఫ్టీ బూట్లు మరియు బ్లూ అప్రాన్ ధరించాలి. నిబంధనలు పాటించని వారిని ప్రాక్టికల్స్‌కు అనుమతించరు.",
                date = "2026-10-24",
                authorRole = "Safety Officer & Principal",
                isUrgent = false,
                readCount = 49
            ),
            Announcement(
                id = "ann_3",
                tradeId = "trade_ev",
                titleEn = "Guest Lecture on EV Battery Swapping Infrastructure",
                titleTe = "EV బ్యాటరీ స్వాపింగ్ టెక్నాలజీపై గెస్ట్ లెక్చర్",
                contentEn = "Senior Technical Manager from T-Works Hyderabad will conduct a live session on Fast DC Charging and Battery Swapping on Wednesday at ATC Seminar Hall.",
                contentTe = "బుధవారం నాడు ATC సెమినార్ హాల్‌లో హైదరాబాద్ T-Works సీనియర్ మేనేజర్ గారిచే EV ఫాస్ట్ ఛార్జింగ్ మరియు బ్యాటరీ స్వాపింగ్ విధానంపై ప్రత్యక్ష తరగతి నిర్వహించబడుతుంది.",
                date = "2026-10-23",
                authorRole = "EV Trade Lead",
                isUrgent = false,
                readCount = 18
            )
        )
        dao.insertAnnouncements(announcements)

        // 12. Seed Leave Request
        val sampleLeave = LeaveRequest(
            studentId = "std_DS_07",
            startDate = "2026-10-23",
            endDate = "2026-10-24",
            reason = "Family medical emergency at Suryapet hospital",
            status = LeaveStatus.APPROVED,
            requestedOn = "2026-10-22",
            reviewNotes = "Approved by P. Srilatha on submission of medical slip."
        )
        dao.insertLeaveRequest(sampleLeave)

        // 13. Audit Log
        val sampleAudit = AttendanceAuditLog(
            studentName = "Rajeshwari Kotla (ATC-24-DS04)",
            date = "2026-10-22",
            oldStatus = "ABSENT",
            newStatus = "PRESENT",
            modifiedBy = "Principal Sharma",
            reason = "Student attended District Skill Competition on official duty."
        )
        dao.insertAuditLog(sampleAudit)
    }
}
