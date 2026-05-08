package com.nsfas.automation.utils;

import com.github.javafaker.Faker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Generates random / dynamic test data using JavaFaker.
 */
public class TestDataUtils {

    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private TestDataUtils() {}

    // ─────────────────────────────────────────────────
    // Person details
    // ─────────────────────────────────────────────────

    public static String getFirstName()    { return faker.name().firstName(); }
    public static String getLastName()     { return faker.name().lastName(); }
    public static String getFullName()     { return faker.name().fullName(); }
    public static String getEmail()        { return faker.internet().emailAddress(); }
    public static String getPhoneNumber()  { return faker.phoneNumber().cellPhone(); }
    public static String getPassword()     { return faker.internet().password(8, 16, true, true, true); }

    // ─────────────────────────────────────────────────
    // Address
    // ─────────────────────────────────────────────────

    public static String getStreetAddress() { return faker.address().streetAddress(); }
    public static String getCity()          { return faker.address().city(); }
    public static String getState()         { return faker.address().state(); }
    public static String getZipCode()       { return faker.address().zipCode(); }
    public static String getCountry()       { return faker.address().country(); }

    // ─────────────────────────────────────────────────
    // Numbers / IDs
    // ─────────────────────────────────────────────────

    public static String getUUID()            { return UUID.randomUUID().toString(); }
    public static int getRandomInt(int min, int max) { return random.nextInt(max - min + 1) + min; }
    public static String getRandomDigits(int length) { return faker.number().digits(length); }
    public static String getIdNumber()        { return faker.idNumber().valid(); }

    // ─────────────────────────────────────────────────
    // Dates
    // ─────────────────────────────────────────────────

    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String getCurrentDate() {
        return getCurrentDate("dd/MM/yyyy");
    }

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    // ─────────────────────────────────────────────────
    // Company / Misc
    // ─────────────────────────────────────────────────

    public static String getCompanyName()  { return faker.company().name(); }
    public static String getJobTitle()     { return faker.job().title(); }
    public static String getLoremSentence(){ return faker.lorem().sentence(); }
    public static String getLoremParagraph(){ return faker.lorem().paragraph(); }

    /** Returns a unique email using timestamp to avoid duplicates in DB. */
    public static String getUniqueEmail() {
        return "test_" + getTimestamp() + "@nsfas.co.za";
    }

    /** Returns a unique username. */
    public static String getUniqueUsername() {
        return faker.name().username() + "_" + getRandomDigits(4);
    }
}
