package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 4 — firstapproverDev
 * First Approver reviews and routes to Senior Manager.
 */
public class Step4_FirstApproverTest extends DisbursementBaseTest {

    @Test(description = "Stage 4: First Approver approves and routes to Senior Manager",
          groups = {"disbursement", "stage4"})
    public void stage4_firstApproverRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.first.approver.username");
        String password = ConfigReader.get("user.first.approver.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 4 — Logged in as: {}", username);

        // ── Resolve sequence number ────────────────────────────
        String sequenceNumber = ConfigReader.get("sequence.number", "").trim();
        if (sequenceNumber.isEmpty()) sequenceNumber = SharedTestData.getSequenceNumber();
        log.info("Stage 4 — Using sequence number: {}", sequenceNumber);

        // ── Open Case ──────────────────────────────────────────
        openCaseBySequenceNumber(sequenceNumber);

        // ── Approve and Route to Senior Manager ────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterFirstApproverComment(approvedComment);
        routing.selectOutcome("Approved route to senior manager");
        routing.clickRouteButton();

        log.info("Stage 4 COMPLETE — First Approver routed to Senior Manager | Sequence: {}", sequenceNumber);
    }
}
