package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.collect.ImmutableMap;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;


public class BaseTest {

    @BeforeAll
     static void setUp() {

        WebDriverManager.chromedriver().setup();
        Configuration.driverManagerEnabled = true;
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.browserSize = "1920x1080";
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("enable-automation");
        Configuration.browserCapabilities = options;

        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Бразуер", "Chrome")
                        .put("Версия браузера", "105.0.5195.127")
                        .put("Сайт", "https://miuz.ru")
                        .put("Площадка", "Продакшн")
                        .put("Компания", "Факт")
                        .build());

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


