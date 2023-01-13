package tapper.tests;


import api.ApiRKeeper;
import com.codeborne.selenide.*;
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

import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Configuration.browser;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.Best2PayPage.paymentContainer;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.divideCheckSlider;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.tips20;


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
    public void test() throws ParseException {

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_3);

        rootPageNestedTests.chooseDishesWithRandomAmount(3);

        HashMap<Integer, Map<String, Double>> chosenDishes = rootPage.getChosenDishesAndSetCollection();

        System.out.println(chosenDishes);
        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPage.setUserCookie(COOKIE_GUEST_FIRST_USER, COOKIE_SESSION_FIRST_USER);
        rootPage.checkIfDishesDisabledEarlier(chosenDishes);

        rootPage.switchTab(0);

        paymentContainer.shouldBe(Condition.exist, Duration.ofSeconds(5));

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPage.clickOnPaymentButton();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        rootPage.switchTab(1);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

        rootPage.switchTab(0);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);


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
    public void addDiscount() {

       /* Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        String visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "3000"));

        apiRKeeper.addDiscount(rqParamsAddCustomDiscount(R_KEEPER_RESTAURANT,guid, CUSTOM_DISCOUNT_ON_ORDER,"5000"),API_STAGE_URI);
        apiRKeeper.addDiscount(rqParamsAddDiscount(R_KEEPER_RESTAURANT,guid, DISCOUNT_ON_DISH),API_STAGE_URI); */


         double totalPay;
         HashMap<String, Integer> paymentDataKeeper;
         String transactionId;
         String visit;
         String guid;
         double discount;

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);


        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

        apiRKeeper.addDiscount(rqParamsAddCustomDiscount(R_KEEPER_RESTAURANT,guid, CUSTOM_DISCOUNT_ON_ORDER,"5000"),API_STAGE_URI);
        apiRKeeper.addDiscount(rqParamsAddDiscount(R_KEEPER_RESTAURANT,guid, DISCOUNT_ON_DISH),API_STAGE_URI);


        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_3);
        discount = rootPageNestedTests.getTotalDiscount(TABLE_3_ID);

        rootPageNestedTests.checkAllDishesSumsWithAllConditions(discount);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPageNestedTests.clickPayment();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);


    }

    @Disabled
    @Test
    @DisplayName("add modificator")
    public void addModificator1() {

        Response rs = apiRKeeper.getOrderInfo(TABLE_3_ID,API_STAGE_URI);
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

        //Configuration.headless = false;

        //rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);

      //  Response rsGetOrder = apiRKeeper.getOrderInfo(TABLE_3_ID,API_STAGE_URI);
      //  guid = rsGetOrder.jsonPath().getString("@attributes.guid");
        List<Object> tgMessages = apiRKeeper.getUpdates();
        System.out.println(guid);

        boolean hasOrderMessage = false;
        String isOrderMsg = "Название";
        String isCallWaiterMsg = "Вызов официанта";
        String isReviewMsg = "Рейтинг";
        String partPay = "Частично оплачено";
        String fullPay = "Полностью оплачено";

        for (int index = 0 ; index < tgMessages.size(); index++) {

            String tgMsg = tgMessages.get(index).toString();

            if (tgMessages.get(index).toString().contains(guid)) {

                hasOrderMessage = true;
                System.out.println("\n Сообщение подходящее под guid \n");
                System.out.println(tgMsg);

                if (tgMsg.contains(isOrderMsg)) {

                    System.out.println("Тип сообщения 'Оплата'");

                    String tableRegex = "(\\n|.)?Стол: ([\\d+]+)(\\n|.)*";
                    String sumInCheckRegex = "(\\n|.)*Сумма в чеке: (\\d+\\.?\\d+)(\\n|.)";
                    String restToPayRegex = "(\\n|.)*Осталось оплатить: (\\d+\\.?\\d+)(\\n|.)*";
                    String tipsRegex = "(\\n|.)*Чаевые: (\\d+\\.?\\d+)(\\n|.)*";
                    String paySumRegex = "(\\n|.)*Осталось оплатить: (\\d+\\.?\\d+)(\\n|.)*";
                    String totalPaidRegex = "(\\n|.)*Всего оплачено: (\\d+\\.?\\d+)(\\n|.)*";
                    String markUpRegex = "(\\n|.)*Наценка: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
                    String discountRegex = "(\\n|.)*Скидка: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
                    String payStatusRegex = "(\\n|.)*Статус оплаты: ([а-яА-Я\\s]+)\\nСтатус заказа(\\n|.)*";
                    String orderStatusRegex = "(\\n|.)*Статус заказа: ([а-яА-Я\\:\\,\\s]+)Дата заказа(\\n|.)*";
                    String dateOrderRegex = "(\\n|.)*Дата заказа: ([\\d\\.\\s\\|\\:]+)(\\n|.)*";
                    String waiterRegex = "(\\n|.)*Официант: ([а-яА-Я\\s]+)\\n(\\n|.)*";


                    String table = tgMsg.replaceAll(tableRegex,"$2");
                    String sumInCheck = tgMsg.replaceAll(sumInCheckRegex,"$2");
                    String restToPay = tgMsg.replaceAll(restToPayRegex,"$2");
                    String tips = tgMsg.replaceAll(tipsRegex,"$2");
                    String paySum = tgMsg.replaceAll(paySumRegex,"$2");
                    String totalPaid = tgMsg.replaceAll(totalPaidRegex,"$2");
                    String markUp = tgMsg.replaceAll(markUpRegex,"$2");
                    String discount = tgMsg.replaceAll(discountRegex,"$2");
                    String payStatus = tgMsg.replaceAll(payStatusRegex,"$2");
                    String orderStatus = tgMsg.replaceAll(orderStatusRegex,"$2");
                    String dateOrder = tgMsg.replaceAll(dateOrderRegex,"$2");
                    String waiter = tgMsg.replaceAll(waiterRegex,"$2");

                    HashMap<String, String> tgParsedData = new HashMap<>();

                    tgParsedData.put("table",table);
                    tgParsedData.put("sumInCheck",sumInCheck);
                    tgParsedData.put("restToPay",restToPay);
                    tgParsedData.put("tips",tips);
                    tgParsedData.put("paySum",paySum);
                    tgParsedData.put("totalPaid",totalPaid);

                    if (!markUp.equals("")) {

                        tgParsedData.put("markUp",markUp);

                    } else if (!discount.equals("")) {

                        tgParsedData.put("discount",discount);

                    }

                    tgParsedData.put("payStatus",payStatus);
                    tgParsedData.put("orderStatus",orderStatus);
                    tgParsedData.put("dateOrder",dateOrder);
                    tgParsedData.put("waiter",waiter);

                    System.out.println(tgParsedData);




                } else if (tgMsg.contains(isCallWaiterMsg)) {

                    System.out.println("Тип сообщения 'Вызов официанта'");

                } else if (tgMsg.contains(isReviewMsg)) {

                    System.out.println("Тип сообщения 'Отзыв'");

                }






            }


        }


        if (hasOrderMessage) {

            System.out.println("Есть сообщения");

        } else {

            System.out.println("Нет сообщений");

        }

       // rootPageNestedTests.closeOrder();

    }

    @Test
    @DisplayName("magic")
    public void magic() {


        Response rs = apiRKeeper
                .createOrder
                        (rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_3);


        /*  apiRKeeper.addDiscount(rqParamsAddCustomDiscount
                (R_KEEPER_RESTAURANT,guid, CUSTOM_DISCOUNT_ON_ORDER,"5000"),API_STAGE_URI);


        apiRKeeper.orderPay(rqParamsOrderPay(R_KEEPER_RESTAURANT,guid),API_STAGE_URI);


        if(apiRKeeper.isClosedOrder())
        System.out.println("Заказ закрыт на кассе"); */


    }

}
