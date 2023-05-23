package tapper.tests.keeper_e2e._4_1_discount;


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
import static data.Constants.TestData.TapperTable.*;


@Epic("RKeeper")
@Feature("Скидка")
@Story("Уменьшение скидки после частичной оплаты")
@DisplayName("Уменьшение скидки после частичной оплаты")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReduceDiscountAfterPartPayTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_444.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_444.tableCode;
    protected final String waiter = TableData.Keeper.Table_444.waiter;
    protected final String apiUri = TableData.Keeper.Table_444.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_444.tableUrl;
    protected final String tableId = TableData.Keeper.Table_444.tableId;

    static String uni;
    static String guid;
    static double totalPay;
    static String orderType = "part";

    static String discountAmount = "20000";
    static String reducedDiscountAmount = "10000";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int uniIndex = 0;
    static int amountDishes = 3;
    static int amountDishesForFillingOrder = 6;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode,waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        uni = rootPageNestedTests.getDiscountUni(tableId,apiUri).get(uniIndex);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Проверка суммы, чаевых, сервисного сбора, скидку")
    void checkSumTipsSC() {

        rootPageNestedTests.checkChosenDishesSumsWithAllConditionsConsideringDiscount(amountDishes);

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Удаляем старую скидку и добавляем новую, но большую")
    void addDiscountAndCheckSums() {

        apiRKeeper.deleteDiscount(apiRKeeper.rqBodyDeleteDiscount(restaurantName, guid, uni), apiUri);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,reducedDiscountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем скидку на столе")
    void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "keeper");

    }

    @Test
    @Order(9)
    @DisplayName("Оплачиваем остатки")
    void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,"full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
