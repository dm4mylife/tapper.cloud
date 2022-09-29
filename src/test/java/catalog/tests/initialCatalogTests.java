package catalog.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.MainCatalogPage;
import tests.BaseTest;

import static constants.Constant.Urls.ROOT_CATALOG_URL;

@Epic("Каталог")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class initialCatalogTests extends BaseTest {

     MainCatalogPage mainCatalogPage = new MainCatalogPage();


     @Test
     @Order(1)
     @DisplayName("Листинг каталога")
     @Description("Проверка что каталог не пустой и он есть на странице")
     public void checkIsCatalogNotEmpty() {

          mainCatalogPage.openPage(ROOT_CATALOG_URL);
          mainCatalogPage.checkIsCatalogNotEmpty();

     }

     @Test
     @DisplayName("Фотографии изделий каталога")
     @Description("Проверка что на главной странице нет карточек товара без фотографий")
     public void checkIsCatalogProductsHasImage() {

          mainCatalogPage.checkIsCatalogProductsHasImage();

     }



}
