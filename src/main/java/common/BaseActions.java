package common;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlStartingWith;


public class BaseActions {

    @Step("Открытие страницы {url}")
    public void openPage(String url) {
        open(url);
    }

    @Step("Кнопка видна и клик по ней")
    public void click(@NotNull SelenideElement element) {
        element.shouldBe(visible).click();
    }

    @Step("Кнопка видна и клик по ней (JS)")
    public void clickByJS(String selector) {
        Selenide.executeJavaScript("document.querySelector(\"" + selector + "\").click();");
    }

    @Step("Скрол до элемента и клик по нему")
    public void scrollAndClick(SelenideElement element) {
        element.scrollIntoView(false);
        element.click();
    }

    @Step("Элемент присутствует на странице")
    public void isElementVisible(@NotNull SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Элемент присутствует на странице в ходе длительной загрузки ({time}сек.)")
    public void isElementVisibleDuringLongTime(@NotNull SelenideElement element, int time) {
        element.shouldBe(visible, Duration.ofSeconds(time));
    }

    @Step("Получаем текст из селектора")
    public String getSelectorText(SelenideElement element) {
        return element.getText();
    }

    @Step("Элемент должен содержать значение {value}")
    public SelenideElement elementShouldHaveValue(SelenideElement element, String value) {
        return element.shouldHave(value(value));
    }

    @Step("Переключение на другого гостя ({guest})")
    public void switchOnAnotherGuest(int guest) {
        Selenide.switchTo().window(guest);
    }

    @Step("Элемент не видим на странице")
    public void isElementInvisible(@NotNull SelenideElement element) {
        element.shouldNotBe(visible);
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

    @Step("Элемент присутствует и кликабельный на странице")
    public void isElementVisibleAndClickable(@NotNull SelenideElement element) {
        element.shouldBe(visible, enabled);
    }

    @Step("Генерация рандомного значения от {min} до {max}")
    public int generateRandomNumber(int min, int max) {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    @Step("Принудительное ожидание из-за долгой загрузки страницы,элементов,скриптов ({ms} мс)")
    public void forceWait(int ms) {
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

        Selenide.executeJavaScript("window.scrollTo({ left: 0, top: document.body.scrollHeight, behavior: \"smooth\" });\n;");
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

    @Step("Ввод данных {text} с задержкой")
    public void sendHumanKeys(SelenideElement element, @NotNull String text) {
        Random r = new Random();
        for (int i = 0; i < text.length(); i++) {

            Selenide.sleep((int) (r.nextGaussian() * 15 + 50));

            String s = String.valueOf(text.charAt(i));
            element.sendKeys(s);
        }

    }

    @Step("Ввод данных {text} без задержки")
    public void sendKeys(@NotNull SelenideElement element, String text) {
        element.sendKeys(text);
    }

    @Step("Проверка что текст {text} содержится в текущем URL")
    public void isTextContainsInURL(String url) {

        webdriver().shouldHave(urlStartingWith(url), Duration.ofSeconds(20));

    }

    @Step("Преобразовываем\\вырезаем текст из селектора в число")
    public int convertSelectorTextIntoIntByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex, "");
        return Integer.parseInt(text);

    }

    @Step("Преобразовываем\\вырезаем текст из селектора в дабл")
    public double convertSelectorTextIntoDoubleByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex, "");
        return Double.parseDouble(text);

    }

    @Step("Преобразовываем\\вырезаем текст из селектора строку")
    public String convertSelectorTextIntoStrByRgx(@NotNull SelenideElement selector, String regex) {
        return selector.getText().replaceAll(regex, "");

    }

    @Step("Обрезаем у дабла всё до двух чисел после запятой")
    public double convertDouble(@NotNull Double doubleNumber) {

        String formattedDouble = new DecimalFormat("#0.00").format(doubleNumber).replace(",", ".");
        return Double.parseDouble(formattedDouble);
    }

}
