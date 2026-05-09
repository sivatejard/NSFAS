package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Disbursement Projections page — /DisbursementProjections/Index
 *
 * Confirmed from live app:
 *  - Filter fields: InstitutionType (Select2), ProjectionStatus (Select2),
 *    ProjectionRunFromDate, ProjectionRunToDate (date inputs)
 *  - Filter/Show button: <input type='button' class='btn btn-primary'>
 *  - Results displayed in a DataTable after filtering
 *
 * "Sequence number" = the projection reference ID in the first data column
 * of the first result row (used to locate the corresponding case).
 */
public class DisbursementProjectionsPage extends BasePage {

    // Filter area — confirmed element IDs
    private final By institutionTypeFilter  = By.id("InstitutionType");
    private final By statusFilter           = By.id("ProjectionStatus");
    private final By fromDateFilter         = By.id("ProjectionRunFromDate");
    private final By toDateFilter           = By.id("ProjectionRunToDate");

    // "Show Result" / filter trigger button (type='button', class contains btn-primary)
    private final By showResultButton       = By.cssSelector("#kt_content_container input[type='button'].btn-primary, #kt_content input[type='button'].btn-primary, input.btn-primary[type='button']");

    // Results table
    private final By projectionTable        = By.cssSelector("table.dataTable, table[id*='dt'], table[id*='projection']");
    private final By loadingOverlay         = By.cssSelector(".dataTables_processing, .spinner-border");

    // Sequence number — first td of first body row (projection reference/ID column)
    private final By firstRowFirstCell      = By.cssSelector("table tbody tr:first-child td:first-child");

    public void clickShowResult() {
        log.info("Clicking Show Result / filter button");
        jsClick(showResultButton);
        waitForResults();
        log.info("Results loaded");
    }

    public String captureSequenceNumber() {
        waitForResults();
        String seqNo = "";
        try {
            seqNo = getText(firstRowFirstCell).trim();
        } catch (Exception e) {
            log.warn("Could not read sequence number from first row: {}", e.getMessage());
        }
        log.info("Captured projection sequence number: {}", seqNo);
        return seqNo;
    }

    public boolean isProjectionResultDisplayed() {
        return isElementPresent(projectionTable);
    }

    private void waitForResults() {
        try {
            wait.waitForInvisibility(loadingOverlay, 60);
        } catch (Exception ignored) {}
        wait.waitForPageLoad();
    }
}
