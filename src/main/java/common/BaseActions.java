package common;


import com.codeborne.selenide.*;

import io.qameta.allure.Step;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Random;


import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class BaseActions {

    @Step("Кнопка видна и клик по ней")
    public void click(@NotNull SelenideElement element) {
        element.shouldBe(visible).click();
    }

    @Step("Кнопка видна и клик по ней (JS)")
    public void clickByJS(String selector) {
        Selenide.executeJavaScript("document.querySelector(\"" + selector + "\").click();");
    }

    @Step("Элемент присутствует на странице")
    public void isElementVisible(@NotNull SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Список элементов присутствует на странице")
    public void isElementsListVisible(ElementsCollection elements) {
        elements = elements.filterBy(visible);
        elements.shouldBe(sizeGreaterThan(0));
    }

    @Step("Список элементов НЕ присутствует на странице")
    public void isElementsListInVisible(@NotNull ElementsCollection elements) {
        elements.shouldBe(size(0));
    }

    @Step("Элемент присутствует на странице в ходе длительной загрузки ({time}сек.)")
    public void isElementVisibleDuringLongTime(@NotNull SelenideElement element, int time) {
        element.shouldBe(visible, Duration.ofSeconds(time));
    }

    @Step("Элемент присутствует и кликабельный на странице")
    public void isElementVisibleAndClickable(@NotNull SelenideElement element) {
        element.shouldBe(visible,enabled);
    }

    @Step("Наведение на элемент мышью")
    public void moveMouseToElement(@NotNull SelenideElement element) {
        element.hover();
    }

    @Step("Генерация рандомного значения от {min} до {max}")
    public int generateRandomNumber(int min,int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    @Step("Получить аттрибут у элемента")
    public String getElementAttribute(@NotNull SelenideElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    @Step("Элемент присутствует на странице (ожидание 10сек)")
    public void isElementVisibleLongWait(@NotNull SelenideElement element) {
        element.shouldBe(enabled, Duration.ofSeconds(10));
    }

    @Step("Принудительное ожидание")
    public void forceWait(Long ms) {
        Selenide.sleep(ms);
    }

    @Step("Плавный скрол до видимого элемента (без JS)")
    public void scroll(@NotNull SelenideElement element) {
        element.scrollIntoView(false);
    }

    @Step("Плавный скрол до видимого элемента (только JS)")
    public void scrollByJS(String selector) {
        Selenide.executeJavaScript("document.querySelector(\"" + selector + "\").scrollIntoView({block: 'end',  behavior: 'smooth' })");
        Selenide.sleep(500);
    }

    @Step("Плавный скрол до самого низа страницы")
    public void scrollTillBottom() {

        Selenide.executeJavaScript("window.scrollTo({top: 5000,  behavior: 'smooth' })");
        Selenide.sleep(1000);

    }

    @Step("Принудительно прячем таббар")
    public void hideTapBar() {
        Selenide.executeJavaScript("document.querySelector('.menu').style.display = 'none'");
    }

    @Step("Принудительно раскрываем таббар")
    public void showTapBar() {
        Selenide.executeJavaScript("document.querySelector('.menu').style.display = 'block'");
    }

    @Step("Элемент не видим на странице")
    public void isElementInvisible(@NotNull SelenideElement element) {
        element.shouldNotBe(visible);
    }

    @Step("Элемент присутствует и видим на странице")
    public void isClickable(@NotNull SelenideElement element) {
        element.shouldBe(visible).shouldBe(enabled);
    }

    @Step("Видимы ли элементы в коллекции")
    public boolean isElementVisibleInCollections(@NotNull SelenideElement element) {
        return element.isDisplayed();
    }

    @Step("Количество элементов соответствует заданному значению: {counter}")
    public void isElementsSizeGreaterThanNumber(@NotNull ElementsCollection elements, int number) {
        elements.shouldHave(sizeGreaterThan(number));
    }

    @Step("Удаление текста из поля в элементе ")
    public void deleteTextInInput(@NotNull SelenideElement element) {
       element.clear();
    }

    @Step("Ввод данных {text} с задержкой")
    public void sendHumanKeys(SelenideElement element, @NotNull String text) {
        Random r = new Random();
        for(int i = 0; i < text.length(); i++) {

            Selenide.sleep((int)(r.nextGaussian() * 15 + 50));

            String s = String.valueOf(text.charAt(i));
            element.sendKeys(s);
        }

    }

    @Step("Ввод данных {text} без задержки")
    public void sendKeys(@NotNull WebElement element, Keys text) {
        element.sendKeys(text);
    }

    @Step("Проверка заголовка")
    public void checkPageTitle() {
        $("title").shouldHave(attribute("text", "gg"));
    }

    @Step("Открытие страницы {url}")
    public void openPage(String url) {
        open(url);
    }

    @Step("Проверка что текст {text} содержится в текущем URL")
    public void isTextContainsInURL(String text) {

        Assert.assertTrue(WebDriverRunner.url().matches("(.*)"+ text + "(.*)"));


    }

    @Step("Проверка что текст {text} содержится полностью в элементе")
    public void isElementContainsText(@NotNull SelenideElement element, String text) {
        element.shouldHave(matchText(text));
    }

    @Step("Проверка что текст {text} содержится в элементах коллекции")
    public void isElementContainsTextInCollection(@NotNull ElementsCollection elements, String text) {

        for(SelenideElement element: elements) {

            element.shouldHave(Condition.matchText(text));

        }
    }

    @Step("Преобразовываем\\вырезаем текст из селектора в число")
    public int convertSelectorTextIntoIntByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex,"");
        return Integer.parseInt(text);

    }

    @Step("Преобразовываем\\вырезаем текст из селектора в дабл")
    public double convertSelectorTextIntoDoubleByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex,"");
        return Double.parseDouble(text);

    }

    @Step("Преобразовываем\\вырезаем текст из селектора строку")
    public String convertSelectorTextIntoStrByRgx(@NotNull SelenideElement selector, String regex) {
        return selector.getText().replaceAll(regex,"");

    }


}
