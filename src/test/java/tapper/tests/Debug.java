package tapper.tests;


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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_10;


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

        rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_10, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
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



        Response rs = apiRKeeper.getOrderInfo("1000044");

       // apiRKeeper.fillOrderWithAllModiDishes();

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName = null;

        int sessionDishSize = rs.jsonPath().getList("Session.Dish").size();
        System.out.println(sessionDishSize + " количество типов блюд\n");

        for (int currentDishIndex = 0; currentDishIndex <sessionDishSize; currentDishIndex++ ) {

            Map<String, Double> temporaryMap = new HashMap<>();

            double dishPrice = 0;
            int modificatorTypeSize = 0;

            if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") != null) {

                if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") instanceof LinkedHashMap) {

                    modificatorTypeSize = 1;

                } else {

                    modificatorTypeSize = rs.jsonPath().getList("Session.Dish["+ currentDishIndex +"].Modi").size();

                }

            }

            currentDishName = rs.jsonPath().getString("Session.Dish[" + currentDishIndex + "]['@attributes'].name");

            System.out.println(currentDishName + " имя текущего блюда");
            System.out.println(modificatorTypeSize + " количество типов модификатора у текущего типа блюд\n");

            double modificatorTotalPrice = 0;
            if (modificatorTypeSize == 1) {

                String modificatorName =
                        rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                "].Modi['@attributes'].name");
                System.out.println(modificatorName + " имя модификатора");

                String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi['@attributes'].price");

                double modificatorCurrentPrice = 0;

                if (modificatorCurrentPriceFlag != null) {

                    modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                    "].Modi['@attributes'].price") / 100;

                }

                System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi['@attributes'].count");
                System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                modificatorTotalPrice = modificatorCurrentPrice * modificatorCurrentCount;

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice ;

            } else {

                for (int currentModificatorTypeIndex = 0; currentModificatorTypeIndex < modificatorTypeSize; currentModificatorTypeIndex++) {

                    dishPrice = 0;

                    String modificatorName =
                            rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                    "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].name");
                    System.out.println(modificatorName + " имя модификатора");

                    String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi["
                                + currentModificatorTypeIndex + "]['@attributes'].price");

                    double modificatorCurrentPrice = 0;

                    if (modificatorCurrentPriceFlag != null) {

                        modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].price") / 100;

                    }

                    System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                    int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi["
                            + currentModificatorTypeIndex + "]['@attributes'].count");
                    System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                    modificatorTotalPrice += modificatorCurrentPrice * modificatorCurrentCount;
                    System.out.println(modificatorTotalPrice + " цена за количество и типы модификатора");

                }

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice ;

            }
            System.out.println(dishPrice + " общая цена за блюдо + сумма за его модики");

            int dishQuantity = rs.jsonPath().getInt("Session.Dish[" + currentDishIndex + "]['@attributes'].quantity") / 1000;

            for (int k = 0; k < dishQuantity; k++) {

                System.out.println("\nДобавлено в список под индексом " + totalDishIndex + "\n");
                temporaryMap.put(currentDishName, dishPrice);
                allDishesInfo.put(totalDishIndex, temporaryMap);

                totalDishIndex++;

            }

        }

        System.out.println("Итоговый список\n" + allDishesInfo);

        rootPage.forceWait(2000);

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_10);

        rootPage.matchTapperOrderWithOrderInKeeper(allDishesInfo);

       rootPageNestedTests.closeOrder();



    }

    @Disabled
    @Test
    @DisplayName("discount")
    public void deletePosition() {

        //  rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);

        Response rs = apiRKeeper.getOrderInfo("1000046");

        String session = "Session";

        Object sessionSizeFlag = rs.path(session);


        int sessionSize = 0;
        int sessionIndexCounter = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("Session").size();
            session = session + "[" + sessionIndexCounter + "]";

        }

        System.out.println(sessionSize + " количество сессий\n");

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            System.out.println(sessionIndexCounter + " текущая сессия");


            Object dishSizeSizeFlag = rs.path(session + ".Dish");

            String dish = ".Dish.";
            int dishSize = 0;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else {

                dishSize = rs.jsonPath().getList(session).size();
                dish = ".Dish[" + dishIndexCounter + "]";

            }

            System.out.println(dishSize + " количество блюд\n");

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {

                String s = rs.jsonPath().getString(session + dish);

                System.out.println(dishIndexCounter + " текущее блюдо");

                System.out.println(s);

            }

        }


    }




    public void addModificator() {




    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator1() {

        Response rs = apiRKeeper.getOrderInfo(TABLE_3_ID);
        Object sessionSizeFlag = rs.path("Session");

        int sessionSize;
        int sessionIndexCounter = 0;
        int totalDiscountAmount = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("Session").size();

        }

        System.out.println(sessionSize + " количество сессий\n");

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            String session;

            if (sessionSize == 1) {
                session = "Session";
            } else {
                session = "Session" + "[" + sessionIndexCounter + "]";
            }

            System.out.println("\n" + sessionIndexCounter + " текущая сессия");

            Object discountOrderFlag = rs.path(session + ".Discount['@attributes'].amount");

            if (discountOrderFlag != null) {

                String discountOrderPath = ".Discount";
                Object discountFlag = rs.path(session + discountOrderPath);

                int discountSize;
                int discountIndexCounter = 0;

                if (discountFlag instanceof LinkedHashMap) {

                    discountSize = 1;

                } else {

                    discountSize = rs.jsonPath().getList(session + discountOrderPath).size();

                }

                for (; discountIndexCounter < discountSize; discountIndexCounter++) {

                    String discount;

                    if (discountSize == 1) {

                        discount = session + discountOrderPath;

                    } else {

                        discount = session + discountOrderPath + "[" + discountIndexCounter + "]";

                    }

                    double discountOrder = rs.jsonPath().getDouble(discount + "['@attributes'].amount") / 100;
                    System.out.println("\n" + discountOrder + " скидка по заказу");

                    totalDiscountAmount -= discountOrder;

                }

            }

            String dishPath = ".Dish";
            Object dishSizeSizeFlag = rs.path(session + dishPath);

            String dish = ".Dish";
            int dishSize;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else if (dishSizeSizeFlag == null) {

                dishSize = 0;

            } else {

                dishSize = rs.jsonPath().getList(session + dishPath).size();
                dish = dishPath + "[" + dishIndexCounter + "]";

            }

            System.out.println(dishSize + " количество блюд\n");

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {

                Object discountDishFlag = rs.path(session + dish + ".Discount['@attributes'].amount");

                if (discountDishFlag != null) {

                    double discountDish = rs.jsonPath().getDouble(session + dish + ".Discount['@attributes'].amount") / 100;
                    System.out.println(discountDish + " скидка на блюдо");

                    totalDiscountAmount -= discountDish;


                }

            }

        }
        System.out.println("\n" + totalDiscountAmount + " общая скидка");

    }



}
