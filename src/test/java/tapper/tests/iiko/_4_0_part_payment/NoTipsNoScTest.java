package tapper.tests.iiko._4_0_part_payment;


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

import static api.ApiData.IikoData.Dish.BURGER;
import static data.Constants.TestData.TapperTable.COOKIE_GUEST_SECOND_USER;
import static data.Constants.TestData.TapperTable.COOKIE_SESSION_SECOND_USER;


@Epic("Iiko")
@Feature("Частичная оплата")
@Story("Частичная оплата - -чай -сбор")
@DisplayName("Частичная оплата - -чай -сбор")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class NoTipsNoScTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_444.tableUrl;
    protected final String tableId = TableData.Iiko.Table_444.tableId;
    static String orderId;
    static double totalPay;
    static String orderType = "part";
    String cashDeskType = "iiko";
    static HashMap<String, String> paymentData;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 5;


    ApiIiko apiIiko = new ApiIiko();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInIiko +
            AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        orderId = nestedTests.createAndFillOrderAndOpenTapperTable(tableId,tableUrl, BURGER, amountDishesForFillingOrder);

    }

    @Test
    @Order(2)
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, " +
            "проверяем что после шаринга выбранные позиции в ожидаются")
    void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPageNestedTests.deactivateTipsAndDeactivateSc();

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentData = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, cashDeskType);

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

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentData);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка сообщения в телеграмме")
    void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(orderId);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Переход на эквайринг, ввод данных, оплата")
    void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentData);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(orderId,"full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
