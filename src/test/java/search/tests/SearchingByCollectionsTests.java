package search.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск по коллекциям")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchingByCollectionsTests {

    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @Description("Количество совпадений должны быть 8 (на всю ширину контейнера)\n")
    @DisplayName("Поиск по запросу коллекции")
    @ParameterizedTest(name = "#{index}.  {0}")
    @ValueSource(strings = {"Solo 1920", "Brilliance", "Millennium", "Champagne", "Empire", "Энигма", "Arabella",
            "Black&White", "Bonita", "Caprice", "Contrast", "Estella", "Festa", "Fit", "Flaming Ice", "Grace",
            "Infinite love", "Le Chic", "Leviev", "Liola", "Love", "Lunia", "Mia", "Millennium Premium",
             "Naomi", "Princess", "Royal", "Royal Diamond", "Snowflake", "Tenero", "Trendy Oro",
            "Vittoria", "Waterfalls", "Вишня в цвету", "Детская коллекция", "Драгоценное наследие",
            "Мой первый бриллиант", "Мужская", "Обручальные кольца", "Отражение небес", "Помолвочные кольца",
            "Русская классика", "Серебро с бриллиантами", "Символ веры", "Танзаниты", "Трилогия", "Флирт",
            "Вне коллекций",})

    public void checkCollectionsSearchCorrect(String requestText) {

        searchPage.checkIsCollectionSearchCorrect(requestText);

    }

}
