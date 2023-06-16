package tapper.tests.iiko._1_1_full_payment;


import api.ApiIiko;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.IikoData.Dish.BURGER;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;


@Epic("Iiko")
@Feature("Полная оплата")
@Story("Оплата по кнопке 'Оплатить' - -чай +сбор")
@DisplayName("Оплата по кнопке 'Оплатить' - -чай +сбор")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoTipsScTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_111.tableUrl;
    protected final String tableId = TableData.Iiko.Table_111.tableId;

    static String orderId;
    static double totalPay;
    static String orderType = "full";
    String cashDeskType = "iiko";
    static HashMap<String, String> paymentData;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    int amountDishesForFillingOrder = 5;

    ApiIiko apiIiko = new ApiIiko();
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInIiko + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        orderId = nestedTests.createAndFillOrderAndOpenTapperTable(tableId,tableUrl, BURGER, amountDishesForFillingOrder);

    }

    @Test
    @Order(2)
    @DisplayName(TapperTable.isTotalPaySumCorrectNoTipsNoSc)
    void choseAllNonPaidDishesNoTipsNoSc() {

        nestedTests.choseAllNonPaidDishesNoTipsSc();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentData = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, cashDeskType);

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

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentData);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(orderId, tapperDataForTgMsg, orderType);

    }

}
