package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;


public class BaseTest {

    @BeforeAll
    static void setUp() {

        Configuration.driverManagerEnabled = true;
        Configuration.headless = false;
        Configuration.browser = "chrome";
        Configuration.browserSize = "414x896";


        ChromeOptions options = new ChromeOptions();

        options.addArguments("--enable-automation");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");
        options.addArguments("--disable-dev-shm-usage");

        Map<String, String> mobileEmulation = new HashMap<>();
        //  options.setExperimentalOption("mobileEmulation", mobileEmulation);
        //  mobileEmulation.put("deviceName", "iPhone 12 Pro");

        //options.addArguments("--disable-gpu");
        // options.addArguments("--auto-open-devtools-for-tabs");

        Configuration.browserCapabilities = options;

    }

    @AfterAll
    @DisplayName("Закрытие браузера")
    static void tearDown() {
        Selenide.closeWebDriver();
    }

    @BeforeEach
    void setupAllureReports() {

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().
                screenshots(true).
                savePageSource(false).
                includeSelenideSteps(false));

    }


}


