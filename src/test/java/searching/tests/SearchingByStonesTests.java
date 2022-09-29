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

@Epic("Поиск по камням")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByStonesTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n")
    @DisplayName("Поиск по запросу  камня")
    @ParameterizedTest(name = "#{index}.  {0}")
    @MethodSource("constants.SearchingData#stones")
    public void checkOnlyStonesSearchCorrect(String requestText) {

        searchPage.checkIsOnlyStonesSearchCorrect(requestText);

    }

}
