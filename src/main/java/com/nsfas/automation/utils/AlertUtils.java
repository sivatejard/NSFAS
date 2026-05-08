package com.nsfas.automation.utils;

import com.nsfas.automation.drivers.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

public class AlertUtils {

    private static final Logger log = LogManager.getLogger(AlertUtils.class);

    private AlertUtils() {}

    private static WebDriver driver() {
        return DriverManager.getDriver();
    }

    public static boolean isAlertPresent() {
        try {
            driver().switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static void acceptAlert() {
        Alert alert = driver().switchTo().alert();
        log.info("Accepting alert: {}", alert.getText());
        alert.accept();
    }

    public static void dismissAlert() {
        Alert alert = driver().switchTo().alert();
        log.info("Dismissing alert: {}", alert.getText());
        alert.dismiss();
    }

    public static String getAlertText() {
        return driver().switchTo().alert().getText();
    }

    public static void typeInAlert(String text) {
        Alert alert = driver().switchTo().alert();
        log.info("Typing '{}' into alert prompt", text);
        alert.sendKeys(text);
        alert.accept();
    }

    public static void acceptAlertIfPresent() {
        if (isAlertPresent()) acceptAlert();
    }

    public static void dismissAlertIfPresent() {
        if (isAlertPresent()) dismissAlert();
    }
}
