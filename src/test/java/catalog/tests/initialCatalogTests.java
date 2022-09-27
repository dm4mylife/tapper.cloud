package catalog.tests;

import com.codeborne.selenide.SelenideElement;
import pages.RootCatalog;
import tests.BaseTest;

import static com.codeborne.selenide.Selenide.$;

public class initialCatalogTests extends BaseTest {

     RootCatalog rootCatalog  = new RootCatalog();

     public void isCatalogNotEmpty() {

          rootCatalog.checkIsCatalogNotEmpty();
     }

}
