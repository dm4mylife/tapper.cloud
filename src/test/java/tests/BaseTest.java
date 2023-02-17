package tests;


import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.codeborne.selenide.Browsers.CHROME;

@ExtendWith({
        TestListener.class,
        BrowserLogsListener.class
})
public class BaseTest {

    @BeforeAll
    static void setUp() {

       // Configuration.browserSize = "400x980";
       // Configuration.browserPosition = "600x20";
        Configuration.browserSize = "1920x1080";

        Configuration.browser = CHROME;
        Configuration.savePageSource = true;
        Configuration.headless = true;


        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

        loggingPreferences.enable(LogType.BROWSER, Level.WARNING);
        desiredCapabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        // options.addArguments("--enable-automation");
       // options.addArguments("--auto-open-devtools-for-tabs");

        options.addArguments("--safebrowsing-disable-download-protection");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        Configuration.browserCapabilities = options;

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .includeSelenideSteps(false)
                .savePageSource(false)
        );

    }

    @AfterAll
    @DisplayName("Закрытие браузера")
    static void tearDown() {

        Selenide.clearBrowserLocalStorage();
        Selenide.clearBrowserCookies();
        Selenide.closeWebDriver();

    }


}


