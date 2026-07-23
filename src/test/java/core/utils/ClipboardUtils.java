package core.utils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class ClipboardUtils {

    private static final Logger log = LoggerFactory.getLogger(ClipboardUtils.class);

    private ClipboardUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        log.info("Данные скопированы в буфер обмена");
    }

    public static void pasteToClipboard(WebElement element) {
        String os = System.getProperty("os.name").toLowerCase();
        Keys pasteKey = os.contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        element.sendKeys(Keys.chord(pasteKey, "v"));
        log.info("Данные вставлены из буфера обмена");
    }

    public static String getClipboardContent() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            log.info("Данные из буфера обмена получены");
            return String.valueOf(clipboard.getData(DataFlavor.stringFlavor));
        } catch (Exception e) {
            log.warn("Не удалось получить данные из буфера обмена");
            e.printStackTrace();
            return "";
        }
    }
}
