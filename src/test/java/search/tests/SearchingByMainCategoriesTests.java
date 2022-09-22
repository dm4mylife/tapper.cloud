package search.tests;


import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pages.SearchPage;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск по главным категориям изделий")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByMainCategoriesTests {


    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }


    @ParameterizedTest(name = "#{index}. Значение в поиске - {0}")
    @Feature("Проверка по имени ключевых категорий изделий")
    @DisplayName("Проверка по имени ключевых категорий изделий")
    @Description("Кол-во совпадений должны быть 8 (на всю ширину формы)" + "Все совпадения должны содержать в имени ")
    @ValueSource(strings = {"Кольцо","Серьги","Колье","Серьги","Браслет","Крест","Цепь","Броши и заколки",
            "Запонки и зажимы","Часы", "Пирсинг","Комплект","Оправа"})

    public void checkMainCategorySearchCorrect(String requestText) {

        searchPage.checkIsCategorySearchCorrect(requestText);

    }

}
