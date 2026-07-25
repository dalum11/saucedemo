package tests.auth.ui;

import core.assertions.AuthAssertions;
import core.utils.CursorUtils;
import core.utils.TestUtils;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.auth.LoginPage;
import pages.auth.LoginPageAssertions;
import tests.common.BaseTest;

@Tag("regression")
@Tag("ui")
@Tag("smoke")
@Tag("validation")
@Epic("Авторизация")
@Severity(SeverityLevel.NORMAL)
@Feature("Валидация полей формы авторизации")
@DisplayName("Тесты валидации полей авторизации")
public class LoginPageFieldsValidation extends BaseTest {

    private LoginPage loginPage;
    private AuthAssertions authAssertions;
    private LoginPageAssertions loginPageAssertions;
    private CursorUtils cursorUtils;

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

    @DisplayName("ID 14 - Проверка ограничения длины поля Username на фронте")
    @ParameterizedTest(name = "Валидная длина логина: {0} символов")
    @Description("Тест проверяет возможность ввода логина с допустимым количеством символов")
    @Step("Тест валидной длины логина: {0} символов")
    @ValueSource(ints = {1, 15, 30})
    void loginLengthBoundaryValue_ShouldPassValidation(int length) {
        String login = TestUtils.generateRandomString(length);

        loginPage.enterLogin(login);
        loginPage.blurActiveElement();

        loginPageAssertions.assertUsernameTextLength(length);
        loginPageAssertions.verifyUsernameFieldText(login);
    }

    @Disabled("Баг - нет ограничения на количество символов для ввода логина")
    @ParameterizedTest(name = "Невалидная длина логина: {0} символов")
    @ValueSource(ints = {31, 32})
    @DisplayName("ID 14 - Проверка ограничения длины поля Username на фронте")
    @Step("Тест превышения количества символов логина: {0} символов")
    void loginLengthBoundaryValue_ShouldFailValidation(int length) {
        String login = TestUtils.generateRandomString(length);
        int expectedLength = 30;

        loginPage.enterLogin(login);
        loginPage.blurActiveElement();

        loginPageAssertions.assertUsernameTextLength(expectedLength);
    }

    @DisplayName("ID 15 - Проверка ограничения длины поля Password на фронте")
    @ParameterizedTest(name = "Валидная длина пароля: {0} символов")
    @Description("Тест проверяет возможность ввода пароля с допустимым количеством символов")
    @Step("Тест валидной длины пароля: {0} символов")
    @ValueSource(ints = {1, 15, 30})
    void passwordLengthBoundaryValue_ShouldPassValidation(int length) {
        String password = TestUtils.generateRandomString(length);

        loginPage.enterPassword(password);
        loginPage.blurActiveElement();

        loginPageAssertions.assertPasswordTextLength(length);
        authAssertions.assertPasswordIsMasked(password, password.length());
    }

    @Disabled("Баг - нет ограничения на количество символов для ввода пароля")
    @ParameterizedTest(name = "невалидная длина пароля: {0} символов")
    @ValueSource(ints = {31, 32})
    @DisplayName("ID 15 - Проверка ограничения длины поля Password на фронте")
    @Step("Тест превышения количества символов пароля: {0} символов")
    void passwordLengthBoundaryValue_ShouldFailValidation(int length) {
        String password = TestUtils.generateRandomString(length);
        int expectedLength = 30;

        loginPage.enterPassword(password);
        loginPage.blurActiveElement();

        loginPageAssertions.assertPasswordTextLength(expectedLength);
        authAssertions.assertPasswordIsMasked(password, expectedLength);
    }
}
