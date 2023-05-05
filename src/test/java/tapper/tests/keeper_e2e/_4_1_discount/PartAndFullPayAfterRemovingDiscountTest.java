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


@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление скидки из заказа, полная оплата")
@DisplayName("Удаление скидки из заказа, полная оплата")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartAndFullPayAfterRemovingDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_444.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_444.tableCode;
    protected final String waiter = TableData.Keeper.Table_444.waiter;
    protected final String apiUri = TableData.Keeper.Table_444.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_444.tableUrl;
    protected final String tableId = TableData.Keeper.Table_444.tableId;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static String uni;
    static String discountAmount = "10000";
    static String orderType = "part";
    static int amountDishes = 1;
    static int amountDishesForFillingOrder = 4;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;



    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName,
                tableCode, waiter, apiUri, dishesForFillingOrder, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID, discountAmount);

        Map<String, Object> rqBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName, guid, discounts);
        apiRKeeper.createDiscount(rqBodyCreateDiscount);

        uni = apiRKeeper.getUniDiscountFromCreateOrder(tableId, apiUri);

        rootPage.openNotEmptyTable(tableUrl);

    }
    @Test
    @Order(2)
    @DisplayName("Проверка скидки")
    void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "keeper");

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем суммы, чаевые, сб")
    void checkAllDishesSumsWithAllConditions() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditionsConsideringDiscount();

    }

    @Test
    @Order(4)
    @DisplayName("Удаляем скидку из заказа и проверяем суммы")
    void addDiscountAndCheckSums() {

        apiRKeeper.deleteDiscount(apiRKeeper.rqBodyDeleteDiscount(restaurantName, guid, uni), apiUri);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @Order(5)
    @DisplayName("Выбираем рандомные блюда для частичной оплаты")
    void getRandomDishes() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @Order(6)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiringPartPay() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(7)
    @DisplayName("Переходим в эквайринг, оплачиваем позиции")
    void savePaymentDataForAcquiringAfterDeletedDiscount() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPaymentAfterDeletedDiscount() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @Order(9)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperDataPartPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(10)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiringFullPay() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(11)
    @DisplayName("Переходим в эквайринг, оплачиваем позиции")
    void savePaymentDataForAcquiringAfterPartialPayment() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(11)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPaymentAfterPartialPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @Order(12)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperDataFullPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
