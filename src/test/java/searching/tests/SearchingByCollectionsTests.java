package searching.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;
import tests.BaseTest;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск по коллекциям")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByCollectionsTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @DisplayName("Поиск по запросу коллекции")
    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n")
    @ParameterizedTest(name = "#{index}.  {0}")
    @MethodSource("constants.SearchingData#collections")
    public void checkCollectionsSearchCorrect(String requestText) {

        searchPage.checkIsCollectionSearchCorrect(requestText);

    }

}
