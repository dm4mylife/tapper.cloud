package tapper.tests.keeper._3_1_waiter;


import api.ApiRKeeper;
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

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.text;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.AdminPersonalAccount.MEGATRON_WAITER;
import static data.Constants.TestData.AdminPersonalAccount.MEGATRON_WAITER_ID;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.serviceWorkerName;


@Epic("RKeeper")
@Feature("Официант")
@Story("Смена официанта в заказе")
@DisplayName("Смена официанта в заказе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChangedWaiterTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_333.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_333.tableCode;
    protected final String waiter = TableData.Keeper.Table_333.waiter;
    protected final String apiUri = TableData.Keeper.Table_333.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_333.tableUrl;
    protected final String tableId = TableData.Keeper.Table_333.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    int amountDishesForFillingOrder = 2;

    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(2)
    @DisplayName("Смена официанта на кассе")
    void changeWaiter() {

        Assertions.assertTrue(apiRKeeper.changeWaiter
                (apiRKeeper.rqBodyChangeWaiter(restaurantName, guid, MEGATRON_WAITER_ID), apiUri));

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем на столе что официант сменился")
    void isChangedWaiterCorrect() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        serviceWorkerName.shouldHave(text(MEGATRON_WAITER));

    }

    @Test
    @Order(4)
    @DisplayName(TapperTable.isTotalPaySumCorrectTipsSc)
    void choseAllNonPaidDishesOneByOneTipsSc() {

        nestedTests.choseAllNonPaidDishesTipsSc();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg, "full");

    }

}
