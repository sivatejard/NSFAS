package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Handles sidebar navigation. All links confirmed from live app inspection.
 * Sidebar links have empty text but stable href attributes, so we use href-based locators.
 * JS click is used as fallback since the sidebar can be in collapsed state.
 */
public class NavigationPage extends BasePage {

    // Confirmed from live app — sidebar link hrefs
    private final By createScheduleLink      = By.cssSelector("a[href='/CreateSchedule/Index']");
    private final By disbProjectionsLink     = By.cssSelector("a[href='/DisbursementProjections/Index']");
    private final By consolidatedRemitLink   = By.cssSelector("a[href='/DisbursementProjections/ConsolidatedRemittances']");
    private final By myTeamCasesLink         = By.cssSelector("a[href='/casemanagement/myteamcases']");
    private final By myCasesLink             = By.cssSelector("a[href='/casemanagement/mycases']");
    private final By caseSearchLink          = By.cssSelector("a[href='/casemanagement/casesearch']");

    private String getAppRoot() {
        String url = driver.getCurrentUrl();
        return url.replaceAll("(https?://[^/]+).*", "$1");
    }

    public void goToCreateSchedule() {
        log.info("Navigating to Create Schedule");
        try {
            jsClick(createScheduleLink);
        } catch (Exception e) {
            driver.get(getAppRoot() + "/CreateSchedule/Index");
        }
        wait.waitForPageLoad();
    }

    public void goToDisbursementProjections() {
        log.info("Navigating to Disbursement Projections");
        try {
            jsClick(disbProjectionsLink);
        } catch (Exception e) {
            driver.get(getAppRoot() + "/DisbursementProjections/Index");
        }
        wait.waitForPageLoad();
    }

    public void goToMyTeamCases() {
        log.info("Navigating to My Team Cases");
        try {
            jsClick(myTeamCasesLink);
        } catch (Exception e) {
            driver.get(getAppRoot() + "/casemanagement/myteamcases");
        }
        wait.waitForPageLoad();
    }

    public void goToMyCases() {
        log.info("Navigating to My Cases");
        try {
            jsClick(myCasesLink);
        } catch (Exception e) {
            driver.get(getAppRoot() + "/casemanagement/mycases");
        }
        wait.waitForPageLoad();
    }

    public void goToCaseSearch() {
        log.info("Navigating to Case Search");
        try {
            jsClick(caseSearchLink);
        } catch (Exception e) {
            driver.get(getAppRoot() + "/casemanagement/casesearch");
        }
        wait.waitForPageLoad();
    }

    // Convenience aliases kept for backward compatibility
    public void goToDisbursements() {
        goToDisbursementProjections();
    }

    public void goToCaseManagement() {
        goToMyTeamCases();
    }
}
