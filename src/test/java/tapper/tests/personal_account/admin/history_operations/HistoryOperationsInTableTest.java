package tapper.tests.personal_account.admin.history_operations;


import admin_personal_account.history_operations.HistoryOperations;
import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.AdminPersonalAccount.*;



@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Частичная и полная оплата, проверка их в историях операций")
@DisplayName("Частичная и полная оплата, проверка их в историях операций")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HistoryOperationsInTableTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String orderType = "part";
    static HashMap<Integer, HashMap<String, String>> tapperOrderData;
    static HashMap<Integer, HashMap<String, String>> adminOrderData;
    static int amountDishesForFillingOrder = 4;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    HistoryOperations operationsHistory = new HistoryOperations();
    AuthorizationPage authorizationPage = new AuthorizationPage();


    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri,dishesForFillingOrder,tableUrl,tableId);

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesForFillingOrder / 2);
        rootPageNestedTests.activateRandomTipsAndActivateSc();
        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(100, 1000)));

    }

    @Test
    @Order(2)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        tapperOrderData = rootPage.saveOrderDataForOperationsHistoryInAdmin();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

    }

    @Test
    @Order(3)
    @DisplayName("Переходим на эквайринг, оплачиваем там")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(5)
    @DisplayName("Открываем историю операций, проверяем что платёж есть и корректный")
    void openAdminOperationsHistory() {

        rootPage.openNewTabAndSwitchTo(PERSONAL_ACCOUNT_AUTHORIZATION_STAGE_URL);
        authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        operationsHistory.goToHistoryOperationsCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData, adminOrderData);

    }

    @Test
    @Order(6)
    @DisplayName("Делаем полную оплату на столе")
    void clearDataAndChoseAgain() {

        rootPage.switchBrowserTab(0);
        savePaymentDataForAcquiring();

    }

    @Test
    @Order(7)
    @DisplayName("Производим полную оплату")
    void payAndGoToAcquiringAgain() {

        payAndGoToAcquiring();
        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

    }

    @Test
    @Order(8)
    @DisplayName("Переход на эквайринг, ввод данных, полная оплата")
    void checkFullPayInAdmin() {

        rootPage.switchBrowserTab(1);

        rootPage.refreshPage();
        operationsHistory.goToHistoryOperationsCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData, adminOrderData);

    }

}
