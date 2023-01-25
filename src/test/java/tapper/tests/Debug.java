package tapper.tests;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.operations_history.OperationsHistory;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.Telegram;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_PASSWORD;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.tableNumber;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.*;


@Epic("Debug")
@DisplayName("E2E")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Debug {

    static Response rsGetOrder;
    static Response rsFillingOrder;
    static String visit;
    static String guid;
    static String uni;
    static String orderType;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Telegram telegram = new Telegram();

    //  <---------- Tests ---------->

    @Disabled
    @Test
    @DisplayName("kill")
    public void killTable() {


        apiRKeeper.orderPay
                (rqParamsOrderPay(R_KEEPER_RESTAURANT,rootPage.getGuid(TABLE_AUTO_1_ID)),API_STAGE_URI);

    }

    @Disabled
    @Test
    @DisplayName("info")
    public void getOrderInfo() {



        Response rs = apiRKeeper.getOrderInfo("1000044",API_STAGE_URI);

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

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_10);

        rootPage.matchTapperOrderWithOrderInKeeper(allDishesInfo);

       rootPageNestedTests.closeOrder();



    }

    @Disabled
    @Test
    @DisplayName("discount")
    public void deletePosition() {

        //  rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);

        Response rs = apiRKeeper.getOrderInfo("1000046", API_STAGE_URI);

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



    @Test
    @DisplayName("add discount")
    public void addDiscount() throws ParseException {

         String visit;
         String guid;
         double totalPay;
        String orderType = "part";
         HashMap<String, Integer> paymentDataKeeper;
         String transactionId;

        AuthorizationPage authorizationPage = new AuthorizationPage();
        OperationsHistory operationsHistory = new OperationsHistory();
        RootPage rootPage = new RootPage();
        ApiRKeeper apiRKeeper = new ApiRKeeper();
        RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
        NestedTests nestedTests = new NestedTests();


        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        operationsHistory.goToOperationsHistoryCategory();

        operationsHistory.isHistoryOperationsCorrect();


        operationsHistory.isDatePeriodSetByDefault();

        operationsHistory.checkTipsAndSumNotEmpty();
        operationsHistory.isWeekPeriodCorrect();
        operationsHistory.isMonthPeriodCorrect();
        operationsHistory.resetPeriodDate();
        operationsHistory.setCustomPeriod();

    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator1() {

        Response rs = apiRKeeper.getOrderInfo(TABLE_AUTO_1_ID,API_STAGE_URI);
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


    @Test
    @DisplayName("tg")
    public void tg() {



        rootPage.openPage(STAGE_RKEEPER_TABLE_111);
        rootPage.forceWait(4000);

        String tapperTable = "Стол: " + baseActions.convertSelectorTextIntoStrByRgx(tableNumber,"\\D+");
        System.out.println(tapperTable);

        double totalSumInCheck = rootPage.countAllDishes();
        String sumInCheck = String.valueOf(totalSumInCheck);

        System.out.println(totalSumInCheck + " totalSumInCheck");

        double restToPaySumD = rootPage.countAllNonPaidAndDisabledDishesInOrder();

        System.out.println(restToPaySumD + " restToPaySumD");

        String restToPaySum = "";

        if (discountSum.isDisplayed()) {

            double discountD = baseActions.convertSelectorTextIntoDoubleByRgx(discountSum,"[^\\d\\.]+");

            restToPaySumD -= discountD;

            System.out.println(restToPaySum + " restToPaySum with discount");

        }

        restToPaySum = String.valueOf(restToPaySumD);

        String tipsInTheMiddle = totalTipsSumInMiddle.getValue();
        System.out.println(tipsInTheMiddle + " tipsInTheMiddle");

        double paySumD = rootPage.getClearOrderAmount();
        String paySum = String.valueOf(paySumD);
        System.out.println(paySum + " paySum");

        Response rsGetOrder = apiRKeeper.getOrderInfo(TABLE_AUTO_1_ID,API_STAGE_URI);

        String totalPaid = "0";
        String payStatus = null;

        if (rsGetOrder.path("@attributes.prepaySum") != null) {


            totalPaid = rsGetOrder.jsonPath().getString("@attributes.prepaySum");
            Integer totalPaidInt = Integer.parseInt(totalPaid) / 100;
            totalPaid = String.valueOf(totalPaidInt);
            payStatus = "Частично оплачено";

        }

        System.out.println("\ntotalPaid\n" + totalPaid);

        String waiter = waiterName.getText();
        System.out.println(waiter + " waiter");

        LinkedHashMap<String, String> tapperDataForTgMsg = new LinkedHashMap<>();

        tapperDataForTgMsg.put("table",tapperTable);
        tapperDataForTgMsg.put("sumInCheck",sumInCheck);
        tapperDataForTgMsg.put("restToPaySum",restToPaySum);
        tapperDataForTgMsg.put("tips",tipsInTheMiddle);
        tapperDataForTgMsg.put("paySum",paySum);
        tapperDataForTgMsg.put("totalPaid",totalPaid);
        tapperDataForTgMsg.put("payStatus",payStatus);
        tapperDataForTgMsg.put("waiter",waiter);

        System.out.println(tapperDataForTgMsg);

    }

    @Test
    @DisplayName("magic")
    public void magic() {

        //String guid = rootPage.getGuid(TABLE_3_ID);
       // rootPage.closeOrderByAPI(TABLE_3_ID);

        String gg = "НЕ закрыт на кассе\n" +
                "НЕ закрыт на кассе \n" +
                "Причина: SeqNumber должен быть увеличен";

        System.out.println(gg.matches("(?s).*[\\n\\r].*SeqNumber.*"));


    }

}
