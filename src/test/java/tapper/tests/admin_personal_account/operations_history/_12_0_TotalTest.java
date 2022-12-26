package tapper.tests.admin_personal_account.operations_history;


import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.operations_history.OperationsHistory;
import tests.BaseTest;

import java.text.ParseException;

import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static constants.Constant.TestDataRKeeperAdmin.ADMIN_RESTAURANT_PASSWORD;


@Order(120)
@Epic("Личный кабинет администратора ресторана")
@Feature("История операций")
@DisplayName("Проверка всех диапазонов, списка операций по фильтрации")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _12_0_TotalTest extends BaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    OperationsHistory operationsHistory = new OperationsHistory();

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
