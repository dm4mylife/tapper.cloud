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
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.logging.Level;

import static api.ApiData.KeeperEndPoints.selenoidUiHubUrl;
import static com.codeborne.selenide.Browsers.CHROME;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static data.Constants.*;
import static io.restassured.RestAssured.given;

@ExtendWith({
        TestListener.class,
        BrowserLogsListener.class,
        AddSelenoidVideoToAllure.class
})
public class PersonalAccountTest {

    @BeforeAll
    static void setUp() {

        Configuration.browserSize = DESKTOP_BROWSER_SIZE;
        Configuration.browserPosition = DESKTOP_BROWSER_POSITION;
        Configuration.browser = CHROME;
        Configuration.savePageSource = false;
        Configuration.remote = selenoidUiHubUrl;
        Configuration.downloadsFolder = downloadFolderPath;
        Configuration.fileDownload = FOLDER;
        Configuration.pageLoadTimeout = PAGE_LOAD_TIMEOUT;
        Configuration.headless = false;

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        LoggingPreferences loggingPreferences = new LoggingPreferences();

        loggingPreferences.enable(LogType.BROWSER, Level.WARNING);
        desiredCapabilities.setCapability("goog:loggingPrefs", loggingPreferences);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{

            put("name", "Personal Account test");
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});
            put("enableVNC", true);
            put("enableLog", true);
            put("enableVideo", true);

        }});

        options.addArguments("safebrowsing-disable-extension-blacklist");
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
        Selenide.closeWebDriver();

    }


}


