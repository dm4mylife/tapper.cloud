package tapper.tests.iiko._3_1_discount;


import api.ApiIiko;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.IikoData.Dish.*;
import static data.Constants.TestData.TapperTable.COOKIE_GUEST_SECOND_USER;
import static data.Constants.TestData.TapperTable.COOKIE_SESSION_SECOND_USER;


@Epic("Iiko")
@Feature("Скидка")
@Story("Уменьшение скидки после частичной оплаты")
@DisplayName("Уменьшение скидки после частичной оплаты")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReduceDiscountAfterPartPayTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_333.tableUrl;
    protected final String tableId = TableData.Iiko.Table_333.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "part";

    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishes = 3;
    static int amountDishesForFillingOrder = 6;

    RootPage rootPage = new RootPage();
    ApiIiko apiIiko = new ApiIiko();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInIiko)
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,ESPRESSO.getId(),amountDishesForFillingOrder));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,SHASHLIK_GOVYADINA.getId(),amountDishesForFillingOrder));

        Map<String, Object> rsBodyCreateDiscount =
                apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),200);

        apiIiko.createDiscount(rsBodyCreateDiscount);

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
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

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

        Map<String, Object> rsBodyDeleteDiscount =
                apiIiko.rqBodyDeleteDiscount(guid,CUSTOM_DISCOUNT_ID.getId());

        apiIiko.deleteDiscount(rsBodyDeleteDiscount);

        Map<String, Object> rsBodyCreateDiscount =
                apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),400);

        apiIiko.createDiscount(rsBodyCreateDiscount);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем скидку на столе")
    void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "iiko");

    }

    @Test
    @Order(9)
    @DisplayName("Оплачиваем остатки")
    void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
