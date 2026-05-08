package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Disbursement Projections page — shows projection results and sequence number.
 * UPDATE locators after inspecting actual app HTML.
 */
public class DisbursementProjectionsPage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By showResultButton      = By.xpath("//button[contains(text(),'Show result') or contains(text(),'Show Result') or @id='btnShowResult']");
    private final By sequenceNumberCell    = By.xpath("//table//td[contains(@class,'sequence') or contains(@class,'seqNo')] | //td[@data-label='Sequence Number']");
    private final By firstRowSeqNumber     = By.xpath("(//table//tbody//tr[1]//td[1])[1]");
    private final By projectionResultTable = By.xpath("//table[contains(@id,'projection') or contains(@class,'projection')]");
    private final By loadingSpinner        = By.xpath("//*[contains(@class,'spinner') or contains(@class,'loading')]");

    public void clickShowResult() {
        log.info("Clicking Show Result button");
        click(showResultButton);
        waitForResultsToLoad();
        log.info("Results loaded");
    }

    private void waitForResultsToLoad() {
        if (isElementPresent(loadingSpinner)) {
            wait.waitForInvisibility(loadingSpinner, 60);
        }
        wait.waitForPageLoad();
    }

    public String captureSequenceNumber() {
        wait.waitForVisibility(projectionResultTable, 30);
        String seqNo = "";

        if (isElementPresent(sequenceNumberCell)) {
            seqNo = getText(sequenceNumberCell).trim();
        } else {
            // fallback: grab first cell of first data row
            seqNo = getText(firstRowSeqNumber).trim();
        }

        log.info("Captured projection sequence number: {}", seqNo);
        return seqNo;
    }

    public boolean isProjectionResultDisplayed() {
        return isElementPresent(projectionResultTable);
    }
}
