package tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TestListener implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        Allure.getLifecycle().addAttachment("Screenshot", "image/png",
                "png",((TakesScreenshot) Selenide.webdriver().driver().getWebDriver()).getScreenshotAs(OutputType.BYTES));
    }

}

