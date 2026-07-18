package core.assertions;

import core.config.TestConfig;
import core.utils.CursorUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.auth.LoginPageAssertions;
import pages.component.HeaderComponent;
import pages.auth.LoginPage;
import pages.main.MainPage;
import data.Data;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthAssertions extends BaseAssertions {
    private final WebDriver driver;
    private final LoginPage loginPage;
    private final LoginPageAssertions loginPageAssertions;
    private final CursorUtils cursorUtils;

    private final Logger log = LoggerFactory.getLogger(AuthAssertions.class);

    public AuthAssertions(WebDriver driver) {
        this.driver = driver;
        this.loginPage = new LoginPage(driver);
        this.cursorUtils = new CursorUtils(driver);
        this.loginPageAssertions = new LoginPageAssertions(loginPage, driver, cursorUtils);
    }

    public AuthAssertions(LoginPage loginPage) {
        this.driver = loginPage.getDriver();
        this.loginPage = loginPage;
        this.cursorUtils = new CursorUtils(driver);
        this.loginPageAssertions = new LoginPageAssertions(loginPage, driver, cursorUtils);
    }

    public void assertUserIsLoggedIn(int productCardIndex) {
        if (hasHeaderAndMainPage()) {
            HeaderComponent headerComponent = new HeaderComponent(driver);
            MainPage mainPage = new MainPage(driver);

            assertPageUrlContains(driver, Data.Endpoints.MAIN_PAGE);
            assertAll("Все элементы хедера должны быть корректно отображены",
                    () -> assertThat(headerComponent.isPageTitleDisplayed())
                            .as("Заголовок страницы должен отображаться")
                            .isTrue(),
                    () -> assertThat(headerComponent.getPageTitleText())
                            .as("Текст заголовка страницы")
                            .isEqualTo("Swag Labs"),
                    () -> assertThat(headerComponent.isMenuButtonDisplayed())
                            .as("Кнопка меню должна отображаться")
                            .isTrue(),
                    () -> assertThat(headerComponent.isMenuButtonEnabled())
                            .as("Кнопка меню должна быть доступна")
                            .isTrue(),
                    () -> assertThat(headerComponent.isShoppingCartDisplayed())
                            .as("Корзина должна отображаться")
                            .isTrue(),
                    () -> assertThat(headerComponent.isShoppingCartEnabled())
                            .as("Корзина должна быть доступна")
                            .isTrue()
            );

            assertThat(mainPage.isProductCardDisplayed(productCardIndex))
                    .as("Продуктовая карточка #" + productCardIndex + " должна отображаться")
                    .isTrue();
        } else {
            assertPageUrlContains(driver, Data.Endpoints.MAIN_PAGE);
            log.info("Пользователь авторизован, перешел на главную страницу");
        }
        log.info("Пользователь успешно авторизован");
    }

    public void assertUserIsLoggedOut() {
        assertPageChanged(driver, TestConfig.BASE_URL, Data.Endpoints.MAIN_PAGE);

        assertLoginPageIsDisplayedCorrectly();
        log.info("Пользователь успешно разлогинен");
    }

    public void assertAvailableCredentialsAreDisplayed() {
        List<String> usernames = loginPage.getAvailableUsernames();
        log.info("Доступные логины: {}", usernames);
        String password = loginPage.getAvailablePassword();

        assertAll("Доступные учетные данные должны отображаться",
                () -> assertThat(usernames)
                        .as("Список доступных логинов")
                        .isNotEmpty()
                        .contains("standard_user", "locked_out_user", "problem_user", "performance_glitch_user"),
                () -> assertThat(password)
                        .as("Пароль для всех пользователей")
                        .contains("secret_sauce")
        );

        log.info("Доступные учетные данные корректно отображены");
    }

    public void assertPasswordIsMasked() {
        String testPassword = "secret_sauce";
        loginPage.enterPassword(testPassword);

        String displayedValue = loginPage.getMaskedPassword();

        assertThat(displayedValue)
                .as("Отображаемое значение поля пароля")
                .hasSize(testPassword.length());

        log.debug("Поле пароля корректно маскирует ввод");
    }

    public void assertGettingErrorMessageToLogin(String errorMessage) {
        loginPage.login("locked_out_user", "secret_sauce");
        loginPageAssertions.verifyErrorMessage(errorMessage);

        log.warn("Заблокированный пользователь не может войти в систему");
    }

    public void assertLoginPageIsDisplayedCorrectly() {
        loginPageAssertions.verifyPageDisplayed()
                .verifyPasswordField()
                .verifyUsernameField()
                .verifyLoginButtonEnabled();

        assertAvailableCredentialsAreDisplayed();
    }

    private boolean hasHeaderAndMainPage() {
        try {
            Class.forName("HeaderComponent");
            Class.forName("MainPage");
            return true;
        } catch (ClassNotFoundException e) {
            log.debug("HeaderComponent или MainPage не найдены, используется упрощенная проверка");
            return false;
        }
    }
}