package searchingInHeader;

import com.codeborne.selenide.*;
import common.BaseActions;
import io.qameta.allure.Description;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import tests.BaseTest;


import static com.codeborne.selenide.Selenide.*;
import static constants.Constant.Urls.ROOT_URL;
import static org.junit.jupiter.api.Assertions.fail;

@Nested
@DisplayName("Поиск в шапке")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchingInHeaderTests extends BaseTest {

    BaseActions baseActions = new BaseActions();

    private final SelenideElement searchInput = $("#search_textbox_input_top");
    private final SelenideElement searchContainer = $(".title-search-result");
    private final ElementsCollection linksInSearchContainer = $$(".search-result-grid .title-search-item a");
    private final SelenideElement linkInSearchContainer = $(".search-result-grid .title-search-item a");
    private final SelenideElement spanText = $("span.search-item-info");
    private final SelenideElement resultPageProductContainer = $("a[data-target='click_search_listing']");


   /* @Disabled
    @Test
    @DisplayName("тестовый для дебага") */

    public void simpleBaseTestForDebugging() {

        baseActions.openPage("https://miuz.ru/catalog/rings/R01-PL-34397/");

        baseActions.sendHumanKeys(searchInput,"Средство");
        baseActions.isElementVisible(searchContainer);
        baseActions.isElementsContainsText($$(linksInSearchContainer),"Средство");

        WebDriverRunner.closeWebDriver();

    }

    @Test
    @Order(1)
    @DisplayName("Вввод поискового значения в строку поиска и применение поиска")
    @Description("При вводе запроса должна появится форма с результатами по запросу")

    public void checkIsSearchResultContainerVisible() {

        baseActions.openPage(ROOT_URL);
        baseActions.click(searchInput);
        baseActions.sendHumanKeys(searchInput, "Кольцо");
        baseActions.isElementVisible(searchContainer);
        baseActions.deleteTextInInput(searchInput);
        baseActions.isElementInVisible(searchContainer);

    }


    @Order(2)
    @DisplayName("Подсчёт и сравнение ссылок по результату запросов")
    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n" +
            "Все совпадения должны содержать в имени ")
    @ParameterizedTest(name = "#{index}. Значение в поиске - {0}")
    @ValueSource(strings = {"Кольцо","Серьги","Колье","Серьги","Браслет","Крест","Цепь","Броши и заколки"
            ,"Запонки и зажимы","Часы", "Пирсинг","Комплект","Оправа"})

    public void checkIsLinksCorrectInContainer(String requestText) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,requestText);
        baseActions.isElementVisible(searchContainer);

        if (linksInSearchContainer.size() != 8) {

            fail("Нет значений в поиске или их меньше 8. Найдено элементов: " + linksInSearchContainer.size());

        } else {

            baseActions.isElementsContainsText(linksInSearchContainer,requestText);

        }


    }

    @Order(3)
    @DisplayName("Поиск по артикулу")
    @Description("Ввод артикула, переход на страницу результатов, переход в детальную карточку артикула")
    @ParameterizedTest(name = "#{index}. Поиск по артикулу - {0}")
    @ValueSource(strings = {"R01-WED-00171-4","E2034-20OR2999L","C30-NC12-206PG-05040"})
    public void checkIsArticleSearchCorrect(String article) {

        baseActions.deleteTextInInput(searchInput);
        baseActions.sendHumanKeys(searchInput,article);

        baseActions.isElementVisible(linkInSearchContainer);
        baseActions.isElementContainsText(linkInSearchContainer,article);

        baseActions.sendKeys(searchInput, Keys.ENTER);

        baseActions.isTextContainsInURL(article);
        baseActions.isClickable(resultPageProductContainer);

        baseActions.click(resultPageProductContainer);

        baseActions.isTextContainsInURL(article);


    }

}
