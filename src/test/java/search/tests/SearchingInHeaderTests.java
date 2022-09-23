package search.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;
import tests.BaseTest;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск в шапке")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchingInHeaderTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();



    //  <---------- Tests ---------->


    @Test
    @Order(1)
    @DisplayName("Проверка формы быстрого поиска")
    @Description("При вводе запроса должна появится форма с результатами по запросу")
    public void checkInitialFastSearch() {
        searchPage.checkIsSearchResultContainerVisibleAndInvisible();
    }


    @Description("Ввод артикула, переход на страницу результатов, переход в детальную карточку артикула")
    @DisplayName("Поиск по артикулу")
    @ParameterizedTest(name = "#{index}. Поиск по артикулу - {0}")
    @ValueSource(strings = {"R01-WED-00171-4","E2034-20OR2999L","C30-NC12-206PG-05040"})
    public void checkArticleSearchCorrect(String requestText) {
        searchPage.checkIsArticleSearchCorrect(requestText);
    }


    @DisplayName("Поиск по ID")
    @Description("Ввод ID, переход на страницу результатов, переход в детальную карточку ID")
    @ParameterizedTest(name = "#{index}. Поиск по ID - {0}")
    @ValueSource(strings = {"65059253","65717917","65754114","66558219"})
    public void checkIsIDSearchCorrect(String element_ID) {
        searchPage.checkIsIDSearchCorrect(element_ID);
    }

}