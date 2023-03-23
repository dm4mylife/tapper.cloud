package common;


import com.codeborne.selenide.*;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static data.Constants.WAIT_FOR_FULL_LOAD_PAGE;
import static data.Constants.WAIT_FOR_IMAGE_IS_FULL_LOAD_ON_CONTAINER;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentOptionsContainer;
import static data.selectors.TapperTable.RootPage.TapBar.appFooter;


public class BaseActions {

    @Step("Открытие страницы {url}")
    public void openPage(String url) {

        open(url);

    }

    public void click(@NotNull SelenideElement element) {

        element.shouldBe(visible).click();

    }



    public void clickByJs(String selector) {

        Selenide.executeJavaScript("document.querySelector('" + selector +"').click();");

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

    @Step("Смена разрешения браузера")
    public void changeBrowserSizeDuringTest(int width,int height) {

        WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(width,height));
        Selenide.refresh();
        forceWait(WAIT_FOR_FULL_LOAD_PAGE);

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

        Selenide.executeJavaScript
                ("window.scrollTo({ left: 0, top: document.body.scrollHeight, behavior: \"smooth\" });\n;");
        Selenide.sleep(1000);

    }

    public void switchBrowserTab(int tabIndex) {

        Selenide.switchTo().window(tabIndex);

    }

    @Step("Принудительно прячем способы оплаты и футер")
    public void hidePaymentOptionsAndTapBar() {

        if (paymentOptionsContainer.isDisplayed())
            Selenide.executeJavaScript
                    ("document.querySelector('[class=\"payedVariants\"]').style.display = 'none'");

        if (appFooter.isDisplayed())
            Selenide.executeJavaScript("document.querySelector('[class=\"appFooter\"]').style.display = 'none'");

    }

    @Step("Принудительно раскрываем способы оплаты и футер")
    public void showPaymentOptionsAndTapBar() {

        if (!paymentOptionsContainer.isDisplayed())
            Selenide.executeJavaScript("document.querySelector('[class=\"payedVariants\"]').style.display = 'block'");

        if (!appFooter.isDisplayed())
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

    @Step("Проверка что текст содержится в текущем URL")
    public void isTextContainsInURL(String url) {

        webdriver().shouldHave(urlContaining(url), Duration.ofSeconds(60));

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

        element.click(ClickOptions.usingDefaultMethod().timeout(Duration.ofSeconds(3)));
        element.sendKeys(Keys.CONTROL + "A");
        forceWait(500);
        element.sendKeys(Keys.BACK_SPACE);
        forceWait(500);
        element.shouldHave(empty,Duration.ofSeconds(5));

    }

    public double updateDoubleByDecimalFormat(@NotNull Double doubleNumber) {

        String formattedDouble =
                new DecimalFormat("#0.00").format(doubleNumber).replace(",", ".");
        return Double.parseDouble(formattedDouble);
    }

    @Step("Открытие страницы в новой вкладке с фокусом")
    public void openNewTabAndSwitchTo(String url) {

        Selenide.executeJavaScript("window.open('" + url + "', '_blank').focus();");
        forceWait(2000);
        switchBrowserTab(1);

    }

    @Step("Корректность отображения изображения")
    public void isImageCorrect(String element,String assertFailMessage) {

        final String JsScript = "function isImageNotBroken()  " +
                "{ var img = document.querySelector(\"" + element +
            "\"); if (img !== null && img.complete && typeof img.naturalWidth != 'undefined' && img.naturalWidth > 0) " +
            "{ return true; } else { return false; }} return isImageNotBroken();";

        forceWait(WAIT_FOR_IMAGE_IS_FULL_LOAD_ON_CONTAINER);
        boolean image = Boolean.TRUE.equals(Selenide.executeJavaScript(JsScript));
        Assertions.assertTrue(image, assertFailMessage);

    }

    @Step("Получить дату в формате {pattern}")
    public String getCurrentDateInFormat(String pattern) {

        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));

    }

}
