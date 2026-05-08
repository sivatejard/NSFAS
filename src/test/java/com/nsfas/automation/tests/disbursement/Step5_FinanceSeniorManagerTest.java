package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 5 — FinanceSeniorManagerDev
 * Senior Manager approves and routes to COO.
 */
public class Step5_FinanceSeniorManagerTest extends DisbursementBaseTest {

    @Test(description = "Stage 5: Finance Senior Manager approves and routes to COO",
          groups = {"disbursement", "stage5"},
          dependsOnGroups = {"stage4"})
    public void stage5_seniorManagerRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.senior.manager.username");
        String password = ConfigReader.get("user.finance.senior.manager.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 5 — Logged in as: {}", username);

        // ── Open Case ──────────────────────────────────────────
        String sequenceNumber = SharedTestData.getSequenceNumber();
        openCaseBySequenceNumber(sequenceNumber);

        // ── Approve and Route to COO ───────────────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterSeniorManagerComment(approvedComment);
        routing.selectOutcome("Approved route to COO");
        routing.clickRouteButton();

        log.info("Stage 5 COMPLETE — Senior Manager routed to COO | Sequence: {}", sequenceNumber);
    }
}
