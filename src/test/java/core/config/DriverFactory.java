package core.config;

import core.annotations.BrowserResolution;
import core.annotations.CurrentBrowser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;

public class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            Browser browser = getBrowserFromProperty();
            BrowserOrientation orientation = getOrientationFromProperty();
            driverThreadLocal.set(createDriver(browser, orientation));
        }
        return driverThreadLocal.get();
    }

    public static WebDriver getDriver(Browser browser, BrowserOrientation orientation) {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver(browser, orientation));
        }
        return driverThreadLocal.get();
    }

    public static WebDriver getDriver(Method testMethod) {
        if (driverThreadLocal.get() == null) {
            Browser browser;
            BrowserOrientation orientation;

            if (testMethod != null && testMethod.isAnnotationPresent(CurrentBrowser.class)) {
                browser = testMethod.getAnnotation(CurrentBrowser.class).value();
                log.debug("Браузер получен из аннотации @CurrentBrowser: {}", browser);
            } else {
                browser = getBrowserFromProperty();
                log.debug("Браузер получен из системного свойства: {}", browser);
            }

            if (testMethod != null && testMethod.isAnnotationPresent(BrowserResolution.class)) {
                orientation = testMethod.getAnnotation(BrowserResolution.class).value();
                log.debug("Разрешение получено из аннотации @BrowserResolution: {}", orientation);
            } else {
                orientation = getOrientationFromProperty();
                log.debug("Разрешение получено из системного свойства: {}", orientation);
            }

            driverThreadLocal.set(createDriver(browser, orientation));
        }
        return driverThreadLocal.get();
    }


    private static WebDriver createDriver(Browser browser, BrowserOrientation orientation) {
        log.info("Создание драйвера для браузера: {}, разрешение: {}", browser, orientation);

        switch (browser) {
            case GOOGLE_CHROME -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = TestConfig.getChromeOptions(orientation);
                return new ChromeDriver(options);
            }

            case Yandex -> {
                boolean isCI = Boolean.parseBoolean(System.getProperty("ci", "false"));
                String os = System.getProperty("os.name").toLowerCase();

                ChromeOptions options = TestConfig.getChromeOptions(orientation);

                if (isCI || os.contains("linux")) {
                    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/yandexdriver");

                    String yandexPath = "/usr/bin/yandex-browser";
                    options.setBinary(yandexPath);
                    return new ChromeDriver(options);
                } else if (os.contains("win")) {
                    System.setProperty("webdriver.chrome.driver",
                            "C:\\Users\\dalum\\Downloads\\yandexdriver-26.6.0.1742-win64\\yandexdriver.exe");
                    options.setBinary("C:\\Program Files (x86)\\Yandex\\YandexBrowser\\Application\\browser.exe");
                    return new ChromeDriver(options);
                } else {
                    WebDriverManager.chromedriver().setup();
                    return new ChromeDriver(options);
                }
            }

            default -> throw new IllegalStateException("Unexpected browser: " + browser);
        }
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
            log.info("Драйвер закрыт и удалён из ThreadLocal");
        }
    }

    private static Browser getBrowserFromProperty() {
        String browserProperty = System.getProperty("browser", "GOOGLE_CHROME");
        try {
            return Browser.valueOf(browserProperty.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Неизвестный браузер: {}, используется GOOGLE_CHROME", browserProperty);
            return Browser.GOOGLE_CHROME;
        }
    }

    private static BrowserOrientation getOrientationFromProperty() {
        String orientationProperty = System.getProperty("browser.resolution", "DESKTOP");
        try {
            return BrowserOrientation.valueOf(orientationProperty.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Неизвестное разрешение: {}, используется DESKTOP", orientationProperty);
            return BrowserOrientation.DESKTOP;
        }
    }

    public static void setDriver(WebDriver driver) {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
        }
        driverThreadLocal.set(driver);
    }
}
