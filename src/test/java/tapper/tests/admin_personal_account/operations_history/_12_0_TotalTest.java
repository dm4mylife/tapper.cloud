package tapper.tests.admin_personal_account.operations_history;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.operations_history.OperationsHistory;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.text.ParseException;
import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_PASSWORD;


@Order(120)
@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _12_0_TotalTest extends BaseTest {

    static String visit;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;

    AuthorizationPage authorizationPage = new AuthorizationPage();
    OperationsHistory operationsHistory = new OperationsHistory();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.0 Создание заказа в r_keeper, оплата его, чтобы появилась операция в истории операций")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder
                (rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD),
                        API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId = nestedTests.acquiringPayment(totalPay);
        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на категорию история операций")
    public void goToOperationsHistory() {
        operationsHistory.goToOperationsHistoryCategory();
    }

    @Test
    @DisplayName("1.3. Проверка что все элементы корректны")
    public void isOperationsHistoryCorrect() {
        operationsHistory.isHistoryOperationsCorrect();
    }

    @Test
    @DisplayName("1.4. Проверка диапазона по умолчанию")
    public void checkInitialDateByDefault() {

        operationsHistory.isDatePeriodSetByDefault();
        operationsHistory.checkTipsAndSumNotEmpty();

    }

    @Test
    @DisplayName("1.5. Проверка недельного и месячного фильтра")
    public void isWeekAndMonthPeriodCorrect() {

        operationsHistory.isWeekPeriodCorrect();
        operationsHistory.isMonthPeriodCorrect();

    }

    @Test
    @DisplayName("1.6. Сброс периода фильтра")
    public void resetPeriod() {
        operationsHistory.resetPeriodDate();
    }

    @Test
    @DisplayName("1.7. Установка и проверка кастомного периода")
    public void isCustomPeriodCorrect() throws ParseException {
        operationsHistory.setCustomPeriod();
    }

}
