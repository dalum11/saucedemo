package base;

import config.TestConfig;
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
    private Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeEach
    @Step("Инициализация WebDriver")
    protected void setUp(TestInfo testInfo) {
        log.info("Запуск теста {}", testInfo.getDisplayName());

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = TestConfig.getChromeOptions();
        driver = new ChromeDriver(options);
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

        if (!TestConfig.isHeadless()) {
            driver.manage().window().maximize();
        }

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

}
