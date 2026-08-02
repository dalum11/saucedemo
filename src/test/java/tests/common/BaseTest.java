package tests.common;

import core.annotations.CurrentBrowser;
import core.annotations.BrowserResolution;
import core.config.Browser;
import core.config.BrowserOrientation;
import core.config.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;

@ExtendWith(AllureJunit5.class)
public abstract class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor jsExecutor;
    protected BrowserOrientation browserOrientation;
    protected Browser browser;

    private Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeEach
    @Step("Инициализация WebDriver")
    protected void setUp(TestInfo testInfo) {
        log.info("Запуск теста {}", testInfo.getDisplayName());

        this.browserOrientation = testInfo.getTestMethod()
                        .map(method -> method.getAnnotation(BrowserResolution.class))
                        .map(BrowserResolution::value)
                        .orElseGet(this::getBrowserOrientation);

        this.browser = testInfo.getTestMethod()
                        .map(method -> method.getAnnotation(CurrentBrowser.class))
                        .map(CurrentBrowser::value)
                        .orElseGet(this::getBrowser);

        switch (browser) {
            case GOOGLE_CHROME -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = TestConfig.getChromeOptions(browserOrientation);
                driver = new ChromeDriver(options);
            }
            case Yandex -> {
                boolean isCI = Boolean.parseBoolean(System.getProperty("ci", "false"));
                String os = System.getProperty("os.name").toLowerCase();

                ChromeOptions options = TestConfig.getChromeOptions(browserOrientation);

                if (isCI || os.contains("linux")) {
                    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/yandexdriver");

                    String yandexPath = "usr/bin/yandex-browser";
                    options.setBinary(yandexPath);
                    driver = new ChromeDriver(options);
                } else if (os.contains("win")) {
                    System.setProperty("webdriver.chrome.driver",
                            "C:\\Users\\dalum\\Downloads\\yandexdriver-26.6.0.1742-win64\\yandexdriver.exe");
                    options.setBinary("C:\\Program Files (x86)\\Yandex\\YandexBrowser\\Application\\browser.exe");
                    driver = new ChromeDriver(options);
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(options);
                }
            }
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        jsExecutor = (JavascriptExecutor) driver;
        configureDriver();

        log.info("Драйвер инициализирован");
    }

    @AfterEach
    @Step("Закрытие драйвера с сохранением скриншота")
    protected void tearDown(TestInfo testInfo) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String fileName = testInfo.getDisplayName()
                    .replaceAll("[^a-zA-Z0-9]", "_") + "_"
                    + System.currentTimeMillis() + ".png";

            File screenshotDir = new File("target/screenshots");
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            File destFile = new File(screenshotDir, fileName);
            FileUtils.copyFile(screenshot, destFile);

            log.info("Скриншот сохранён: {}", destFile.getAbsolutePath());

        } catch (Exception e) {
            log.error("Не удалось сохранить скриншот: {}", e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void configureDriver() {
        driver.manage().timeouts().pageLoadTimeout(TestConfig.getPageLoadTimeout());
        driver.manage().timeouts().implicitlyWait(TestConfig.getImplicitWait());
        driver.manage().deleteAllCookies();
    }

    @Step("Открыть страницу: {url}")
    protected void open(String url) {
        log.info("Открытие страницы: {}", url);

        driver.get(url);
        log.info("Текущий URL: {}", driver.getCurrentUrl());
    }

    @Step("Открыть страницу авторизации")
    protected void openLoginPage() {
        open(TestConfig.getBaseUrl());
    }

    protected BrowserOrientation  getBrowserOrientation() {
        String orientationProperty = System.getProperty("browser.resolution", "DESKTOP");
        try {
            BrowserOrientation browserOrientation = BrowserOrientation.valueOf(orientationProperty.toUpperCase());
            log.info("Разрешение получено из системы: {}", browserOrientation);
            return browserOrientation;
        } catch (IllegalArgumentException e) {
            log.warn("Разрешение не удалось получить из системы, используется DESKTOP");
            return BrowserOrientation.DESKTOP;
        }

    }

    protected Browser getBrowser() {
        String browserProperty = System.getProperty("browser", "GOOGLE_CHROME");
        try {
            Browser browser = Browser.valueOf(browserProperty.toUpperCase());
            log.info("Браузер получен из системы: {}", browser);
            return browser;
        } catch (IllegalArgumentException e) {
            log.warn("Тип браузера не удалось получить из системы, используется GOOGLE_CHROME");
            return Browser.GOOGLE_CHROME;
        }
    }
}
