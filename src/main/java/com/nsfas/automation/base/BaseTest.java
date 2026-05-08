package com.nsfas.automation.base;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.drivers.DriverManager;
import com.nsfas.automation.utils.ExtentReportManager;
import com.nsfas.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * All test classes extend this. Handles driver lifecycle and reporting hooks.
 */
public class BaseTest {

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

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
        String url = ConfigReader.get("base.url");
        DriverManager.getDriver().get(url);
        log.info("Navigated to: {}", url);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(result.getName());
            log.error("Test FAILED: {} | Screenshot: {}", result.getName(), screenshotPath);
        }
        log.info("====== Finished test: {} | Status: {} ======",
                result.getName(), getStatus(result.getStatus()));
        DriverManager.quitDriver();
    }

    private String getStatus(int status) {
        if (status == ITestResult.SUCCESS) return "PASSED";
        if (status == ITestResult.FAILURE) return "FAILED";
        if (status == ITestResult.SKIP)    return "SKIPPED";
        return "UNKNOWN";
    }
}
