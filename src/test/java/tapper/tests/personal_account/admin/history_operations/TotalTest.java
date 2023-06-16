package tapper.tests.personal_account.admin.history_operations;


import admin_personal_account.history_operations.HistoryOperations;
import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;



@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Проверка всех диапазонов, списка операций по фильтрации")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 3;


    AuthorizationPage authorizationPage = new AuthorizationPage();
    HistoryOperations operationsHistory = new HistoryOperations();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @Order(1)
    @DisplayName("Оплачиваем заказ на столе чтобы была хоть одна транзакция в истории операций")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode,waiter,
                apiUri,dishesForFillingOrder, tableUrl,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId = nestedTests.acquiringPayment(totalPay);
        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(2)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(3)
    @DisplayName("Переход на категорию история операций")
    void goToOperationsHistory() {

        operationsHistory.goToHistoryOperationsCategory();

    }

    @Test
    @Order(4)
    @DisplayName("Проверка что все элементы корректны")
    void isOperationsHistoryCorrect() {

        operationsHistory.isHistoryOperationsCorrect();

    }

    @Test
    @Order(5)
    @DisplayName("Проверка диапазона по умолчанию")
    void checkInitialDateByDefault() {

        operationsHistory.isDatePeriodSetByDefault();
        operationsHistory.checkTipsAndSumNotEmpty();

    }

    @Test
    @Order(6)
    @DisplayName("Проверка недельного фильтра")
    void isWeekAndMonthPeriodCorrect() {

        operationsHistory.isWeekPeriodCorrect();

    }

    @Test
    @Order(7)
    @DisplayName("Проверка месячного фильтра")
    void isMonthPeriodCorrect() {

        operationsHistory.isMonthPeriodCorrect();

    }

    @Test
    @Order(8)
    @DisplayName("Сброс периода фильтра")
    void resetPeriod() {

        operationsHistory.resetPeriodDate();

    }

    @Test
    @Order(9)
    @DisplayName("Установка и проверка кастомного периода")
    void isCustomPeriodCorrect() throws ParseException {

        operationsHistory.setCustomPeriodInAdminHistoryOperations("Ноябрь",1,18);

    }

    @Test
    @Order(10)
    @DisplayName("Проверка периода за который не было операций")
    void noResultsOperationPeriod(){

        operationsHistory.noResultsOperationPeriod();

    }

    @Test
    @Order(11)
    @DisplayName("Проверка что после обновления страницы мы остаемся на этой вкладке")
    void isCorrectAfterRefreshPage(){

        operationsHistory.isCorrectAfterRefreshPage();

    }

}
