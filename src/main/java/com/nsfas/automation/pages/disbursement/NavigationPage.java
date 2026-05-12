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

    private void navigate(String path) {
        String url = getAppRoot() + path;
        log.info("Navigating to: {}", url);
        driver.get(url);          // blocks until page load timeout (120s from config)
        wait.waitForLoadingOverlay(120);  // also wait for any post-load spinner
    }

    public void goToCreateSchedule() {
        log.info("Navigating to Create Schedule");
        navigate("/CreateSchedule/Index");
    }

    public void goToDisbursementProjections() {
        log.info("Navigating to Disbursement Projections");
        navigate("/DisbursementProjections/Index");
    }

    public void goToMyTeamCases() {
        log.info("Navigating to My Team Cases");
        navigate("/casemanagement/myteamcases");
    }

    public void goToMyCases() {
        log.info("Navigating to My Cases");
        navigate("/casemanagement/mycases");
    }

    public void goToCaseSearch() {
        log.info("Navigating to Case Search");
        navigate("/casemanagement/casesearch");
    }

    // Convenience aliases kept for backward compatibility
    public void goToDisbursements() {
        goToDisbursementProjections();
    }

    public void goToCaseManagement() {
        goToMyTeamCases();
    }
}
