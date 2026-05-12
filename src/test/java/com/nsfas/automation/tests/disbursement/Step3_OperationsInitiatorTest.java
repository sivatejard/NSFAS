package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 3 — OperationsInitiator
 * Initiates disbursement and routes the case.
 */
public class Step3_OperationsInitiatorTest extends DisbursementBaseTest {

    @Test(description = "Stage 3: Operations Initiator initiates disbursement",
          groups = {"disbursement", "stage3"})
    public void stage3_operationsInitiatorRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.operations.initiator.username");
        String password = ConfigReader.get("user.operations.initiator.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 3 — Logged in as: {}", username);

        // ── Resolve sequence number ────────────────────────────
        String sequenceNumber = ConfigReader.get("sequence.number", "").trim();
        if (sequenceNumber.isEmpty()) sequenceNumber = SharedTestData.getSequenceNumber();
        log.info("Stage 3 — Using sequence number: {}", sequenceNumber);

        // ── Open Case ──────────────────────────────────────────
        openCaseBySequenceNumber(sequenceNumber);

        // ── Route with Disbursement Initiation outcome ─────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();
        routing.selectOutcome("Disbursement initiation");
        routing.clickRouteButton();

        log.info("Stage 3 COMPLETE — Disbursement initiated | Sequence: {}", sequenceNumber);
    }
}
