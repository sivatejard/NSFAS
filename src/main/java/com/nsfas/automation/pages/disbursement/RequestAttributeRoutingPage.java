package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Request Attribute and Routing page — used by all approvers (Stages 1-8).
 * UPDATE locators after inspecting actual app HTML.
 */
public class RequestAttributeRoutingPage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By requestAttrRoutingTab = By.xpath("//a[contains(text(),'Request attribute') or contains(text(),'Routing') or contains(@href,'RequestAttribute')]");

    // Comment text areas — each role has its own field
    private final By financialCommentField   = By.xpath("//textarea[@id='FinancialComment' or contains(@name,'FinancialComment') or contains(@placeholder,'Financial')]");
    private final By firstApproverComment    = By.xpath("//textarea[@id='FirstApproverComment' or contains(@name,'FirstApprover') or contains(@placeholder,'First Approver')]");
    private final By seniorManagerComment    = By.xpath("//textarea[@id='FSManagerComment' or contains(@name,'SeniorManager') or contains(@placeholder,'Senior Manager')]");
    private final By cooComment              = By.xpath("//textarea[@id='COOComment' or contains(@name,'COO') or contains(@placeholder,'COO')]");
    private final By cfoComment              = By.xpath("//textarea[@id='CFOComment' or contains(@name,'CFO') or contains(@placeholder,'CFO')]");
    private final By ceoComment              = By.xpath("//textarea[@id='CEOComment' or contains(@name,'CEO') or contains(@placeholder,'CEO')]");

    // Generic comment field fallback (if all comments go to same field)
    private final By genericCommentField     = By.xpath("//textarea[contains(@name,'Comment') or contains(@id,'Comment') or contains(@placeholder,'comment')]");

    // Outcome / Route dropdown or radio buttons
    private final By outcomeDropdown         = By.xpath("//select[@id='Outcome' or contains(@name,'Outcome') or contains(@id,'Route')]");
    private final By outcomeRadioGroup       = By.xpath("//input[@type='radio' and (contains(@name,'Outcome') or contains(@name,'Route'))]");

    // Route button
    private final By routeButton             = By.xpath("//button[contains(text(),'Route') or @id='btnRoute' or contains(@class,'route-btn')]");

    // Success indicator after routing
    private final By routeSuccessMsg         = By.xpath("//*[contains(text(),'successfully') or contains(text(),'routed') or contains(@class,'success')]");

    public void clickRequestAttributeAndRouting() {
        log.info("Clicking Request Attribute and Routing tab");
        scrollToElement(requestAttrRoutingTab);
        click(requestAttrRoutingTab);
        wait.waitForPageLoad();
    }

    public void enterFinancialComment(String comment) {
        log.info("Entering financial comment: {}", comment);
        enterComment(financialCommentField, comment);
    }

    public void enterFirstApproverComment(String comment) {
        log.info("Entering first approver comment: {}", comment);
        enterComment(firstApproverComment, comment);
    }

    public void enterSeniorManagerComment(String comment) {
        log.info("Entering senior manager comment: {}", comment);
        enterComment(seniorManagerComment, comment);
    }

    public void enterCOOComment(String comment) {
        log.info("Entering COO comment: {}", comment);
        enterComment(cooComment, comment);
    }

    public void enterCFOComment(String comment) {
        log.info("Entering CFO comment: {}", comment);
        enterComment(cfoComment, comment);
    }

    public void enterCEOComment(String comment) {
        log.info("Entering CEO comment: {}", comment);
        enterComment(ceoComment, comment);
    }

    private void enterComment(By specificField, String comment) {
        if (isElementPresent(specificField)) {
            clearAndType(specificField, comment);
        } else if (isElementPresent(genericCommentField)) {
            clearAndType(genericCommentField, comment);
        } else {
            log.warn("Comment field not found — please update locator");
        }
    }

    public void selectOutcome(String outcomeText) {
        log.info("Selecting outcome: {}", outcomeText);
        if (isElementPresent(outcomeDropdown)) {
            selectByVisibleText(outcomeDropdown, outcomeText);
        } else {
            By radio = By.xpath("//input[@type='radio'][@value='" + outcomeText + "'] | " +
                    "//label[contains(text(),'" + outcomeText + "')]/preceding-sibling::input[@type='radio'] | " +
                    "//label[contains(text(),'" + outcomeText + "')]");
            click(radio);
        }
        log.info("Outcome selected: {}", outcomeText);
    }

    public void clickRouteButton() {
        log.info("Clicking Route button");
        scrollToElement(routeButton);
        click(routeButton);
        wait.waitForPageLoad();
        log.info("Route button clicked — case routed");
    }

    public boolean isRoutedSuccessfully() {
        return isElementPresent(routeSuccessMsg);
    }
}
