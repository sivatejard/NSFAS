package com.nsfas.automation.constants;

/**
 * Routing outcome (Pick an Outcome dropdown) values for each disbursement stage.
 * Centralised here so a label change in the app needs fixing in exactly one place.
 */
public final class RoutingOutcome {

    private RoutingOutcome() {}

    // Stage 2 — Finance Reviewer
    public static final String APPROVE_FOR_PROJECTION       = "Approve for Projection";

    // Stage 3 — Operations Initiator
    public static final String DISBURSEMENT_INITIATION      = "Disbursement initiation";

    // Stage 4 — First Approver
    public static final String ROUTE_TO_SENIOR_MANAGER      = "Approve - Route to Senior Manager";

    // Stage 5 — Finance Senior Manager
    public static final String ROUTE_TO_COO                 = "Approve - Route to COO";

    // Stage 6 — Finance COO
    public static final String ROUTE_TO_CFO                 = "Approve - Route to CFO";

    // Stage 7 — Finance CFO
    public static final String ROUTE_TO_CEO                 = "Approve - Route to CEO";

    // Stage 8 — Finance CEO
    public static final String GENERATE_PAYMENT_FILES       = "Approve- Generate Payment Files";
}
