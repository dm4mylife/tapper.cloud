package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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



public class ScreenMobileTest {

    int imagePixelSize = MOBILE_IMAGE_PIXEL_SIZE;
    double diffPercent = DIFF_PERCENT_IMAGE;
    public static String browserSizeType = "mobile";

    public static String getBrowserSizeType() {
        return browserSizeType;
    }

    public int getImagePixelSize() {
        return imagePixelSize;
    }

    public double getDiffPercent() {
        return diffPercent;
    }

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

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("name","Screenshot test");
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


