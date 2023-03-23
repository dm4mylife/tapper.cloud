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
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$$;
import static data.AnnotationAndStepNaming.DisplayName.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK;
import static data.selectors.TapperTable.RootPage.DishList.allDishesInOrder;
import static data.selectors.TapperTable.RootPage.DishList.dishPriceWithDiscountSelector;



@Epic("RKeeper")
@Feature("Общие")
@Story("Оплата заказа с дублем стола и скидкой")
@DisplayName("Оплата заказа с дублем стола и скидкой")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DoubleTableWithDiscountTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListOriginalTable;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListDoubleTable;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName(1.1 + createOrderInKeeper + isDishesCorrectInCashDeskAndTapperTable + " Добавляем скидку к заказу")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,
                STAGE_RKEEPER_TABLE_111,TABLE_AUTO_111_ID);

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,"3500");

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        dishListOriginalTable = rootPage.getDishList(allDishesInOrder);

    }

    @Test
    @DisplayName(1.2 + " Создание второго дубля")
    public void createAndFillOrderSecondTable() {

        apiRKeeper.createDishObject(dishesForFillingOrder, SOLYANKA, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,AUTO_API_URI);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishesForFillingOrder));


    }

    @Test
    @DisplayName(1.3 + " Создание третьего дубля")
    public void createAndFillOrderThirdTable() {

        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,AUTO_API_URI);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishesForFillingOrder));

    }


    @Test
    @DisplayName(1.4 + isTotalPaySumCorrectTipsSc + setRandomTips)
    public void checkSumTipsSC() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        dishListDoubleTable = rootPage.getDishList(allDishesInOrder);

        Assertions.assertNotEquals(dishListOriginalTable,dishListDoubleTable);

        $$(dishPriceWithDiscountSelector).filter(exist).shouldHave(size(amountDishesForFillingOrder)
                        .because("Скидка должна быть только у двух позиций"));

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(200,500)));

    }

    @Test
    @DisplayName(1.5 + savePaymentData)
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName(1.6 + goToAcquiringAndPayOrder)
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName(1.7 + isPaymentCorrect)
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName(1.8 + isTelegramMessageCorrect)
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);
        rootPage.forceWait(WAIT_FOR_ORDER_TO_BE_CLOSED_AT_CASH_DESK);

    }

}
