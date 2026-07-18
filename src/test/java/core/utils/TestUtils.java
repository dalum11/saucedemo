package core.utils;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

    private static final Random RANDOM = new Random();

    public static void validateIndex(List<WebElement> elements, int index) {
        if (index < 0 || index >= elements.size()) throw new IllegalArgumentException("Такого индекса не существует");
    }

    public static String generateRandomString(int length) {
        String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~!@#$%^&*()_+|?><";
        return IntStream.range(0, length)
                .map(i -> RANDOM.nextInt(charPool.length()))
                .mapToObj(charPool::charAt)
                .map(Objects::toString)
                .collect(Collectors.joining());
    }
}
