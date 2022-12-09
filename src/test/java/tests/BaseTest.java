package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
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
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(TestListener.class)
public class BaseTest {

    @BeforeAll
    static void setUp() {

       // Configuration.browserSize = "350x1100";

        Configuration.browser = "chrome";
        Configuration.savePageSource = false;

       // Configuration.driverManagerEnabled = true;

       // WebDriver driver = WebDriverRunner.getWebDriver();
       // driver.manage().window().setPosition(new Point(0, 0));
        Configuration.headless = false;
        Configuration.browserPosition = "0x0";
        Configuration.browserSize = "350x900";

        //   SelenideLogger.addListener("AllureSelenide", new AllureSelenide().
         //       screenshots(true).
        //        savePageSource(false).
         //       includeSelenideSteps(false));



        ChromeOptions options = new ChromeOptions();

       // options.addArguments("--enable-automation");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");
        options.addArguments("--disable-dev-shm-usage");
        //options.addArguments("--disable-gpu");
        // options.addArguments("--auto-open-devtools-for-tabs");

        //Map<String, String> mobileEmulation = new HashMap<>();

       //   mobileEmulation.put("deviceName", "iPhone 12 Pro");

        /* Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 428);
        deviceMetrics.put("height", 926);
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/11.2 Mobile/15E148 Safari/604.1");
        options.setExperimentalOption("mobileEmulation", mobileEmulation); */

        Configuration.browserCapabilities = options;

    }

    @AfterAll
    @DisplayName("Закрытие браузера")
    static void tearDown() {

        Selenide.closeWindow();
        Selenide.closeWebDriver();

    }

    /*  @BeforeEach
    void setupAllureReports() {

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().
                screenshots(true).
                savePageSource(false).
                includeSelenideSteps(false));

    } */


}


