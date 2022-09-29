package searching.tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;
import tests.BaseTest;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск по категориям с камнями")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByCategoryWithStonesTests extends BaseTest {


    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @DisplayName("Поиск по запросу изделий с камнями")
    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)")
    @Feature("Поиск по запросу изделий с камнями")
    @ParameterizedTest(name = "#{index}. {0}")
    @MethodSource("constants.SearchingData#categoryWithStones")
    public void checkIsCategoryWithStonesSearchCorrect(String requestText) {

        searchPage.checkIsCategoryWithStonesSearchCorrect(requestText);

    }

}
