package core.assertions;

import core.config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.auth.LoginPageAssertions;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class BaseAssertions {

    protected final Logger log = LoggerFactory.getLogger(BaseAssertions.class);
    protected static final Duration DEFAULT_TIMEOUT = TestConfig.DEFAULT_TIMEOUT;

    protected WebDriverWait getWait(WebDriver driver, Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    protected WebDriverWait getDefaultWait(WebDriver driver) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public void assertPageNotChanged(WebDriver driver, String expectedUrl) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL не должен измениться. Ожидался: " + expectedUrl)
                .isEqualTo(expectedUrl);
        log.info("URL остался неизменным: {}", expectedUrl);
    }

    public void assertPageChanged(WebDriver driver, String expectedUrlPart, String previousUrlPart) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL должен содержать '" + expectedUrlPart + "' и не содержать '" + previousUrlPart + "'")
                .contains(expectedUrlPart)
                .doesNotContain(previousUrlPart);
        log.info("URL изменился с содержания '{}' на '{}'", previousUrlPart, currentUrl);
    }

    public void assertPageTitle(WebDriver driver, String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertThat(actualTitle)
                .as("Заголовок страницы")
                .isNotBlank()
                .isEqualTo(expectedTitle);
        log.info("Заголовок страницы соответствует: {}", expectedTitle);
    }

    public void assertPageUrlContains(WebDriver driver, String expectedUrlPart) {
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl)
                .as("URL должен содержать: " + expectedUrlPart)
                .contains(expectedUrlPart);
        log.info("URL содержит '{}'", expectedUrlPart);
    }

    public void assertElementVisible(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = getWait(driver, timeout);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            assertThat(element)
                    .as("Элемент с локатором " + locator + " должен быть видимым")
                    .isNotNull();
            log.debug("Элемент {} видим", locator);
        } catch (TimeoutException e) {
            fail("Элемент " + locator + " не стал видимым за " + timeout.getSeconds() + " секунд", e);
        }
    }

    public void assertElementVisible(WebDriver driver, By locator) {
        assertElementVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    public void assertElementNotVisible(WebDriver driver, By locator, Duration timeout) {
        WebDriverWait wait = getWait(driver, timeout);
        try {
            boolean isInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            assertThat(isInvisible)
                    .as("Элемент с локатором " + locator + " должен быть невидимым")
                    .isTrue();
            log.debug("Элемент {} невидим", locator);
        } catch (TimeoutException e) {
            fail("Элемент " + locator + " не стал невидимым за " + timeout.getSeconds() + " секунд", e);
        }
    }

    public void assertElementEnabled(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            assertThat(element.isEnabled())
                    .as("Элемент с локатором " + locator + " должен быть доступен")
                    .isTrue();
            log.debug("Элемент {} доступен", locator);
        } catch (NoSuchElementException e) {
            fail("Элемент " + locator + " не найден", e);
        } catch (StaleElementReferenceException e) {
            fail("Элемент " + locator + " устарел", e);
        }
    }

    public void assertElementDisabled(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            assertThat(element.isEnabled())
                    .as("Элемент с локатором " + locator + " должен быть недоступен")
                    .isFalse();
            log.debug("Элемент {} недоступен", locator);
        } catch (NoSuchElementException e) {
            fail("Элемент " + locator + " не найден", e);
        } catch (StaleElementReferenceException e) {
            fail("Элемент " + locator + " устарел", e);
        }
    }

    public void assertElementText(WebDriver driver, By locator, String expectedText) {
        try {
            WebElement element = driver.findElement(locator);
            String actualText = element.getText().trim();
            assertThat(actualText)
                    .as("Текст элемента с локатором " + locator)
                    .isEqualTo(expectedText);
            log.info("Текст элемента {} соответствует: '{}'", locator, expectedText);
        } catch (NoSuchElementException e) {
            fail("Элемент " + locator + " не найден", e);
        } catch (StaleElementReferenceException e) {
            fail("Элемент " + locator + " устарел", e);
        }
    }

    public void assertElementTextContains(WebDriver driver, By locator, String expectedTextPart) {
        try {
            WebElement element = driver.findElement(locator);
            String actualText = element.getText().trim();
            assertThat(actualText)
                    .as("Текст элемента с локатором " + locator + " должен содержать: '" + expectedTextPart + "'")
                    .contains(expectedTextPart);
            log.info("Текст элемента {} содержит: '{}'", locator, expectedTextPart);
        } catch (NoSuchElementException e) {
            fail("Элемент " + locator + " не найден", e);
        } catch (StaleElementReferenceException e) {
            fail("Элемент " + locator + " устарел", e);
        }
    }

    public void assertElementAttribute(WebDriver driver, By locator, String attribute, String expectedValue) {
        try {
            WebElement element = driver.findElement(locator);
            String actualValue = element.getAttribute(attribute);
            assertThat(actualValue)
                    .as("Атрибут '" + attribute + "' элемента с локатором " + locator)
                    .isEqualTo(expectedValue);
            log.debug("Атрибут '{}' элемента {} имеет значение: '{}'", attribute, locator, expectedValue);
        } catch (NoSuchElementException e) {
            fail("Элемент " + locator + " не найден", e);
        } catch (StaleElementReferenceException e) {
            fail("Элемент " + locator + " устарел", e);
        }
    }

    public void assertElementVisible(WebDriver driver, WebElement element, Duration timeout) {
        WebDriverWait wait = getWait(driver, timeout);
        try {
            WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));
            assertThat(visibleElement)
                    .as("Элемент должен быть видимым")
                    .isNotNull();
            log.debug("Элемент видим");
        } catch (TimeoutException e) {
            fail("Элемент не стал видимым за " + timeout.getSeconds() + " секунд", e);
        }
    }

    public void assertElementEnabled(WebElement element) {
        try {
            assertThat(element.isEnabled())
                    .as("Элемент должен быть доступен")
                    .isTrue();
            log.debug("Элемент доступен");
        } catch (StaleElementReferenceException e) {
            fail("Элемент устарел", e);
        }
    }

    public void assertElementText(WebElement element, String expectedText) {
        try {
            String actualText = element.getText().trim();
            assertThat(actualText)
                    .as("Текст элемента")
                    .isEqualTo(expectedText);
            log.info("Текст элемента соответствует: '{}'", expectedText);
        } catch (StaleElementReferenceException e) {
            fail("Элемент устарел", e);
        }
    }

    public void assertListSize(List<?> list, int expectedSize) {
        assertThat(list)
                .as("Размер списка")
                .hasSize(expectedSize);
        log.info("Размер списка соответствует: {}", expectedSize);
    }

    public void assertElementWidthPercent(WebDriver driver, WebElement element, int expectedWidth) {
        String actualWidth = getElementWidthInPercent(driver, element);

        assertTrue(actualWidth.contains(expectedWidth + ""),
                "Ожидалось " + expectedWidth + ", получилось: " + actualWidth);
    }

    private String getElementWidthInPercent(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String width = (String) js.executeScript(
                "var target = arguments[0];" +
                        "var sheets = document.styleSheets;" +
                        "for (var i = 0; i < sheets.length; i++) {" +
                        "  try {" +
                        "    var sheet = sheets[i];" +
                        "    var rules = sheet.cssRules || sheet.rules;" +
                        "    if (rules) {" +
                        "      for (var j = 0; j < rules.length; j++) {" +
                        "        var rule = rules[j];" +
                        "        if (rule.selectorText && target.matches(rule.selectorText)) {" +
                        "          if (rule.style && rule.style.width) {" +
                        "            return rule.style.width;" +
                        "          }" +
                        "        }" +
                        "      }" +
                        "    }" +
                        "  } catch (e) {" +
                        "  }" +
                        "}" +
                        "return null;",
                element
        );
        return width;
    }

    public void assertElementsCount(List<WebElement> elements, int expectedCount) {
        assertThat(elements)
                .as("Количество элементов")
                .hasSize(expectedCount);
        log.info("Найдено {} элементов (ожидалось {})", elements.size(), expectedCount);
    }

    protected void fail(String message, Throwable cause) {
        log.error(message, cause);
        throw new AssertionError(message, cause);
    }

    protected void fail(String message) {
        log.error(message);
        throw new AssertionError(message);
    }
}