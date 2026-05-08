package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Handles main menu/sidebar navigation.
 * UPDATE locators after inspecting actual app HTML.
 */
public class NavigationPage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By disbursementsMenu    = By.xpath("//a[contains(text(),'Disbursements') or contains(@href,'Disbursement')]");
    private final By caseManagementMenu   = By.xpath("//a[contains(text(),'Case Management') or contains(@href,'CaseManagement')]");
    private final By disbProjectionsMenu  = By.xpath("//a[contains(text(),'Disbursement Projections') or contains(@href,'Projection')]");

    public void goToDisbursements() {
        log.info("Navigating to Disbursements menu");
        click(disbursementsMenu);
        wait.waitForPageLoad();
    }

    public void goToCaseManagement() {
        log.info("Navigating to Case Management");
        click(caseManagementMenu);
        wait.waitForPageLoad();
    }

    public void goToDisbursementProjections() {
        log.info("Navigating to Disbursement Projections");
        click(disbProjectionsMenu);
        wait.waitForPageLoad();
    }
}
