package core.config;

import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class TestConfig {
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);
    public static final Duration IMPLICIT_WAIT_TIMEOUT = Duration.ofSeconds(3);
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

    public static ChromeOptions getChromeOptions(BrowserOrientation orientation, boolean isSavePassword) {
        ChromeOptions options = new ChromeOptions();

        if (!isSavePassword) {
            options.addArguments("--incognito");
            options.addArguments("--disable-save-password-bubble");
            options.addArguments("--disable-features=PasswordLeakDetection,SavePassword");

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.password_manager_leak_detection.enabled", false);
            options.setExperimentalOption("prefs", prefs);
        } else {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", true);
            prefs.put("profile.password_manager_enabled", true);
            prefs.put("profile.password_manager_leak_detection.enabled", true);
            options.setExperimentalOption("prefs", prefs);
        }

        switch (orientation) {
            case MB:
                options.addArguments("--window-size=320,568");
                break;
            case DESKTOP:
                options.addArguments("--window-size=1920,1080");
                break;
            case STANDARD:
                options.addArguments("--window-size=1440,1920");
                break;
        }

        if (isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
        }

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
