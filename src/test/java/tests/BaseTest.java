package tests;


import com.codeborne.selenide.*;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestListener.class)
public class BaseTest {

    @BeforeAll
    static void setUp() {


        Configuration.browser = Browsers.CHROME;
        Configuration.savePageSource = false;

       // WebDriver driver = WebDriverRunner.getWebDriver();
       // driver.manage().window().setPosition(new Point(0, 0));

        Configuration.headless = false;
        Configuration.browserPosition = "0x0";
        Configuration.browserSize = "350x900";

        ChromeOptions options = new ChromeOptions();

        // options.addArguments("--enable-automation");
        // options.addArguments("--auto-open-devtools-for-tabs");
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

        Selenide.closeWindow();
        Selenide.closeWebDriver();

    }

}


