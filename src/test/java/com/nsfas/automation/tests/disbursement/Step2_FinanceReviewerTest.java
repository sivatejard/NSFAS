package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
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
          groups = {"disbursement", "stage2"},
          dependsOnGroups = {"stage1"})
    public void stage2_financeReviewerApprove() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.reviewer.username");
        String password = ConfigReader.get("user.finance.reviewer.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 2 — Logged in as: {}", username);

        // ── Open Case ──────────────────────────────────────────
        String sequenceNumber = SharedTestData.getSequenceNumber();
        openCaseBySequenceNumber(sequenceNumber);

        // ── Review Related Entities (Financial Summary) ────────
        RelatedEntitiesPage relatedEntities = new RelatedEntitiesPage();
        relatedEntities.clickRelatedEntities();
        Assert.assertTrue(relatedEntities.isOverallSummaryVisible(),
                "Overall summary not visible in Related Entities");
        relatedEntities.logSummary();

        // ── Go to Routing and Approve ──────────────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterFinancialComment(approvedComment);
        routing.selectOutcome("APPROVE");
        routing.clickRouteButton();

        log.info("Stage 2 COMPLETE — Finance Reviewer approved | Sequence: {}", sequenceNumber);
    }
}
