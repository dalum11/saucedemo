package pageobject;

import base.BasePage;
import data.ProductCardModel;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.TestUtils;

import java.math.BigDecimal;
import java.util.List;

public class MainPage extends BasePage {

    private static final By PRODUCTS_IMAGE = By.className("inventory_item_img");
    private static final By PRODUCTS_NAME = By.className("inventory_item_name");
    private static final By PRODUCTS_PRICE = By.className("inventory_item_price");
    private static final By PRODUCTS_CARDS = By.className("inventory_item");
    private static final By ADD_IN_CART_BUTTON = By.xpath("//button[@class='btn btn_primary btn_small btn_inventory ']");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ожидание загрузки страницы")
    public void waitForPageLoad() {
        super.waitForPageLoad();
    }

    @Step("Получение карточки товара по индексу")
    public ProductCardModel getProductCardByIndex(int index) {
        List<WebElement> cards = driver.findElements(PRODUCTS_CARDS);
        TestUtils.validateIndex(cards, index);

        WebElement card = cards.get(index);
        return convertToProductCardModel(card);
    }

    private ProductCardModel convertToProductCardModel(WebElement card) {
        return new ProductCardModel.Builder()
                .name(getProductName(card))
                .price(getProductPrice(card))
                .imageUrl(getProductImageUrl(card))
                .build();
    }

    private String getProductName(WebElement card) {
        return card.findElement(PRODUCTS_NAME).getText();
    }

    private BigDecimal getProductPrice(WebElement card) {
        String priceText = card.findElement(PRODUCTS_PRICE).getText();
        String cleanText = priceText.replace("$", "");
        return new BigDecimal(cleanText);
    }

    private String getProductImageUrl(WebElement card) {
        return card.findElement(PRODUCTS_IMAGE)
                .getAttribute("src");
    }

    @Step("Проверка видимости элементов карточки товара")
    public boolean isProductCardDisplayed(int index) {
        List<WebElement> cards = findElements(PRODUCTS_CARDS);
        TestUtils.validateIndex(cards, index);
        WebElement card = cards.get(index);

        return isElementDisplayed(card.findElement(PRODUCTS_IMAGE)) &&
                isElementDisplayed(card.findElement(PRODUCTS_PRICE)) &&
                isElementDisplayed(card.findElement(ADD_IN_CART_BUTTON)) &&
                isElementDisplayed(card.findElement(PRODUCTS_NAME));
    }
}
