# BioMedTrack - Gemini CLI Context

BioMedTrack is a real-time, multi-user medical equipment tracking and maintenance management Android app built for hospital environments. It replaces manual logs and spreadsheets with a digital system that tracks machine health, schedules maintenance, and provides instant documentation.

## Project Overview

- **Purpose**: Manage and track medical equipment lifecycle, maintenance schedules, and service history across 22+ hospital departments.
- **Key Features**: Real-time sync, offline support, role-based access control (RBAC), photo documentation, automated maintenance reminders, and PDF/Excel report generation.
- **Current Status**: ~99% Complete (Phase 13: User Management - 100% Complete; Phase 14 next).

## Architecture & Design Principles

### Clean Architecture
The project is strictly divided into three layers where dependencies only point inward:
- **Presentation (UI + ViewModels)**: Jetpack Compose, Material 3, MVVM.
- **Domain (Use Cases + Models + Interfaces)**: Pure Kotlin logic, zero Android/Firebase dependencies.
- **Data (Firestore + Room + Repositories)**: Implementation of repository interfaces using Firestore (Source of Truth) and Room (Local Cache).

### Core Design Decisions
- **Fluid Layouts (Stability)**: Avoid fixed heights (dp) in nested components (`TextField`, `Card`, `LazyColumn`) to ensure IME (Keyboard) stability and prevent layout measurement loops that cause "Davey" lag.
- **MVI State Hoisting**: Single source of truth for all form fields is managed in ViewModels via `MutableStateFlow`, ensuring data persistence and focus stability during rapid recompositions.
- **Precision Timing**: All logs and tasks use `Long` timestamps (Unix time) instead of `String` dates to enable accurate chronological sorting and "14m ago" style relative time formatting in the UI.
- **Unified Department Model**: The project uses a rich `Department` object instead of simple enums to carry metadata (counts, IDs) throughout all layers.
- **Firestore + Room**: Firestore handles real-time sync and is the source of truth. Room provides instant local reads and offline support via JSON/Gson serialization of nested objects.
- **In-Memory Filtering**: Most lists are filtered in memory within ViewModels after a single Firestore snapshot fetch to ensure high performance.
- **SessionManager**: Handles in-memory user store and permission checks using ID-resilient logic to avoid frequent Firestore reads.
- **Denormalization**: Technician and equipment names are denormalized in logs and status changes to save Firestore read costs.

## Roles & Permissions (RBAC)

| Feature                         | Technician   | Supervisor | Admin   |
|---------------------------------|--------------|------------|---------|
| View equipment                  | ✅            | ✅          | ✅       |
| Log maintenance / Change status | ✅ (Assigned) | ✅ (All)    | ✅ (All) |
| View history / Scheduler        | ✅            | ✅          | ✅       |
| Add/Edit/Delete equipment       | ❌            | ✅          | ✅       |
| Assign tasks / Generate reports | ❌            | ✅          | ✅       |
| Manage users                    | ❌            | ✅          | ✅       |
| Import equipment                | ❌            | ✅          | ✅       |

## Tech Stack

- **UI**: Jetpack Compose, Material 3, Compose Navigation.
- **Asynchronous Logic**: Kotlin Coroutines & Flow (heavy use of `combine` for real-time UI states).
- **Dependency Injection**: Hilt.
- **Database**: Room (Local), Firebase Firestore (Cloud).
- **Auth & Storage**: Firebase Authentication, Firebase Storage (Photos), Coil (Image Loading).
- **Messaging**: Firebase Cloud Messaging (FCM) + Cloud Functions.
- **Reporting**: iTextPDF (PDF), Apache POI (Excel).
- **Background Work**: WorkManager (Daily reminders).

## Project Roadmap (Current Status)

- **✅ Phase 1 — Domain Layer**: Models, Enums, Use Cases, Repository Interfaces.
- **✅ Phase 2 — Room Database**: Entities, DAOs, TypeConverters.
- **✅ Phase 3 — Firebase Layer**: DTOs, Repository Implementations (Sync logic).
- **✅ Phase 4 — Hilt Modules**: DI setup for Database, Firebase, and Repositories.
- **✅ Phase 5 — Navigation Skeleton**: Routes, NavHost, Role-aware BottomNavBar.
- **✅ Phase 6 — Theme & Design System**: Custom Material 3 theme, high-fidelity reusable components, and Shimmer effects.
- **✅ Phase 7 — Auth Screens UI**: Splash and Login screens fully implemented and wired.
- **✅ Phase 8 — Core Screens UI**: Dashboard, Add Equipment, Edit Equipment, and Inventory Quick Status Change fully implemented and reactive.
- **✅ Phase 9 — Work UI**: Log Maintenance screen with universal checklist logic and Photo Documentation fully implemented.
- **✅ Phase 10 — Notifications**: Precision timing refactor, real-time activity feed, and FCM push alerts fully implemented.
- **✅ Phase 11 — Reports**: Professional PDF (iText 7) and Excel (Apache POI) report generation engines complete.
- **✅ Phase 12 — Scheduler & Task Assignment**: Fully functional scheduler calendar, supervisor task scheduling form, active technician filtering, custom checklist toggles, and safe Firestore task creation engine.
- **✅ Phase 13 — User Management**: Fully functional admin dashboard for managing users, roles, and department access. Secure user creation via Identity Toolkit, dynamic role changes, active status toggling, and robust form validation.
- **⏳ Phase 14**: Polish and Portfolio Prep.

## Screen Explanations

| Screen                   | Description                                                                                                                                                      |
|--------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Splash**               | Initial loading screen that checks for an active session and routes the user to the Dashboard or Login.                                                          |
| **Login**                | Secure authentication portal for engineers to sign in using their hospital credentials.                                                                          |
| **Dashboard**            | Real-time overview of machine health metrics (Online/Down/Service Due) and a feed of recent activities.                                                          |
| **Equipment Inventory**  | Centralized list of all assets with instant search, department filtering, and quick-action status toggles.                                                       |
| **Equipment Detail**     | Technical specifications, complete maintenance logs, and a chronological history of status changes for a specific machine.                                       |
| **Log Maintenance**      | Interactive form for engineers to record service work, including a 10-item safety checklist and photo documentation.                                             |
| **Scheduler**            | Week-view calendar for technicians to track assigned tasks and upcoming service deadlines.                                                                       |
| **Schedule Maintenance** | Supervisor dashboard form to schedule preventive maintenance tasks, select equipment, set due dates, assign active technicians, and configure custom checklists. |
| **Reports**              | Supervisor tool for generating professional PDF/Excel summaries of equipment performance and maintenance history.                                                |
| **User Management**      | Administrative interface to manage staff accounts, roles (Technician/Supervisor/Admin), and department access.                                                   |
| **Add / Edit Equipment** | Data entry forms for registering new machinery or updating technical specs and warranty information.                                                             |
| **Import Equipment**     | Admin utility for bulk-importing thousands of equipment records via Excel files.                                                                                 |
| **Notifications**        | Real-time activity log showing status changes and maintenance updates across all departments.                                                                    |

## Data Schema (Firestore)

- `/users/{userId}`: Profiles, roles, and assigned departments.
- `/equipment/{equipmentId}`: Specs, status, and maintenance dates.
- `/maintenanceLogs/{logId}`: Service records, checklist items, and photo URLs.
- `/statusChangeLogs/{logId}`: History of equipment status changes.
- `/tasks/{taskId}`: Assigned maintenance tasks for technicians.

## Core Feature Workflows

### 1. Authentication & Session Flow
- **Entry**: `SplashScreen` checks `SessionManager` for a cached user ID.
- **Login**: `LoginScreen` → `AuthRepo` (Firebase Auth) → `TechnicianDao` (Sync Profile to Room).
- **Session**: On success, the `Technician` object is hoisted into `SessionManager` (Singleton), which provides real-time access to the user's role and assigned departments across all screens.

### 2. Real-time Inventory & Filtering
- **Data Source**: `EquipmentRepo` provides a `Flow<List<Equipment>>` from Firestore with a local Room fallback.
- **Processing**: `InventoryVm` combines the equipment flow with `searchQuery` and `filter` StateFlows.
- **Efficiency**: Filtering is performed in-memory using Kotlin operators (`filter`, `map`) to ensure the UI remains responsive even with 1000+ assets.
- **Action**: Status toggles in the list trigger `EquipmentRepo.updateStatus()`, which updates both Firestore and the local cache simultaneously.

### 3. Maintenance Logging & Media Pipeline
- **Validation**: `LogMaintenanceVm` enforces a 10-point safety checklist before allowing submission.
- **Media**: Photos are captured via `CameraLauncher` → Uploaded to `Firebase Storage` → URL is returned.
- **Persistence**: `MaintenanceRepo` creates a `MaintenanceLog` entry and updates the `Equipment.lastMaintenanceDate` in a single transaction-like flow.
- **RBAC**: The "Submit" button is only enabled if `Permission.canLogMaintenance` returns true for the current user in `SessionManager`.

### 4. Scheduler & Task Management
- **Query**: `TaskRepo` fetches tasks assigned to the `Technician.id` found in `SessionManager`.
- **UI**: `SchedulerScreen` maps tasks to a calendar view based on their `scheduledDate` (Long timestamp).
- **Completion**: Completing a task automatically triggers the navigation to `LogMaintenanceScreen`, pre-filling the equipment details.

### 5. Activity Feed & Notifications
- **Activity**: Every status change or maintenance log triggers an entry in the `activityLogs` Firestore collection via `ActivityRepo`.
- **FCM**: A Cloud Function listens to the `activityLogs` collection and dispatches push notifications to users in relevant departments.
- **Local Feed**: The `NotificationsScreen` provides a real-time chronological feed of these events, denormalized for instant display.

### 6. Reporting Engine (PDF & Excel)
The reporting system follows a decoupled, thread-safe architecture leveraging Android's `MediaStore`:
- **Initiation**: `FileExportHelper` requests a placeholder `Uri` in the `Downloads` folder.
- **Aggregation**: `GenerateReportUseCase` filters `Equipment` and `Logs` into a unified `ReportData` model.
- **Generation**: `ReportsVm` launches a `Dispatchers.IO` coroutine → Generators (iText 7 / Apache POI) write to a `ByteArrayOutputStream`.
- **Finalization**: The ViewModel measures the buffer for the UI, flushes bytes to the `MediaStore` stream, and triggers native `Intent` dialogs for Sharing/Previewing.

## Building and Running

### Prerequisites
- Android Studio Ladybug or newer.
- JDK 11+.
- `google-services.json` in `app/` (Firebase required).

### Key Commands
- **Build**: `./gradlew assembleDebug`
- **Unit Tests**: `./gradlew test` (Uses JUnit4, MockK, Turbine).
- **Instrumented Tests**: `./gradlew connectedAndroidTest`.

## Development Conventions

- **Permission Checks**: Enforced at three layers: UI (hide buttons), Use Cases (logic check), and Firestore (Security Rules).
- **Testing**: Use `Turbine` for testing `Flow` outputs and `MockK` for repository mocking.
- **Theme**: Strictly use `BioMedTheme` values for colors and typography.
- **Git**: Proposed commit style is "feat: ...", "fix: ...", "refactor: ...".

