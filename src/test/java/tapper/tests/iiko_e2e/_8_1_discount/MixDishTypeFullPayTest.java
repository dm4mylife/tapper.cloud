package tapper.tests.iiko_e2e._8_1_discount;


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
import static api.ApiData.IikoData.Dish.CUSTOM_DISCOUNT_ID;


@Epic("Iiko")
@Feature("Скидка")
@Story("Полная оплата с общей скидкой, позиции обычные и с модификаторами")
@DisplayName("Полная оплата с общей скидкой, позиции обычные и с модификаторами")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MixDishTypeFullPayTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_333.tableUrl;
    protected final String tableId = TableData.Iiko.Table_333.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 3;


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

        dishesForFillingOrder.clear();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, HOT_DOG_GORCHIZA_FREE_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, HOT_DOG_SOUS_FREE_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, HOT_DOG.getId(), 1, dishesForFillingOrder));

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
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(5)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
