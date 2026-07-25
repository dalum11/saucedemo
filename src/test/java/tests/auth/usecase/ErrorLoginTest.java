package tests.auth.usecase;

import core.assertions.AuthAssertions;
import core.utils.CursorUtils;
import pages.auth.LoginPageAssertions;
import tests.common.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.auth.LoginPage;
import data.Data;

@Epic("Авторизация")
@Feature("Вход")
@Story("Авторизация пользователя")
@Severity(SeverityLevel.CRITICAL)
@DisplayName("Тесты неуспешной авторизации")
public class ErrorLoginTest extends BaseTest {

    private LoginPage loginPage;
    private AuthAssertions authAssertions;
    private CursorUtils cursorUtils;
    private LoginPageAssertions loginPageAssertions;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        cursorUtils = new CursorUtils(driver);
        loginPageAssertions = new LoginPageAssertions(loginPage, driver, cursorUtils);
        openLoginPage();
    }

    @Test
    @DisplayName("ID 10 - Авторизация пользователя, которого нет в системем")
    @Description("Проверка, что система не позволяет выполнить вход для пользователя, данных которого нет в системе, и \n" +
            "получение ошибки авторизации")
    @Tag("smoke")
    @Tag("regression")
    @Step("Выполнить авторизацию с данными, которых нет в системе")
    void loginWithWrongCredentials_ShouldFailAuth() {
        String invalidLogin = "unknown_user";
        String invalidPassword = "unknown_password";

        authAssertions.assertCredentialsAreFilled(invalidLogin, invalidPassword);
        loginPage.clickLogin();

        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("ID 12 - Попытка авторизации заблокированного пользователя")
    @Description("Проверка, что заблокированный пользователь не сможет войти в систему и получит соответствующую ошибку")
    @Tag("smoke")
    @Tag("regression")
    @Step("Ввести данные заблокированного пользователя")
    void loginByBlockedUser_ShouldFailAuth() {
        authAssertions.assertCredentialsAreFilled(Data.Login.LOCKED_OUT_LOGIN, Data.Login.VALID_PASSWORD);
        loginPage.clickLogin();

        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.BLOCKED_USER);
    }

    @Test
    @Tag("validation")
    @Tag("regression")
    @DisplayName("ID 6 - Пустое поле Username")
    @Description("Проверка, что нельзя авторизоваться с пустым логином и получение ошибки")
    @Step("Попытка входа с пустым логином")
    void loginWithEmptyUsername_ShouldShowUsernameRequiredError() {
        String expectedPassword = Data.Login.VALID_PASSWORD;

        authAssertions.assertCredentialsAreFilled("", expectedPassword);
        loginPageAssertions.assertUsernameIsEmpty();

        loginPage.clickLogin();
        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.EMPTY_USERNAME);
    }

    @Tag("validation")
    @Tag("regression")
    @ParameterizedTest
    @Disabled("Строка из пробелов считается как введённый логин")
    @ValueSource(strings = {" ", "  ", "   "})
    @DisplayName("ID 8 - Поле Username заполнено пробелами")
    @Description("Проверка, что система не позволяет выполнить вход, если поле Username ссодержит только пробелы,\n" +
            "и отображает сообщение об ошибке валидации")
    @Step("Попытка входа с логином из пробелов")
    void loginWithBlankUsername_ShouldShowUsernameRequiredError(String username) {
        authAssertions.assertCredentialsAreFilled(username, Data.Login.VALID_PASSWORD);

        loginPage.clickLogin();
        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.EMPTY_USERNAME);
    }


    @Test
    @Tag("validation")
    @Tag("regression")
    @DisplayName("ID 7 - Пустое поле Password")
    @Description("Проверка, что нельзя авторизоваться с пустым паролем и получение ошибки")
    @Step("Попытка входа с пустым паролем")
    void loginWithEmptyPassword_ShouldShowPasswordRequiredError() {
        loginPage.enterLogin(Data.Login.VALID_LOGIN);
        loginPageAssertions
                .verifyUsernameFieldText(Data.Login.VALID_LOGIN)
                .assertPasswordIsEmpty();

        loginPage.clickLogin();
        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.EMPTY_PASSWORD);
    }

    @Tag("validation")
    @Tag("regression")
    @ParameterizedTest
    @Disabled("Строка из пробелов считается как введённый пароль")
    @ValueSource(strings = {" ", "  ", "   "})
    @DisplayName("ID 9 - Поле Password заполнено пробелами")
    @Description("Проверка, что система не позволяет выполнить вход, если поле Username ссодержит только пробелы,\n" +
            "и отображает сообщение об ошибке валидации")
    @Step("Попытка входа с паролем из пробелов")
    void loginWithBlankPassword_ShouldShowUsernameRequiredError(String password) {
        authAssertions.assertCredentialsAreFilled(Data.Login.VALID_PASSWORD, password);

        loginPage.clickLogin();
        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.EMPTY_PASSWORD);
    }

    @Test
    @DisplayName("Вход с пустыми логином и паролем должен показывать ошибку валидации логина")
    @Tag("validation")
    @Tag("regression")
    @Step("Тест попытки входа с пустым логином и паролем")
    @Disabled("Баг - вместо ошибки валидации логина показывает ошибку для заблокированного пользователя")
    void loginWithEmptyBothFields_ShouldShowUsernameRequiredError() {
        loginPage.login("", "");

        authAssertions.assertPageNotChangedAfterGettingError(Data.ErrorMessages.EMPTY_USERNAME);
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("ID 13 - Сброс данных формы после обновления страницы")
    @Description("Проверка, что данные не сохраняются после обновления страницы авторизации, если они не сохранены в браузере")
    @Step("Сбросить состояние полей авторизации после обновления страницы")
    void loginFieldsStateAfterRefresh_ShouldDisplayEmptyFields(){
        authAssertions.assertCredentialsAreFilled(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);
        loginPage.refresh();

        loginPageAssertions.assertCredentialsIsEmpty();
    }
}
