package tests.auth.usecase;

import core.annotations.CurrentBrowser;
import core.assertions.AuthAssertions;
import core.config.Browser;
import core.utils.ClipboardUtils;
import core.utils.CursorUtils;
import data.Data;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import pages.auth.LoginPage;
import pages.auth.LoginPageAssertions;
import pages.main.MainPage;
import tests.common.BaseTest;

@DisplayName("Кросс-браузерные тесты авторизации (Хром/Яндекс)")
@Tag("cross-browser")
@Tag("smoke")
@Severity(SeverityLevel.CRITICAL)
public class CrossBrowserTest extends BaseTest {

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

    @Step("Выполнить успешную авторизацию")
    private void checkSuccessfulAuth() {
        int productCardIndex = 0;
        loginPage.login(Data.Login.VALID_LOGIN, Data.Login.VALID_PASSWORD);

        mainPage.waitForPageLoad();
        authAssertions.assertUserIsLoggedIn(productCardIndex);
    }

    @Step("Выполнить проверку копирования и вставки")
    private void checkCopyPaste() {
        String expectedUsername = Data.Login.VALID_LOGIN;
        String expectedPassword = Data.Login.VALID_PASSWORD;

        loginPage.enterLogin(expectedUsername);

        String copiedUsername = loginPage.getUsernameText();
        ClipboardUtils.copyToClipboard(copiedUsername);

        loginPageAssertions.assertThatCopiedUsernameEqualsExpected(expectedUsername);

        loginPage.clearUsername();
        loginPageAssertions.assertUsernameIsEmpty();
        ClipboardUtils.pasteToClipboard(loginPage.getUsername());
        loginPageAssertions.verifyUsernameFieldText(expectedUsername);

        loginPage.enterPassword(expectedPassword);
        ClipboardUtils.copyToClipboard(loginPage.getPasswordText());

        loginPageAssertions.assertThatCopiedPasswordEqualsExpected(expectedPassword);

        loginPage.clearPassword();
        loginPageAssertions.assertPasswordIsEmpty();
        ClipboardUtils.pasteToClipboard(loginPage.getPassword());
        authAssertions.assertPasswordIsMasked(expectedPassword, expectedPassword.length());
    }

    @Test
    @DisplayName("ID 23 - Успешная первая авторизация в Яндекс Браузере")
    @Description("Тест проверяет, что валидный пользователь авторизуется успешно в Яндекс Браузере")
    @Step("Тест успешной авторизации в Яндекс Браузере")
    @CurrentBrowser(Browser.Yandex)
    void checkYandexSuccessAuth() {
        checkSuccessfulAuth();
    }

    @Test
    @DisplayName("ID 25 - Успешная первая авторизация в Google Chrome")
    @Description("Тест проверяет, что валидный пользователь авторизуется успешно в Google Chrome")
    @Step("Тест успешной авторизации в Google Chrome")
    @CurrentBrowser(Browser.GOOGLE_CHROME)
    void checkGoogleChromeSuccessAuth() {
        checkSuccessfulAuth();
    }

    @Test
    @DisplayName("ID 24 - Копирование и вставка в Яндекс Браузере")
    @Description("Проверка, что в Яндекс Браузере работают копирование и вставка в полях ввода")
    @Step("Тест копирования и вставки в Яндекс Браузере")
    @CurrentBrowser(Browser.Yandex)
    @DisabledIfSystemProperty(named = "ci", matches = "true")
    void checkYandexCopyPaste() {
        checkCopyPaste();
    }

    @Test
    @DisplayName("ID 26 - Копирование и вставка в Google Chrome")
    @Description("Проверка, что в Google Chrome работают копирование и вставка в полях ввода")
    @Step("Тест копирования и вставки в Google Chrome")
    @CurrentBrowser(Browser.GOOGLE_CHROME)
    @DisabledIfSystemProperty(named = "ci", matches = "true")
    void checkGoogleChromeCopyPaste() {
        checkCopyPaste();
    }
}
