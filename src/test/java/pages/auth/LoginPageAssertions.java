package pages.auth;

import core.assertions.BaseAssertions;
import core.utils.ClipboardUtils;
import core.utils.CursorUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginPageAssertions extends BaseAssertions{

    private final LoginPage loginPage;
    private final WebDriver driver;
    private final CursorUtils cursorUtils;

    public LoginPageAssertions(LoginPage loginPage, WebDriver driver, CursorUtils cursorUtils) {
        this.loginPage = loginPage;
        this.driver = driver;
        this.cursorUtils = cursorUtils;
    }

    public LoginPageAssertions verifyPageDisplayed() {
        assertThat(loginPage.isPageTitleDisplayed())
                .as("Заголовок страницы должен быть видим")
                .isTrue();
        assertThat(loginPage.getPageTitleText())
                .as("Текст заголовка страницы логина")
                .isEqualTo("Swag Labs");
        assertPageUrlContains(loginPage.getDriver(), "/");
        return this;
    }

    public LoginPageAssertions verifyUsernameField() {
        assertThat(loginPage.isLoginDisplayed()).isTrue();
        assertThat(loginPage.isLoginEnabled()).isTrue();
        assertThat(loginPage.getLoginPlaceholder()).isEqualTo("Username");
        return this;
    }

    public LoginPageAssertions verifyUsernameFieldText(String text) {
        assertThat(loginPage.getUsernameText()).isEqualTo(text);
        return this;
    }

    public LoginPageAssertions verifyPasswordFieldText(String text) {
        assertThat(loginPage.getPasswordText()).isEqualTo(text);
        return this;
    }

    public LoginPageAssertions verifyPasswordField() {
        assertThat(loginPage.isPasswordFieldDisplayed()).isTrue();
        assertThat(loginPage.isPasswordFieldDisplayed()).isTrue();
        assertThat(loginPage.getPasswordPlaceholder()).isEqualTo("Password");
        return this;
    }

    public LoginPageAssertions verifyErrorMessage(String expectedMessage) {
        assertAll("Сообщение об ошибке должно быть видимо и содержать определённый текст",
                () -> assertThat(loginPage.isErrorMessageDisplayed())
                        .as("Сообщение об ошибке должно отображаться")
                        .isTrue(),
                () -> assertThat(loginPage.getErrorMessageText())
                        .as("Текст сообщения об ошибке")
                        .isEqualTo(expectedMessage)
        );

        return this;
    }

    public LoginPageAssertions verifyLoginButtonEnabled() {
        assertThat(loginPage.isLoginButtonDisplayed())
                .as("Кнопка входа должна отображаться")
                .isTrue();

        assertThat(loginPage.isLoginButtonEnabled())
                .as("Кнопка входа должна быть доступна")
                .isTrue();

        assertThat(loginPage.getLoginButtonText())
                .as("Текст кнопки входа")
                .isEqualTo("Login");
        return this;
    }

    public LoginPageAssertions verifyLoginButtonDisabled() {
        assertThat(loginPage.isLoginButtonDisplayed())
                .as("Кнопка входа должна отображаться")
                .isTrue();

        assertThat(loginPage.isLoginButtonEnabled())
                .as("Кнопка входа не должна быть доступна")
                .isFalse();

        assertThat(loginPage.getLoginButtonText())
                .as("Текст кнопки входа")
                .isEqualTo("Login");
        return this;
    }

    public LoginPageAssertions assertUsernameIsEmpty() {
        assertThat(loginPage.getUsernameText()).as("Логин").isEmpty();
        return this;
    }

    public LoginPageAssertions assertPasswordIsEmpty() {
        assertThat(loginPage.getPasswordText()).as("Пароль").isEmpty();
        return this;
    }

    public LoginPageAssertions assertCredentialsIsEmpty() {
        return assertPasswordIsEmpty().assertUsernameIsEmpty();
    }

    public LoginPageAssertions assertLogoIsCentered() {
        WebElement logo = loginPage.getLogo();
        String marginLeft = logo.getCssValue("margin-left");
        String marginRight = logo.getCssValue("margin-right");
        assertThat(marginLeft).isEqualTo(marginRight);
        return this;
    }

    public LoginPageAssertions assertUsernameFieldWidthInPercent() {
        assertElementWidthPercent(driver, loginPage.getUsername(), 100);
        return this;
    }

    public LoginPageAssertions assertPasswordFieldWidthInPercent() {
        assertElementWidthPercent(driver, loginPage.getPassword(), 100);
        return this;
    }

    public LoginPageAssertions assertLoginFieldsWidthInPercent() {
        return assertUsernameFieldWidthInPercent().assertPasswordFieldWidthInPercent();
    }

    public LoginPageAssertions assertCrossButtonLoginIsVisible() {
        assertThat(loginPage.isCrossButtonUsernameDisplayed())
                .as("Кнопка Крестик должна быть видна в поле Логин")
                .isTrue();
        return this;
    }

    public LoginPageAssertions assertCrossButtonPasswordIsVisible() {
        assertThat(loginPage.isCrossButtonUsernameDisplayed())
                .as("Кнопка Крестик должна быть видна в поле Пароль")
                .isTrue();
        return this;
    }

    public LoginPageAssertions assertCrossButtonLoginIsInvisible() {
        assertThat(loginPage.isCrossButtonUsernameDisplayed())
                .as("Кнопка Крестик не должна быть видна в поле Логин")
                .isFalse();
        return this;
    }

    public LoginPageAssertions assertCrossButtonPasswordIsInvisible() {
        assertThat(loginPage.isCrossButtonUsernameDisplayed())
                .as("Кнопка Крестик не должна быть видна в поле Пароль")
                .isFalse();
        return this;
    }

    public LoginPageAssertions assertUsernameFilled(String username) {
        return verifyUsernameFieldText(username).assertCrossButtonLoginIsVisible();
    }

    public LoginPageAssertions assertUsernameCleared() {
        return assertUsernameIsEmpty().assertCrossButtonLoginIsInvisible();
    }

    public LoginPageAssertions assertPasswordFilled(String password) {
        return verifyUsernameFieldText(password).assertCrossButtonPasswordIsInvisible();
    }

    public LoginPageAssertions assertPasswordCleared() {
        return assertPasswordIsEmpty().assertCrossButtonLoginIsInvisible();
    }

    public LoginPageAssertions assertThatCopiedUsernameEqualsExpected(String expectedUsername) {
        String clipboardUsername = ClipboardUtils.getClipboardContent();
        assertThat(clipboardUsername)
                .as("Скопированный логин должен быть равен ожидаемому")
                .isEqualTo(expectedUsername);
        return this;
    }

    public LoginPageAssertions assertThatCopiedPasswordEqualsExpected(String expectedPassword) {
        String  clipboardPassword = ClipboardUtils.getClipboardContent();
        assertThat(clipboardPassword)
                .as("Скопированный пароль должен быть равен ожидаемому")
                .isEqualTo(expectedPassword);
        return this;
    }

    public LoginPageAssertions assertUsernameTextLength(int expectedLength) {
        assertElementTextLength(loginPage.getUsername(), expectedLength);
        return this;
    }

    public LoginPageAssertions assertPasswordTextLength(int expectedLength) {
        assertElementTextLength(loginPage.getPassword(), expectedLength);
        return this;
    }
}
