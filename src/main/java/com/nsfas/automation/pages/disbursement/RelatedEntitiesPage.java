package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Related Entities page — visible during Finance Reviewer stage (Stage 2).
 * Shows overall financial summary used for review.
 * UPDATE locators after inspecting actual app HTML.
 */
public class RelatedEntitiesPage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By relatedEntitiesTab      = By.xpath("//a[contains(text(),'Related Entities') or contains(@href,'RelatedEntities')]");
    private final By overallSummarySection   = By.xpath("//*[contains(text(),'Overall summary') or contains(@id,'OverallSummary')]");
    private final By noOfStudents            = By.xpath("//*[@id='noOfStudents' or contains(@class,'student-count') or contains(text(),'students')]/following-sibling::* | //td[@data-label='No. of Students']");
    private final By directPaymentTotal      = By.xpath("//*[@id='directPaymentTotal' or contains(@data-label,'Direct payment')]");
    private final By fnbTotal                = By.xpath("//*[@id='fnbTotal' or contains(@data-label,'FNB Total')]");
    private final By totalAmount             = By.xpath("//*[@id='totalAmount' or contains(@data-label,'Total amount')]");
    private final By summaryByInstitutionType = By.xpath("//*[contains(text(),'summary by institution type') or contains(@id,'SummaryByInstType')]");
    private final By summaryByInstitution    = By.xpath("//*[contains(text(),'summary by institution') and not(contains(text(),'type'))]");
    private final By summaryBySubFunders     = By.xpath("//*[contains(text(),'summary by subfunders') or contains(@id,'SummaryBySubFunder')]");
    private final By summaryByAcademicYear   = By.xpath("//*[contains(text(),'summary by academic year') or contains(@id,'SummaryByAcademicYear')]");

    public void clickRelatedEntities() {
        log.info("Clicking Related Entities tab");
        scrollToElement(relatedEntitiesTab);
        click(relatedEntitiesTab);
        wait.waitForPageLoad();
    }

    public boolean isOverallSummaryVisible() {
        return isDisplayed(overallSummarySection);
    }

    public String getNumberOfStudents() {
        return isElementPresent(noOfStudents) ? getText(noOfStudents) : "N/A";
    }

    public String getDirectPaymentTotal() {
        return isElementPresent(directPaymentTotal) ? getText(directPaymentTotal) : "N/A";
    }

    public String getFnbTotal() {
        return isElementPresent(fnbTotal) ? getText(fnbTotal) : "N/A";
    }

    public String getTotalAmount() {
        return isElementPresent(totalAmount) ? getText(totalAmount) : "N/A";
    }

    public void logSummary() {
        log.info("=== Financial Summary ===");
        log.info("No. of Students    : {}", getNumberOfStudents());
        log.info("Direct Payment Total: {}", getDirectPaymentTotal());
        log.info("FNB Total           : {}", getFnbTotal());
        log.info("Total Amount        : {}", getTotalAmount());
    }
}
