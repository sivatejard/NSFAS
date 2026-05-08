package com.nsfas.automation.tests;

import com.nsfas.automation.base.BaseTest;
import com.nsfas.automation.drivers.DriverManager;
import com.nsfas.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Sample test demonstrating how to write tests using this framework.
 * Replace locators and URLs with your actual NSFAS application details.
 */
public class SampleLoginTest extends BaseTest {

    // Replace these locators with your actual NSFAS app locators
    private final By usernameField  = By.id("username");
    private final By passwordField  = By.id("password");
    private final By loginButton    = By.id("loginBtn");
    private final By dashboardHeader = By.cssSelector("h1.dashboard-title");
    private final By errorMessage   = By.cssSelector(".error-message");

    @Test(description = "Verify successful login with valid credentials")
    public void testValidLogin() {
        WaitUtils wait = new WaitUtils(DriverManager.getDriver());

        wait.waitForVisibility(usernameField).sendKeys("validUser");
        wait.waitForVisibility(passwordField).sendKeys("validPass123");
        wait.waitForClickable(loginButton).click();

        wait.waitForVisibility(dashboardHeader);
        String headerText = DriverManager.getDriver().findElement(dashboardHeader).getText();
        Assert.assertTrue(headerText.contains("Dashboard"),
                "Expected dashboard header but got: " + headerText);
        log.info("Login successful — dashboard visible");
    }

    @Test(description = "Verify error message for invalid credentials")
    public void testInvalidLogin() {
        WaitUtils wait = new WaitUtils(DriverManager.getDriver());

        wait.waitForVisibility(usernameField).sendKeys("wrongUser");
        wait.waitForVisibility(passwordField).sendKeys("wrongPass");
        wait.waitForClickable(loginButton).click();

        wait.waitForVisibility(errorMessage);
        String error = DriverManager.getDriver().findElement(errorMessage).getText();
        Assert.assertFalse(error.isEmpty(), "Error message should be displayed for invalid login");
        log.info("Invalid login correctly rejected with message: {}", error);
    }

    @Test(description = "Verify login button is disabled when fields are empty")
    public void testEmptyFieldsLogin() {
        boolean isLoginButtonEnabled = DriverManager.getDriver().findElement(loginButton).isEnabled();
        // Adjust assertion based on your app's actual behavior
        log.info("Login button enabled with empty fields: {}", isLoginButtonEnabled);
    }
}
