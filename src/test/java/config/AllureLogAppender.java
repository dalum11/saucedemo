package config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.qameta.allure.Allure;

public class AllureLogAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        String message = event.getFormattedMessage();
        String level = event.getLevel().toString();

        Allure.addAttachment("Лог: " + level, "text/plain", message);
    }
}
