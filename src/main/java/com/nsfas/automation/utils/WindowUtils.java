package com.nsfas.automation.utils;

import com.nsfas.automation.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WindowUtils {

    private static final Logger log = LogManager.getLogger(WindowUtils.class);

    private WindowUtils() {}

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    public static String getCurrentWindowHandle() {
        return driver().getWindowHandle();
    }

    public static Set<String> getAllWindowHandles() {
        return driver().getWindowHandles();
    }

    /** Switch to the most recently opened window/tab. */
    public static void switchToNewWindow() {
        String current = driver().getWindowHandle();
        for (String handle : driver().getWindowHandles()) {
            if (!handle.equals(current)) {
                driver().switchTo().window(handle);
                log.info("Switched to new window: {}", handle);
                break;
            }
        }
    }

    /** Switch to window by its index (0-based). */
    public static void switchToWindowByIndex(int index) {
        List<String> handles = new ArrayList<>(driver().getWindowHandles());
        if (index < handles.size()) {
            driver().switchTo().window(handles.get(index));
            log.info("Switched to window index {}: {}", index, handles.get(index));
        } else {
            throw new IllegalArgumentException("Window index out of range: " + index);
        }
    }

    /** Switch to window whose title contains the given text. */
    public static void switchToWindowByTitle(String titleFragment) {
        String original = driver().getWindowHandle();
        for (String handle : driver().getWindowHandles()) {
            driver().switchTo().window(handle);
            if (driver().getTitle().contains(titleFragment)) {
                log.info("Switched to window with title containing: {}", titleFragment);
                return;
            }
        }
        driver().switchTo().window(original);
        throw new RuntimeException("No window found with title containing: " + titleFragment);
    }

    /** Switch to window whose URL contains the given text. */
    public static void switchToWindowByUrl(String urlFragment) {
        String original = driver().getWindowHandle();
        for (String handle : driver().getWindowHandles()) {
            driver().switchTo().window(handle);
            if (driver().getCurrentUrl().contains(urlFragment)) {
                log.info("Switched to window with URL containing: {}", urlFragment);
                return;
            }
        }
        driver().switchTo().window(original);
        throw new RuntimeException("No window found with URL containing: " + urlFragment);
    }

    public static void switchToWindow(String handle) {
        driver().switchTo().window(handle);
    }

    public static void closeCurrentWindow() {
        log.info("Closing window: {}", driver().getWindowHandle());
        driver().close();
    }

    /** Close newly opened window and switch back to parent. */
    public static void closeNewWindowAndSwitchBack(String parentHandle) {
        driver().close();
        driver().switchTo().window(parentHandle);
        log.info("Closed child window, returned to parent");
    }

    public static void openNewTab() {
        ((org.openqa.selenium.JavascriptExecutor) driver())
                .executeScript("window.open('about:blank', '_blank');");
        switchToNewWindow();
        log.info("Opened and switched to new tab");
    }

    public static int getWindowCount() {
        return driver().getWindowHandles().size();
    }
}
