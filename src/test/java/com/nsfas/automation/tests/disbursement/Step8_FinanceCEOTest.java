package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.constants.RoutingOutcome;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 8 — FinanceCEODev
 * CEO gives final approval and generates payment files.
 */
public class Step8_FinanceCEOTest extends DisbursementBaseTest {

    @Test(description = "Stage 8: Finance CEO final approval — Generate Payment Files",
          groups = {"disbursement", "stage8"})
    public void stage8_ceoFinalApprovalAndGeneratePayment() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.ceo.username");
        String password = ConfigReader.get("user.finance.ceo.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 8 — Logged in as: {}", username);

        // ── Resolve sequence number ────────────────────────────
        String sequenceNumber = ConfigReader.get("sequence.number", "").trim();
        if (sequenceNumber.isEmpty()) sequenceNumber = SharedTestData.getSequenceNumber();
        log.info("Stage 8 — Using sequence number: {}", sequenceNumber);

        // ── Open Case ──────────────────────────────────────────
        openCaseBySequenceNumber(sequenceNumber);

        // ── Final Approval — Generate Payment Files ────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterCEOComment(approvedComment);
        routing.ensureRoutingTabActive();
        routing.selectOutcome(RoutingOutcome.GENERATE_PAYMENT_FILES);
        routing.clickRouteButton();

        log.info("Stage 8 COMPLETE — CEO final approval done. Payment files generated. | Sequence: {}",
                sequenceNumber);
        log.info("====== FULL DISBURSEMENT E2E FLOW COMPLETE ======");
    }
}
