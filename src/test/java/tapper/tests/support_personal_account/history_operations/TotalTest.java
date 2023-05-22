package tapper.tests.support_personal_account.history_operations;

import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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
@Story("Проверка категории Истории операций")
@DisplayName("Проверка истории операций")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    String tableNumber = "111";
    String orderStatus = "Закрыт";
    String waiterName = "Robocop";

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
    @Order(6)
    @DisplayName("Проверка фильтров и диапазона")
    void checkInitialDateByDefault() throws ParseException {

        historyOperations.isMonthPeriodCorrect();
        historyOperations.setCustomPeriod("Ноябрь",1,30);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка фильтра Ресторан")
    void isRestaurantFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isRestaurantFilterCorrect(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(8)
    @DisplayName("Проверка фильтра Столы")
    void isTableFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isTableFilterCorrect(KEEPER_RESTAURANT_NAME,tableNumber);

    }

    @Test
    @Order(9)
    @DisplayName("Проверка фильтра Статус заказа")
    void isOrderStatusFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isOrderStatusFilterCorrect(KEEPER_RESTAURANT_NAME,tableNumber, orderStatus);

    }

    @Test
    @Order(10)
    @DisplayName("Проверка фильтра Официант")
    void isWaiterFilterCorrect() {

        rootPage.refreshPage();
        historyOperations.isWaiterFilterCorrect(KEEPER_RESTAURANT_NAME,tableNumber, orderStatus,waiterName);

    }

    @Test
    @Order(11)
    @DisplayName("Проверка списка операций у определенного официанта на определенном столе")
    void isOperationListCorrectWithCertainWaiterAndTable() {

        rootPage.refreshPage();
        historyOperations.isOperationListCorrectWithCertainWaiterAndTable
                (KEEPER_RESTAURANT_NAME,tableNumber, orderStatus,waiterName);

    }

    @Test
    @Order(12)
    @DisplayName("Проверка списка операций со всеми фильтрами и диапазоном")
    void isOperationListCorrectWithAllFiltersAndDataRange() throws ParseException {

        rootPage.refreshPage();
        historyOperations.isOperationListCorrectWithAllFiltersAndDataRange
                (KEEPER_RESTAURANT_NAME,tableNumber, orderStatus,waiterName,"Январь");


    }
    @Test
    @Order(13)
    @DisplayName("Проверка кнопки Загрузить еще")
    void isLoadMoreButtonCorrect() throws ParseException {

        rootPage.refreshPage();
        historyOperations.isLoadMoreButtonCorrect("Январь");


    }
    @Test
    @Order(14)
    @DisplayName("Проверка Показать только возвраты")
    void isShowOnlyRefundsCorrect() {

        rootPage.refreshPage();
        historyOperations.isShowOnlyRefundsCorrect();

    }

    @Test
    @Order(15)
    @DisplayName("Проверка раскрытой истории операции по возврату")
    void isOpenedRefundOperationCorrect()  {

        rootPage.refreshPage();
        historyOperations.isOpenedRefundOperationCorrect();

    }

    @Test
    @Order(16)
    @DisplayName("Проверка что после обновления страницы мы остаемся в этой вкладке")
    void isCorrectAfterRefreshPage()  {

        historyOperations.isCorrectAfterRefreshPage();

    }

    @Test
    @Order(17)
    @DisplayName("Проверка периода за который не было операций")
    void noResultsOperationPeriod() throws ParseException {

        rootPage.refreshPage();
        historyOperations.noResultsOperationPeriod();

    }

}
