package tests.auth;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import pageobject.LoginPage;

public class ElementsVisibilityTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    @Step("Подготовка теста")
    protected void setUp(TestInfo testInfo) {
        super.setUp(testInfo);
        loginPage = new LoginPage(driver);
        openLoginPage();
    }

    @Test
    @DisplayName("Проверка отображения всех обязательных элементов страницы")
    @Description("Тест проверяет, что все основные элементы страницы авторизации присутствуют")
    @Tag("smoke")
    @Tag("regression")
    @Step("Проверка элементов страницы логина")
    void loginPage_shouldDisplayAllRequiredElements() {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(loginPage.isPageTitleDisplayed())
                .as("Заголовок страницы должен отображаться")
                .isTrue();

        softly.assertThat(loginPage.getPageTitleText())
                .as("Текст заголовка страницы")
                .isEqualTo("Swag Labs");

        softly.assertThat(loginPage.isLoginDisplayed())
                .as("Поле логина должно отображаться")
                .isTrue();

        softly.assertThat(loginPage.isLoginEnabled())
                .as("Поле логина должно быть доступно")
                .isTrue();

        softly.assertThat(loginPage.getLoginPlaceholder())
                .as("Placeholder поля логина")
                .isEqualTo("Username");

        softly.assertThat(loginPage.isPasswordFieldDisplayed())
                .as("Поле пароля должно отображаться")
                .isTrue();

        softly.assertThat(loginPage.isPasswordFieldEnabled())
                .as("Поле пароля должно быть доступно")
                .isTrue();

        softly.assertThat(loginPage.getPasswordPlaceholder())
                .as("Placeholder поля пароля")
                .isEqualTo("Password");

        softly.assertThat(loginPage.isLoginButtonDisplayed())
                .as("Кнопка логина должна отображаться")
                .isTrue();

        softly.assertThat(loginPage.isLoginButtonEnabled())
                .as("Кнопка логина должна быть доступна")
                .isTrue();

        softly.assertThat(loginPage.getLoginButtonText())
                .as("Текст кнопки логина")
                .isEqualTo("Login");

        softly.assertAll();
    }
}
