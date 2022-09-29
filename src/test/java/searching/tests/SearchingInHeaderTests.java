package searching.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.SearchPage;
import tests.BaseTest;



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

    @DisplayName("Поиск по артикулу")
    @Description("Ввод артикула, переход на страницу результатов, переход в детальную карточку артикула")
    @ParameterizedTest(name = "#{index}. Поиск по артикулу - {0}")
    @MethodSource("constants.SearchingData#articles")
    public void checkArticleSearchCorrect(String requestText) {
        searchPage.checkIsArticleSearchCorrect(requestText);
    }


    @DisplayName("Поиск по ID")
    @Description("Ввод ID, переход на страницу результатов, переход в детальную карточку ID")
    @ParameterizedTest(name = "#{index}. Поиск по ID - {0}")
    @MethodSource("constants.SearchingData#ids")
    public void checkIsIDSearchCorrect(String element_ID) {
        searchPage.checkIsIDSearchCorrect(element_ID);
    }

}
