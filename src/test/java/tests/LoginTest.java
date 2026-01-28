package tests;

import base.BaseTest;
import config.TestConfig;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pageobject.HeaderComponent;
import pageobject.LoginPage;
import pageobject.MainPage;
import utils.Data;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Epic("Авторизация")
@Feature("Вход")
@Story("Авторизация пользователя")
@Severity(SeverityLevel.CRITICAL)
@DisplayName("Тесты авторизации")
public class LoginTest extends BaseTest {

    LoginPage loginPage;
    MainPage mainPage;
    HeaderComponent headerComponent;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        headerComponent = new HeaderComponent(driver);
        openLoginPage();
    }

    @Test
    @DisplayName("Все обязательные элементы страницы логина отображаются")
    @Description("Тест проверяет, что все основные элементы страницы авторизации присутствуют")
    @Tag("smoke")
    @Tag("regression")
    @Step("Проверка элементов страницы логина")
    void loginPage_shouldDisplayAllRequiredElements() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(loginPage.isPageTitleDisplayed())
                .as("Заголовок страницы должен отображаться")
                .isTrue();

        softly.assertThat(loginPage.getPageTitleText())
                .as("Текст заголовка страницы")
                .isEqualTo("Swag Labs");

        softly.assertThat(loginPage.isLoginDisplayed())
                .as("Поле логина должно отображаться")
                .isTrue();

        softly.assertThat(loginPage.isLoginEnabled())
                .as("Поле логина должно быть доступно")
                .isTrue();

        softly.assertThat(loginPage.getLoginPlaceholder())
                .as("Placeholder поля логина")
                .isEqualTo("Username");

        softly.assertThat(loginPage.isPasswordFieldDisplayed())
                .as("Поле пароля должно отображаться")
                .isTrue();

        softly.assertThat(loginPage.isPasswordFieldEnabled())
                .as("Поле пароля должно быть доступно")
                .isTrue();

        softly.assertThat(loginPage.getPasswordPlaceholder())
                .as("Placeholder поля пароля")
                .isEqualTo("Password");

        softly.assertThat(loginPage.isLoginButtonDisplayed())
                .as("Кнопка логина должна отображаться")
                .isTrue();

        softly.assertThat(loginPage.isLoginButtonEnabled())
                .as("Кнопка логина должна быть доступна")
                .isTrue();

        softly.assertThat(loginPage.getLoginButtonText())
                .as("Текст кнопки логина")
                .isEqualTo("Login");

        softly.assertAll();
    }

    @Test
    @DisplayName("Проверка авторизации с логином и паролем от валидного пользователя")
    @Description("Тест проверяет, что валидный пользователь авторизуется успешно")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест успешной авторизации")
    void checkSuccessfulLoginWithValidUser() {
        int productCartIndex = 0;
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        mainPage.waitForPageLoad();

        assertThat(driver.getCurrentUrl()).as("URL текущей страницы")
                .startsWith("https://")
                .contains(Data.Endpoints.MAIN_PAGE);
        assertAll("Все элементы должны быть видимы",
                () -> assertThat(headerComponent.isPageTitleDisplayed()).isTrue(),
                () -> assertThat(headerComponent.getPageTitleText()).isEqualTo("Swag Labs"),
                () -> assertThat(headerComponent.isMenuButtonDisplayed()).isTrue(),
                () -> assertThat(headerComponent.isMenuButtonEnabled()).isTrue(),
                () -> assertThat(headerComponent.isShoppingCartDisplayed()).isTrue(),
                () -> assertThat(headerComponent.isShoppingCartEnabled()).isTrue());
        assertThat(mainPage.isProductCardDisplayed(productCartIndex)).isTrue();
    }

    @Test
    @DisplayName("Проверка авторизации с несуществующим в базе паролем")
    @Description("Тест проверяет, что невалидный пользователь получает сообщение об ошибке вместо авторизации")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест неуспешной авторизации с невалидным паролем")
    void loginWithWrongPassword_ShouldFailAuth() {
        String expectedErrorMessage = "Epic sadface: Username and password do not match any user in this service";
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.INVALID_PASSWORD);

        assertErrorMessage(expectedErrorMessage);
        assertThatPageNotChanged();
    }

    @Test
    @DisplayName("Проверка авторизации заблокированного пользователя")
    @Description("Тест проверяет, что заблокированный пользователь получает сообщение об ошибке вместо авторизации")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест неуспешной авторизации заблокированного пользователя")
    void loginByBlockedUser_ShouldFailAuth() {
        String expectedErrorMessage = "Epic sadface: Sorry, this user has been locked out.";
        loginPage.login(Data.Login.LOCKED_OUT_LOGIN, Data.Login.VALID_PASSWORD);

        assertErrorMessage(expectedErrorMessage);
        assertThatPageNotChanged();
    }

    @Disabled("Строка из пробелов считается как введённый логин")
    @ParameterizedTest(name = "Пустой логин: {0}")
    @ValueSource(strings = {"", " ", "  "})
    @Tag("validation")
    @DisplayName("Вход с пустым логином должен показывать ошибку и ищется в базе")
    @Step("Тест попытки входа с пустым логином")
    void loginWithEmptyUsername_ShouldShowUsernameRequiredError(String username) {
        String expectedErrorMessage = "Epic sadface: Username is required";
        loginPage.login(username, Data.Login.VALID_PASSWORD);

        assertThat(loginPage.getErrorMessageText())
                .as("Ошибка для пустого логина")
                .isEqualTo(expectedErrorMessage);

        assertThatPageNotChanged();
    }

    @Disabled("Строка из пробелов считается как введённый пароль и ищется в базе")
    @ParameterizedTest(name = "Пустой пароль: {0}")
    @ValueSource(strings = {"", " ", "  "})
    @Tag("validation")
    @DisplayName("Вход с пустым паролем должен показывать ошибку")
    @Step("Тест попытки входа с пустым паролем")
    void loginWithEmptyPassword_ShouldShowPasswordRequiredError(String password) {
        String expectedErrorMessage = "Epic sadface: Password is required";
        loginPage.login(Data.Login.VALID_LOGIN, password);

        assertThat(loginPage.getErrorMessageText())
                .as("Ошибка для пустого пароля")
                .isEqualTo(expectedErrorMessage);

        assertThatPageNotChanged();
    }

    @Test
    @DisplayName("Вход с пустыми обоими полями должен показывать ошибку логина")
    @Tag("validation")
    @Step("Тест попытки входа с пустым логином и паролем")
    void loginWithEmptyBothFields_ShouldShowUsernameRequiredError() {
        String expectedErrorMessage = "Epic sadface: Username is required";
        loginPage.login("", "");

        assertThat(loginPage.getErrorMessageText())
                .as("При пустых обоих полях должна быть ошибка логина")
                .isEqualTo(expectedErrorMessage);

        assertThatPageNotChanged();
    }

    private void assertErrorMessage(String expectedErrorMessage) {
        assertAll("Сообщение об ошибке должно быть видимо и содержать определённый текст",
                () -> assertThat(loginPage.isErrorMessageDisplayed())
                        .as("Сообщение об ошибке должно отображаться").isTrue(),
                () -> assertThat(loginPage.getErrorMessageText())
                        .as("Текст ошибки").isEqualTo(expectedErrorMessage)
        );
    }

    private void assertThatPageNotChanged() {
        assertThat(driver.getCurrentUrl())
                .as("После ошибки пользователь должен остаться на странице авторизации")
                .isEqualTo(TestConfig.getBaseUrl())
                .doesNotContain(Data.Endpoints.MAIN_PAGE);
    }
}
