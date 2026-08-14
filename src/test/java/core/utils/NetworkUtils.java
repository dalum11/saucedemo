package core.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    private static final Logger log = LoggerFactory.getLogger(NetworkUtils.class);
    private final WebDriver driver;

    public NetworkUtils(WebDriver driver) {
         if (!(driver instanceof ChromeDriver)) {
            log.warn("NetworkUtils: драйвер не поддерживает CDP (не Chrome/Chromium)");
        }
        this.driver = driver;
        log.info("NetworkUtils инициализирован для браузера: {}", driver.getClass().getSimpleName());
    }

    public void disableNetwork() {
        log.info("Отключение сети (offline mode)");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        Map<String, Object> params = new HashMap<>();
        params.put("offline", true);
        params.put("latency", 100);
        params.put("downloadThroughput", 1000);
        params.put("uploadThroughput", 1000);
        chromeDriver.executeCdpCommand("Network.emulateNetworkConditions", params);
    }

    public void enableNetwork() {
        log.info("Включение сети (online mode)");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        Map<String, Object> params = new HashMap<>();
        params.put("offline", false);
        params.put("latency", 0);
        params.put("downloadThroughput", -1);
        params.put("uploadThroughput", -1);
        chromeDriver.executeCdpCommand("Network.emulateNetworkConditions", params);
    }

    public void clearCache() {
        log.info("Очистка кэша");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        chromeDriver.executeCdpCommand("Network.clearBrowserCache", new HashMap<>());
    }

    public void disableCache() {
        log.info("Отключение кэша");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        Map<String, Object> params = new HashMap<>();
        params.put("cacheDisabled", true);
        chromeDriver.executeCdpCommand("Network.setCacheDisabled", params);
    }

    public void enableCache() {
        log.info("Включение кэша");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        Map<String, Object> params = new HashMap<>();
        params.put("cacheDisabled", false);
        chromeDriver.executeCdpCommand("Network.setCacheDisabled", params);
    }

    public void clearCookies() {
        log.info("Очистка cookies");
        verifyBrowser();

        ChromeDriver chromeDriver = (ChromeDriver) driver;
        chromeDriver.executeCdpCommand("Network.clearBrowserCookies", new HashMap<>());
    }

    public void setNetworkConditions(boolean offline, boolean cacheDisabled) {
        log.info("Установка условий сети: offline={}, cacheDisabled={}", offline, cacheDisabled);
        verifyBrowser();

        if (offline) {
            disableNetwork();
        } else {
            enableNetwork();
        }

        if (cacheDisabled) {
            disableCache();
        } else {
            enableCache();
        }
    }

    public void saveInSessionStorage(String paramName, String paramValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.sessionStorage.setItem('" + paramName + "', arguments[0]);", paramValue);
    }

    public void getFromSessionStorage(String paramName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("return window.sessionStorage.getItem('" + paramName + "');");
    }

    public void forceCachePage() {
        Map<String, Object> params = new HashMap<>();
        params.put("patterns", new Object[]{
                Map.of("urlPattern", ".*", "resourceType", "Document")
        });
        ((ChromeDriver) driver).executeCdpCommand("Network.setCacheDisabled", Map.of("cacheDisabled", false));
    }

    public void setIsSavePassword(String isSavePassword) {
        System.setProperty("save.passwords", isSavePassword);
    }

    private void verifyBrowser() {
        if (!(driver instanceof ChromeDriver)) {
            log.warn("Управление сетью не поддерживается для этого браузера");
            return;
        }
    }
}