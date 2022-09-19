package common;



import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.Random;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;



public class BaseActions {

    @Step("Кнопка видна и клик по ней ${locator}")
    public void click(By locator) {

        $(locator).shouldBe(visible).click();

    }

    @Step("Элемент присутствует и видим на странице")
    public void isElementVisible(By locator) {
        $(locator).shouldBe(visible);
    }
    @Step("Элемент не видим на странице")
    public void isElementInVisible(By locator) {
        $(locator).shouldNotBe(visible);
    }

    @Step("Видимы ли элементы в коллекции")
    public boolean isElementVisibleInCollections(WebElement locator) {
        try {
            return $(locator).isDisplayed();
        }
        catch (StaleElementReferenceException e) {
            return false;
        }
    }

    @Step("Удаление текста из поля")
    public void deleteTextInInput(By locator) {

        $(locator).clear();
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
            $(locator).sendKeys(s);
        }

    }

    @Step("Проверка заголовка")
    public void checkPageTitle() {
        $("title").shouldHave(attribute("text", "gg"));
    }
    @Step("Открытие страницы")
    public void openPage(String url) {
        open(url);
    }


}
