package config;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestConfig {
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration IMPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(3);
    public static final String DEFAULT_BROWSER = "chrome";
    public static final boolean DEFAULT_HEADLESS = false;
    public static final String BASE_URL = "https://www.saucedemo.com/";

    public static boolean isHeadless() {
        String headless = System.getProperty("headless",
                System.getenv("HEADLESS") != null ? System.getenv("HEADLESS") : DEFAULT_HEADLESS + "");
        return Boolean.parseBoolean(headless);
    }

    public static String getBaseUrl() {
        return System.getProperty("base.url",
                System.getenv("BASE_URL") != null ? System.getenv("BASE_URL") : BASE_URL);
    }

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");

        if (isHeadless()) {
            options.addArguments("--headless=new");
        }

        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        options.setCapability("goog:loggingPrefs", Map.of(
                "browser", "INFO",
                "performance", "ALL"
        ));

        return options;
    }

    public static Duration getTimeout() {
        String timeout = System.getProperty("timeout");
        return timeout != null ? Duration.ofSeconds(Long.parseLong(timeout)) : DEFAULT_TIMEOUT;
    }

    public static Duration getPageLoadTimeout() {
        String timeout = System.getProperty("page.load.timeout");
        return timeout != null ? Duration.ofSeconds(Long.parseLong(timeout)) : PAGE_LOAD_TIMEOUT;
    }

    public static Duration getImplicitWait() {
        String timeout = System.getProperty("implicit.wait");
        return timeout != null ? Duration.ofSeconds(Long.parseLong(timeout)) : IMPLICIT_WAIT_TIMEOUT;
    }
}
