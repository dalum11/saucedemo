package tests.auth.usecase;

import core.assertions.AuthAssertions;
import core.utils.CursorUtils;
import pages.auth.LoginPageAssertions;
import tests.common.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pages.auth.LoginPage;
import pages.main.MainPage;
import data.Data;

@Epic("Авторизация")
@Feature("Вход")
@Story("Авторизация пользователя")
@DisplayName("Тесты успешной авторизации")
@Severity(SeverityLevel.CRITICAL)
public class SuccessLoginTest extends BaseTest {

    private LoginPage loginPage;
    private MainPage mainPage;
    private AuthAssertions authAssertions;
    private LoginPageAssertions loginPageAssertions;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo)  {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        loginPageAssertions = new LoginPageAssertions(loginPage, driver, new CursorUtils(driver));
        openLoginPage();
    }

    @Test
    @DisplayName("ID 5 - Успешная первая авторизация")
    @Description("Проверка успешной авторизации пользователя, который ни разу не посещал сайт")
    @Tag("smoke")
    @Tag("regression")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Выполнить успешный вход в систему")
    void checkSuccessfulLoginWithValidUser() {
        int productCardIndex = 0;
        authAssertions.assertCredentialsAreFilled(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        loginPage.clickLogin();
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
        authAssertions.assertCredentialsAreFilled(Data.Login.PERFORMANCE_GLITCH_LOGIN, Data.Login.VALID_PASSWORD);
        loginPage.clickLogin();
        mainPage.waitForPageLoad();

        authAssertions.assertUserIsLoggedIn(productCardIndex);
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
