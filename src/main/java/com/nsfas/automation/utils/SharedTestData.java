package com.nsfas.automation.utils;

/**
 * Holds test data shared across all 8 stages of the disbursement process.
 * Sequence number captured in Stage 1 is reused in Stages 2-8.
 */
public class SharedTestData {

    private SharedTestData() {}

    private static String projectionSequenceNumber = "";

    public static void setSequenceNumber(String seqNo) {
        projectionSequenceNumber = seqNo;
        LogUtils.info("Sequence number stored: " + seqNo);
    }

    public static String getSequenceNumber() {
        if (projectionSequenceNumber == null || projectionSequenceNumber.isEmpty()) {
            throw new IllegalStateException(
                    "Projection sequence number not set. Ensure Stage 1 ran successfully.");
        }
        return projectionSequenceNumber;
    }

    public static boolean hasSequenceNumber() {
        return projectionSequenceNumber != null && !projectionSequenceNumber.isEmpty();
    }
}
