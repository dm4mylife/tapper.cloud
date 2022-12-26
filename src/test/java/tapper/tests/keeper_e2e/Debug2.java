package tapper.tests.keeper_e2e;


import api.ApiRKeeper;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.$;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug2 extends BaseTest {

    static Response rsGetOrder;
    static Response rsFillingOrder;
    static String visit;
    static String guid;
    static String uni;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    //  <---------- Tests ---------->


    @Test
    @DisplayName("create and fill")
    public void test() {

        rootPage.openTapperTable("https://tapper3.zedform.ru/testrkeeper/1000048");

        int counter = 1;

        while (counter <= 1000) {

            String text = "Попытка ddos`а: " + counter;
            System.out.println(text);

            baseActions.click($(".callWaiter"));

            baseActions.sendKeys($(".callWaiter__textarea"), text);

            baseActions.click($(".callWaiter-btn.send"));
            baseActions.click($(".successfulSendin__btn"));

            counter++;

        }




    }




}
