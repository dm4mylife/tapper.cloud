package searchingInHearder;

import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.Constant.Urls.ROOT_URL;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Поиск в шапке")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)


public class searchingInHeaderTest extends BaseTest {

    private final By searchInput = By.cssSelector("#search_textbox_input_top");
    private final By searchContainer = By.cssSelector(".title-search-result");
    private final By linksInSearchContainer = By.cssSelector(".search-result-grid .title-search-item a");
    private final By spanText = By.cssSelector("span.search-item-info");



    @Test
    @DisplayName("тестовый для дебага")
    @Attachment(value = "Скриншот теста", type = "image/png")

    public void simpleBaseTestForDebugging() {

        basePage.open(ROOT_URL);
        basePage.fluentWait().until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("body"))));
        basePage.AttachScreen();
    }

   // @Test
    @Order(1)
    @DisplayName("Вввод поискового значения в строку поиска в шапке и применение поиска")
    @Description("При вводе запроса должна появится форма с результатами по запросу")
    @Attachment(value = "Скриншот теста", type = "image/png")

    public void checkIsSearchResultContainerVisible() throws IOException {

        basePage.open(ROOT_URL);
        basePage.click(searchInput);
        String searchRequestText = "Кольцо";
        basePage.sendHumanKeys(searchInput, searchRequestText);
        basePage.fluentWait().until(ExpectedConditions.visibilityOf(driver.findElement(searchContainer)));
        basePage.deleteTextInInput(searchInput);
        basePage.fluentWait().until(ExpectedConditions.invisibilityOf(driver.findElement(searchContainer)));
        basePage.AttachScreen();
    }

     //   @Test
  //  @Order(2)
    @DisplayName("Подсчёт и сравнение ссылок по результату запросов")
    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n" +
            "Все совпадения должны содержать в имени ")
    @ParameterizedTest(name = "#{index}. Значение в поиске - {0}")
    @ValueSource(strings = {"Кольцо","Серьги","Колье","Серьги","Браслет","Крест","Цепь","Броши и заколки"
            ,"Запонки и зажимы","Часы", "Пирсинг","Комплект","Оправа"})
    public void isLinksCorrectInContainer(String requestText) {

        basePage.deleteTextInInput(searchInput);
        basePage.sendHumanKeys(searchInput,requestText);
        basePage.fluentWait().until(ExpectedConditions.visibilityOf(driver.findElement(searchContainer)));

        List<WebElement> linksCount = driver.findElements(linksInSearchContainer);

        if (linksCount.size() != 8) {
            fail("Нет значений в поиске или их меньше 8 " + linksCount.size());
        }

        for(WebElement link: linksCount) {

            String linkText = link.findElement(spanText).getText();
            Pattern pattern = Pattern.compile(requestText);
            Matcher matcher = pattern.matcher(linkText);

            if(!matcher.find()) {
                fail("Ошибка в совпадении по запросу " + requestText + ".В ссылке оказалось " + linkText);
                System.out.println(link);
            }

        }

    }




}
