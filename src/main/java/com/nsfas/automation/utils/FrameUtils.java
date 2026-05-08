package com.nsfas.automation.utils;

import com.nsfas.automation.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FrameUtils {

    private static final Logger log = LogManager.getLogger(FrameUtils.class);

    private FrameUtils() {}

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    public static void switchToFrame(int index) {
        log.info("Switching to frame by index: {}", index);
        driver().switchTo().frame(index);
    }

    public static void switchToFrame(String nameOrId) {
        log.info("Switching to frame: {}", nameOrId);
        driver().switchTo().frame(nameOrId);
    }

    public static void switchToFrame(By locator) {
        log.info("Switching to frame by locator: {}", locator);
        WebElement frame = driver().findElement(locator);
        driver().switchTo().frame(frame);
    }

    public static void switchToFrame(WebElement frameElement) {
        driver().switchTo().frame(frameElement);
    }

    public static void switchToParentFrame() {
        driver().switchTo().parentFrame();
        log.info("Switched to parent frame");
    }

    public static void switchToDefaultContent() {
        driver().switchTo().defaultContent();
        log.info("Switched to default content");
    }

    /** Switch into nested frames by order of locators. */
    public static void switchToNestedFrame(By... locators) {
        driver().switchTo().defaultContent();
        for (By locator : locators) {
            driver().switchTo().frame(driver().findElement(locator));
        }
        log.info("Switched to nested frame");
    }
}
