package tests.auth.usecase;

import core.assertions.AuthAssertions;
import core.utils.CursorUtils;
import pages.auth.LoginPageAssertions;
import tests.common.BaseTest;
import core.config.TestConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.auth.LoginPage;
import data.Data;
import core.utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("Вход")
@Story("Авторизация пользователя")
@Severity(SeverityLevel.CRITICAL)
@DisplayName("Тесты неуспешной авторизации")
public class ErrorLoginTest extends BaseTest {

    private LoginPage loginPage;
    private AuthAssertions authAssertions;
    private CursorUtils cursorUtils;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        cursorUtils = new CursorUtils(driver);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с неподходящим паролем")
    @Description("Тест проверяет, что невалидный пользователь получает сообщение об ошибке вместо авторизации")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест неуспешной авторизации с невалидным паролем")
    @Disabled("Баг - одно сообщение об ошибке накладывается на другое")
    void loginWithWrongPassword_ShouldFailAuth() {
        openLoginPage();
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.INVALID_PASSWORD);

        authAssertions.assertGettingErrorMessageToLogin(Data.ErrorMessages.INVALID_CREDENTIALS);
        authAssertions.assertPageNotChanged(driver, TestConfig.BASE_URL);
    }

    @Test
    @DisplayName("Проверка авторизации заблокированного пользователя")
    @Description("Тест проверяет, что заблокированный пользователь получает сообщение об ошибке вместо авторизации")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест неуспешной авторизации заблокированного пользователя")
    void loginByBlockedUser_ShouldFailAuth() {
        openLoginPage();
        loginPage.login(Data.Login.LOCKED_OUT_LOGIN, Data.Login.VALID_PASSWORD);

        authAssertions.assertGettingErrorMessageToLogin(Data.ErrorMessages.BLOCKED_USER);
        authAssertions.assertPageNotChanged(driver, TestConfig.BASE_URL);
    }

    @Disabled("Строка из пробелов считается как введённый логин")
    @ParameterizedTest(name = "Пустой логин: {0}")
    @ValueSource(strings = {"", " ", "  "})
    @Tag("validation")
    @DisplayName("Получение ошибки при входе с пустым логином")
    @Step("Тест попытки входа с пустым логином")
    void loginWithEmptyUsername_ShouldShowUsernameRequiredError(String username) {
        openLoginPage();
        loginPage.login(username, Data.Login.VALID_PASSWORD);

        authAssertions.assertGettingErrorMessageToLogin(Data.ErrorMessages.EMPTY_USERNAME);
        authAssertions.assertPageNotChanged(driver, TestConfig.BASE_URL);
    }

    @Disabled("Строка из пробелов считается как введённый пароль и ищется в базе")
    @ParameterizedTest(name = "Пустой пароль: {0}")
    @ValueSource(strings = {"", " ", "  "})
    @Tag("validation")
    @DisplayName("Получение ошибки при входе с пустым паролем")
    @Step("Тест попытки входа с пустым паролем")
    void loginWithEmptyPassword_ShouldShowPasswordRequiredError(String password) {
        openLoginPage();
        loginPage.login(Data.Login.VALID_LOGIN, password);

        authAssertions.assertGettingErrorMessageToLogin(Data.ErrorMessages.EMPTY_PASSWORD);
        authAssertions.assertPageNotChanged(driver, TestConfig.BASE_URL);
    }

    @Test
    @DisplayName("Вход с пустыми логином и паролем должен показывать ошибку валидации логина")
    @Tag("validation")
    @Tag("regression")
    @Step("Тест попытки входа с пустым логином и паролем")
    @Disabled("Баг - вместо ошибки валидации логина показывает ошибку для заблокированного пользователя")
    void loginWithEmptyBothFields_ShouldShowUsernameRequiredError() {
        openLoginPage();
        loginPage.login("", "");

        authAssertions.assertGettingErrorMessageToLogin(Data.ErrorMessages.EMPTY_USERNAME);
        authAssertions.assertPageNotChanged(driver, TestConfig.BASE_URL);
    }

    @Test
    @DisplayName("Поля авторизации очищаются после обновления страницы")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест сброса состояния полей авторизации после обновления страницы")
    void loginFieldsStateAfterRefresh_ShouldDisplayEmptyFields(){
        openLoginPage();
        loginPage.enterLogin(Data.Login.VALID_LOGIN);
        loginPage.enterPassword(Data.Login.VALID_PASSWORD);
        loginPage.refresh();

        LoginPageAssertions loginPageAssertions = new LoginPageAssertions(loginPage, driver, cursorUtils);
        loginPageAssertions.assertCredentialsIsEmpty();
    }
}
