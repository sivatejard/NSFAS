package com.nsfas.automation.constants;

/**
 * Project-wide constants. Add your static values here.
 */
public class Constants {

    private Constants() {}

    // ─── Timeouts ────────────────────────────────────
    public static final int DEFAULT_TIMEOUT       = 20;
    public static final int SHORT_TIMEOUT         = 5;
    public static final int LONG_TIMEOUT          = 60;
    public static final int PAGE_LOAD_TIMEOUT     = 30;
    public static final int POLLING_INTERVAL_MS   = 500;

    // ─── Paths ───────────────────────────────────────
    public static final String SCREENSHOT_DIR     = "test-output/screenshots/";
    public static final String REPORT_DIR         = "test-output/reports/";
    public static final String TEST_DATA_DIR      = "src/test/resources/testdata/";
    public static final String CONFIG_FILE        = "config.properties";

    // ─── Test data file names ─────────────────────────
    public static final String LOGIN_DATA_FILE    = TEST_DATA_DIR + "LoginData.xlsx";
    public static final String STUDENT_DATA_FILE  = TEST_DATA_DIR + "StudentData.xlsx";

    // ─── Messages ────────────────────────────────────
    public static final String LOGIN_SUCCESS_MSG  = "Welcome";
    public static final String LOGIN_FAILURE_MSG  = "Invalid credentials";

    // ─── URLs ────────────────────────────────────────
    // These come from config.properties; add static ones below if needed.
    public static final String LOGIN_PATH         = "/login";
    public static final String DASHBOARD_PATH     = "/dashboard";

    // ─── Browsers ────────────────────────────────────
    public static final String CHROME  = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE    = "edge";
}
