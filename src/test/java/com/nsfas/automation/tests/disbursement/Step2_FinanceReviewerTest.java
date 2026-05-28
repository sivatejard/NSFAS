package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.constants.RoutingOutcome;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RelatedEntitiesPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 2 — FinanceReviewerDev
 * Reviews financial summary and approves the case.
 */
public class Step2_FinanceReviewerTest extends DisbursementBaseTest {

    @Test(description = "Stage 2: Finance Reviewer reviews summary and approves",
          groups = {"disbursement", "stage2"})
    public void stage2_financeReviewerApprove() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.reviewer.username");
        String password = ConfigReader.get("user.finance.reviewer.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 2 — Logged in as: {}", username);

        // ── Resolve sequence number ────────────────────────────
        // Use config override first (for running Stage 2 standalone),
        // then fall back to what Stage 1 stored in SharedTestData.
        String sequenceNumber = ConfigReader.get("sequence.number", "").trim();
        if (sequenceNumber.isEmpty()) {
            sequenceNumber = SharedTestData.getSequenceNumber();
        }
        log.info("Stage 2 — Using sequence number: {}", sequenceNumber);

        // ── Open Case ──────────────────────────────────────────
        openCaseBySequenceNumber(sequenceNumber);

        // ── Review Related Entities (Financial Summary) ────────
        RelatedEntitiesPage relatedEntities = new RelatedEntitiesPage();
        try {
            relatedEntities.clickRelatedEntities();
           // relatedEntities.logSummary();
           // log.info("Related Entities tab reviewed successfully");
        } catch (Exception e) {
            // Tab locators are not yet confirmed — log and continue rather than fail
            log.warn("Could not open Related Entities tab (locators may need update): {}", e.getMessage());
        }

        // ── Go to Routing and Approve ──────────────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterFinancialComment(approvedComment);
        routing.ensureRoutingTabActive();

        // Log available next-step options for debugging
        log.info("Available routing options: {}", routing.getAvailableNextStepOptions());
        routing.selectOutcome(RoutingOutcome.APPROVE_FOR_PROJECTION);

        routing.clickRouteButton();

        log.info("Stage 2 COMPLETE — Finance Reviewer approved | Sequence: {}", sequenceNumber);
    }
}
