package core.utils;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardUtils {

    private ClipboardUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void copyToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public static void pasteToClipboard(WebElement element) {
        String os = System.getProperty("os.name").toLowerCase();
        Keys pasteKey = os.contains("mac") ? Keys.COMMAND : Keys.CONTROL;
        element.sendKeys(Keys.chord(pasteKey, "v"));
    }
}
