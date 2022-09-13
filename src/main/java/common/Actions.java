package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static common.ConfigDriver.PLATFORM_AND_BROWSER;


public class Actions {

    public static WebDriver createWebDriver() {

        WebDriver driver = null;

        if (PLATFORM_AND_BROWSER.equals("win+chrome")) {

            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

        } else {

            Assert.fail("Incorrect type or browser name" + PLATFORM_AND_BROWSER);
        }

        driver.manage().window().maximize();
        return  driver;


    }


}
