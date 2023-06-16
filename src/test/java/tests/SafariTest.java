package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

import static api.ApiData.KeeperEndPoints.selenoidUiHubUrl;
import static com.codeborne.selenide.Browsers.SAFARI;
import static data.Constants.*;

@ExtendWith({
        TestListener.class
})
public class SafariTest {

    @BeforeAll
    static void setUp() {

        Configuration.browserSize = MOBILE_BROWSER_SIZE;
        Configuration.browserPosition = MOBILE_BROWSER_POSITION;
        Configuration.browser = SAFARI;
        Configuration.remote = selenoidUiHubUrl;
        Configuration.pageLoadTimeout = PAGE_LOAD_TIMEOUT;
        Configuration.savePageSource = false;

        SafariOptions options = new SafariOptions();

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");
        options.setCapability("mobileEmulation", mobileEmulation);

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{

            put("name", "E2E test");
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);

        }});

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


