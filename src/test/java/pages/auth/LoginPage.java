package pages.auth;

import core.utils.CursorUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import pages.base.BasePage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoginPage extends BasePage {

    private static final By PAGE_TITLE = By.className("login_logo");
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By AVAILABLE_USERNAMES = By.xpath("//div[@id='login_credentials']");
    private static final By AVAILABLE_PASSWORDS = By.xpath("//div[@class='login_password']");
    private static final By ERROR_MESSAGE = By.xpath("//div[@class='error-message-container error']//h3");
    private static final By CROSS_BUTTON_USERNAME = By.xpath("//div[@class='form_group'][1]//svg");
    private static final By CROSS_BUTTON_PASSWORD = By.xpath("//div[@class='form_group'][2]//svg]");


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получение элемента Username")
    public WebElement getUsername() {
        return findElement(USERNAME);
    }

    @Step("Получение элемента Password")
    public WebElement getPassword() {
        return findElement(PASSWORD);
    }

    @Step("Получение элемента LoginButton")
    public WebElement getLoginButton() {
        return findElement(LOGIN_BUTTON);
    }

    @Step("Получение элемента Logo")
    public WebElement getLogo() {
        return findElement(PAGE_TITLE);
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
        return getPlaceholder(USERNAME);
    }

    @Step("Проверка доступности поля логина")
    public boolean isLoginEnabled() {
        return isEnabled(USERNAME);
    }

    @Step("Проверка отображения поля логина")
    public boolean isLoginDisplayed() {
        return isDisplayed(USERNAME);
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
        typeText(USERNAME, username);
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
        clearUsername();
        clearPassword();
    }

    @Step("Очистка логина")
    public void clearUsername() {
        findElement(USERNAME).clear();
    }

    @Step("Очистка пароля")
    public void clearPassword() {
        findElement(PASSWORD).clear();
    }

    @Step("Проверка видимости сообщения об ошибке")
    public boolean isErrorMessageDisplayed() {
        try {
            return findElement(ERROR_MESSAGE).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Проверка видимости кнопки Крестик поля Логин")
    public boolean isCrossButtonUsernameDisplayed() {
        try {
            return findElement(CROSS_BUTTON_USERNAME).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("Проверка видимости кнопки Крестик поля Пароль")
    public boolean isCrossButtonPasswordDisplayed() {
        try {
            return findElement(CROSS_BUTTON_PASSWORD).isDisplayed();
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
    public String getUsernameText() {
        return getValue(USERNAME);
    }

    @Step("Получение текста пароля")
    public String getPasswordText() {
        return getValue(PASSWORD);
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

    @Step("Наведение курсора на поле Username")
    public void hoverOverUsernameField() {
        new CursorUtils(driver).hoverOverElement(findElement(USERNAME));
    }

    @Step("Наведение курсора на поле Password")
    public void hoverOverPasswordField() {
        new CursorUtils(driver).hoverOverElement(findElement(PASSWORD));
    }

    @Step("Наведение курсора на кнопку Login")
    public void hoverOverLoginButton() {
        new CursorUtils(driver).hoverOverElement(findElement(LOGIN_BUTTON));
    }

    @Step("Нажатие на поле Username")
    public void clickOnUsernameField() {
        click(USERNAME);
    }

    @Step("Нажатие на поле Password")
    public void clickOnPasswordField() {
        click(PASSWORD);
    }

    @Step("Нажатие на иконку Глаз")
    public void clickOnEyeIcon() {
        int fieldWidth = getPassword().getSize().getWidth();
        int fieldHeight = getPassword().getSize().getHeight();
        int xOffset = fieldWidth - 30;
        int yOffset = fieldHeight / 2;

        Actions actions = new Actions(driver);
        actions.moveToElement(getPassword(), xOffset, yOffset).click().perform();
    }

    @Step("Нажатие на кнопку Крестик поля Логин")
    public void clickOnCrossButtonUsername() {
        click(CROSS_BUTTON_USERNAME);
    }

    @Step("Нажатие на кнопку Крестик поля Пароль")
    public void clickOnCrossButtonPassword() {
        click(CROSS_BUTTON_PASSWORD);
    }

    @Step("Нажатие на имя пользователя {username} в инфоблоке")
    public void clickOnUsernameInInfoBlock(String username) {
        if (!getAvailableUsernames().contains(username)) {
            throw new IllegalArgumentException("Такого имени пользователя нет");
        }

        WebElement usernameNeeded = getUsernameInInfoBlock(username);
        usernameNeeded.click();
    }

    private WebElement getUsernameInInfoBlock(String username) {
        int usernameIndex = getAvailableUsernames().indexOf(username);
        return findElements(AVAILABLE_USERNAMES).get(usernameIndex);
    }

    @Step("Получение текста имени пользователя из инфоблока")
    public String getUsernameInInfoBlockText(int usernameIndex) {
        return getAvailableUsernames().get(usernameIndex);
    }

    @Step("Нажатие на пароль в инфоблоке")
    public void clickOnPasswordInInfoBlock() {
        WebElement passwords = findElement(AVAILABLE_PASSWORDS);
        passwords.click();
    }

    @Step("Очистка поля Username через Backspace")
    public void clearUsernameWithBackspace() {
        clearFieldWithBackspace(findElement(USERNAME));
    }

    @Step("Очистка поля Password через Backspace")
    public void clearPasswordWithBackspace() {
        clearFieldWithBackspace(findElement(PASSWORD));
    }
}
