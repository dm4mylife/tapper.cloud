package tapper.tests.admin_personal_account.operations_history;


import admin_personal_account.operations_history.OperationsHistory;
import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.*;


@Order(120)
@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _12_0_TotalTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 3;


    AuthorizationPage authorizationPage = new AuthorizationPage();
    OperationsHistory operationsHistory = new OperationsHistory();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();

    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @DisplayName("1.0 Оплачиваем заказ на столе чтобы была хоть одна транзакция в истории операций")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_333,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_333,TABLE_AUTO_333_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId = nestedTests.acquiringPayment(totalPay);
        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

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
