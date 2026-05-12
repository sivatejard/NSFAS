package com.nsfas.automation.drivers;

import com.nsfas.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver manager using ThreadLocal — supports parallel execution.
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static void initDriver() {
        String browser = ConfigReader.get("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "false"));
        log.info("Initializing {} driver | headless={}", browser, headless);

        WebDriver driver;
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless");
                driver = new EdgeDriver(edgeOptions);
                break;
            default:
                setupChromeDriver();
                driver = new ChromeDriver(buildChromeOptions(headless));
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
                Long.parseLong(ConfigReader.get("implicit.wait", "10"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
                Long.parseLong(ConfigReader.get("page.load.timeout", "30"))));

        driverThreadLocal.set(driver);
        log.info("Driver initialized successfully");
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Call initDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("Driver closed and removed from ThreadLocal");
        }
    }

    /**
     * Sets up ChromeDriver using a local cached driver when available,
     * falling back to WebDriverManager download if not found.
     * This avoids corporate network blocks on driver download endpoints.
     */
    private static void setupChromeDriver() {
        String localDriver = ConfigReader.get("webdriver.chrome.driver", "");
        if (!localDriver.isEmpty()) {
            log.info("Using chromedriver from config: {}", localDriver);
            System.setProperty("webdriver.chrome.driver", localDriver);
            return;
        }
        // Auto-detect from common local cache locations (Python wdm cache)
        String[] candidates = {
            System.getProperty("user.home") + "\\.wdm\\drivers\\chromedriver\\win64\\148.0.7778.97\\chromedriver-win32\\chromedriver.exe",
            System.getProperty("user.home") + "\\.cache\\selenium\\chromedriver\\win64\\chromedriver.exe"
        };
        for (String path : candidates) {
            if (new java.io.File(path).exists()) {
                log.info("Using cached chromedriver: {}", path);
                System.setProperty("webdriver.chrome.driver", path);
                return;
            }
        }
        log.info("Cached chromedriver not found — attempting WebDriverManager download");
        WebDriverManager.chromedriver().timeout(30).setup();
    }

    private static ChromeOptions buildChromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) options.addArguments("--headless=new");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        return options;
    }
}
