package tapper.tests.keeper_e2e._1_1_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK;
import static data.selectors.TapperTable.RootPage.DishList.allDishesInOrder;



@Epic("RKeeper")
@Feature("Общие")
@Story("Оплата заказа с дублем стола")
@DisplayName("Оплата заказа с дублем стола")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoubleTableTest extends BaseTest {


    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;



    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListOriginalTable;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListDoubleTable;
    int amountDishesForFillingOrder = 1;


    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

        dishListOriginalTable = rootPage.getDishList(allDishesInOrder);

    }

    @Test
    @Order(2)
    @DisplayName("Создание второго дубля стола")
    void createAndFillOrderSecondTable() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, SOLYANKA, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,AUTO_API_URI);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishesForFillingOrder));

    }

    @Test
    @Order(3)
    @DisplayName("Создание третьего дубля стола")
    void createAndFillOrderThirdTable() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,AUTO_API_URI);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishesForFillingOrder));

    }


    @Test
    @Order(4)
    @DisplayName(TapperTable.isTotalPaySumCorrectTipsSc + TapperTable.setRandomTips)
    void checkSumTipsSC() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        dishListDoubleTable = rootPage.getDishList(allDishesInOrder);

        Map<String, Double> firstValueOriginal = null;
        for (Map.Entry<Integer, Map<String, Double>> entry: dishListOriginalTable.entrySet()) {
            firstValueOriginal = entry.getValue();
            break;
        }

        Map<String, Double> firstValueDouble = null;
        for (Map.Entry<Integer, Map<String, Double>> entry: dishListDoubleTable.entrySet()) {
            firstValueDouble = entry.getValue();
            break;
        }

        Assertions.assertEquals(firstValueOriginal,firstValueDouble);
        Assertions.assertNotEquals(dishListOriginalTable,dishListDoubleTable);

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);
        rootPage.forceWait(WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK);

    }

}
