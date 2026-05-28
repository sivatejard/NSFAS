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

    // Toast notification locators (confirmed from live app HTML)
    // Container: div#toast-container.toast-top-right
    // Each toast: div.toast.toast-success or div.toast.toast-error
    private final By toastSuccess    = By.cssSelector("#toast-container .toast.toast-success");
    private final By toastError      = By.cssSelector("#toast-container .toast.toast-error");
    private final By anyToast        = By.cssSelector("#toast-container .toast");
    private final By toastMessageEl  = By.cssSelector("#toast-container .toast-message");
    private final By toastTitleEl    = By.cssSelector("#toast-container .toast-title");

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
        // HTML date inputs require yyyy-MM-dd; convert dd-MM-yyyy if needed
        String isoDate = date;
        if (date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            String[] p = date.split("-");
            isoDate = p[2] + "-" + p[1] + "-" + p[0];
        }
        org.openqa.selenium.WebElement el = driver.findElement(projectRunDateInput);
        executeJS("arguments[0].value = arguments[1];", el, isoDate);
        executeJS("arguments[0].dispatchEvent(new Event('input',  {bubbles:true}));", el);
        executeJS("arguments[0].dispatchEvent(new Event('change', {bubbles:true}));", el);
        log.info("Project run date set (ISO): {}", isoDate);
    }

    public void submitCreateSchedule() {
        pause();
        log.info("Submitting Create Schedule form");
        jsClick(createScheduleSubmit);
    }

    /**
     * Submits the Create Schedule form and retries with fresh random institutions
     * whenever the server responds with "No students found for disbursement".
     *
     * Flow (confirmed from live app):
     *   1. Inject JS MutationObserver on toast-container BEFORE clicking submit
     *   2. Click Create Schedule
     *   3. Loading overlay appears immediately and runs 30s – 5 min
     *   4. Overlay disappears → toast appears briefly (success or error)
     *   5. Read captured toast (even if already gone) via JS observer
     *   6. If "No students found" → pick fresh institutions and retry
     *
     * Returns true on success, false if unrecoverable error or all retries exhausted.
     */
    public boolean submitWithRetry(int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            log.info("Create Schedule submit — attempt {}/{}", attempt, maxAttempts);
            pause();
            jsClick(createScheduleSubmit);

            // Wait for overlay to start then finish (30s – 5 min)
            log.info("Attempt {}: waiting for loading overlay...", attempt);
            try {
                wait.waitForVisibility(By.cssSelector("div.loadingoverlay"), 15);
                log.info("Attempt {}: loading overlay started", attempt);
            } catch (Exception ignored) {
                log.warn("Attempt {}: overlay did not appear within 15s", attempt);
            }
            wait.waitForLoadingOverlay(600);
            log.info("Attempt {}: loading overlay cleared", attempt);

            // SUCCESS: app auto-redirects to DisbursementProjections on success
            String currentUrl = driver.getCurrentUrl();
            log.info("Attempt {}: current URL after overlay = {}", attempt, currentUrl);

            if (currentUrl.toLowerCase().contains("disbursementprojections")) {
                log.info("Attempt {}: SUCCESS — redirected to Disbursement Projections", attempt);
                return true;
            }

            // Still on CreateSchedule — error toast appeared (short-lived)
            // Read it from live DOM or fallback message
            String errorMsg = "";
            if (isToastPresent(toastError, 5)) {
                errorMsg = readToastMessage();
            }
            log.warn("Attempt {}: still on CreateSchedule — error: '{}'", attempt, errorMsg);

            if (attempt < maxAttempts) {
                log.warn("Attempt {}: error '{}' — retrying with all institutions...", attempt, errorMsg);
                waitForToastToDisappear();
                selectAllInstitutions();
                continue;
            }

            log.error("Attempt {}: schedule creation failed — '{}'", attempt, errorMsg);
            return false;
        }
        log.error("Schedule creation failed after {} attempts", maxAttempts);
        return false;
    }

    /**
     * Injects a MutationObserver on #toast-container that captures the first
     * toast (success or error) into window._toastCapture as soon as it appears.
     * Must be called before clicking submit so no toast is missed.
     */
    private void injectToastObserver() {
        String js =
            "window._toastCapture = null;" +
            "if (window._toastObserver) { window._toastObserver.disconnect(); }" +
            "var container = document.getElementById('toast-container');" +
            "if (!container) { return 'no #toast-container'; }" +
            "window._toastObserver = new MutationObserver(function(mutations) {" +
            "  mutations.forEach(function(m) {" +
            "    m.addedNodes.forEach(function(node) {" +
            "      if (node.nodeType !== 1 || window._toastCapture) return;" +
            "      var ok  = node.classList.contains('toast-success');" +
            "      var err = node.classList.contains('toast-error');" +
            "      if (ok || err) {" +
            "        var msgEl = node.querySelector('.toast-message');" +
            "        window._toastCapture = {" +
            "          type:    ok ? 'success' : 'error'," +
            "          message: msgEl ? msgEl.textContent.trim() : ''" +
            "        };" +
            "      }" +
            "    });" +
            "  });" +
            "});" +
            "window._toastObserver.observe(container, {childList: true});" +
            "return 'observer ready';";
        Object result = executeJS(js);
        log.info("Toast observer: {}", result);
    }

    /**
     * Reads the toast captured by the JS observer.
     * Falls back to checking visible toasts in case the observer missed anything.
     * Returns String[]{type, message} where type is "success", "error", or "".
     */
    private String[] readCapturedToast() {
        String js = "if (!window._toastCapture) return null;" +
                    "return [window._toastCapture.type, window._toastCapture.message];";
        Object result = executeJS(js);

        if (result instanceof java.util.List) {
            java.util.List<?> list = (java.util.List<?>) result;
            String type = list.size() > 0 && list.get(0) != null ? list.get(0).toString() : "";
            String msg  = list.size() > 1 && list.get(1) != null ? list.get(1).toString() : "";
            return new String[]{type, msg};
        }

        // Observer missed it — fall back to checking live DOM
        if (isToastPresent(toastSuccess, 3)) return new String[]{"success", readToastMessage()};
        if (isToastPresent(toastError,   3)) return new String[]{"error",   readToastMessage()};
        return new String[]{"", ""};
    }

    public boolean isScheduleCreatedSuccessfully() {
        wait.waitForLoadingOverlay(600);
        return isToastPresent(toastSuccess, 30);
    }

    public String getToastMessage() {
        return readToastMessage();
    }

    public void closeSuccessPopup() {
        waitForToastToDisappear();
    }

    // ── Toast helpers ──────────────────────────────────────────────────────────

    private boolean isToastPresent(By locator, int timeoutSeconds) {
        try {
            wait.waitForVisibility(locator, timeoutSeconds);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String readToastMessage() {
        try {
            return driver.findElement(toastMessageEl).getText().trim();
        } catch (Exception e) {
            try {
                return driver.findElement(anyToast).getText().trim();
            } catch (Exception ex) {
                return "";
            }
        }
    }

    private void waitForToastToDisappear() {
        try {
            wait.waitForInvisibility(anyToast, 10);
        } catch (Exception ignored) {}
    }
}
