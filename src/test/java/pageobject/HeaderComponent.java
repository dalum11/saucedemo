package pageobject;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HeaderComponent extends BasePage {

    @FindBy(className = "app_logo")
    private WebElement pageTitle;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement menuButton;

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCart;


    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть меню")
    public void openMenu() {
        clickOnElement(menuButton);
    }

    @Step("Проверка видимости кнопки Меню")
    public boolean isMenuButtonDisplayed() {
        return isElementDisplayed(menuButton);
    }

    @Step("Проверка доступности кнопки Меню для нажатия")
    public boolean isMenuButtonEnabled() {
        return isElementEnabled(menuButton);
    }

    @Step("Проверка видимости кнопки Корзина")
    public boolean isShoppingCartDisplayed() {
        return isElementDisplayed(shoppingCart);
    }

    @Step("Проверка доступности кнопки Корзина для нажатия")
    public boolean isShoppingCartEnabled() {
        return isElementEnabled(shoppingCart);
    }

    @Step("Получение названия страницы")
    public String getPageTitleText() {
        return getElementText(pageTitle);
    }

    @Step("Проверка видимости названия страницы")
    public boolean isPageTitleDisplayed() {
        return pageTitle.isDisplayed();
    }
}
