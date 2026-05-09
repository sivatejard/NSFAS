package com.nsfas.automation.pages.disbursement;

import com.nsfas.automation.pages.BasePage;
import org.openqa.selenium.By;

/**
 * Create Disbursement Schedule page — /CreateSchedule/Index
 *
 * All dropdowns use Select2. Direct Selenium selectByText does NOT work on them
 * because Select2 hides the native <select> (adds select2-hidden-accessible).
 * We use JavaScript to set values and trigger the change event so Select2 updates.
 *
 * For "Select All" multi-selects we click the dedicated Select All button inside
 * the container div named <fieldId>_select_all_container.
 */
public class CreateSchedulePage extends BasePage {

    // Confirmed element IDs from live app HTML inspection
    private static final String ID_INSTITUTION_TYPE   = "InstitutionType";
    private static final String ID_FUNDER_TYPE         = "FunderType";
    private static final String ID_YEAR                = "Year";
    private static final String ID_INSTITUTIONS        = "Institutions";
    private static final String ID_SUB_FUNDERS         = "SubFunders";
    private static final String ID_DISB_TYPES          = "DisbursementTypes";
    private static final String ID_PROCESS_CYCLES      = "ProcessCycles";
    private static final String ID_ALLOWANCE_TYPES     = "AllowanceTypes";

    private final By projectRunDateInput  = By.id("ProjectRunDate");
    private final By createScheduleSubmit = By.xpath("//button[@type='button' and normalize-space()='Create Schedule']");
    private final By successNotification  = By.cssSelector(".alert-success, .swal2-popup, .toast-success, [class*='success']");
    private final By popupOkButton        = By.cssSelector(".swal2-confirm, button.btn-success, button.confirm");

    // ── Select2 helpers ────────────────────────────────────────────────────────

    private void select2Single(String elementId, String visibleText) {
        log.info("Select2 single [{}] = '{}'", elementId, visibleText);
        String js = "var sel=document.getElementById('" + elementId + "');" +
                    "for(var i=0;i<sel.options.length;i++){" +
                    "  if(sel.options[i].text.trim()==='" + visibleText.replace("'", "\\'") + "'){" +
                    "    sel.value=sel.options[i].value; break;" +
                    "  }" +
                    "}" +
                    "if(typeof $!=='undefined'){$(sel).trigger('change');}";
        executeJS(js);
    }

    private void select2SelectAll(String elementId) {
        log.info("Select2 select-all [{}]", elementId);
        By selectAllBtn = By.cssSelector("#" + elementId + "_select_all_container button:first-child");
        if (isElementPresent(selectAllBtn)) {
            jsClick(selectAllBtn);
        } else {
            String js = "var sel=document.getElementById('" + elementId + "');" +
                        "for(var i=0;i<sel.options.length;i++){sel.options[i].selected=true;}" +
                        "if(typeof $!=='undefined'){$(sel).trigger('change');}";
            executeJS(js);
        }
    }

    private void select2MultiByText(String elementId, String visibleText) {
        log.info("Select2 multi [{}] += '{}'", elementId, visibleText);
        String js = "var sel=document.getElementById('" + elementId + "');" +
                    "for(var i=0;i<sel.options.length;i++){" +
                    "  if(sel.options[i].text.trim()==='" + visibleText.replace("'", "\\'") + "'){" +
                    "    sel.options[i].selected=true;" +
                    "  }" +
                    "}" +
                    "if(typeof $!=='undefined'){$(sel).trigger('change');}";
        executeJS(js);
    }

    // ── Public page methods ────────────────────────────────────────────────────

    public void selectInstitutionType(String type) {
        log.info("Selecting institution type: {}", type);
        select2Single(ID_INSTITUTION_TYPE, type);
    }

    public void selectFunderType(String funderType) {
        log.info("Selecting funder type: {}", funderType);
        select2Single(ID_FUNDER_TYPE, funderType);
    }

    public void selectAcademicYear(String year) {
        log.info("Selecting academic year: {}", year);
        select2Single(ID_YEAR, year);
    }

    public void selectAllInstitutions() {
        log.info("Selecting all institutions");
        select2SelectAll(ID_INSTITUTIONS);
    }

    public void selectInstitutionByName(String name) {
        select2MultiByText(ID_INSTITUTIONS, name);
    }

    public void selectAllSubFunders() {
        log.info("Selecting all sub funders");
        select2SelectAll(ID_SUB_FUNDERS);
    }

    public void selectSubFunderByName(String name) {
        select2MultiByText(ID_SUB_FUNDERS, name);
    }

    public void selectAllDisbursementTypes() {
        log.info("Selecting all disbursement types");
        select2SelectAll(ID_DISB_TYPES);
    }

    public void selectDisbursementTypeByName(String name) {
        select2MultiByText(ID_DISB_TYPES, name);
    }

    public void selectAllProcessCycles() {
        log.info("Selecting all process cycles");
        select2SelectAll(ID_PROCESS_CYCLES);
    }

    public void selectProcessCycleByName(String name) {
        select2MultiByText(ID_PROCESS_CYCLES, name);
    }

    public void selectAllAllowanceTypes() {
        log.info("Selecting all allowance types");
        select2SelectAll(ID_ALLOWANCE_TYPES);
    }

    public void selectAllowanceTypeByName(String name) {
        select2MultiByText(ID_ALLOWANCE_TYPES, name);
    }

    public void setProjectRunDate(String date) {
        log.info("Setting project run date: {}", date);
        clearAndType(projectRunDateInput, date);
    }

    public void submitCreateSchedule() {
        log.info("Submitting Create Schedule form");
        jsClick(createScheduleSubmit);
    }

    public boolean isScheduleCreatedSuccessfully() {
        try {
            wait.waitForVisibility(successNotification, 20);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        if (isElementPresent(successNotification)) {
            return getText(successNotification);
        }
        return "";
    }

    public void closeSuccessPopup() {
        if (isElementPresent(popupOkButton)) {
            click(popupOkButton);
        }
    }
}
