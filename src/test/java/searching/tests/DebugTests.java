package searching.tests;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import pages.SearchPage;
import tests.BaseTest;

@Disabled
@Epic("Debug")
@DisplayName("Debug")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();


    //  <---------- Tests ---------->

    @Test
    @DisplayName("тестовый для дебага")
    public void test() {
        searchPage.simpleBaseTestForDebugging();
    }



}
