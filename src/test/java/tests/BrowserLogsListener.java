package tests;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.util.List;

public class BrowserLogsListener implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {

        LogEntries logs = WebDriverRunner.getWebDriver().manage().logs().get(LogType.BROWSER);
        logs.forEach(System.out::println);

        if (logs.getAll().size() != 0)
            attachLogRequest(logs);

    }

    @Attachment(value = "browser dev tool logs")
    public List<LogEntry> attachLogRequest(LogEntries logEntries) {

        return logEntries.getAll();

    }

}
