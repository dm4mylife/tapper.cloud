package tapper.tests.admin_personal_account.history_operations;


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

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Проверка всех диапазонов, списка операций по фильтрации в табе Отчет")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации в табе Отчет")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReportTest extends PersonalAccountTest {

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
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию история операций")
    void goToOperationsHistory() {

        operationsHistory.goToHistoryOperationsCategory();

    }

    @Test
    @Order(3)
    @DisplayName("Проверка что все элементы корректны")
    void isOperationsHistoryCorrect() {

        operationsHistory.isHistoryOperationsCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Проверка что все элементы корректны в табе Отчет")
    void goToReportTab() {

        operationsHistory.goToReportTab();

    }



    @Test
    @Order(5)
    @DisplayName("Выставление диапазона")
    void choseFromAndToDatePeriod() {

        operationsHistory.choseFromAndToDatePeriod("Май",2,10);

    }

    @Test
    @Order(6)
    @DisplayName("Выставление времени")
    void choseTimePeriod() {

        operationsHistory.choseTimePeriod(10,30,16,50);

    }

    @Test
    @Order(7)
    @DisplayName("Скачивание отчета")
    void downloadFile() throws FileNotFoundException {

        operationsHistory.downloadFile();

    }


    @Test
    @Order(8)
    @DisplayName("Проверка элементов, выставление диапазона в отчете одного дня")
    void isOneDayReportCorrect() {

        operationsHistory.isOneDayReportCorrect("Май",10);

    }

    @Test
    @Order(9)
    @DisplayName("Скачивание отчета")
    void downloadFileOneDayReport() throws FileNotFoundException {

        operationsHistory.downloadFile();

    }

}
