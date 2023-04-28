package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;

import static api.ApiData.KeeperEndPoints.selenoidUiHubUrl;
import static com.codeborne.selenide.Browsers.FIREFOX;
import static data.Constants.*;

@ExtendWith({
        TestListener.class
})
public class FirefoxTest {

    @BeforeAll
    static void setUp() {

        Configuration.browserSize = MOBILE_BROWSER_SIZE;
        Configuration.browserPosition = MOBILE_BROWSER_POSITION;
        Configuration.browser = FIREFOX;
        Configuration.savePageSource = false;

        Configuration.pageLoadTimeout = PAGE_LOAD_TIMEOUT;
        Configuration.remote = selenoidUiHubUrl;
        Configuration.headless = false;

        FirefoxOptions options = new FirefoxOptions();

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{

            put("name", "E2E test");
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


