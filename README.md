# NSFAS Automation Framework

End-to-end Selenium automation framework for the NSFAS disbursement approval workflow, covering all 8 approval stages from Operations Dev through Finance CEO.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Framework Architecture](#framework-architecture)
- [Building the Project](#building-the-project)
- [Running Tests](#running-tests)
- [Test Reports](#test-reports)
- [Test Suites](#test-suites)

---

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Java JDK | 11+ | Ensure `JAVA_HOME` is set |
| Apache Maven | 3.8+ | Ensure `mvn` is on PATH |
| Google Chrome | Latest | Default browser |
| Git | Any | For cloning the repo |

Verify your environment:

```bash
java -version
mvn -version
```

---

## Project Setup

**1. Clone the repository**

```bash
git clone https://github.com/sivatejard/NSFAS.git
cd NSFAS
```

**2. Create the config file**

The `config.properties` file is git-ignored to protect credentials. Copy the template and fill in your values:

```bash
cp src/test/resources/config.properties.template src/test/resources/config.properties
```

Then edit `src/test/resources/config.properties` with your environment details (see [Configuration](#configuration) below).

**3. Install dependencies**

```bash
mvn clean install -DskipTests
```

---

## Configuration

Edit `src/test/resources/config.properties`:

```properties
# Application
app.name=NSFAS
environment=QA
base.url=https://your-nsfas-app-url.co.za

# Browser: chrome | firefox | edge
browser=chrome
headless=false

# Timeouts (seconds)
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# Credentials
app.username=your_username
app.password=your_password

# Reporting
extent.report.path=test-output/reports/
screenshot.on.failure=true
```

You can also override any property via Maven `-D` system properties at runtime (see [Running Tests](#running-tests)).

---

## Project Structure

```
NSFAS/
├── pom.xml                                  # Maven build configuration
├── README.md
│
├── src/
│   ├── main/
│   │   ├── java/com/nsfas/automation/
│   │   │   ├── base/
│   │   │   │   └── BaseTest.java            # TestNG lifecycle hooks
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java        # Loads config.properties
│   │   │   ├── constants/
│   │   │   │   └── Constants.java           # Project-wide constants
│   │   │   ├── drivers/
│   │   │   │   └── DriverManager.java       # ThreadLocal WebDriver manager
│   │   │   ├── pages/
│   │   │   │   ├── BasePage.java            # 50+ reusable Selenium methods
│   │   │   │   └── disbursement/
│   │   │   │       ├── LoginPage.java
│   │   │   │       ├── NavigationPage.java
│   │   │   │       ├── CaseManagementPage.java
│   │   │   │       ├── CreateSchedulePage.java
│   │   │   │       ├── DisbursementProjectionsPage.java
│   │   │   │       ├── RelatedEntitiesPage.java
│   │   │   │       └── RequestAttributeRoutingPage.java
│   │   │   └── utils/
│   │   │       ├── AlertUtils.java
│   │   │       ├── ExcelUtils.java
│   │   │       ├── ExtentReportManager.java
│   │   │       ├── FrameUtils.java
│   │   │       ├── LogUtils.java
│   │   │       ├── ScreenshotUtils.java
│   │   │       ├── SharedTestData.java      # Cross-stage data sharing
│   │   │       ├── TestDataUtils.java
│   │   │       ├── WaitUtils.java
│   │   │       └── WindowUtils.java
│   │   └── resources/
│   │       └── log4j2.xml                   # Log4j2 configuration
│   │
│   └── test/
│       ├── java/com/nsfas/automation/
│       │   ├── listeners/
│       │   │   └── TestListener.java        # Screenshots on failure, reporting
│       │   └── tests/
│       │       ├── SampleLoginTest.java     # Smoke & regression login test
│       │       └── disbursement/
│       │           ├── DisbursementBaseTest.java   # Base for all 8 stages
│       │           ├── Step1_OperationsDevTest.java
│       │           ├── Step2_FinanceReviewerTest.java
│       │           ├── Step3_OperationsInitiatorTest.java
│       │           ├── Step4_FirstApproverTest.java
│       │           ├── Step5_FinanceSeniorManagerTest.java
│       │           ├── Step6_FinanceCOOTest.java
│       │           ├── Step7_FinanceCFOTest.java
│       │           └── Step8_FinanceCEOTest.java
│       └── resources/
│           ├── config.properties.template   # Config template (commit safe)
│           ├── testng.xml                   # Smoke + regression suite
│           ├── testng_disbursement.xml      # Full 8-stage E2E suite
│           └── testdata/
│               └── LoginData.xlsx           # Excel test data
│
├── test-output/                             # Generated at runtime (git-ignored)
│   ├── reports/                             # Extent HTML reports
│   ├── screenshots/                         # Failure screenshots
│   └── logs/                               # Rolling log files
│
└── target/                                  # Maven build output (git-ignored)
```

---

## Framework Architecture

### Design Patterns

| Pattern | Implementation |
|---------|---------------|
| Page Object Model | All pages extend `BasePage`; locators are private fields; action methods are public |
| ThreadLocal WebDriver | `DriverManager` isolates drivers per thread for parallel execution |
| Configuration as Code | `ConfigReader` loads `config.properties` with system property override support |
| Centralized Waits | `WaitUtils` provides explicit wait methods (visibility, clickable, presence, URL, title) |
| Test Listener | `TestListener` auto-embeds Base64 screenshots on failure into Extent Reports |
| Extent Reporting | Timestamped HTML reports generated in `test-output/reports/` |
| Cross-Stage Data Sharing | `SharedTestData` holds the projection sequence number across all 8 disbursement stages |
| Base Test Classes | `BaseTest` for smoke/regression; `DisbursementBaseTest` for sequential E2E stages |

### Core Components

- **DriverManager** — Manages WebDriver lifecycle using ThreadLocal; supports Chrome, Firefox, Edge; handles headless mode; auto-downloads drivers via WebDriverManager
- **ConfigReader** — Singleton properties loader; any key can be overridden with a `-D` JVM argument
- **BasePage** — Central Selenium helper with 50+ methods covering clicks, input, waits, dropdowns, checkboxes, scrolling, JS execution, multi-window, and file upload
- **ExtentReportManager** — ThreadLocal ExtentTest tracking; generates one HTML report per run with embedded screenshots
- **SharedTestData** — Static data holder that carries the disbursement sequence number captured in Stage 1 through all subsequent stages

### Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Selenium Java | 4.18.1 | Browser automation |
| WebDriverManager | 5.7.0 | Auto driver download |
| TestNG | 7.9.0 | Test framework |
| ExtentReports | 5.1.1 | HTML reporting |
| Apache POI | 5.2.5 | Excel data reading |
| Log4j2 | 2.23.1 | Logging |
| AssertJ | 3.25.3 | Fluent assertions |

---

## Building the Project

**Compile only:**

```bash
mvn compile
```

**Compile test classes:**

```bash
mvn test-compile
```

**Full build (skip tests):**

```bash
mvn clean install -DskipTests
```

**Full build with tests:**

```bash
mvn clean install
```

**Clean build output:**

```bash
mvn clean
```

---

## Running Tests

### Run Default Suite (Smoke + Regression)

The default suite is `testng.xml`, configured in `pom.xml`:

```bash
mvn clean test
```

### Run Full 8-Stage Disbursement E2E Suite

```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng_disbursement.xml
```

### Run a Specific Stage Only

```bash
# Stage 1 - Operations Dev
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng_disbursement.xml -Dgroups=stage1

# Stage 2 - Finance Reviewer
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng_disbursement.xml -Dgroups=stage2

# Stage 3 - Operations Initiator
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng_disbursement.xml -Dgroups=stage3
```

### Run Smoke Tests Only

```bash
mvn clean test -Dgroups=smoke
```

### Run Regression Tests Only

```bash
mvn clean test -Dgroups=regression
```

### Run a Specific Test Class

```bash
mvn clean test -Dtest=SampleLoginTest
mvn clean test -Dtest=Step1_OperationsDevTest
```

### Run a Specific Test Method

```bash
mvn clean test -Dtest=SampleLoginTest#testValidLogin
```

### Override Config at Runtime

Pass any `config.properties` key as a `-D` system property to override it without editing the file:

```bash
# Run headless on a different URL
mvn clean test -Dbase.url=https://staging.nsfas.co.za -Dheadless=true

# Use Firefox
mvn clean test -Dbrowser=firefox

# Override credentials
mvn clean test -Dapp.username=testuser -Dapp.password=testpass
```

---

## Test Suites

### testng.xml — Smoke & Regression Suite

Runs in **parallel** (3 threads, method-level).

| Test Group | Classes | Purpose |
|-----------|---------|---------|
| `smoke` | SampleLoginTest | Fast login sanity check |
| `regression` | SampleLoginTest | Full login regression |

### testng_disbursement.xml — 8-Stage E2E Suite

Runs **sequentially** (parallel=none). Each stage depends on the previous stage completing successfully. The sequence number captured in Stage 1 is automatically passed to all subsequent stages via `SharedTestData`.

| Stage | Group | Test Class | Role |
|-------|-------|-----------|------|
| 1 | `stage1` | Step1_OperationsDevTest | Creates disbursement schedule; captures sequence number |
| 2 | `stage2` | Step2_FinanceReviewerTest | Finance Reviewer approval |
| 3 | `stage3` | Step3_OperationsInitiatorTest | Operations Initiator review |
| 4 | `stage4` | Step4_FirstApproverTest | First level approval |
| 5 | `stage5` | Step5_FinanceSeniorManagerTest | Finance Senior Manager approval |
| 6 | `stage6` | Step6_FinanceCOOTest | Finance COO approval |
| 7 | `stage7` | Step7_FinanceCFOTest | Finance CFO approval |
| 8 | `stage8` | Step8_FinanceCEOTest | Finance CEO final approval |

---

## Test Reports

After each test run, reports are generated in `test-output/` (created automatically):

| Output | Location | Description |
|--------|----------|-------------|
| HTML Report | `test-output/reports/NSFAS_Report_<timestamp>.html` | Extent HTML report with screenshots |
| Logs | `test-output/logs/nsfas_automation.log` | Rolling log file (daily + 10MB rollover) |
| Screenshots | `test-output/screenshots/` | Failure screenshots (also embedded in report) |

Open the HTML report in any browser to view pass/fail results, embedded screenshots, and step-level logs.
