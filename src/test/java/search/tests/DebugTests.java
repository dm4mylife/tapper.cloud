package search.tests;


import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.SearchPage;
import tests.BaseTest;


@Epic("Debug")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.SAME_THREAD)
public class DebugTests extends BaseTest {

    SearchPage searchPage  = new SearchPage();


    //  <---------- Tests ---------->

    @Disabled
    @Test
    @DisplayName("тестовый для дебага")
    @Attachment(value = "Page screenshot", type = "image/png")

    public void test() {
        searchPage.simpleBaseTestForDebugging();
    }



}
