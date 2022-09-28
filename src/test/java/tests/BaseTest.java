package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;


public class BaseTest {

    @BeforeAll
     static void setUp() {

        WebDriverManager.chromedriver().setup();
        Configuration.driverManagerEnabled = true;
        Configuration.browser = "chrome";
       // Configuration.headless = true;
        Configuration.browserSize = "1920x1080";

    }

    @AfterAll
     static void tearDown() {
        Selenide.closeWindow();
    }

    @BeforeEach
      void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide",  new AllureSelenide().screenshots(true).savePageSource(false));
    }

}


