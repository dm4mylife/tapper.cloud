package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import org.openqa.selenium.chrome.ChromeOptions;



public class BaseTest {

    @BeforeAll
     static void setUp() {


        Configuration.driverManagerEnabled = true;
        Configuration.browser = "chrome";
        Configuration.headless = false;
        Configuration.browserSize = "1920x1080";
        ChromeOptions options = new ChromeOptions();
       // options.addArguments("--auto-open-devtools-for-tabs");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        //options.addArguments("--disable-gpu");
        Configuration.browserCapabilities = options;


    }

    @AfterAll
    static void tearDown() {
        Selenide.closeWebDriver();
    }

    @BeforeAll
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide",  new AllureSelenide().screenshots(true).savePageSource(false));
    }



}


