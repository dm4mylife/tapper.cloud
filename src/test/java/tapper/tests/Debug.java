package tapper.tests;


import api.ApiRKeeper;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug extends BaseTest {

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

    @Disabled
    @Test
    @DisplayName("create and fill")
    public void test() {

        rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        visit = rsGetOrder.jsonPath().getString("result.visit");

        rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, GLAZUNYA, "1000"));
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, SOLYANKA, "1000"));

        guid = rsGetOrder.jsonPath().getString("result.guid");
        //uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish[\"@attributes\"].uni");

        System.out.println(visit + " visit");
        System.out.println(guid + " guid");
        System.out.println(uni + " uni");

        apiRKeeper.addModificatorOrder(guid, "1000055", "1000", "1000111", "2");
        apiRKeeper.addModificatorOrder(guid, "1000055", "1000", "1000112", "2");
        apiRKeeper.addModificatorOrder(guid, "1000147", "3000", "1000117", "3");
        apiRKeeper.addModificatorOrder(guid, "1000147", "1000", "1000118", "1");


        System.out.println("get order data\n");

        System.out.println(rsGetOrder.body());

        // apiRKeeper.deletePosition(guid,"1000055","4000");
        // apiRKeeper.deletePosition(guid,"1000147","4000");

        // apiRKeeper.deleteOrder(visit);

    }

    @Disabled
    @Test
    @DisplayName("info")
    public void getOrderInfo() {

        Response rs = apiRKeeper.getOrderInfo("1000046");

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        System.out.println(rs.jsonPath().getList("Session.Dish").size() + " dishes size");

        int dishesSize = rs.jsonPath().getList("Session.Dish").size();

        int i = 0;

        for (; i < dishesSize; i++) {

            Map<String, Double> temporaryMap = new HashMap<>();


            String modificator = rs.path("Session.Dish[" + i + "]['@attributes'].Modi");

            if (modificator != null) {

                String dishName = rs.jsonPath().getString("Session.Dish[" + i + "]['@attributes'].name");
                System.out.println("\n dish name " + rs.jsonPath().getString("Session.Dish[" + i + "]['@attributes'].name"));

                double dishPrice = rs.jsonPath().getDouble("Session.Dish[" + i + "]['@attributes'].price");
                dishPrice = baseActions.convertDouble(dishPrice) / 100;

                int modificatorSize = rs.jsonPath().getList("Session.Dish[" + i + "]['@attributes'].Modi").size();
                System.out.println(modificatorSize + " modificator size");

                int l = 0;

                if (modificatorSize > 1) {

                    for (; l < modificatorSize; l++) {

                        System.out.println("modificatorSize больше 1");
                        temporaryMap.put(dishName, dishPrice);
                        allDishesInfo.put(i + l, temporaryMap);

                    }

                } else {

                    System.out.println("modificatorSize всего 1");
                    temporaryMap.put(dishName, dishPrice);
                    allDishesInfo.put(i + l, temporaryMap);


                }


            } else {

                System.out.println("no modi dish");

                int dishQuantity = 1;

                String dishName = rs.jsonPath().getString("Session.Dish[" + i + "]['@attributes'].name");
                System.out.println("\n dish name " + rs.jsonPath().getString("Session.Dish[" + i + "]['@attributes'].name"));

                double dishPrice = rs.jsonPath().getDouble("Session.Dish[" + i + "]['@attributes'].price");
                dishPrice = baseActions.convertDouble(dishPrice) / 100;

                System.out.println("\n dish price " + rs.jsonPath().getString("Session.Dish[" + i + "]['@attributes'].price"));
                System.out.println(dishPrice + " clean dish price");

                int dishAmount = Integer.parseInt(rs.path("Session.Dish[" + i + "]['@attributes'].quantity")) / 1000;

                if (dishAmount != 0) {

                    dishQuantity = rs.jsonPath().getInt("Session.Dish[" + i + "]['@attributes'].quantity") / 1000;
                    System.out.println(dishQuantity + " dishCount");

                }

                int k = 0;

                if (dishQuantity > 1) {

                    for (; k < dishQuantity; k++) {

                        System.out.println("dishQuantity больше 1");
                        temporaryMap.put(dishName, dishPrice);
                        allDishesInfo.put(i + k, temporaryMap);

                    }

                } else {

                    System.out.println("dishQuantity всего 1");
                    temporaryMap.put(dishName, dishPrice);
                    allDishesInfo.put(i + k, temporaryMap);


                }

            }


        }

        System.out.println(allDishesInfo);
    }

    @Disabled
    @Test
    @DisplayName("pay order")
    public void payOrder() {

        apiRKeeper.payOrder(guid, "50000");

    }

    @Disabled
    @Test
    @DisplayName("delete")
    public void deletePosition() {

        apiRKeeper.deletePosition("{4AB8A1FB-08DC-48A4-B368-6DA2834CD48B}", "2", "1");

    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator() {

        rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));

    }

    @Test
    @DisplayName("add modificator")
    public void gg() {


        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);


    }


}
