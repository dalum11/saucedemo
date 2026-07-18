package core.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.assertj.core.api.Assertions.assertThat;

public class CursorUtils {

    private WebDriver driver;

    public CursorUtils(WebDriver driver) {
        this.driver = driver;
    }

    public void assertCursorIsPointer(WebElement element) {
        String cursor = getCursorValue(element);
        assertThat(cursor).isEqualTo("pointer");
    }

    public void assertCursorIsDefault(WebElement element) {
        String cursor = getCursorValue(element);
        assertThat(cursor).isIn("text", "default");
    }


    public String getCursorValue(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return window.getComputedStyle(arguments[0]).cursor;",
                element);
    }

    public void hoverOverElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    public void assertThatCursorInField(WebElement element) {
        assertThat(driver.switchTo().activeElement().equals(element));
    }
}
