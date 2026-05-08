package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.*;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 1 — OperationsDev
 * Creates disbursement schedule, retrieves projection sequence number,
 * routes the case to Finance Review.
 */
public class Step1_OperationsDevTest extends DisbursementBaseTest {

    @Test(description = "Stage 1: Create disbursement schedule and route to Finance Review",
          groups = {"disbursement", "stage1", "smoke"})
    public void stage1_createScheduleAndRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.operations.dev.username");
        String password = ConfigReader.get("user.operations.dev.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 1 — Logged in as: {}", username);

        // ── Navigate to Disbursements ──────────────────────────
        navigationPage().goToDisbursements();

        // ── Create Schedule ────────────────────────────────────
        CreateSchedulePage schedulePage = new CreateSchedulePage();
        schedulePage.clickCreateSchedule();

        schedulePage.selectInstituteType(ConfigReader.get("schedule.institute.type", "All institutions"));
        schedulePage.selectAcademicYear(ConfigReader.get("schedule.academic.year", "2025"));
        schedulePage.selectAllInstitutions();
        schedulePage.selectFunderType(ConfigReader.get("schedule.funder.type", "DHET"));
        schedulePage.selectAllSubFunders();
        schedulePage.selectAllDisbursementTypes();
        schedulePage.selectAllProcessCycles();
        schedulePage.selectAllAllowanceTypes();

        String runDate = com.nsfas.automation.utils.TestDataUtils.getCurrentDate("dd-MM-yyyy");
        schedulePage.setProjectRunDate(runDate);

        schedulePage.submitCreateSchedule();

        Assert.assertTrue(schedulePage.isScheduleCreatedSuccessfully(),
                "Schedule was not created successfully — popup not visible");
        log.info("Schedule created successfully");
        schedulePage.closeSuccessPopup();

        // ── Get Projection Sequence Number ─────────────────────
        navigationPage().goToDisbursementProjections();
        DisbursementProjectionsPage projectionsPage = new DisbursementProjectionsPage();
        projectionsPage.clickShowResult();

        Assert.assertTrue(projectionsPage.isProjectionResultDisplayed(),
                "Projection results not displayed after clicking Show Result");

        String sequenceNumber = projectionsPage.captureSequenceNumber();
        Assert.assertFalse(sequenceNumber.isEmpty(),
                "Projection sequence number is empty — cannot proceed to next stages");

        SharedTestData.setSequenceNumber(sequenceNumber);
        log.info("Projection sequence number captured and stored: {}", sequenceNumber);

        // ── Route Case to Finance Review ───────────────────────
        openCaseBySequenceNumber(sequenceNumber);

        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();
        routing.clickRouteButton();

        log.info("Stage 1 COMPLETE — Case routed to Finance Review | Sequence: {}", sequenceNumber);
    }
}
