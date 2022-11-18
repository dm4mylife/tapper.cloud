package tapper.tests;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {

    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    static Response rsGetOrder;
    static Response rsFillingOrder;
    static String visit;
    static String guid;
    static String uni;

    //  <---------- Tests ---------->


    @Test
    @DisplayName("create and fill")
    public void test() {

        rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        visit = rsGetOrder.jsonPath().getString("result.visit");

        rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));

        guid = rsGetOrder.jsonPath().getString("result.guid");
        //  uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish[\"@attributes\"].uni");

        System.out.println(visit + " visit");
        System.out.println(guid + " guid");
        System.out.println(uni + " uni");

        apiRKeeper.addModificatorOrder(guid,"1000055","1000","1000111","2");
        apiRKeeper.addModificatorOrder(guid,"1000055","1000","1000112","2");
        apiRKeeper.addModificatorOrder(guid,"1000147","3000","1000117","3");
        apiRKeeper.addModificatorOrder(guid,"1000147","1000","1000118","1");


    }
    @Disabled
    @Test
    @DisplayName("delete")
    public void delete() {

       apiRKeeper.deletePosition(guid,uni,"5000");


    }
    @Disabled
    @Test
    @DisplayName("pay order")
    public void payOrder() {

        apiRKeeper.payOrder(guid,"50000");


    }
    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator() {

        System.out.println(guid + " guid");




    }




}
