package tests;


import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Browsers.CHROME;


public class BaseTestTwoBrowsers {

     public static WebDriver firstBrowser;
     public static WebDriver secondBrowser;

    @BeforeAll
    static void setUp() {

        Configuration.browser = CHROME;
        Configuration.savePageSource = false;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        capabilities.setCapability(ChromeOptions.CAPABILITY,options);

        firstBrowser = new ChromeDriver(options);
        firstBrowser.manage().window().setPosition(new Point(0,0));

        secondBrowser = new ChromeDriver(options);
        secondBrowser.manage().window().setPosition(new Point(960,0));

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .includeSelenideSteps(false)
                .savePageSource(false)
        );

    }

    @AfterAll
    static void tearDown() {

        if (firstBrowser != null) firstBrowser.quit();
        if (secondBrowser != null) secondBrowser.quit();

    }

}


