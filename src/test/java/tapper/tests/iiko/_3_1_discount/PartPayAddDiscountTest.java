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


@Epic("Iiko")
@Feature("Скидка")
@Story("Частичная оплата + применение скидки (Частичная оплата, потом скидка, потом полная оплата)")
@DisplayName("Частичная оплата + применение скидки (Частичная оплата, потом скидка, потом полная оплата)")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartPayAddDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_333.tableUrl;
    protected final String tableId = TableData.Iiko.Table_333.tableId;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String orderType = "part";
    static String guid;
    static int amountDishes = 2;
    static int amountDishesForFillingOrder = 2;
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

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Проверка суммы, чаевых, сервисного сбора")
    void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiringPartPay() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(4)
    @DisplayName("Переходим в эквайринг, оплачиваем позиции")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPaymentAfterDeletedDiscount() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperDataPartPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Добавляем скидку")
    void addDiscountAndCheckSums() {

        Map<String, Object> rsBodyCreateDiscount =
                apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),200);

        apiIiko.createDiscount(rsBodyCreateDiscount);

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем суммы и скидку")
    void checkIsDiscountPresent() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        rootPageNestedTests.checkIsDiscountPresent(tableId, "iiko");
        rootPageNestedTests.hasNoDiscountPriceOnPaidDishesIfDiscountWasAppliedAfterPayment();
        rootPageNestedTests.checkChosenDishesSumsWithAllConditionsConsideringDiscount(amountDishes);

    }

    @Test
    @Order(9)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiringAfterAddedDiscount() {

        savePaymentDataForAcquiringPartPay();

    }

    @Test
    @Order(10)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiringAfterAddedDiscount() {

        payAndGoToAcquiring();

    }

    @Test
    @Order(11)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterAddedDiscount() {

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @Order(12)
    @DisplayName("Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperDataFullPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
