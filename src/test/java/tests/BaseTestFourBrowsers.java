package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Browsers.CHROME;


public class BaseTestFourBrowsers {

    public static WebDriver firstBrowser;
    public static WebDriver secondBrowser;
    public static WebDriver thirdBrowser;
    public static WebDriver fourthBrowser;

    @BeforeAll
    static void setUp() {

        Configuration.browser = CHROME;
        Configuration.savePageSource = false;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--headless");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "iPhone XR");
        options.setExperimentalOption("mobileEmulation", mobileEmulation);

        capabilities.setCapability(ChromeOptions.CAPABILITY,options);

        firstBrowser = new ChromeDriver(options);
        firstBrowser.manage().window().setPosition(new Point(0,0));
        firstBrowser.manage().window().setSize(new Dimension(300,1020));

        secondBrowser = new ChromeDriver(options);
        secondBrowser.manage().window().setPosition(new Point(430,0));
        secondBrowser.manage().window().setSize(new Dimension(300,1020));

        thirdBrowser = new ChromeDriver(options);
        thirdBrowser.manage().window().setPosition(new Point(890,0));
        thirdBrowser.manage().window().setSize(new Dimension(300,1020));

        fourthBrowser = new ChromeDriver(options);
        fourthBrowser.manage().window().setPosition(new Point(1360,0));
        fourthBrowser.manage().window().setSize(new Dimension(300,1020));

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .includeSelenideSteps(false)
                .savePageSource(false)
        );

    }

    @AfterAll
    static void tearDown() {

        if (firstBrowser != null) firstBrowser.quit();
        if (secondBrowser != null) secondBrowser.quit();
        if (thirdBrowser != null) thirdBrowser.quit();
        if (fourthBrowser != null) fourthBrowser.quit();

    }

}


