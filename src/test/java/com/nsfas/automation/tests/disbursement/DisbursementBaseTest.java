package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.drivers.DriverManager;
import com.nsfas.automation.pages.disbursement.CaseManagementPage;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.NavigationPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.ExtentReportManager;
import com.nsfas.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all 8 disbursement stage tests.
 * Each test logs in with its own user and performs the stage-specific actions.
 */
public abstract class DisbursementBaseTest {

    protected static final Logger log = LogManager.getLogger(DisbursementBaseTest.class);

    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        ExtentReportManager.initReport();
    }

    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        ExtentReportManager.flushReport();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        log.info("====== Starting test: {} ======", method.getName());
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.get("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtils.captureScreenshot(result.getName());
            log.error("Test FAILED: {}", result.getName());
        }
        log.info("====== Finished: {} | {} ======", result.getName(), statusLabel(result.getStatus()));
        DriverManager.quitDriver();
    }

    protected LoginPage loginPage() {
        return new LoginPage();
    }

    protected NavigationPage navigationPage() {
        return new NavigationPage();
    }

    protected CaseManagementPage caseManagementPage() {
        return new CaseManagementPage();
    }

    protected RequestAttributeRoutingPage routingPage() {
        return new RequestAttributeRoutingPage();
    }

    /**
     * Reusable: navigate to My Team Cases → search sequence number → click View/Edit.
     */
    protected void openCaseBySequenceNumber(String sequenceNumber) {
        CaseManagementPage casePage = caseManagementPage();
        casePage.navigateToMyTeamCases();
        casePage.searchBySequenceNumber(sequenceNumber);
        casePage.clickViewEdit();
        log.info("Case opened for sequence number: {}", sequenceNumber);
    }

    private String statusLabel(int status) {
        if (status == ITestResult.SUCCESS) return "PASSED";
        if (status == ITestResult.FAILURE) return "FAILED";
        if (status == ITestResult.SKIP)    return "SKIPPED";
        return "UNKNOWN";
    }
}
