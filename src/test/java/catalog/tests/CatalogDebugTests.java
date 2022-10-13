package catalog.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import org.junit.jupiter.api.*;

import pages.MainCatalogPage;
import tests.BaseTest;


import static constants.Constant.Urls.MAIN_CATALOG_URL;


@Disabled
@Epic("Debug")
@DisplayName("Debug")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CatalogDebugTests extends BaseTest {


        MainCatalogPage mainCatalogPage  = new MainCatalogPage();


        //  <---------- Tests ---------->

        @Test
        @Flaky
        @Feature("Переход в рандомную карточку товара")
        @DisplayName("Клик по рандомной карточки каталога")
        @Description("Проверка клика по карточке изделия каталога и переход в детальную карточку")
        public void clickOnRandomCatalogProduct() {
                Configuration.headless = false;
                Selenide.open(MAIN_CATALOG_URL);
                mainCatalogPage.isProductContainerVisibleByHover();
        }



}
