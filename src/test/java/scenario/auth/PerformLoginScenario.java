package scenario.auth;

import core.assertions.AuthAssertions;
import core.utils.ClipboardUtils;
import core.utils.CursorUtils;
import data.Data;
import core.utils.KeyboardUtils;
import io.qameta.allure.Step;
import pages.auth.LoginPage;
import pages.auth.LoginPageAssertions;
import pages.main.MainPage;

public class PerformLoginScenario {

    private final LoginPage loginPage;
    private final LoginPageAssertions loginPageAssertions;
    private final AuthAssertions authAssertions;
    private final CursorUtils cursorUtils;
    private final MainPage mainPage;
    private final KeyboardUtils keyboardUtils;
    private static final int EXPECTED_PRODUCTS_COUNT = 6;


    public PerformLoginScenario(LoginPage loginPage, LoginPageAssertions loginPageAssertions,
                                AuthAssertions authAssertions, CursorUtils cursorUtils,
                                MainPage mainPage, KeyboardUtils keyboardUtils) {
        this.loginPage = loginPage;
        this.loginPageAssertions = loginPageAssertions;
        this.authAssertions = authAssertions;
        this.cursorUtils = cursorUtils;
        this.mainPage = mainPage;
        this.keyboardUtils = keyboardUtils;
    }

    @Step("Выполнить успешный логин")
    public void performSuccessfulLogin() {
        loginPage.enterUsername(Data.Login.VALID_LOGIN);
        loginPageAssertions.verifyUsernameFieldText(Data.Login.VALID_LOGIN);
//        loginPageAssertions.verifyLoginButtonDisabled();

        loginPage.enterPassword(Data.Login.VALID_PASSWORD);
        authAssertions.assertPasswordIsMasked(Data.Login.VALID_PASSWORD, Data.Login.VALID_PASSWORD.length());
        loginPageAssertions.verifyLoginButtonEnabled();

        loginPage.clickLogin();
        mainPage.waitForPageLoad();
        authAssertions.assertUserIsLoggedIn(EXPECTED_PRODUCTS_COUNT);
    }

    @Step("Выполнить успешный логин с проверкой курсора")
    public void performSuccessfulLoginWithCursorChecks() {
        loginPage.hoverOverUsernameField();
        cursorUtils.assertCursorIsDefault(loginPage.getUsername());

        loginPage.hoverOverPasswordField();
        cursorUtils.assertCursorIsDefault(loginPage.getPassword());

        loginPage.hoverOverLoginButton();
        cursorUtils.assertCursorIsPointer(loginPage.getLoginButton());

        loginPage.clickOnUsernameField();
        keyboardUtils.pressTab();
        cursorUtils.assertThatCursorInField(loginPage.getPassword());

        keyboardUtils.pressTab();
        cursorUtils.assertCursorIsDefault(loginPage.getUsername());

        ClipboardUtils.copyToClipboard(Data.Login.VALID_LOGIN);
        ClipboardUtils.pasteToClipboard(loginPage.getUsername());
        loginPageAssertions.verifyUsernameFieldText(Data.Login.VALID_LOGIN);

        ClipboardUtils.copyToClipboard(Data.Login.VALID_PASSWORD);
        ClipboardUtils.pasteToClipboard(loginPage.getPassword());
        authAssertions.assertPasswordIsMasked(Data.Login.VALID_PASSWORD, Data.Login.VALID_PASSWORD.length());
        loginPageAssertions.verifyLoginButtonEnabled();

        loginPage.clickLogin();
        mainPage.waitForPageLoad();
        authAssertions.assertUserIsLoggedIn(EXPECTED_PRODUCTS_COUNT);
    }
}
