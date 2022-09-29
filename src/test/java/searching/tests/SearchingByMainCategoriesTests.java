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


@Epic("Поиск по главным категориям изделий")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class SearchingByMainCategoriesTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();

    @Test
    @Order(1)
    @DisplayName("Открытие корневой страницы")
    public void openPageForConcurrentExecution() {
        searchPage.openPage(ROOT_URL);
    }

    @DisplayName("Проверка по имени ключевых категорий изделий")
    @Feature("Проверка по имени ключевых категорий изделий")
    @Description("Кол-во совпадений должны быть 8 (на всю ширину формы)" + "Все совпадения должны содержать в имени ")
    @ParameterizedTest(name = "#{index}. Значение в поиске - {0}")
    @MethodSource("constants.SearchingData#mainCategories")
    public void checkMainCategorySearchCorrect(String requestText) {

        searchPage.checkIsCategorySearchCorrect(requestText);

    }

}
