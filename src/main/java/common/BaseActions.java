package common;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;


public class BaseActions {

    @Step("Открытие страницы {url}")
    public void openPage(String url) {
        open(url);
    }

    public void click(@NotNull SelenideElement element) {
        element.shouldBe(visible).click();
    }

    public void scrollAndClick(SelenideElement element) {
        element.scrollIntoView(false);
        element.click();
    }

    public void isElementVisible(@NotNull SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Элемент присутствует на странице в ходе длительной загрузки ({time}сек.)")
    public void isElementVisibleDuringLongTime(@NotNull SelenideElement element, int time) {
        element.shouldBe(visible, Duration.ofSeconds(time));
    }

    @Step("Переключение на другого гостя ({guest})")
    public void switchTab(int tabIndex) {
        Selenide.switchTo().window(tabIndex);
    }


    public void isElementInvisible(@NotNull SelenideElement element) {
        element.shouldBe(hidden);
    }


    public void isElementsListVisible(ElementsCollection elements) {
         elements.filterBy(visible).shouldBe(sizeGreaterThan(0));
    }

    public void isElementsListInVisible(@NotNull ElementsCollection elements) {
        elements.shouldBe(size(0));
    }

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

    @Step("Плавный скрол до самого низа страницы")
    public void scrollTillBottom() {

        Selenide.executeJavaScript("window.scrollTo({ left: 0, top: document.body.scrollHeight, behavior: \"smooth\" });\n;");
        Selenide.sleep(1000);

    }

    @Step("Принудительно прячем футер")
    public void hideTapBar() {
        Selenide.executeJavaScript("document.querySelector('[class=\"appFooter\"]').style.display = 'none'");
    }

    @Step("Принудительно раскрываем футер")
    public void showTapBar() {
        Selenide.executeJavaScript("document.querySelector('[class=\"appFooter\"]').style.display = 'block'");
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

        webdriver().shouldHave(urlContaining(url), Duration.ofSeconds(30));

    }

    public int convertSelectorTextIntoIntByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex, "");
        return Integer.parseInt(text);

    }

    public double convertSelectorTextIntoDoubleByRgx(@NotNull SelenideElement selector, String regex) {

        String text = selector.getText().replaceAll(regex, "");
        return Double.parseDouble(text);

    }

    public String convertSelectorTextIntoStrByRgx(@NotNull SelenideElement selector, String regex) {
        return selector.getText().replaceAll(regex, "");

    }

    @Step("Удаление текста из поля")
    public void clearText(SelenideElement element) {

        element.click();
        element.sendKeys(Keys.CONTROL + "A");
        element.sendKeys(Keys.BACK_SPACE);

    }

    public double convertDouble(@NotNull Double doubleNumber) {

        String formattedDouble = new DecimalFormat("#0.00").format(doubleNumber).replace(",", ".");
        return Double.parseDouble(formattedDouble);
    }

    @Step("Открытие страницы в новой вкладке с фокусом")
    public void openNewTabAndSwitchTo(String url) {

        Selenide.executeJavaScript("window.open('" + url + "', '_blank').focus();");
        forceWait(2000);
        Selenide.switchTo().window(1);

    }

    @Step("Корректность отображения изображения")
    public void isImageCorrect(String element,String assertMessage) {

        final String JsScript = "function isImageNotBroken()  " +
                "{ var img = document.querySelector(\"" + element +
            "\"); if (img !== null && img.complete && typeof img.naturalWidth != 'undefined' && img.naturalWidth > 0) " +
            "{ return true; } else { return false; }} return isImageNotBroken();";

        boolean image = Boolean.TRUE.equals(Selenide.executeJavaScript(JsScript));
        Assertions.assertTrue(image, assertMessage);


    }

    @Step("Получить дату в формате {pattern}")
    public String getCurrentDateInFormat(String pattern) {

        return new SimpleDateFormat(pattern).format(new Date());

    }

}
