package catalog.tests;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import pages.MainCatalogPage;


@Epic("Debug")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CatalogDebugTests {


        MainCatalogPage catalogPage  = new MainCatalogPage();


        //  <---------- Tests ---------->

        @Test
        @DisplayName("тестовый для дебага")
        public void test() {

        }




}
