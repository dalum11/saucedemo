package tests.auth.usecase;

import core.assertions.AuthAssertions;
import core.config.TestConfig;
import core.utils.CursorUtils;
import core.utils.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.auth.LoginPageAssertions;
import scenario.auth.PerformLoginScenario;
import tests.common.BaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pages.auth.LoginPage;
import pages.main.MainPage;
import data.Data;

import static org.assertj.core.api.Assertions.assertThat;

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
    private NetworkUtils networkUtils;
    private static final Logger log = LoggerFactory.getLogger(SuccessLoginTest.class);

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo)  {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        loginPageAssertions = new LoginPageAssertions(loginPage, driver, new CursorUtils(driver));
        networkUtils = new NetworkUtils(driver);
    }

    @Test
    @DisplayName("ID 5 - Успешная первая авторизация")
    @Description("Проверка успешной авторизации пользователя, который ни разу не посещал сайт")
    @Tag("smoke")
    @Tag("regression")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Выполнить успешный вход в систему")
    void checkSuccessfulLoginWithValidUser() {
        openLoginPage();
        int productCardIndex = 0;
        authAssertions.assertCredentialsAreFilled(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        loginPage.clickLogin();
        mainPage.waitForPageLoad();
        authAssertions.assertUserIsLoggedIn(productCardIndex);
    }

    @Test
    @DisplayName("Авторизация при необычно долгой загрузке страницы")
    @Description("Тест проверяет необычно долгую загрузку страницы")
    @Tag("smoke")
    @Tag("regression")
    @Step("Тест долгой загрузки страницы")
    void loginWithPerformanceGlitchUser_ShouldLoadPageWithTimeout() {
        openLoginPage();
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
        openLoginPage();
        int productCardIndex = 0;
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);
        mainPage.waitForPageLoad();

        authAssertions.assertUserIsLoggedIn(productCardIndex);
        mainPage.logout();

        authAssertions.assertUserIsLoggedOut();
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("ID 17 - Проверка авторизации при потере соединения, если пользователь уже посещал сайт")
    @Description("Проверка, что при повторном посещении сайта и потере соединения отображается страница авторизации," +
            "возможен вход в систему")
    @Step("Проверить первую авторизацию пользователя при потере соединения (с кэшем)")
    void authWithoutConnection_FirstLoginWithCache_ShouldLoginCorrectly() {
        networkUtils.setIsSavePassword("true");
        networkUtils.enableCache();

        openLoginPage();
        authAssertions.assertLoginPageIsDisplayedCorrectly();

        loginPage.refresh();
        authAssertions.assertLoginPageIsDisplayedCorrectly();

        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);
        mainPage.waitForPageLoad();

        mainPage.logout();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        networkUtils.clearCookies();
        networkUtils.setNetworkConditions(true, false);

        authAssertions.assertLoginPageIsDisplayedCorrectly();
        authAssertions.assertCredentialsAreFilled(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        loginPage.clickLogin();
        authAssertions.assertUserIsLoggedIn(0);

        networkUtils.enableNetwork();
        networkUtils.setIsSavePassword("false");
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("ID 27 - Проверка автозаполнения сохранёнными данными")
    @Description("Проверка успешной авторизации пользователя с сохранёнными в браузере данными")
    @Step("Проверить успешную авторизацию с сохранёнными в браузере данными")
    void authWithSavedCredentials_ShouldAuthSuccessful() {
        String username = Data.Login.VALID_LOGIN;
        String password = Data.Login.VALID_PASSWORD;
        networkUtils.enableCache();

        openLoginPage();

        networkUtils.saveToLocalStorage("savedUsername", username);
        networkUtils.saveToLocalStorage("savedPassword", password);

        String savedUsername = networkUtils.getFromLocalStorage("savedUsername");
        String savedPassword = networkUtils.getFromLocalStorage("savedPassword");
        loginPage.enterUsername(savedUsername);
        loginPage.enterPassword(savedPassword);

        loginPageAssertions.verifyUsernameFieldText(savedUsername);
        authAssertions.assertPasswordIsMasked(savedPassword, savedPassword.length());
        loginPageAssertions.verifyLoginButtonEnabled();

        loginPage.clickLogin();
        mainPage.waitForPageLoad();
        mainPage.logout();
        loginPage.refresh();

        String reloadedUsername = networkUtils.getFromLocalStorage("savedUsername");
        String reloadedPassword = networkUtils.getFromLocalStorage("savedPassword");

        loginPage.enterUsername(reloadedUsername);
        loginPage.enterPassword(reloadedPassword);

        loginPageAssertions.verifyUsernameFieldText(reloadedUsername);
        authAssertions.assertPasswordIsMasked(reloadedPassword, reloadedPassword.length());
        loginPageAssertions.verifyLoginButtonEnabled();

        loginPage.clickLogin();
        authAssertions.assertUserIsLoggedIn(0);

        networkUtils.clearLocalStorage();
    }

    @Test
    @Tag("smoke")
    @Tag("regression")
    @DisplayName("ID 16 - Проверка получения ошибки при  потере соединения, если пользователь впервые на сайте")
    @Description("Проверка, что при первом посещении сайта и потере соединения отображается ошибка браузера," +
            "а не кастомная")
    @Step("Проверить первую авторизацию пользователя при потере соединения (нет кэша)")
    void authWithoutConnection_FirstLoginWithoutCache_ShouldLoginCorrectly() {
        networkUtils.setNetworkConditions(true, true);
        try {
            openLoginPage();
        } catch (Exception e) {
            log.info("Получена ошибка при открытии страницы {}: {}", driver.getCurrentUrl(), e.getMessage());
        }

        authAssertions.assertGettingBrowserConnectionError();
        networkUtils.enableNetwork();
        loginPage.refresh();

        authAssertions.assertLoginPageIsDisplayedCorrectly();
    }
}
