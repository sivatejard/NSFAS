package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Create Disbursement Schedule page — 9 form fields + submit.
 * UPDATE locators after inspecting actual app HTML.
 */
public class CreateSchedulePage extends BasePage {

    // ── Locators ── UPDATE after inspecting actual app HTML ──
    private final By createScheduleButton   = By.xpath("//a[contains(text(),'Create schedule') or contains(@href,'Create')]");

    // Field 1 — Institute Type dropdown
    private final By instituteTypeDropdown  = By.xpath("//select[@id='InstituteType' or contains(@name,'InstituteType')]");

    // Field 2 — Academic Year dropdown
    private final By academicYearDropdown   = By.xpath("//select[@id='AcademicYear' or contains(@name,'AcademicYear')]");

    // Field 3 — Institutions (multi-select / checkboxes)
    private final By institutionSelectAll   = By.xpath("//button[contains(text(),'Select all') or contains(@id,'SelectAllInstitution')]");
    private final By institutionDropdown    = By.xpath("//select[@id='Institutions'] | //div[contains(@id,'Institution')]");

    // Field 4 — Funder Type
    private final By funderTypeDropdown     = By.xpath("//select[@id='FunderType' or contains(@name,'FunderType')]");

    // Field 5 — Sub Funder
    private final By subFunderSelectAll     = By.xpath("//button[contains(text(),'Select all') and contains(following::*,'SubFunder')]");
    private final By subFunderDropdown      = By.xpath("//select[@id='SubFunder'] | //div[contains(@id,'SubFunder')]");

    // Field 6 — Disbursement Type
    private final By disbTypeSelectAll      = By.xpath("//button[contains(text(),'Select all') and contains(following::*,'DisbursementType')]");
    private final By disbTypeDropdown       = By.xpath("//select[@id='DisbursementType'] | //div[contains(@id,'DisbursementType')]");

    // Field 7 — Process Cycles
    private final By processCycleSelectAll  = By.xpath("//button[contains(text(),'Select all') and contains(following::*,'ProcessCycle')]");
    private final By processCycleDropdown   = By.xpath("//select[@id='ProcessCycles'] | //div[contains(@id,'ProcessCycle')]");

    // Field 8 — Allowance Types
    private final By allowanceSelectAll     = By.xpath("//button[contains(text(),'Select all') and contains(following::*,'Allowance')]");
    private final By allowanceDropdown      = By.xpath("//select[@id='AllowanceTypes'] | //div[contains(@id,'AllowanceType')]");

    // Field 9 — Project Run Date
    private final By projectRunDateInput    = By.xpath("//input[@id='ProjectRunDate' or contains(@name,'RunDate') or @type='date']");

    // Submit
    private final By createScheduleSubmit   = By.xpath("//button[contains(text(),'Create schedule') or @id='btnCreateSchedule']");

    // Success popup / notification
    private final By successPopup           = By.xpath("//*[contains(text(),'schedule created successfully') or contains(@class,'success') or contains(@class,'alert-success')]");
    private final By popupOkButton          = By.xpath("//button[contains(text(),'OK') or contains(text(),'Close') or contains(@class,'confirm')]");

    public void clickCreateSchedule() {
        log.info("Clicking Create Schedule button");
        click(createScheduleButton);
        wait.waitForPageLoad();
    }

    public void selectInstituteType(String type) {
        log.info("Selecting institute type: {}", type);
        selectByVisibleText(instituteTypeDropdown, type);
    }

    public void selectAcademicYear(String year) {
        log.info("Selecting academic year: {}", year);
        selectByVisibleText(academicYearDropdown, year);
    }

    public void selectAllInstitutions() {
        log.info("Selecting all institutions");
        click(institutionSelectAll);
    }

    public void selectInstitutionByName(String institutionName) {
        log.info("Selecting institution: {}", institutionName);
        By option = By.xpath("//option[contains(text(),'" + institutionName + "')]");
        click(option);
    }

    public void selectFunderType(String funderType) {
        log.info("Selecting funder type: {}", funderType);
        selectByVisibleText(funderTypeDropdown, funderType);
    }

    public void selectAllSubFunders() {
        log.info("Selecting all sub funders");
        click(subFunderSelectAll);
    }

    public void selectSubFunderByName(String subFunder) {
        log.info("Selecting sub funder: {}", subFunder);
        By option = By.xpath("//option[contains(text(),'" + subFunder + "')]");
        click(option);
    }

    public void selectAllDisbursementTypes() {
        log.info("Selecting all disbursement types");
        click(disbTypeSelectAll);
    }

    public void selectDisbursementType(String type) {
        log.info("Selecting disbursement type: {}", type);
        By option = By.xpath("//label[contains(text(),'" + type + "')]/input | //input[@value='" + type + "']");
        click(option);
    }

    public void selectAllProcessCycles() {
        log.info("Selecting all process cycles");
        click(processCycleSelectAll);
    }

    public void selectProcessCycle(String cycle) {
        log.info("Selecting process cycle: {}", cycle);
        By option = By.xpath("//label[contains(text(),'" + cycle + "')]/input | //option[contains(text(),'" + cycle + "')]");
        click(option);
    }

    public void selectAllAllowanceTypes() {
        log.info("Selecting all allowance types");
        click(allowanceSelectAll);
    }

    public void selectAllowanceTypes(String... allowances) {
        for (String allowance : allowances) {
            log.info("Selecting allowance: {}", allowance);
            By option = By.xpath("//label[contains(text(),'" + allowance + "')]/input | //option[contains(text(),'" + allowance + "')]");
            click(option);
        }
    }

    public void setProjectRunDate(String date) {
        log.info("Setting project run date: {}", date);
        type(projectRunDateInput, date);
    }

    public void submitCreateSchedule() {
        log.info("Submitting Create Schedule form");
        click(createScheduleSubmit);
    }

    public boolean isScheduleCreatedSuccessfully() {
        return wait.waitForVisibility(successPopup, 15) != null;
    }

    public String getSuccessMessage() {
        return getText(successPopup);
    }

    public void closeSuccessPopup() {
        if (isElementPresent(popupOkButton)) {
            click(popupOkButton);
        }
    }
}
