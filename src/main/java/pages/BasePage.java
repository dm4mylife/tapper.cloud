package pages;

import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Random;

import static constants.Constant.TimeoutVar.*;


public class BasePage {

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitElementIsVisible(WebElement element) {

        new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT)).until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    @Step("Переход на страницу {url}")
    public void open(String url) {
        driver.get(url);

    }

    @Step("Клик по кнопке ${locator}")
    public void click(By locator) {

        WebElement element = driver.findElement(locator);
        waitElementIsVisible(element).click();

    }

    @Step("Адаптивная задержка перед появлением элемента")
    public Wait<WebDriver> fluentWait() {

        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(WITH_TIME_OUT))
                .pollingEvery(Duration.ofSeconds(POLLING_EVERY))
                .ignoring(NoSuchElementException.class);
    }

    @Step("Удаление текста из поля")
    public void deleteTextInInput(By locator) {

        WebElement element = driver.findElement(locator);
        element.clear();
    }

    @Step("Ввод данных {text} с задержкой в {locator}")
    public void sendHumanKeys(By locator, String text) {
        Random r = new Random();
        for(int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep((int)(r.nextGaussian() * 15 + 100));
            } catch(InterruptedException e) {
                System.out.println("Error " + e);
            }
            String s = String.valueOf(text.charAt(i));
            driver.findElement(locator).sendKeys(s);
        }

    }

    /* @Attachment(value = "Page screenshot", type = "image/png")
    public  makeScreenshot() throws IOException {

        Date dateNow = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh_mm_ss");
        String fileName = format.format(dateNow) + ".png";

        File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);

        FileUtils.copyFile(screenShot,new File("C:\\Screenshots\\" + fileName));
        return screenshots;
    } */



    @Rule
    public byte[] AttachScreen() {
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

}
