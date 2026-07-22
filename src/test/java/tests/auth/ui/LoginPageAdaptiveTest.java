package tests.auth.ui;

import core.annotations.BrowserResolution;
import core.assertions.AuthAssertions;
import core.config.BrowserOrientation;
import core.utils.CursorUtils;
import core.utils.KeyboardUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import pages.auth.LoginPage;
import pages.auth.LoginPageAssertions;
import pages.main.MainPage;
import scenario.auth.PerformLoginScenario;
import tests.common.BaseTest;

@DisplayName("Тесты адаптивности для страницы авторизации")
@Tag("regression")
@Tag("ui")
public class LoginPageAdaptiveTest extends BaseTest {

    private LoginPage loginPage;
    private MainPage mainPage;
    private AuthAssertions authAssertions;
    private LoginPageAssertions loginPageAssertions;
    private CursorUtils cursorUtils;
    private PerformLoginScenario performLoginScenario;
    private KeyboardUtils keyboardUtils;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
        authAssertions = new AuthAssertions(loginPage);
        cursorUtils = new CursorUtils(driver);
        keyboardUtils = new KeyboardUtils(driver);
        loginPageAssertions = new LoginPageAssertions(loginPage, driver, cursorUtils);
        performLoginScenario= new PerformLoginScenario(loginPage, loginPageAssertions, authAssertions, cursorUtils
                ,mainPage, keyboardUtils);
        openLoginPage();
    }

    @Test
    @BrowserResolution(BrowserOrientation.MB)
    @Step("Проверка отображения формы авторизации в мобильном разрешении")
    @Description("Тест проверет отобржение элементов страницы авторизации в мобильном разрешении")
    @DisplayName("ID 18 - Проверка отображения страницы авторизации в мобильном разрешении")
    void mobileView_ElementsShouldDisplayCorrectly() {
        authAssertions.assertLoginPageIsDisplayedCorrectly();

        loginPageAssertions
                .assertLogoIsCentered()
                .assertLoginFieldsWidthInPercent();
    }

    @Test
    @BrowserResolution(BrowserOrientation.MB)
    @DisplayName("ID 19 - Проверка функциональности элементов страницы в мобильном разрешении")
    @Description("Тест проверяет работу формы авторизации на мобильном разрешении браузера")
    @Step("Проверка интерактивности формы в мобильно разрешении")
    @Disabled("Баг - кнопка Логин доступна без заполнения всех обязательных полей")
    void mobileActions_PageShouldBeInteractive() {
        performLoginScenario.performSuccessfulLogin();
    }

    @Test
    @BrowserResolution(BrowserOrientation.DESKTOP)
    @Description("Тест проверяет отображение формы авторизации на десктопном разрешении браузера")
    @Step("Проверка отображения формы в десктопном разрешении")
    @DisplayName("ID 20 - Проверка отображения страницы авторизации в десктопном разрешении")
    void desktopView_ElementsShouldDisplayCorrectly() {
        authAssertions.assertLoginPageIsDisplayedCorrectly();

        loginPageAssertions
                .assertLogoIsCentered()
                .assertLoginFieldsWidthInPercent();
    }

    @Test
    @BrowserResolution(BrowserOrientation.DESKTOP)
    @Description("Тест проверяет интерактивность формы авторизации на десктопном разрешении браузера")
    @Step("Проверка работы формы в десктопном разрешении")
    @DisplayName("ID 21 - Проверка функциональности элементов страницы в десктопном разрешении")
    @Disabled("Баг - кнопка Логин доступна без заполнения всех обязательных полей")
    void desktopActions_PageShouldBeInteractive() {
        performLoginScenario.performSuccessfulLoginWithCursorChecks();
    }
}
