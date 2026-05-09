package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Request Attributes & Routing tab on the case details page
 * (/casemanagement/casedetails?id=<guid>)
 *
 * Confirmed from live app HTML inspection:
 *  - Tab link uses Bootstrap 4 data-toggle="tab" pointing to #tab_default-1
 *  - Comment fields are <input class='qform form-control'> inside named accordion sections
 *  - Outcome/next step: <select id='NextStepId'>
 *  - Route: <button class='btn btn-primary btn-block'>Route</button>
 */
public class RequestAttributeRoutingPage extends BasePage {

    // Tab link — "Request Attributes & Routing" is the second nav-link
    private final By routingTabLink = By.xpath(
        "//a[@data-toggle='tab' and contains(normalize-space(),'Request Attributes')]");

    // Comment inputs inside accordion sections — each section has a predictable accordion div
    // Section names from live app: FinanceReview, First Approver note, FSManager, COO, CFO, CEO
    // The collapse divs' ids match the data-target on the button headers.
    // We locate inputs relative to the section button so it is resilient even if div IDs change.
    private By commentInSection(String sectionButtonText) {
        return By.xpath(
            "//button[@type='button' and normalize-space()='" + sectionButtonText + "']" +
            "/ancestor::div[contains(@class,'accordion')]" +
            "/following-sibling::div[contains(@class,'collapse')]" +
            "//input[contains(@class,'qform')]");
    }

    // Routing controls at bottom of the tab pane
    private final By nextStepDropdown = By.id("NextStepId");
    private final By routeButton      = By.xpath(
        "//button[@type='button' and normalize-space()='Route']");
    private final By saveButton       = By.xpath(
        "//button[@type='button' and normalize-space()='Save']");
    private final By statusDropdown   = By.id("Status");

    // ── Navigation ────────────────────────────────────────────────────────────

    public void clickRequestAttributeAndRouting() {
        log.info("Clicking 'Request Attributes & Routing' tab");
        scrollToElement(routingTabLink);
        jsClick(routingTabLink);
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        wait.waitForPageLoad();
    }

    // ── Comment entry per approver role ───────────────────────────────────────

    public void enterFinanceComment(String comment) {
        log.info("Entering Finance Review comment");
        enterCommentInSection("FinanceReview", comment);
    }

    public void enterFirstApproverComment(String comment) {
        log.info("Entering First Approver comment");
        enterCommentInSection("First Approver note", comment);
    }

    public void enterSeniorManagerComment(String comment) {
        log.info("Entering Senior Manager comment");
        enterCommentInSection("FSManager", comment);
    }

    public void enterCOOComment(String comment) {
        log.info("Entering COO comment");
        enterCommentInSection("COO", comment);
    }

    public void enterCFOComment(String comment) {
        log.info("Entering CFO comment");
        enterCommentInSection("CFO", comment);
    }

    public void enterCEOComment(String comment) {
        log.info("Entering CEO comment");
        enterCommentInSection("CEO", comment);
    }

    private void enterCommentInSection(String sectionName, String comment) {
        By field = commentInSection(sectionName);
        if (isElementPresent(field)) {
            clearAndType(field, comment);
            click(saveButton);
            wait.waitForPageLoad();
        } else {
            log.warn("Comment field for section '{}' not found — section may not be visible", sectionName);
        }
    }

    // ── Routing outcome ────────────────────────────────────────────────────────

    public void selectNextStep(String stepText) {
        log.info("Selecting next step: {}", stepText);
        selectByVisibleText(nextStepDropdown, stepText);
    }

    public void clickRouteButton() {
        log.info("Clicking Route button");
        scrollToElement(routeButton);
        jsClick(routeButton);
        wait.waitForPageLoad();
        log.info("Route button clicked — case routed to next step");
    }

    public String getAvailableNextStep() {
        if (isElementPresent(nextStepDropdown)) {
            return getText(nextStepDropdown).trim();
        }
        return "";
    }

    // Aliases for backward compatibility with existing test code
    public void enterFinancialComment(String comment) {
        enterFinanceComment(comment);
    }

    public void selectOutcome(String outcomeText) {
        selectNextStep(outcomeText);
    }

    public boolean isRoutingTabActive() {
        try {
            String cls = driver.findElement(routingTabLink).getAttribute("class");
            return cls != null && cls.contains("active");
        } catch (Exception e) {
            return false;
        }
    }
}
