package core.utils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class KeyboardUtils {

    private final WebDriver driver;
    private final Actions actions;

    public KeyboardUtils(WebDriver driver) {
        this.driver = driver;
        actions = new Actions(driver);
    }

    public void pressTab() {
        actions.sendKeys(Keys.TAB).perform();
    }

    public void pressBackspace() {
        actions.sendKeys(Keys.BACK_SPACE).perform();
    }
}
