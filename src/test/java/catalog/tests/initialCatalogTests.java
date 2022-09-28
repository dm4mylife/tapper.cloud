package catalog.tests;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import org.junit.jupiter.api.Test;
import pages.RootCatalogPage;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Selenide.$;
import static constants.Selectors.RootCatalogPage.catalogProducts;

public class initialCatalogTests extends BaseTest {

     RootCatalogPage rootCatalogPage = new RootCatalogPage();

     BaseActions baseActions = new BaseActions();

     @Test
     public void debugTest() {

     rootCatalogPage.openPage("https://miuz.ru/catalog/");
     rootCatalogPage.checkIsCatalogNotEmpty();
     rootCatalogPage.checkIsCatalogProductsHasImage();


     }



}
