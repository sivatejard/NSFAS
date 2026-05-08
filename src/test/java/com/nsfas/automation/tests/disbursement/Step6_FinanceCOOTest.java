package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 6 — FinanceCOODev
 * COO approves and routes to CFO.
 */
public class Step6_FinanceCOOTest extends DisbursementBaseTest {

    @Test(description = "Stage 6: Finance COO approves and routes to CFO",
          groups = {"disbursement", "stage6"},
          dependsOnGroups = {"stage5"})
    public void stage6_cooRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.coo.username");
        String password = ConfigReader.get("user.finance.coo.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 6 — Logged in as: {}", username);

        // ── Open Case ──────────────────────────────────────────
        String sequenceNumber = SharedTestData.getSequenceNumber();
        openCaseBySequenceNumber(sequenceNumber);

        // ── Approve and Route to CFO ───────────────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterCOOComment(approvedComment);
        routing.selectOutcome("Approved route to CFO");
        routing.clickRouteButton();

        log.info("Stage 6 COMPLETE — COO routed to CFO | Sequence: {}", sequenceNumber);
    }
}
