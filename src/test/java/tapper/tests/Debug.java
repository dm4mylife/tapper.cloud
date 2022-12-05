package tapper.tests;


import api.ApiRKeeper;
import com.google.gson.internal.bind.util.ISO8601Utils;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_10;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.RootPage.TipsAndCheck.discountField;
import static constants.Selectors.RootPage.TipsAndCheck.markupField;


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

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_10);

        Response rs = apiRKeeper.getOrderInfo("1000044");

        double dishDiscountRs = rs.jsonPath().getDouble("Session.Dish.Discount['@attributes'].amount") / 100;

        System.out.println(dishDiscountRs + " скидка на блюдо на кассе");

        double dishDiscountTapper = baseActions.convertSelectorTextIntoDoubleByRgx(discountField,"\\s₽");

        System.out.println(dishDiscountTapper + " поле 'Скидка' в таппере");

        double discountOrderRs = rs.jsonPath().getDouble("Session.Discount['@attributes'].amount") / 100;

        System.out.println(discountOrderRs + " скидка на заказ на кассе");

        double totalDiscount = dishDiscountRs + discountOrderRs;

        System.out.println(totalDiscount + " общая скидка");



    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator() {

        Response rs = apiRKeeper.getOrderInfo("1000044");

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName = null;
        int sessionSize = getSessionSize(rs);





    }





    public static int getSessionSize(Response rs) {

        int sessionSize = 0;

        Object sessionSizeFlag = rs.path("Session");

        if (sessionSizeFlag instanceof ArrayList) {

            sessionSize = rs.jsonPath().getList("Session").size();

        } else  {

            sessionSize = 1;
        }

        System.out.println(sessionSize + " размер сессии");
        return sessionSize;

    }


}
