package searching.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.SearchPage;
import tests.BaseTest;



@Epic("Поиск")
@DisplayName("Поиск")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SearchingInHeaderTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();


    //  <---------- Tests ---------->


    @Test
    @Order(1)
    @Feature("Проверка формы быстрого поиска")
    @DisplayName("Проверка формы быстрого поиска")
    @Description("При вводе запроса должна появится форма с результатами по запросу")
    public void checkInitialFastSearch() {
        searchPage.checkIsSearchResultContainerVisibleAndInvisible();
    }

    @Feature("Поиск по артикулу")
    @DisplayName("Поиск по артикулу")
    @Description("Ввод артикула, переход на страницу результатов, переход в детальную карточку артикула")
    @ParameterizedTest(name = "#{index}. - {0}")
    @MethodSource("constants.DDT.SearchingData#articles")
    public void checkArticleSearchCorrect(String requestText) {
        searchPage.checkIsArticleSearchCorrect(requestText);
    }


    @Feature("Поиск по ID")
    @DisplayName("Поиск по ID")
    @Description("Ввод ID, переход на страницу результатов, переход в детальную карточку ID")
    @ParameterizedTest(name = "#{index}. - {0}")
    @MethodSource("constants.DDT.SearchingData#ids")
    public void checkIsIDSearchCorrect(String element_ID) {
        searchPage.checkIsIDSearchCorrect(element_ID);
    }

}
