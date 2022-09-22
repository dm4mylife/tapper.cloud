package search.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;

import static constants.Constant.Urls.ROOT_URL;


@Epic("Поиск по камня")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchingByStonesTests {

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
    @ValueSource(strings = {"Агат", "Аквамарин", "Александрит", "Алпанит", "Аметист", "Аметрин", "Без камней", "Берил",
            "Бирюза", "Бриллианит", "Бриллиант", "Выращенный Бриллиант", "Гранат", "Жемчуг", "Изумруд", "Иолит",
            "Каучук", "Кварц", "Керамика", "Кианит", "Корунд", "Кристалл", "Кунцит", "Лондон Топаз", "Лунный Камень",
            "Морганит", "Наноситал", "Оникс", "Опал", "Перидот", "Перламутр", "Пластик", "Родолит", "Рубин", "Сапфир",
            "Сваровски", "Силикон", "Стекло", "Султанит", "Танзанит", "Топаз", "Турмалин", "Фианит", "Халцедон",
            "Хризолит", "Хромдиопсид", "Хрусталь", "Цаворит", "Цитрин", "Шнурок", "Шпинель", "Эмаль", "Янтарь"})

    public void checkOnlyStonesSearchCorrect(String requestText) {

        searchPage.checkIsOnlyStonesSearchCorrect(requestText);

    }

}
