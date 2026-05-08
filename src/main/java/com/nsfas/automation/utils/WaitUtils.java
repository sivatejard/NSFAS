package com.nsfas.automation.utils;

import com.nsfas.automation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Centralised wait utilities — explicit, fluent, and custom waits.
 */
public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final int defaultTimeout;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.defaultTimeout = ConfigReader.getInt("explicit.wait", 20);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
    }

    // ─────────────────────────────────────────────────
    // Visibility
    // ─────────────────────────────────────────────────

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public List<WebElement> waitForVisibilityOfAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public boolean waitForInvisibility(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public boolean waitForInvisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // ─────────────────────────────────────────────────
    // Clickable
    // ─────────────────────────────────────────────────

    public WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public WebElement waitForClickable(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ─────────────────────────────────────────────────
    // Presence
    // ─────────────────────────────────────────────────

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForPresenceOfAll(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // ─────────────────────────────────────────────────
    // URL / Title
    // ─────────────────────────────────────────────────

    public boolean waitForUrlContains(String urlFragment) {
        return wait.until(ExpectedConditions.urlContains(urlFragment));
    }

    public boolean waitForUrlToBe(String exactUrl) {
        return wait.until(ExpectedConditions.urlToBe(exactUrl));
    }

    public boolean waitForTitleContains(String titleFragment) {
        return wait.until(ExpectedConditions.titleContains(titleFragment));
    }

    // ─────────────────────────────────────────────────
    // Text
    // ─────────────────────────────────────────────────

    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public boolean waitForTextPresent(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean waitForValuePresent(By locator, String value) {
        return wait.until(ExpectedConditions.textToBePresentInElementValue(locator, value));
    }

    // ─────────────────────────────────────────────────
    // Alert
    // ─────────────────────────────────────────────────

    public org.openqa.selenium.Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    // ─────────────────────────────────────────────────
    // Frame
    // ─────────────────────────────────────────────────

    public WebDriver waitForFrameAndSwitch(By locator) {
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    public WebDriver waitForFrameAndSwitch(String nameOrId) {
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(nameOrId));
    }

    public WebDriver waitForFrameAndSwitch(int index) {
        return wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }

    // ─────────────────────────────────────────────────
    // Attribute / CSS
    // ─────────────────────────────────────────────────

    public boolean waitForAttributeContains(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }

    public boolean waitForAttributeToBe(By locator, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
    }

    // ─────────────────────────────────────────────────
    // Page load
    // ─────────────────────────────────────────────────

    public void waitForPageLoad() {
        wait.until(driver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete"));
        log.info("Page load complete");
    }

    // ─────────────────────────────────────────────────
    // Fluent wait
    // ─────────────────────────────────────────────────

    public WebElement fluentWait(By locator, int timeoutSeconds, int pollingMillis) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class)
                .ignoring(org.openqa.selenium.StaleElementReferenceException.class);
        return fluentWait.until(d -> d.findElement(locator));
    }

    // ─────────────────────────────────────────────────
    // Count
    // ─────────────────────────────────────────────────

    public boolean waitForNumberOfElementsToBe(By locator, int count) {
        return wait.until(d -> d.findElements(locator).size() == count);
    }

    public boolean waitForNumberOfElementsToBeMoreThan(By locator, int count) {
        return wait.until(d -> d.findElements(locator).size() > count);
    }
}
