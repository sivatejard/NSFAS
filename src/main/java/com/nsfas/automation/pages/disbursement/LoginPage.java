package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Login page: http://10.15.64.77:85/Identity/Account/Login
 * UPDATE locators after inspecting actual elements in the app.
 */
public class LoginPage extends BasePage {

    // ── Locators ── UPDATE these after inspecting the actual app HTML ──
    private final By usernameField = By.id("Input_UserName");
    private final By passwordField = By.id("Input_Password");
    private final By loginButton   = By.id("login-submit");
    private final By errorMessage  = By.cssSelector(".validation-summary-errors, .text-danger");
    private final By logoutLink    = By.cssSelector("a[href*='Logout'], form[action*='Logout'] button");

    public LoginPage loginAs(String username, String password) {
        log.info("Logging in as: {}", username);
        wait.waitForVisibility(usernameField).clear();
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
        wait.waitForPageLoad();
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
