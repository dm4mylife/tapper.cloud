package tapper.tests.keeper._1_1_common;


import api.ApiRKeeper;
import data.TableData;
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

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$$;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK;
import static data.selectors.TapperTable.RootPage.DishList.allDishesInOrder;
import static data.selectors.TapperTable.RootPage.DishList.dishPriceWithDiscountSelector;


@Epic("RKeeper")
@Feature("Общие")
@Story("Оплата заказа с дублем стола и скидкой")
@DisplayName("Оплата заказа с дублем стола и скидкой")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoubleTableWithDiscountTest extends BaseTest {


    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    int amountDishesForFillingOrder = 1;
    static LinkedHashMap<Integer, Map<String, Double>> dishListOriginalTable;
    static LinkedHashMap<Integer, Map<String, Double>> dishListDoubleTable;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable +
            " Добавляем скидку к заказу")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder, tableUrl, tableId);

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID, "3500");

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName, guid, discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        dishListOriginalTable = rootPage.getDishList(allDishesInOrder);

    }

    @Test
    @Order(2)
    @DisplayName("Создание второго дубля стола")
    void createAndFillOrderSecondTable() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, SOLYANKA, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);
        Response rs = apiRKeeper.createOrder(rqCreateOrder, apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishesForFillingOrder));

    }

    @Test
    @Order(3)
    @DisplayName("Создание третьего дубля стола")
    void createAndFillOrderThirdTable() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);
        Response rs = apiRKeeper.createOrder(rqCreateOrder, apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishesForFillingOrder));

    }


    @Test
    @Order(4)
    @DisplayName(TapperTable.isTotalPaySumCorrectTipsSc + TapperTable.setRandomTips)
    void checkSumTipsSC() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        dishListDoubleTable = rootPage.getDishList(allDishesInOrder);

        rootPage.activateDivideCheckSliderIfDeactivated();

        $$(dishPriceWithDiscountSelector).shouldHave(size(1));
        Assertions.assertNotEquals(dishListOriginalTable, dishListDoubleTable);

        rootPage.deactivateDivideCheckSliderIfActivated();

        rootPageNestedTests.checkAllDishesSumsWithAllConditionsConsideringDiscount();
        rootPage.setRandomTipsAndActivateScIfDeactivated();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

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

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);
        rootPage.forceWait(WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK);

    }

}
