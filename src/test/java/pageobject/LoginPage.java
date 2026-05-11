package pageobject;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoginPage extends BasePage {

    private static final By PAGE_TITLE = By.className("login_logo");
    private static final By LOGIN = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By AVAILABLE_USERNAMES = By.xpath("//div[@id='login_credentials']");
    private static final By AVAILABLE_PASSWORDS = By.xpath("//div[@class='login_password']");
    private static final By ERROR_MESSAGE = By.xpath("//div[@class='error-message-container error']//h3");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получение заголовка страницы")
    public String getPageTitleText() {
        return getText(PAGE_TITLE);
    }

    @Step("Проверка отображения заголовка")
    public boolean isPageTitleDisplayed() {
        return isDisplayed(PAGE_TITLE);
    }

    @Step("Получение placeholder поля логина")
    public String getLoginPlaceholder() {
        return getPlaceholder(LOGIN);
    }

    @Step("Проверка доступности поля логина")
    public boolean isLoginEnabled() {
        return isEnabled(LOGIN);
    }

    @Step("Проверка отображения поля логина")
    public boolean isLoginDisplayed() {
        return isDisplayed(LOGIN);
    }

    @Step("Получение placeholder поля пароля")
    public String getPasswordPlaceholder() {
        return getPlaceholder(PASSWORD);
    }

    @Step("Получение плейсхолдера")
    public String getPlaceholder(By locator) {
        return getAttribute(locator, "placeholder");
    }

    @Step("Получение отображаемого значения")
    public String getValue(By locator) {
        return getAttribute(locator, "value");
    }

    @Step("Проверка доступности поля пароля")
    public boolean isPasswordFieldEnabled() {
        return isEnabled(PASSWORD);
    }

    @Step("Проверка отображения поля пароля")
    public boolean isPasswordFieldDisplayed() {
        return isDisplayed(PASSWORD);
    }

    @Step("Получение текста кнопки логина")
    public String getLoginButtonText() {
        return getAttribute(LOGIN_BUTTON, "value");
    }

    @Step("Проверка доступности кнопки логина")
    public boolean isLoginButtonEnabled() {
        return isEnabled(LOGIN_BUTTON);
    }

    @Step("Проверка отображения кнопки логина")
    public boolean isLoginButtonDisplayed() {
        return isDisplayed(LOGIN_BUTTON);
    }

    @Step("Получение списка доступных логинов")
    public List<String> getAvailableUsernames() {
        WebElement availableUsernames = findElement(AVAILABLE_USERNAMES);
        return getUsernamesFromCredentialsBlock(availableUsernames);
    }

    @Step("Получение доступного пароля")
    public String getAvailablePassword() {
        WebElement passwords = findElement(AVAILABLE_PASSWORDS);
        return getUsernamesFromCredentialsBlock(passwords).get(1);
    }

    @Step("Ввод логина: {username}")
    public void enterLogin(String username) {
        typeText(LOGIN, username);
    }

    @Step("Ввод пароля: {password}")
    public void enterPassword(String password) {
        typeText(PASSWORD, password);
    }

    @Step("Нажатие кнопки входа")
    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    @Step("Вход с логином: {username} и паролем: {password}")
    public void login(String username, String password) {
        enterLogin(username);
        enterPassword(password);
        clickLogin();
    }

    @Step("Очистка полей ввода")
    public void clearFields() {
        findElement(LOGIN).clear();
        findElement(LOGIN).clear();
    }

    @Step("Проверка видимости сообщения об ошибке")
    public boolean isErrorMessageDisplayed() {
        try {
            return findElement(ERROR_MESSAGE).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Блюр элемента {element}")
    public void blurActiveElement() {
        super.blurActiveElement();
    }

    @Step("Обновление страницы")
    public void refresh() {
        super.refreshPage();
    }

    @Step("Получение текста логина")
    public String getLoginText() {
        return findElement(LOGIN).getText();
    }

    @Step("Получение текста пароля")
    public String getPasswordText() {
        return findElement(PASSWORD).getText();
    }

    @Step("Получение текста ошибки авторизации")
    public String getErrorMessageText() {
        return findElement(ERROR_MESSAGE).getText();
    }

    private List<String> getUsernamesFromCredentialsBlock(WebElement credentialsBlock) {
        String fullText = (String) (jsExecutor)
                .executeScript("return arguments[0].innerText;", credentialsBlock);
        String withoutHeader = fullText.replace("Accepted usernames are:", "").trim();
        return Arrays.stream(withoutHeader.split("\\n"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .collect(Collectors.toList());
    }

    @Step("Получение маскированного пароля")
    public String getMaskedPassword() {
        return getValue(PASSWORD);
    }
}
