package tests;


import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.netty.buffer.ByteBufInputStream;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;


public class BaseTestTwoBrowsers {

     public static WebDriver firstBrowser;
     public static WebDriver secondBrowser;

    @BeforeAll
    static void setUp() {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        capabilities.setCapability(ChromeOptions.CAPABILITY,options);

        firstBrowser = new ChromeDriver(options);
        firstBrowser.manage().window().setPosition(new Point(0,0));

        secondBrowser = new ChromeDriver(options);
        secondBrowser.manage().window().setPosition(new Point(960,0));

    }

    @AfterAll
    static void tearDown() {

        firstBrowser.close();
        secondBrowser.close();

    }



}


