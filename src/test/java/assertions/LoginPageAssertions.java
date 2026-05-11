package assertions;

import base.BaseAssertions;
import org.openqa.selenium.WebDriver;
import pageobject.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginPageAssertions extends BaseAssertions{

    private final LoginPage loginPage;

    public LoginPageAssertions(LoginPage loginPage) {
        this.loginPage = loginPage;
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

    public LoginPageAssertions verifyLoginButton() {
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

    public LoginPageAssertions assertLoginIsEmpty() {
        assertThat(loginPage.getLoginText()).as("Логин").isEmpty();
        return this;
    }

    public LoginPageAssertions assertPasswordIsEmpty() {
        assertThat(loginPage.getPasswordText()).as("Пароль").isEmpty();
        return this;
    }

    public LoginPageAssertions assertCredentialsIsEmpty() {
        return assertPasswordIsEmpty().assertPasswordIsEmpty();
    }
}
