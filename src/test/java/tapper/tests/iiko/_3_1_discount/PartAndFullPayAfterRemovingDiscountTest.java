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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.IikoData.Dish.*;


@Epic("Iiko")
@Feature("Скидка")
@Story("Удаление скидки из заказа, полная оплата")
@DisplayName("Удаление скидки из заказа, полная оплата")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartAndFullPayAfterRemovingDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_333.tableUrl;
    protected final String tableId = TableData.Iiko.Table_333.tableId;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static String orderType = "part";
    static int amountDishes = 1;
    static int amountDishesForFillingOrder = 1;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;



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
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,SHASHLIK_SVININA.getId(),amountDishesForFillingOrder));

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_TOMATO_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, BURGER.getId(), 1, dishesForFillingOrder));

        Map<String, Object> rsBodyCreateDiscount =
                apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),200);

        apiIiko.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

    }
    @Test
    @Order(2)
    @DisplayName("Проверка скидки")
    void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "iiko");

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

        Map<String, Object> rsBodyDeleteDiscount =
                apiIiko.rqBodyDeleteDiscount(guid,CUSTOM_DISCOUNT_ID.getId());

        apiIiko.deleteDiscount(rsBodyDeleteDiscount);

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
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

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
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

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
