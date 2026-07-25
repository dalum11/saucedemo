package tests.auth.ui;

import core.assertions.AuthAssertions;
import core.utils.ClipboardUtils;
import core.utils.CursorUtils;
import data.Data;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import pages.auth.LoginPage;
import pages.auth.LoginPageAssertions;
import tests.common.BaseTest;

@DisplayName("Тесты состония полей авторизации")
@Tag("regression")
@Tag("ui")
@Epic("Авторизация")
@Story("Авторизация пользователя")
@Feature("Состояние полей формы авторизации")
public class LoginPageFieldsState extends BaseTest {

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

    @Test
    @DisplayName("ID 1 - Проверка начального состояния полей ввода")
    @Description("Проверка, что поля Username и Password отображаются пустыми с корректными плейсхолдерами при загрузке страницы")
    @Step("Проверка начального состояния полей ввода")
    @Severity(SeverityLevel.CRITICAL)
    void authPage_checkStartFieldsState() {
        authAssertions.assertLoginPageIsDisplayedCorrectly();
    }

    @Test
    @DisplayName("ID 2 - Проверка ввода и маскирования пароля")
    @Description("Проверка, что символы в поле Password маскируются по умолчанию, и видимостью элемента можно управлять\n"
            + "через кнопку Глаз")
    @Step("Проверка ввода пароля и маскирования символов")
    @Severity(SeverityLevel.CRITICAL)
    void authPage_checkInputAndPasswordMask() {
        loginPage.enterPassword(Data.Login.VALID_PASSWORD);
        authAssertions.assertPasswordIsMasked(Data.Login.VALID_PASSWORD, Data.Login.VALID_PASSWORD.length());

        loginPage.clickOnEyeIcon();
        loginPageAssertions.verifyPasswordFieldText(Data.Login.VALID_PASSWORD);

        loginPage.clickOnEyeIcon();
        authAssertions.assertPasswordIsMasked(Data.Login.VALID_PASSWORD, Data.Login.VALID_PASSWORD.length());
    }

    @Test
    @DisplayName("ID 3 - Проверка очистки полей")
    @Description("Проверка очистки полей с помощью клавиатуры и кнопки Крестик")
    @Disabled("Баг - не появлется кнопка Крестик")
    @Step("Проверка изменения и удаления данных в полях формы авторизации")
    @Severity(SeverityLevel.CRITICAL)
    void authPage_cleanFields_ShouldCleanCorrectly() {
        loginPage.enterLogin(Data.Login.VALID_LOGIN);
        loginPageAssertions.assertUsernameFilled(Data.Login.VALID_LOGIN);

        loginPage.clickOnCrossButtonUsername();
        loginPageAssertions.assertUsernameCleared();

        loginPage.enterLogin(Data.Login.VALID_LOGIN);

        loginPage.clearUsernameWithBackspace();
        loginPageAssertions.assertUsernameCleared();

        loginPage.enterPassword(Data.Login.VALID_PASSWORD);
        loginPageAssertions.assertPasswordFilled(Data.Login.VALID_LOGIN);

        loginPage.clickOnCrossButtonPassword();
        loginPageAssertions.assertPasswordCleared();

        loginPage.enterPassword(Data.Login.VALID_PASSWORD);;

        loginPage.clearPasswordWithBackspace();
        loginPageAssertions.assertPasswordCleared();
    }

    @Test
    @DisplayName("ID 4 - Проверка работы инфоблока с данными для авторизации")
    @Description("Проверка работы инфоблока и возможности копировать данные")
    @Step("Проверка возможности копирования данных из инфоблока")
    @Severity(SeverityLevel.NORMAL)
    void authPage_InfoCellCopy_ShouldCopyCorrectly() {
        int neededUsernameIndex = 0;
        String neededUsername = Data.Login.VALID_LOGIN;
        String neededPassword = Data.Login.VALID_PASSWORD;

        loginPage.clickOnUsernameInInfoBlock(neededUsername);
        loginPage.clickOnPasswordInInfoBlock();

        ClipboardUtils.copyToClipboard(loginPage.getUsernameInInfoBlockText(neededUsernameIndex));
        ClipboardUtils.pasteToClipboard(loginPage.getUsername());
        loginPageAssertions.assertThatCopiedUsernameEqualsExpected(neededUsername);
        loginPageAssertions.verifyUsernameFieldText(neededUsername);

        ClipboardUtils.copyToClipboard(loginPage.getAvailablePassword());
        ClipboardUtils.pasteToClipboard(loginPage.getPassword());
        loginPageAssertions.assertThatCopiedPasswordEqualsExpected(neededPassword);
        authAssertions.assertPasswordIsMasked(neededPassword, neededPassword.length());
    }
}
