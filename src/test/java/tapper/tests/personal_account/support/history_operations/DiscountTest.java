package tapper.tests.personal_account.support.history_operations;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import support_personal_account.history_operations.HistoryOperations;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.AdminPersonalAccount.PERSONAL_ACCOUNT_AUTHORIZATION_STAGE_URL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;


@Epic("Личный кабинет техподдержки")
@Feature("История операций")
@Story("Оплата со скидкой, проверка их в историях операций")
@DisplayName("Оплата со скидкой, проверка их в историях операций")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DiscountTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String orderType = "part";
    static HashMap<Integer, HashMap<String, String>> tapperOrderData;
    protected static LinkedHashMap<String, String> tapperDataForTgMsg;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    HistoryOperations historyOperations = new HistoryOperations();
    AuthorizationPage authorizationPage = new AuthorizationPage();


    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 4);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,"10000");
        apiRKeeper.createDiscountByIdObject(discounts, DISCOUNT_BY_ID);

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

        rootPageNestedTests.checkChosenDishesSumsWithAllConditionsConsideringDiscount(2);
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
    @Order(6)
    @DisplayName("Открываем историю операций, проверяем что платёж есть и корректный")
    void openAdminOperationsHistory() {

        rootPage.openNewTabAndSwitchTo(PERSONAL_ACCOUNT_AUTHORIZATION_STAGE_URL);
        authorizationPage.authorizeUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

        historyOperations.goToHistoryOperationsCategory();
        historyOperations.isHistoryOperationsCategoryCorrect();

        historyOperations.matchAdminAndTapperOrderData(orderType,tapperOrderData,transactionId);

    }

    @Test
    @Order(7)
    @DisplayName("Делаем полную оплату на столе")
    void clearDataAndChoseAgain() {

        rootPage.switchBrowserTab(0);
        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(100, 1000)));
        savePaymentDataForAcquiring();

    }

    @Test
    @Order(8)
    @DisplayName("Производим полную оплату")
    void payAndGoToAcquiringAgain() {

        payAndGoToAcquiring();
        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

    }

    @Test
    @Order(10)
    @DisplayName("Переход на эквайринг, ввод данных, полная оплата. Проверяем транзакцию")
    void checkFullPayInAdmin() {

        rootPage.switchBrowserTab(1);

        rootPage.refreshPage();
        historyOperations.goToHistoryOperationsCategory();

        historyOperations.matchAdminAndTapperOrderData("full",tapperOrderData,transactionId);

    }

}
