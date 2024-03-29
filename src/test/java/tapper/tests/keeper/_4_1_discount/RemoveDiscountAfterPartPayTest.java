package tapper.tests.keeper._4_1_discount;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.TapperTable.COOKIE_GUEST_SECOND_USER;
import static data.Constants.TestData.TapperTable.COOKIE_SESSION_SECOND_USER;


@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление скидки после частичной оплаты")
@DisplayName("Удаление скидки после частичной оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
class RemoveDiscountAfterPartPayTest extends BaseTest {

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
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int uniIndex = 0;
    static int amountDishes = 3;
    static int amountDishesForFillingOrder = 10;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,"10000");

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        uni = rootPageNestedTests.getDiscountUni(tableId,apiUri).get(uniIndex);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @DisplayName("1.1. Проверка скидки")
    public void checkSumTipsSC() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "keeper");


    }
    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора, выбор позиций")
    public void chooseDishesWithRandomAmount() {


        rootPageNestedTests.checkChosenDishesSumsWithAllConditionsConsideringDiscount(amountDishes);

    }
    @Test
    @DisplayName("1.2. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @DisplayName("1.3. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.5. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("1.6. Удаляем скидку из заказа и проверяем суммы")
    public void addDiscountAndCheckSums() {

        apiRKeeper.deleteDiscount(apiRKeeper.rqBodyDeleteDiscount(restaurantName, guid, uni), apiUri);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @DisplayName("1.8. Проверяем как изменилась скидка, сверяем все позиции")
    public void matchDishesDiscountPriceAfterAddingDiscount() {

        rootPageNestedTests.hasDiscountPriceOnPaidDishesIfDiscountAppliedAfter();

    }

    @Test
    @DisplayName("1.9. Оплачиваем остатки")
    public void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,"full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
