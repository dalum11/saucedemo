package utils;

import org.openqa.selenium.WebElement;

import java.util.List;

public class TestUtils {

    public static void validateIndex(List<WebElement> elements, int index) {
        if (index < 0 || index >= elements.size()) throw new IllegalArgumentException("Такого индекса не существует");
    }
}
