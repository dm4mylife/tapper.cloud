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

import static com.codeborne.selenide.Browsers.SAFARI;

@ExtendWith({
        TestListener.class
})
public class SafariBaseTest {

    @BeforeAll
    static void setUp() {

        Configuration.browserSize = "400x1020";
        Configuration.browserPosition = "600x20";
        Configuration.browser = SAFARI;
        Configuration.remote = "http://localhost:4444/wd/hub";


        SafariOptions options = new SafariOptions();

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone 12 Pro");
        options.setCapability("mobileEmulation", mobileEmulation);

        options.setCapability("selenoid:options", new HashMap<String, Object>() {{

            put("name", "Test badge...");
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


