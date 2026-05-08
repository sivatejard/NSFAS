package com.nsfas.automation.utils;

import com.nsfas.automation.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    private ScreenshotUtils() {}

    /** Captures full-page screenshot and returns the absolute path. */
    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverManager.getDriver();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(filePath));
            log.info("Screenshot saved: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
        return new File(filePath).getAbsolutePath();
    }

    /** Captures screenshot as Base64 — used for embedding in ExtentReports. */
    public static String captureScreenshotAsBase64() {
        WebDriver driver = DriverManager.getDriver();
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /** Captures screenshot of a specific element only. */
    public static String captureElementScreenshot(WebElement element, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_element_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            File src = element.getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), Paths.get(filePath));
            log.info("Element screenshot saved: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to capture element screenshot: {}", e.getMessage());
        }
        return new File(filePath).getAbsolutePath();
    }
}
