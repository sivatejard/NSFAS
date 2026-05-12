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
    // Success popup after schedule creation — SweetAlert2 or similar
    private final By successNotification  = By.cssSelector(".swal2-popup, .swal2-success, .alert-success, .toast-success");
    private final By popupOkButton        = By.cssSelector(".swal2-confirm, .swal2-popup button, button.confirm");

    // ── Select2 helpers ────────────────────────────────────────────────────────

    private void select2Single(String elementId, String visibleText) {
        log.info("Select2 single [{}] = '{}'", elementId, visibleText);
        // Find option value by case-insensitive text match, then use Select2 val() API
        String escaped = visibleText.replace("'", "\\'").toLowerCase();
        String js = "var sel=document.getElementById('" + elementId + "');" +
                    "var matchedVal=null;" +
                    "for(var i=0;i<sel.options.length;i++){" +
                    "  if(sel.options[i].text.trim().toLowerCase()==='" + escaped + "'){" +
                    "    matchedVal=sel.options[i].value; break;" +
                    "  }" +
                    "}" +
                    "if(matchedVal!==null && typeof $!=='undefined'){" +
                    "  $('#" + elementId + "').val(matchedVal).trigger('change');" +
                    "} else if(matchedVal!==null){" +
                    "  sel.value=matchedVal;" +
                    "}" +
                    "return matchedVal;";
        Object result = executeJS(js);
        if (result == null) {
            log.warn("Select2 [{}]: no option matched '{}' — value unchanged", elementId, visibleText);
        }
    }

    private void select2SelectAll(String elementId) {
        log.info("Select2 select-all [{}]", elementId);
        // Use Select2's own API: collect all option values then set + trigger change.
        // This is the only reliable way to make Select2 reflect the selection visually.
        String js = "var sel=document.getElementById('" + elementId + "');" +
                    "var vals=[];" +
                    "for(var i=0;i<sel.options.length;i++){vals.push(sel.options[i].value);}" +
                    "if(typeof $!=='undefined'){$('#" + elementId + "').val(vals).trigger('change');}" +
                    "else{for(var j=0;j<sel.options.length;j++){sel.options[j].selected=true;}}" +
                    "return vals.length;";
        Object count = executeJS(js);
        log.info("Select2 select-all [{}]: selected {} options", elementId, count);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
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
        pause();
        log.info("Selecting institution type: {}", type);
        select2Single(ID_INSTITUTION_TYPE, type);
        waitForAjax();  // funInstitution() fires — wait for Institutions list to load
    }

    public void selectFunderType(String funderType) {
        pause();
        log.info("Selecting funder type: {}", funderType);
        select2Single(ID_FUNDER_TYPE, funderType);
        waitForAjax();  // funSubFunder() fires — wait for SubFunders to load
    }

    public void selectAcademicYear(String year) {
        pause();
        log.info("Selecting academic year: {}", year);
        select2Single(ID_YEAR, year);
        waitForAjax();  // funYearChanged() fires — wait for sub-funder refresh
    }

    private void waitForAjax() {
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        wait.waitForPageLoad();
        wait.waitForLoadingOverlay(60);  // AJAX spinner after dropdown changes
    }

    public void selectAllInstitutions() {
        pause();
        log.info("Selecting all institutions");
        select2SelectAll(ID_INSTITUTIONS);
    }

    /**
     * Picks {@code count} institutions at random from the available options
     * and selects them via the Select2 jQuery API.
     */
    public void selectRandomInstitutions(int count) {
        pause();
        log.info("Selecting {} random institution(s)", count);
        String js =
            "var sel = document.getElementById('" + ID_INSTITUTIONS + "');" +
            "var opts = [];" +
            "for(var i=0;i<sel.options.length;i++){" +
            "  if(sel.options[i].value) opts.push(sel.options[i].value);" +
            "}" +
            "for(var i=opts.length-1;i>0;i--){" +
            "  var j=Math.floor(Math.random()*(i+1));" +
            "  var t=opts[i]; opts[i]=opts[j]; opts[j]=t;" +
            "}" +
            "var chosen=opts.slice(0," + count + ");" +
            "if(typeof $!=='undefined'){$('#" + ID_INSTITUTIONS + "').val(chosen).trigger('change');}" +
            "return chosen;";
        Object chosen = executeJS(js);
        log.info("Random institutions selected: {}", chosen);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    public void selectInstitutionByName(String name) {
        pause();
        select2MultiByText(ID_INSTITUTIONS, name);
    }

    public void selectAllSubFunders() {
        pause();
        log.info("Selecting all sub funders");
        select2SelectAll(ID_SUB_FUNDERS);
    }

    public void selectSubFunderByName(String name) {
        pause();
        select2MultiByText(ID_SUB_FUNDERS, name);
    }

    public void selectAllDisbursementTypes() {
        pause();
        log.info("Selecting all disbursement types");
        select2SelectAll(ID_DISB_TYPES);
    }

    public void selectDisbursementTypeByName(String name) {
        pause();
        select2MultiByText(ID_DISB_TYPES, name);
    }

    public void selectAllProcessCycles() {
        pause();
        log.info("Selecting all process cycles");
        select2SelectAll(ID_PROCESS_CYCLES);
    }

    public void selectProcessCycleByName(String name) {
        pause();
        select2MultiByText(ID_PROCESS_CYCLES, name);
    }

    public void selectAllAllowanceTypes() {
        pause();
        log.info("Selecting all allowance types");
        select2SelectAll(ID_ALLOWANCE_TYPES);
    }

    public void selectAllowanceTypeByName(String name) {
        pause();
        select2MultiByText(ID_ALLOWANCE_TYPES, name);
    }

    public void setProjectRunDate(String date) {
        pause();
        log.info("Setting project run date: {}", date);
        clearAndType(projectRunDateInput, date);
    }

    public void submitCreateSchedule() {
        pause();
        log.info("Submitting Create Schedule form");
        jsClick(createScheduleSubmit);
    }

    public boolean isScheduleCreatedSuccessfully() {
        // Schedule creation triggers a long-running server job (up to 10 min on VPN).
        // Wait for the loading overlay to finish before checking for the success popup.
        wait.waitForLoadingOverlay(600);
        try {
            wait.waitForVisibility(successNotification, 30);
            return true;
        } catch (Exception e) {
            log.warn("Success notification not found after overlay disappeared");
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
