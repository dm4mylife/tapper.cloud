package tapper.tests.personal_account.admin.history_operations;


import admin_personal_account.history_operations.HistoryOperations;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Выгрузка и проверка отчета по времени")
@DisplayName("Выгрузка и проверка отчета по времени")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class XlsxDateRangeReportTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static File file;
    static Map<String,Object> dateData;

    int fromHour = 15;
    int fromMinute = 46;
    int toHour = 16;
    int toMinute = 1;

    static LinkedHashMap<Integer, Map<String, String>> adminOperationHistoryData;
    static LinkedHashMap<Integer, Map<String, String>> xlsxOperationHistoryData;
    AuthorizationPage authorizationPage = new AuthorizationPage();
    HistoryOperations operationsHistory = new HistoryOperations();

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
        operationsHistory.isHistoryOperationsCorrect();
        dateData = operationsHistory.getDateData();
    }

    @Test
    @Order(3)
    @DisplayName("Установка и проверка кастомного периода")
    void isCustomPeriodCorrect() throws ParseException {

        operationsHistory.setCustomPeriodInReport(dateData);

    }

    @Test
    @Order(4)
    @DisplayName("Сохраняем список операций")
    void collectOperationsData()  {

        adminOperationHistoryData = operationsHistory.collectOperationsData();
        System.out.println(adminOperationHistoryData);

    }

    @Test
    @Order(5)
    @DisplayName("Переход в таб Отчет и выставление кастомного диапазона только по дате")
    void goToReportTab() {

        operationsHistory.goToReportTab();
        operationsHistory.choseFromAndToDatePeriod(dateData);

    }

    @Test
    @Order(6)
    @DisplayName("Скачивание отчета и проверка отчета с админкой")
    void readDownloadedFile() throws IOException, ParseException {

        file = operationsHistory.downloadFile();
        xlsxOperationHistoryData = operationsHistory.readDownloadedFile(file,false);
        operationsHistory.matchOperationsData(adminOperationHistoryData,xlsxOperationHistoryData);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка операций в диапазоне")
    void matchOperationsData() {

        operationsHistory.isDateHasInDateRange(xlsxOperationHistoryData,
                (Integer) dateData.get("year"),
                (Integer) dateData.get("monthIndex"),
                (Integer) dateData.get("monthIndex"),
                (Integer) dateData.get("startDay"),
                (Integer) dateData.get("endDay"));

    }

    @Test
    @Order(8)
    @DisplayName("Выставляем период в истории операции для сравнения по времени")
    void choseTimePeriod() throws ParseException {

        operationsHistory.goToHistoryTab();
        operationsHistory.setCustomPeriodInReport(dateData);

    }

    @Test
    @Order(9)
    @DisplayName("Переходим в Отчет и выставляем период для сравнения по времени")
    void choseFromAndToDatePeriod() {

        adminOperationHistoryData = operationsHistory.collectOperationsData();

        operationsHistory.goToReportTab();
        operationsHistory.choseFromAndToDatePeriod(dateData);
        operationsHistory.choseTimePeriod(fromHour,fromMinute,toHour,toMinute);

    }

}
