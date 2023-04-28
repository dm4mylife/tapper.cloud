package tapper.tests.support_personal_account.history_operations;

import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import support_personal_account.history_operations.HistoryOperations;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.text.ParseException;

import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.SupportPersonalAccount.HistoryOperations.operationsListTab;



@Epic("Личный кабинет техподдержки")
@Feature("История операций")
@DisplayName("Проверка истории операций")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TotalTest extends PersonalAccountTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    HistoryOperations historyOperations = new HistoryOperations();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию история операций, проверка всех элементов")
    void goToHistoryOperations() {

        historyOperations.goToHistoryOperationsCategory();


    }

    @Test
    @Order(3)
    @DisplayName("Проверка всех элементов в табе Список операции")
    void isHistoryOperationsCategoryCorrect() {

        historyOperations.isHistoryOperationsCategoryCorrect();
        historyOperations.isDatePeriodSetByDefault();
        historyOperations.checkTipsAndSumNotEmpty();

    }

    @Test
    @Order(4)
    @DisplayName("Проверка всех элементов в табе Застрявшие транзакции")
    void isStuckOperationsCorrect() {

        historyOperations.isStuckOperationsCorrect();
        BaseActions.click(operationsListTab);

    }
    @Test
    @Order(5)
    @DisplayName("Проверка фильтров и диапазона")
    void checkInitialDateByDefault() throws ParseException {

        historyOperations.isMonthPeriodCorrect();
        historyOperations.setCustomPeriod();

    }

    @Test
    @Order(6)
    @DisplayName("Проверка фильтра Ресторан")
    void isRestaurantFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isRestaurantFilterCorrect(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка фильтра Столы")
    void isTableFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isTableFilterCorrect(KEEPER_RESTAURANT_NAME,"111");

    }


}
