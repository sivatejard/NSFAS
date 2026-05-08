package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Case Management page — My Team Cases, search, and view/edit.
 * UPDATE locators after inspecting actual app HTML.
 */
public class CaseManagementPage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By myTeamCasesTab      = By.xpath("//a[contains(text(),'My Team') or contains(@href,'MyTeam') or contains(text(),'My team cases')]");
    private final By searchInput         = By.xpath("//input[@type='search' or contains(@placeholder,'Search') or @id='searchInput']");
    private final By searchButton        = By.xpath("//button[contains(text(),'Search') or @type='submit' and contains(@class,'search')]");
    private final By viewEditButton      = By.xpath("(//a[contains(text(),'View') or contains(text(),'Edit') or contains(text(),'View/Edit')])[1]");
    private final By caseTable           = By.xpath("//table[contains(@id,'case') or contains(@class,'case')]");
    private final By firstCaseRow        = By.xpath("//table//tbody//tr[1]");
    private final By noRecordsMsg        = By.xpath("//*[contains(text(),'No records') or contains(text(),'No data')]");
    private final By loadingSpinner      = By.xpath("//*[contains(@class,'spinner') or contains(@class,'loading')]");

    public void clickMyTeamCases() {
        log.info("Clicking My Team Cases tab");
        click(myTeamCasesTab);
        wait.waitForPageLoad();
        waitForTableToLoad();
    }

    public void searchBySequenceNumber(String sequenceNumber) {
        log.info("Searching for sequence number: {}", sequenceNumber);
        wait.waitForVisibility(searchInput).clear();
        type(searchInput, sequenceNumber);

        if (isElementPresent(searchButton)) {
            click(searchButton);
        } else {
            pressEnter(searchInput);
        }
        waitForTableToLoad();
        log.info("Search complete for: {}", sequenceNumber);
    }

    public void clickViewEdit() {
        log.info("Clicking View/Edit on case");
        wait.waitForClickable(viewEditButton).click();
        wait.waitForPageLoad();
    }

    public boolean isCaseFound() {
        return isElementPresent(caseTable) && !isElementPresent(noRecordsMsg);
    }

    private void waitForTableToLoad() {
        if (isElementPresent(loadingSpinner)) {
            wait.waitForInvisibility(loadingSpinner, 30);
        }
        wait.waitForPageLoad();
    }
}
