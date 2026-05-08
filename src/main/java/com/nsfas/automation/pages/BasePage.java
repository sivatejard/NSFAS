package com.nsfas.automation.pages;

import com.nsfas.automation.drivers.DriverManager;
import com.nsfas.automation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Base page class — extend this for every Page Object.
 * Contains all reusable Selenium interactions.
 */
public class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected WebDriver driver;
    protected WaitUtils wait;
    protected Actions actions;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WaitUtils(driver);
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // ─────────────────────────────────────────────────
    // Navigation
    // ─────────────────────────────────────────────────

    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void refreshPage() {
        driver.navigate().refresh();
        log.info("Page refreshed");
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateForward() {
        driver.navigate().forward();
    }

    // ─────────────────────────────────────────────────
    // Click actions
    // ─────────────────────────────────────────────────

    public void click(By locator) {
        log.info("Clicking: {}", locator);
        wait.waitForClickable(locator).click();
    }

    public void click(WebElement element) {
        log.info("Clicking element: {}", element);
        wait.waitForClickable(element).click();
    }

    public void jsClick(By locator) {
        log.info("JS click: {}", locator);
        WebElement el = wait.waitForVisibility(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void doubleClick(By locator) {
        log.info("Double click: {}", locator);
        actions.doubleClick(wait.waitForClickable(locator)).perform();
    }

    public void rightClick(By locator) {
        log.info("Right click: {}", locator);
        actions.contextClick(wait.waitForClickable(locator)).perform();
    }

    // ─────────────────────────────────────────────────
    // Text input
    // ─────────────────────────────────────────────────

    public void type(By locator, String text) {
        log.info("Typing '{}' into: {}", text, locator);
        WebElement el = wait.waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void type(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public void clearAndType(By locator, String text) {
        WebElement el = wait.waitForVisibility(locator);
        el.sendKeys(Keys.CONTROL + "a");
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }

    public void pressKey(By locator, Keys key) {
        wait.waitForVisibility(locator).sendKeys(key);
    }

    public void pressEnter(By locator) {
        wait.waitForVisibility(locator).sendKeys(Keys.ENTER);
    }

    public void pressTab(By locator) {
        wait.waitForVisibility(locator).sendKeys(Keys.TAB);
    }

    // ─────────────────────────────────────────────────
    // Get text / attribute
    // ─────────────────────────────────────────────────

    public String getText(By locator) {
        String text = wait.waitForVisibility(locator).getText();
        log.info("Text from {}: '{}'", locator, text);
        return text;
    }

    public String getText(WebElement element) {
        return element.getText().trim();
    }

    public String getAttribute(By locator, String attribute) {
        return wait.waitForVisibility(locator).getDomAttribute(attribute);
    }

    public String getAttribute(WebElement element, String attribute) {
        return element.getDomAttribute(attribute);
    }

    public String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    public String getPlaceholder(By locator) {
        return getAttribute(locator, "placeholder");
    }

    // ─────────────────────────────────────────────────
    // Visibility / presence checks
    // ─────────────────────────────────────────────────

    public boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSelected(By locator) {
        return driver.findElement(locator).isSelected();
    }

    public boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    // ─────────────────────────────────────────────────
    // Dropdown (Select)
    // ─────────────────────────────────────────────────

    public void selectByVisibleText(By locator, String text) {
        log.info("Select by text '{}' from: {}", text, locator);
        new Select(wait.waitForVisibility(locator)).selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        log.info("Select by value '{}' from: {}", value, locator);
        new Select(wait.waitForVisibility(locator)).selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        new Select(wait.waitForVisibility(locator)).selectByIndex(index);
    }

    public String getSelectedOption(By locator) {
        return new Select(wait.waitForVisibility(locator)).getFirstSelectedOption().getText();
    }

    public List<String> getAllDropdownOptions(By locator) {
        List<WebElement> options = new Select(wait.waitForVisibility(locator)).getOptions();
        List<String> texts = new ArrayList<>();
        options.forEach(opt -> texts.add(opt.getText().trim()));
        return texts;
    }

    // ─────────────────────────────────────────────────
    // Checkbox / Radio
    // ─────────────────────────────────────────────────

    public void checkCheckbox(By locator) {
        WebElement el = wait.waitForClickable(locator);
        if (!el.isSelected()) el.click();
        log.info("Checkbox checked: {}", locator);
    }

    public void uncheckCheckbox(By locator) {
        WebElement el = wait.waitForClickable(locator);
        if (el.isSelected()) el.click();
        log.info("Checkbox unchecked: {}", locator);
    }

    public void selectRadioButton(By locator) {
        WebElement el = wait.waitForClickable(locator);
        if (!el.isSelected()) el.click();
    }

    // ─────────────────────────────────────────────────
    // Hover / Drag & Drop / Actions
    // ─────────────────────────────────────────────────

    public void hoverOver(By locator) {
        log.info("Hovering over: {}", locator);
        actions.moveToElement(wait.waitForVisibility(locator)).perform();
    }

    public void hoverAndClick(By hoverLocator, By clickLocator) {
        actions.moveToElement(wait.waitForVisibility(hoverLocator)).perform();
        wait.waitForClickable(clickLocator).click();
    }

    public void dragAndDrop(By source, By target) {
        log.info("Drag from {} to {}", source, target);
        actions.dragAndDrop(
                wait.waitForVisibility(source),
                wait.waitForVisibility(target)
        ).perform();
    }

    public void dragAndDropByOffset(By source, int xOffset, int yOffset) {
        actions.dragAndDropBy(wait.waitForVisibility(source), xOffset, yOffset).perform();
    }

    // ─────────────────────────────────────────────────
    // Scroll
    // ─────────────────────────────────────────────────

    public void scrollToElement(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void scrollBy(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
    }

    // ─────────────────────────────────────────────────
    // JavaScript
    // ─────────────────────────────────────────────────

    public Object executeJS(String script, Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public void highlightElement(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='3px solid red'", el);
    }

    public void setValueByJS(By locator, String value) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value='" + value + "';", el);
    }

    // ─────────────────────────────────────────────────
    // Multiple elements
    // ─────────────────────────────────────────────────

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public List<String> getTextFromElements(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        List<String> texts = new ArrayList<>();
        elements.forEach(el -> texts.add(el.getText().trim()));
        return texts;
    }

    public int getElementCount(By locator) {
        return driver.findElements(locator).size();
    }

    // ─────────────────────────────────────────────────
    // Upload / File
    // ─────────────────────────────────────────────────

    public void uploadFile(By locator, String absoluteFilePath) {
        log.info("Uploading file: {}", absoluteFilePath);
        driver.findElement(locator).sendKeys(absoluteFilePath);
    }

    // ─────────────────────────────────────────────────
    // Wait shortcuts
    // ─────────────────────────────────────────────────

    public void waitForPageLoad() {
        wait.waitForPageLoad();
    }

    public void waitForElementVisible(By locator) {
        wait.waitForVisibility(locator);
    }

    public void waitForElementInvisible(By locator) {
        wait.waitForInvisibility(locator);
    }

    public void waitForTextToBePresentInElement(By locator, String text) {
        wait.waitForTextPresent(locator, text);
    }
}
