package searching.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.SearchPage;
import tests.BaseTest;

import static constants.Constant.Urls.ROOT_URL;

@Epic("Поиск")
@DisplayName("Поиск")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByCollectionsTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @Story("Открытие корневой страницы")
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @Feature("Поиск по коллекциям")
    @DisplayName("Поиск по коллекциям")
    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n")
    @ParameterizedTest(name = "#{index}. {0}")
    @MethodSource("constants.DDT.SearchingData#collections")
    public void checkCollectionsSearchCorrect(String requestText) {

        searchPage.checkIsCollectionSearchCorrect(requestText);

    }

}
