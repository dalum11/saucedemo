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
        this.driver = (ChromeDriver )driver;
        log.info("NetworkUtils инициализирован для браузера: {}", driver.getClass().getSimpleName());
    }

    public enum NetworkProfile {
        OFFLINE(true, 0, 0, 0),
        SLOW_3G(false, 300, 100, 50),
        FAST_3G(false, 150, 500, 200),
        SLOW_4G(false, 50, 2000, 1000),
        FAST_4G(false, 20, 5000, 3000),
        TIMEOUT(false, 10000, 1, 1),
        NORMAL(false, 0, -1, -1);

        private final boolean offline;
        private final int latency;
        private final int downloadThroughput;
        private final int uploadThroughput;

        NetworkProfile(boolean offline, int latency, int downloadThroughput, int uploadThroughput) {
            this.offline = offline;
            this.latency = latency;
            this.downloadThroughput = downloadThroughput;
            this.uploadThroughput = uploadThroughput;
        }
    }

    public void setNetworkProfile(NetworkProfile profile) {
        log.info("Установка профиля сети: {}", profile.name());

        ChromeDriver chromeDriver = (ChromeDriver)driver;

        Map<String, Object> params = new HashMap<>();
        params.put("offline", profile.offline);
        params.put("latency", profile.latency);
        params.put("downloadThroughput", profile.downloadThroughput);
        params.put("uploadThroughput", profile.uploadThroughput);
        chromeDriver.executeCdpCommand("Network.emulateNetworkConditions", params);
    }

    public void disableNetwork() {
        log.info("Отключение сети (offline mode)");
        verifyBrowser();
        setNetworkProfile(NetworkProfile.OFFLINE);
    }

    public void enableNetwork() {
        log.info("Включение сети (online mode)");
        verifyBrowser();
        setNetworkProfile(NetworkProfile.NORMAL);
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

    public String getFromSessionStorage(String paramName) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return window.sessionStorage.getItem('" + paramName + "');");
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

    public void setSlow3G() {
        setNetworkProfile(NetworkProfile.SLOW_3G);
    }

    public void setFast3G() {
        setNetworkProfile(NetworkProfile.FAST_3G);
    }

    public void setSlow4G() {
        setNetworkProfile(NetworkProfile.SLOW_4G);
    }

    public void setFast4G() {
        setNetworkProfile(NetworkProfile.FAST_4G);
    }

    public void setNormalNetwork() {
        setNetworkProfile(NetworkProfile.NORMAL);
    }

    public void setTimeout() {
        setNetworkProfile(NetworkProfile.TIMEOUT);
    }

    public void saveToLocalStorage(String key, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.setItem(arguments[0], arguments[1]);", key, value);
        log.info("Сохранено в localStorage: {} = {}", key, value);
    }

    public String getFromLocalStorage(String key) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return window.localStorage.getItem(arguments[0]);", key);
    }

    public void clearLocalStorage() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();");
        log.info("LocalStorage очищен");
    }

    private void verifyBrowser() {
        if (!(driver instanceof ChromeDriver)) {
            log.warn("Управление сетью не поддерживается для этого браузера");
            return;
        }
    }
}