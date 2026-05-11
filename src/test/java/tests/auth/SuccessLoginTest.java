package tests.auth;

import assertions.AuthAssertions;
import base.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pageobject.LoginPage;
import pageobject.MainPage;
import utils.Data;
import utils.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Авторизация")
@Feature("Вход")
@Story("Авторизация пользователя")
@Severity(SeverityLevel.CRITICAL)
@DisplayName("Тесты успешной авторизации")
public class SuccessLoginTest extends BaseTest {

    private LoginPage loginPage;
    private MainPage mainPage;
    private AuthAssertions authAssertions;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        openLoginPage();
    }

    @Test
    @DisplayName("Успешная авторизация пользователя с валидными логином и паролем")
    @Description("Тест проверяет, что валидный пользователь авторизуется успешно")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест успешной авторизации")
    void checkSuccessfulLoginWithValidUser() {
        int productCardIndex = 0;
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        mainPage.waitForPageLoad();
        authAssertions.assertUserIsLoggedIn(productCardIndex);
    }

    @Test
    @DisplayName("Авторизация при необычно долгой загрузке страницы")
    @Description("Тест проверяет необычно долгую зашрузку страницы")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест долгой загрузки страницы")
    void loginWithPerformanceGlitchUser_ShouldLoadPageWithTimeout() {
        int productCardIndex = 0;
        loginPage.login(Data.Login.PERFORMANCE_GLITCH_LOGIN, Data.Login.VALID_PASSWORD);
        mainPage.waitForPageLoad();

        authAssertions.assertUserIsLoggedIn(productCardIndex);
    }

    @DisplayName("Логин с допустимым количеством символов - должен пройти валидацию")
    @ParameterizedTest
    @Tag("smoke")
    @Tag("regression")
    @Description("Тест проверяет возможность ввода логина с допустимым количеством символов")
    @Step("Тест валидной длины логина")
    @ValueSource(ints = {1, 15, 30})
    void loginLengthBoundaryValue_ShouldPassValidation(int length) {
        String login = TestUtils.generateRandomString(length);
        loginPage.enterLogin(login);
        loginPage.blurActiveElement();
        assertThat(loginPage.isErrorMessageDisplayed()).isFalse();
    }

    @Test
    @DisplayName("Логин после разлогина - корректный перезаход в систему")
    @Tag("smoke")
    @Tag("regression")
    @Description("Тест проверяет возможность повторного логина")
    @Step("Тест повторного логина")
    void loginAfterLogout_ShouldLoginAndLogoutCorrectly() {
        int productCardIndex = 0;
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);
        mainPage.waitForPageLoad();

        authAssertions.assertUserIsLoggedIn(productCardIndex);
        mainPage.logout();

        authAssertions.assertUserIsLoggedOut();
    }
}
