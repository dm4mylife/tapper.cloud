package common;


import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Random;


import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class BaseActions {

    @Step("Кнопка видна и клик по ней")
    public void click(SelenideElement element) {

        element.shouldBe(visible).click();

    }

    @Step("Элемент присутствует на странице")
    public void isElementVisible(SelenideElement element) {
        element.shouldBe(visible);
    }
    @Step("Список элементов присутствует на странице")
    public void isElementsListVisible(ElementsCollection elements) {
        elements = elements.filterBy(visible);
        elements.shouldBe(sizeGreaterThan(0));
    }
    @Step("Элемент присутствует на странице в ходе длительной загрузки ({time}сек.)")
    public void isElementVisibleDuringLongTime(SelenideElement element, int time) {
        element.shouldBe(visible, Duration.ofSeconds(time));
    }

    @Step("Элемент присутствует и кликабельный на странице")
    public void isElementVisibleAndClickable(SelenideElement element) {
        element.shouldBe(visible,enabled);
    }

    @Step("Наведение на элемент мышью")
    public void moveMouseToElement(SelenideElement element) {
        element.hover();
    }

    @Step("Генерация рандомного значения от {min} до {max}")
    public int generateRandomNumber(int min,int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    @Step("Получить аттрибут у элемента")
    public String getElementAttribute(SelenideElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    @Step("Элемент присутствует на странице (ожидание 10сек)")
    public void isElementVisibleLongWait(SelenideElement element) {
        element.shouldBe(enabled, Duration.ofSeconds(10));
    }

    @Step("Принудительное ожидание")
    public void forceWait(Long ms) {
        Selenide.sleep(ms);
    }

    @Step("Плавный скрол до видимого элемента")
    public void scroll(SelenideElement element) {

        element.scrollIntoView("{block: 'end', behavior: 'smooth'}");

    }

    @Step("Принудительно прячем таббар")
    public void hideTapBar() {

        Selenide.executeJavaScript("document.querySelector('.menu').style.display = 'none'");

    }

    @Step("Элемент не видим на странице")
    public void isElementInvisible(SelenideElement element) {
        element.shouldNotBe(visible);
    }

    @Step("Элемент присутствует и видим на странице")
    public void isClickable(SelenideElement element) {
        element.shouldBe(visible).shouldBe(enabled);
    }

    @Step("Видимы ли элементы в коллекции")
    public boolean isElementVisibleInCollections(SelenideElement element) {
        return element.isDisplayed();
    }

    @Step("Количество элементов соответствует заданному значению: {counter}")
    public void isElementsSizeGreaterThanNumber(ElementsCollection elements, int number) {
        elements.shouldHave(sizeGreaterThan(number));
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
    public void isElementContainsTextInCollection(ElementsCollection elements, String text) {

        for(SelenideElement element: elements) {

            element.shouldHave(Condition.matchText(text));


        }
    }


}
