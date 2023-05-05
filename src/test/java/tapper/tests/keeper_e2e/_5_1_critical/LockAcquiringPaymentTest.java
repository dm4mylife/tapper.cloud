package tapper.tests.keeper_e2e._5_1_critical;

import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;


@Epic("Личный кабинет техподдержки")
@Feature("Заглушка")
@Story("Заглушить только оплаты по эквайрингу")
@DisplayName("Заглушить только оплаты по эквайрингу")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LockAcquiringPaymentTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    static int amountDishesForFillingOrder = 7;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Lock lock = new Lock();

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и авторизация в админке")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        rootPageNestedTests.createAndFillOrder(restaurantName, tableCode,waiter, apiUri, dishesForFillingOrder,tableId);

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на категорию заглушки, проверка всех элементов")
    void goToLock() {

        lock.goToLockCategory();
        lock.isLockCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем только best2pay")
    void activateAllRestaurants() {

        lock.choseOnlyBest2Pay();

    }

    @Test
    @Order(4)
    @DisplayName("Выбираем заглушить только оплату")
    void choseWholeServiceToLockOption() {

        lock.choseOnlyPaymentToLockOption();

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем на выбранном столе, что есть предупреждение")
    void checkOnTable() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.isTableHasOrder();
        rootPage.clickOnPaymentButton();
        rootPage.isPaymentUnavailable();

    }


    @Test
    @Order(6)
    @DisplayName("Отключаем все рестораны в админке, очищаем тестовое окружение")
    void resetAllRestaurants() {

        rootPage.switchBrowserTab(0);
        lock.resetAllRestaurants();

    }

}
