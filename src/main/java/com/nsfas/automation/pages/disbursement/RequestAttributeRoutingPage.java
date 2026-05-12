package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

    // Confirmed from live app inspection — all sections are Bootstrap accordions.
    // Section names: "FinanceReview", "Operations", "First Approver note",
    //                "FSManager", "COO", "CFO", "CEO"
    private By commentInSection(String sectionName) {
        return By.xpath(
            "//h2[normalize-space(.)='" + sectionName + "']/following::input[1]"
        );
    }

    private By accordionButtonFor(String sectionName) {
        return By.xpath(
            "//button[contains(@class,'accordion-button') and normalize-space()='" + sectionName + "']"
        );
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
        pause();
        log.info("Clicking 'Request Attributes & Routing' tab");
        scrollToElement(routingTabLink);
        jsClick(routingTabLink);
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        wait.waitForPageLoad();
        wait.waitForLoadingOverlay(60);
    }

    // ── Comment entry per approver role ───────────────────────────────────────

    public void enterFinanceComment(String comment) {
        log.info("Entering Finance Reviewer comment");
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
        pause();
        log.info("Entering comment in section: '{}'", sectionName);
        // Expand the accordion section if it is currently collapsed
        By btn = accordionButtonFor(sectionName);
        if (isElementPresent(btn)) {
            WebElement btnEl = driver.findElement(btn);
            String expanded = btnEl.getAttribute("aria-expanded");
            if (!"true".equals(expanded)) {
                log.info("Accordion '{}' is collapsed — clicking to expand", sectionName);
                jsClick(btnEl);
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        } else {
            log.warn("Accordion button for section '{}' not found", sectionName);
        }
        By field = commentInSection(sectionName);
        if (isElementPresent(field)) {
            scrollToElement(field);
            clearAndType(field, comment);
            pause();
            // Overlay can appear after typing (onchange AJAX) — wait before clicking Save
            wait.waitForLoadingOverlay(60);
            scrollToElement(saveButton);
            jsClick(saveButton);
            wait.waitForPageLoad();
            wait.waitForLoadingOverlay(60);
        } else {
            log.warn("Comment input for section '{}' NOT FOUND", sectionName);
        }
    }

    // ── Routing outcome ────────────────────────────────────────────────────────

    public void selectNextStep(String stepText) {
        log.info("Selecting next step: {}", stepText);
        selectByVisibleText(nextStepDropdown, stepText);
    }

    public void clickRouteButton() {
        pause();
        log.info("Clicking Route button");
        scrollToBottom();
        wait.waitForLoadingOverlay(60);
        scrollToElement(routeButton);
        pause();
        jsClick(routeButton);
        wait.waitForPageLoad();
        wait.waitForLoadingOverlay(120);
        log.info("Route button clicked — case routed to next step");
    }

    public String getAvailableNextStep() {
        if (isElementPresent(nextStepDropdown)) {
            return getText(nextStepDropdown).trim();
        }
        return "";
    }

    /** Returns all option texts from the NextStepId dropdown, comma-separated. */
    public String getAvailableNextStepOptions() {
        if (!isElementPresent(nextStepDropdown)) return "(NextStepId dropdown not found)";
        try {
            return String.join(", ", getAllDropdownOptions(nextStepDropdown));
        } catch (Exception e) {
            return "(could not read options: " + e.getMessage() + ")";
        }
    }

    // Aliases for backward compatibility with existing test code
    public void enterFinancialComment(String comment) {
        enterFinanceComment(comment);
    }

    /**
     * Scrolls to the Pick an Outcome dropdown, selects by case-insensitive partial match,
     * then waits for any overlay before proceeding.
     */
    public void selectOutcome(String outcomeText) {
        pause();
        if (!isElementPresent(nextStepDropdown)) {
            log.warn("NextStepId dropdown not present — skipping outcome selection");
            return;
        }
        scrollToBottom();
        wait.waitForLoadingOverlay(60);
        scrollToElement(nextStepDropdown);
        pause();
        String js =
            "var sel=document.getElementById('NextStepId');" +
            "var target='" + outcomeText.replace("'", "\\'").toLowerCase() + "';" +
            "for(var i=0;i<sel.options.length;i++){" +
            "  if(sel.options[i].text.trim().toLowerCase().indexOf(target)>=0){" +
            "    sel.value=sel.options[i].value;" +
            "    sel.dispatchEvent(new Event('change',{bubbles:true}));" +
            "    return sel.options[i].text;" +
            "  }" +
            "}" +
            "return null;";
        Object matched = executeJS(js);
        if (matched == null) {
            log.warn("selectOutcome: no option matched '{}' — available: {}", outcomeText, getAvailableNextStepOptions());
        } else {
            log.info("selectOutcome: selected '{}'", matched);
        }
        pause();
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
