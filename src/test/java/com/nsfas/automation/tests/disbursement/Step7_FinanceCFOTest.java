package com.nsfas.automation.tests.disbursement;

import com.nsfas.automation.config.ConfigReader;
import com.nsfas.automation.pages.disbursement.LoginPage;
import com.nsfas.automation.pages.disbursement.RequestAttributeRoutingPage;
import com.nsfas.automation.utils.SharedTestData;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Stage 7 — FinanceCFODev
 * CFO approves and routes to CEO.
 */
public class Step7_FinanceCFOTest extends DisbursementBaseTest {

    @Test(description = "Stage 7: Finance CFO approves and routes to CEO",
          groups = {"disbursement", "stage7"},
          dependsOnGroups = {"stage6"})
    public void stage7_cfoRoute() {

        // ── Login ──────────────────────────────────────────────
        String username = ConfigReader.get("user.finance.cfo.username");
        String password = ConfigReader.get("user.finance.cfo.password");
        loginPage().loginAs(username, password);
        Assert.assertTrue(new LoginPage().isLoginSuccessful(),
                "Login failed for user: " + username);
        log.info("Stage 7 — Logged in as: {}", username);

        // ── Open Case ──────────────────────────────────────────
        String sequenceNumber = SharedTestData.getSequenceNumber();
        openCaseBySequenceNumber(sequenceNumber);

        // ── Approve and Route to CEO ───────────────────────────
        RequestAttributeRoutingPage routing = routingPage();
        routing.clickRequestAttributeAndRouting();

        String approvedComment = ConfigReader.get("comment.approved", "Approved");
        routing.enterCFOComment(approvedComment);
        routing.selectOutcome("Approved route to CEO");
        routing.clickRouteButton();

        log.info("Stage 7 COMPLETE — CFO routed to CEO | Sequence: {}", sequenceNumber);
    }
}
