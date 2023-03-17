package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.logging.Level;

import static com.codeborne.selenide.Browsers.FIREFOX;

@ExtendWith({
        TestListener.class
})
public class AdminFirefoxBaseTest {

    @BeforeAll
    static void setUp() {


        Configuration.browserSize = "1920x1080";
        Configuration.browser = FIREFOX;
        Configuration.savePageSource = false;


        Configuration.remote = "http://localhost:4444/wd/hub";

        Configuration.headless = false;

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        FirefoxOptions options = new FirefoxOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

       // loggingPreferences.enable(LogType.BROWSER, Level.WARNING);
        //desiredCapabilities.setCapability("goog:loggingPrefs", loggingPreferences);
       // desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{

            put("name", "Test badge...");
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);

        }});

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


