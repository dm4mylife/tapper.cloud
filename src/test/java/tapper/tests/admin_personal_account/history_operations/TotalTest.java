package tapper.tests.admin_personal_account.history_operations;


import admin_personal_account.history_operations.HistoryOperations;
import api.ApiRKeeper;
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

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;


@Disabled
@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Проверка всех диапазонов, списка операций по фильтрации")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

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

    @Disabled
    @Test
    @Order(1)
    @DisplayName("Оплачиваем заказ на столе чтобы была хоть одна транзакция в истории операций")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_555,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,
                STAGE_RKEEPER_TABLE_555,TABLE_AUTO_555_ID);

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

       // authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        authorizationPage.authorizationByToken
                (PERSONAL_ACCOUNT_PROFILE_STAGE_URL,ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(3)
    @DisplayName("Переход на категорию история операций")
    void goToOperationsHistory() {

        //authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
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
    @DisplayName("Проверка недельного и месячного фильтра")
    void isWeekAndMonthPeriodCorrect() {

        operationsHistory.isWeekPeriodCorrect();
        operationsHistory.isMonthPeriodCorrect();

    }

    @Test
    @Order(7)
    @DisplayName("Сброс периода фильтра")
    void resetPeriod() {

        operationsHistory.resetPeriodDate();

    }

    @Test
    @Order(8)
    @DisplayName("Установка и проверка кастомного периода")
    void isCustomPeriodCorrect() throws ParseException {

        operationsHistory.setCustomPeriod();

    }

}
