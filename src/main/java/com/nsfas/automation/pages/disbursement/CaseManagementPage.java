package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Case Management — My Team Cases page (/casemanagement/myteamcases)
 *
 * Confirmed structure from live app:
 *  - DataTable id="dt-teams" with wrapper id="dt-teams_wrapper"
 *  - Search input inside id="dt-teams_filter"
 *  - Each row has "View" (casedetailsview) and "Edit" (casedetails) links
 *  - Columns: Request No | Created Date | Current Step | SLA Remaining |
 *             Status | Process Status | Process Reference | Workflow | View Request
 */
public class CaseManagementPage extends BasePage {

    private final By dtSearchInput   = By.cssSelector("#dt-teams_filter input[type='search']");
    private final By dataTable       = By.id("dt-teams");
    private final By loadingOverlay  = By.cssSelector(".dataTables_processing");
    private final By noRecordsMsg    = By.cssSelector("#dt-teams td.dataTables_empty");

    // Edit link navigates to /casemanagement/casedetails?id=<guid> — where routing is done
    private final By firstEditLink   = By.xpath("(//a[contains(@href,'casedetails') and not(contains(@href,'casedetailsview'))])[1]");

    private String getAppRoot() {
        return driver.getCurrentUrl().replaceAll("(https?://[^/]+).*", "$1");
    }

    public void navigateToMyTeamCases() {
        log.info("Navigating to My Team Cases");
        driver.get(getAppRoot() + "/casemanagement/myteamcases");
        wait.waitForPageLoad();
        waitForTable();
    }

    public void searchBySequenceNumber(String sequenceNumber) {
        log.info("Searching My Team Cases for: {}", sequenceNumber);
        wait.waitForVisibility(dtSearchInput).clear();
        type(dtSearchInput, sequenceNumber);
        waitForTable();
    }

    public void clickEditOnFirstResult() {
        log.info("Clicking Edit on first matching case");
        wait.waitForClickable(firstEditLink).click();
        wait.waitForPageLoad();
    }

    // Alias used by DisbursementBaseTest
    public void clickViewEdit() {
        clickEditOnFirstResult();
    }

    public boolean isCaseFound() {
        return isElementPresent(dataTable) && !isElementPresent(noRecordsMsg);
    }

    private void waitForTable() {
        try {
            wait.waitForInvisibility(loadingOverlay, 20);
        } catch (Exception ignored) {}
        wait.waitForPageLoad();
    }
}
