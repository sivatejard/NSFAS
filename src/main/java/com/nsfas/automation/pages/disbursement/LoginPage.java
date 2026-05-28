package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Login page: http://10.15.64.77:85/Identity/Account/Login
 * UPDATE locators after inspecting actual elements in the app.
 */
public class LoginPage extends BasePage {

    // Confirmed from live app HTML inspection
    private final By usernameField = By.id("Input_UserName");
    private final By passwordField = By.id("passwordInput");
    private final By loginButton   = By.id("js-login-btn");
    private final By errorMessage  = By.cssSelector(".alert-danger li, .validation-summary-errors li");
    private final By logoutLink    = By.cssSelector("a[href*='Logout'], form[action*='Logout'] button");

    public LoginPage loginAs(String username, String password) {
        log.info("Logging in as: {}", username);
        wait.waitForVisibility(usernameField).clear();
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        new WebDriverWait(driver, Duration.ofSeconds(120))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("Login")));
        log.info("Login submitted for user: {}", username);
        return this;
    }

    public boolean isLoginSuccessful() {
        return !isDisplayed(errorMessage) && !driver.getCurrentUrl().contains("Login");
    }

    public String getErrorMessage() {
        if (isDisplayed(errorMessage)) {
            return getText(errorMessage);
        }
        return "";
    }

    public boolean isUserLoggedIn() {
        return isDisplayed(logoutLink);
    }

    public void logout() {
        if (isDisplayed(logoutLink)) {
            jsClick(logoutLink);
            wait.waitForPageLoad();
            log.info("Logged out successfully");
        }
    }
}
