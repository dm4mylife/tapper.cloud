package common;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.Random;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.fail;


public class BaseActions {

    @Step("Кнопка видна и клик по ней")
    public void click(SelenideElement element) {

        element.shouldBe(visible).click();

    }

    @Step("Элемент присутствует и видим на странице")
    public void isElementVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Элемент не видим на странице")
    public void isElementInVisible(SelenideElement element) {
        element.shouldNotBe(visible);
    }

    @Step("Элемент присутствует и видим на странице")
    public void isClickable(SelenideElement element) {
        element.shouldBe(visible).shouldBe(enabled);
    }

    @Step("Видимы ли элементы в коллекции")
    public boolean isElementVisibleInCollections(SelenideElement element) {
        try {
            return element.isDisplayed();
        }
        catch (StaleElementReferenceException e) {
            return false;
        }
    }

    @Step("Удаление текста из поля в элементе ")
    public void deleteTextInInput(SelenideElement element) {

       element.clear();
    }

    @Step("Ввод данных {text} с задержкой")
    public void sendHumanKeys(SelenideElement element, String text) {
        Random r = new Random();
        for(int i = 0; i < text.length(); i++) {
            try {
                Thread.sleep((int)(r.nextGaussian() * 15 + 50));
            } catch(InterruptedException e) {
                System.out.println("Error " + e);
            }
            String s = String.valueOf(text.charAt(i));
            element.sendKeys(s);
        }

    }

    @Step("Ввод данных {text} без задержки")
    public void sendKeys(WebElement element, Keys text) {
        element.sendKeys(text);
    }

    @Step("Проверка заголовка")
    public void checkPageTitle() {
        $("title").shouldHave(attribute("text", "gg"));
    }

    @Step("Открытие страницы")
    public void openPage(String url) {
        open(url);
    }

    @Step("Проверка что текст {text} содержится в текущем URL")
    public void isTextContainsInURL(String text) {

        Assertions.assertTrue(WebDriverRunner.url().matches("(.*)"+ text + "(.*)"));
    }

    @Step("Проверка что текст {text} содержится полностью в элементе")
    public void isElementContainsText(SelenideElement element, String text) {
        element.shouldHave(matchText(text));
    }

    @Step("Проверка что текст {text} содержится в элементах коллекции")
    public void isElementsContainsText(ElementsCollection elements,String text) {

        for(WebElement element: elements) {

            String linkText = element.getText();

            if (!linkText.contains(text)) {
                fail("Ошибка в совпадении по запросу " + text + ".В ссылке оказалось " + element.getText());
            }

        }
    }

}
