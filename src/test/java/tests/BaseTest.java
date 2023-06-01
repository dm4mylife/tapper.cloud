package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.rules.TestName;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static api.ApiData.KeeperEndPoints.selenoidUiHubUrl;
import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static data.Constants.*;

@ExtendWith({
        TestListener.class,
        BrowserLogsListener.class,
        AddSelenoidVideoToAllure.class
})
public class BaseTest {


    @BeforeAll
    public static void setUp() {

        Configuration.browserSize = MOBILE_BROWSER_SIZE;
        Configuration.browserPosition = MOBILE_BROWSER_POSITION;
        Configuration.browser = CHROME;
        Configuration.remote = selenoidUiHubUrl;
        Configuration.pageLoadTimeout = PAGE_LOAD_TIMEOUT;
        Configuration.pageLoadStrategy = "eager";
        Configuration.savePageSource = false;

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

        loggingPreferences.enable(LogType.BROWSER, Level.WARNING);
        Configuration.browserCapabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
        Configuration.browserCapabilities.setCapability(ChromeOptions.LOGGING_PREFS, loggingPreferences);

        Configuration.browserCapabilities.setCapability("webdriver.chrome.args", new String[]{"--log-level=3"});

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);



        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("name", "test_name");
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});
            put("sessionTimeout", "7m");
            put("enableVideo", true);
            put("enableVNC", true);

        }});


        options.addArguments("--remote-allow-origins=*");
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
        closeWebDriver();

    }

}


