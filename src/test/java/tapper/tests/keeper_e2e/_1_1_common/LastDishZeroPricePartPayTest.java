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

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;


@Epic("RKeeper")
@Feature("Общие")
@Story("Частичная оплата. Стоимость последней позиции 0 рублей")
@DisplayName("Частичная оплата. Стоимость последней позиции 0 рублей")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LastDishZeroPricePartPayTest extends BaseTest {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 1;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable + " Добавляем блюдо с нулевой ценой")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        apiRKeeper.createDishObject(dishesForFillingOrder, ZERO_PRICE_DISH, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri, dishesForFillingOrder,tableUrl, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, " +
            "проверяем что после шаринга выбранные позиции в ожидаются")
    void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.click(allNonPaidAndNonDisabledDishesName.first());
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);

    }

    @Test
    @Order(4)
    @DisplayName(TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("7. Проверяем что последняя позиция с нулевой ценой и невозможно оплатить заказ")
    void payAndGoToAcquiringAgain() {

        rootPage.isPaymentDisabled();

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.closedOrder)
    void closedOrderByApi() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid, apiUri);

    }

}
