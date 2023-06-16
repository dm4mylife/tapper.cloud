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
import java.util.LinkedHashMap;
import java.util.Map;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@Story("Выгрузка и проверка отчета, одним днём, негативных сценариев")
@DisplayName("Выгрузка и проверка отчета, одним днём, негативных сценариев")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class XlsxOneDayAndRefundReportTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static File file;
    String reportMonth = "Май";
    int reportDayFrom = 15;
    int reportDayTo = 20;
    static Map<String,Object> dateData;

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
    @DisplayName("Переход в категорию история операций")
    void goToOperationsHistory() {

        operationsHistory.goToHistoryOperationsCategory();
        operationsHistory.isHistoryOperationsCorrect();
        dateData = operationsHistory.getDateData();
    }


    @Test
    @Order(3)
    @DisplayName("Установка и проверка кастомного периода")
    void isCustomPeriodCorrect() throws ParseException {

        operationsHistory.setCustomPeriodInAdminHistoryOperations(String.valueOf(dateData.get("monthName")),
                (Integer) dateData.get("endDay"), (Integer) dateData.get("endDay"));

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
    @DisplayName("Переход в таб Отчет")
    void goToReportTab() {

        operationsHistory.goToReportTab();

    }

    @Test
    @Order(6)
    @DisplayName("Проверка негативных сценариев")
    void checkUnavailabilityToDownloadReport() {

        operationsHistory.choseOnlyDateFromPeriod("Май",reportDayTo);
        operationsHistory.choseNonExistingDate
                (String.valueOf(dateData.get("monthName")), (Integer) dateData.get("endDay"));

    }

    @Test
    @Order(7)
    @DisplayName("Проверка скачивания отчета одним днём")
    void isOneDayReportCorrect() {

        operationsHistory.isOneDayReportCorrect(String.valueOf(dateData.get("monthName")),
                (Integer) dateData.get("endDay"));

    }

    @Test
    @Order(8)
    @DisplayName("Скачивание отчета и проверка отчета с админкой")
    void readDownloadedFile() throws IOException, ParseException {

        file = operationsHistory.downloadFile();
        xlsxOperationHistoryData = operationsHistory.readDownloadedFile(file,false);
        operationsHistory.matchOperationsData(adminOperationHistoryData,xlsxOperationHistoryData);

    }

    @Test
    @Order(9)
    @DisplayName("Проверяем что есть операции по возвратам")
    void isRefundCorrect() throws ParseException, IOException {

        //operationsHistory.isRefundCorrect();

    }

}
