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
import static data.Constants.RegexPattern.TapperTable.dishPriceRegex;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("RKeeper")
@Feature("Скидка")
@Story("Увеличение скидки (Целой)")
@DisplayName("Увеличение скидки (Целой)")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IncreaseDecimalDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_444.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_444.tableCode;
    protected final String waiter = TableData.Keeper.Table_444.waiter;
    protected final String apiUri = TableData.Keeper.Table_444.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_444.tableUrl;
    protected final String tableId = TableData.Keeper.Table_444.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static String discountAmount = "20050";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 10;
    static Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount = new HashMap<>();
    static Map<Integer,Map<String,Double>> dishesAfterAddingDiscount = new HashMap<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

    }
    @Test
    @Order(2)
    @DisplayName("Проверка суммы, чаевых, сервисного сбора, скидку")
    void checkSumTipsSC() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "keeper");
        rootPageNestedTests.hasDiscountPriceOnPaidDishesIfDiscountAppliedAfter();
        rootPageNestedTests.checkAllDishesSumsWithAllConditionsConsideringDiscount();
        rootPage.setRandomTipsOption();

        dishesBeforeAddingDiscount = rootPage.saveDishPricesInCollection(allDishesInOrder,dishPriceWithDiscountSelector,
                dishPriceWithoutDiscountSelector,dishPriceRegex);

    }
    @Test
    @Order(3)
    @DisplayName("Добавляем скидку из заказа, и проверяем суммы")
    void addDiscountAndCheckSums() {

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

    }
    @Test
    @Order(4)
    @DisplayName("Пытаемся оплатить и получаем ошибку изменения суммы")
    void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем как изменилась скидка, сверяем все позиции")
    void matchDishesDiscountPriceAfterAddingDiscount() {

        dishesAfterAddingDiscount =
                rootPage.saveDishPricesInCollection
                        (allDishesInOrder,dishPriceWithDiscountSelector,dishPriceWithoutDiscountSelector,dishPriceRegex);

        rootPage.matchDishesDiscountPriceAfterAddingDiscount(dishesBeforeAddingDiscount,dishesAfterAddingDiscount);

    }

    @Test
    @Order(6)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(7)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(8)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
