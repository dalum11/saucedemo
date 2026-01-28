package base;

import config.TestConfig;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected JavascriptExecutor jsExecutor;
    protected Logger log = LoggerFactory.getLogger(BasePage.class);

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.getTimeout());
        this.actions = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;

        PageFactory.initElements(driver, this);

        waitForPageLoad();
    }

    @Step("Ожидание загрузки страницы")
    protected void waitForPageLoad() {
        try {
            wait.until(driver -> jsExecutor.executeScript(
                    "return document.readyState").equals("complete"));
            log.debug("Страница загружена");
        } catch (TimeoutException e) {
            log.warn("Таймаут ожидания загрузки страницы");
        }
    }

    @Step("Обновление страницы")
    protected void refreshPage() {
        log.info("Обновление страницы");
        driver.navigate().refresh();
        waitForPageLoad();
    }

    @Step("Ожидание видимости элемента: {locator}")
    protected WebElement waitForElementVisible(By locator) {
        log.debug("Ожидание видимости элемента: {}", locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Ожидание кликабельности элемента: {locator}")
    protected WebElement waitForElementClickable(By locator) {
        log.debug("Ожидание кликабельности элемента: {}", locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Ожидание исчезновения элемента: {locator}")
    protected boolean waitForElementInvisible(By locator) {
        log.debug("Ожидание исчезновения элемента: {}", locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Step("Ожидание появления текста: {text}")
    protected void waitForTextToBePresent(By locator, String text) {
        log.debug("Ожидание текста '{}' в элементе: {}", text, locator);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Step("Поиск элемента: {locator}")
    protected WebElement findElement(By locator) {
        log.debug("Поиск элемента: {}", locator);
        return driver.findElement(locator);
    }

    @Step("Поиск всех элементов: {locator}")
    protected List<WebElement> findElements(By locator) {
        log.debug("Поиск всех элементов: {}", locator);
        return driver.findElements(locator);
    }

    @Step("Клик по элементу")
    protected void click(By locator) {
        log.info("Клик по элементу с JS: {}", locator);
        WebElement element = waitForElementVisible(locator);
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    @Step("Ввод текста '{text}' в элемент: {locator}")
    protected void typeText(By locator, String text) {
        log.info("Ввод текста '{}' в элемент: {}", text, locator);
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Получение текста элемента: {locator}")
    protected String getText(By locator) {
        log.debug("Получение текста элемента: {}", locator);
        WebElement element = waitForElementVisible(locator);
        return element.getText().trim();
    }

    @Step("Получение атрибута '{attribute}' элемента: {locator}")
    protected String getAttribute(By locator, String attribute) {
        log.debug("Получение атрибута '{}' элемента: {}", attribute, locator);
        WebElement element = waitForElementVisible(locator);
        return element.getAttribute(attribute);
    }

    @Step("Скролл к элементу")
    protected void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    @Step("Проверка отображения длемента {locator}")
    protected boolean isDisplayed(By locator) {
        return findElement(locator).isDisplayed();
    }

    @Step("Проверка доступности элемента {locator} для нажатия")
    protected boolean isEnabled(By locator) {
        return findElement(locator).isDisplayed();
    }

    @Step("Нажатие на элемент {element}")
    protected void clickOnElement(WebElement element) {
        element.click();
    }

    protected boolean isElementDisplayed(WebElement element) {
        return element.isDisplayed();
    }

    protected boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    protected String getElementText(WebElement element) {
        return element.getText();
    }
}
