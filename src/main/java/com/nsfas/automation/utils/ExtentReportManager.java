package com.nsfas.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.nsfas.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages ExtentReports lifecycle.
 * Call initReport() once per suite, flushReport() at suite end.
 */
public class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static void initReport() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = "test-output/reports/NSFAS_Report_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("NSFAS Automation Report");
        spark.config().setReportName("NSFAS Test Execution Report");
        spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Application", ConfigReader.get("app.name", "NSFAS"));
        extent.setSystemInfo("Environment", ConfigReader.get("environment", "QA"));
        extent.setSystemInfo("Browser", ConfigReader.get("browser", "Chrome"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        log.info("ExtentReport initialized at: {}", reportPath);
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThread.set(test);
        return test;
    }

    public static ExtentTest createTest(String testName) {
        return createTest(testName, "");
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReport flushed");
        }
    }
}
