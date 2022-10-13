package catalog.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import pages.MainCatalogPage;
import tests.BaseTest;

import static constants.Constant.Urls.MAIN_CATALOG_URL;

@Epic("Каталог")
@DisplayName("Каталог")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class initialCatalogTests extends BaseTest {

     MainCatalogPage mainCatalogPage = new MainCatalogPage();


     @Test
     @Order(1)
     @Story("Листинг каталога")
     @DisplayName("Листинг каталога")
     @Description("Проверка что каталог не пустой и он есть на странице")
     public void checkIsCatalogNotEmpty() {

          mainCatalogPage.openPage(MAIN_CATALOG_URL);
          mainCatalogPage.checkIsCatalogNotEmpty();

     }

     @Test
     @Order(2)
     @Feature("Фотографии изделий каталога")
     @DisplayName("Фотографии изделий каталога")
     @Description("Проверка что на главной странице нет карточек товара без фотографий")
     public void checkIsCatalogProductsHasImage() {

          mainCatalogPage.checkIsCatalogProductsHasImage();

     }

     @Test
     @Order(3)
     @Feature("Раскрытие карточки при наведении")
     @DisplayName("Раскрытие карточки при наведении")
     @Description("После наведения мыши на карточку, должно раскрываться превью самой карточки с доп. информацией")
     public void isProductContainerVisibleByHover() {

          mainCatalogPage.isProductContainerVisibleByHover();

     }

     @Test
     @Order(4)
     @Feature("Сортировка по 'Показать по:'")
     @DisplayName("Показать по: 20, 50, 100")
     @Description("Проверка отображения количества элементов на странице при нажатии на 'Показать по:'")
     public void checkIsPageElementsCountCorrect() {
          mainCatalogPage.checkIsPageElementsCountCorrect();
     }

     @Test
     @Order(5)
     @Feature("Переход в рандомную карточку товара")
     @DisplayName("Клик по рандомной карточки каталога")
     @Description("Проверка клика по карточке изделия каталога и переход в детальную карточку")
     public void clickOnRandomCatalogProduct() {
          mainCatalogPage.clickOnRandomCatalogProduct();
     }

}
