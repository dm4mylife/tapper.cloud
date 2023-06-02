package tapper.tests.keeper_e2e._2_3_critical;

import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import support_personal_account.lock.Lock;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.Condition.disappear;
import static data.Constants.TestData.Best2Pay.BEST2PAY_NAME;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.Common.serviceUnavailabilityContainer;


@Epic("Личный кабинет техподдержки")
@Feature("Заглушка")
@Story("Заглушаем только один ресторан и проверяем все вариации заглушки")
@DisplayName("Заглушаем только один ресторан и проверяем все вариации заглушки")

@TestMethodOrder(MethodOrderer.DisplayName.class)
class LockCertainRestaurantTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiter = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    int admin = 0;
    int tapperKeeper = 1;
    int tapperIiko = 2;


    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
    Lock lock = new Lock();

    @Test
    @DisplayName("1.1. Авторизация в аккаунте техподдержке")
    public void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);
        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.openNewTabAndSwitchToCertainIndex(STAGE_IIKO_TABLE_3,tapperIiko);
        rootPage.switchTab(admin);
    }

    @Test
    @DisplayName("1.2. Переход на категорию заглушки, проверка всех элементов")
    public void goToLock() {

        lock.goToLockCategory();
        lock.isLockCorrect();

    }

    @Test
    @DisplayName("1.3. Выбираем определенный ресторан по чекбоксу из всего списка")
    public void choseOnlyCertainRestaurants() {

        lock.choseOnlyCertainRestaurants(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Выбираем заглушить сервис полностью")
    public void choseWholeServiceToLockOption() {

        lock.choseWholeServiceToLockOption();

    }

    @Test
    @DisplayName("1.5. Проверяем на выбранном столе, что есть предупреждение")
    public void checkOnTable() {

        rootPage.switchTab(tapperKeeper);
        rootPage.refreshPage();
        rootPage.isServiceUnavailable();

    }

    @Test
    @DisplayName("1.6. Проверяем в другом ресторане, что нет предупреждения")
    public void checkOnTableAnotherRestaurant() {

        rootPage.switchBrowserTab(tapperIiko);
        rootPage.refreshPage();
        serviceUnavailabilityContainer.shouldHave(disappear);
        rootPage.switchBrowserTab(admin);

    }

    @Test
    @DisplayName("1.7. Выбираем в админке заглушить только оплату")
    public void choseOnlyPaymentToLockOption() {

        lock.choseOnlyPaymentToLockOption();

    }

    @Test
    @DisplayName("1.8. Проверяем на выбранном столе, что оплата заглушена")
    public void isPaymentUnavailable() {

        rootPage.switchBrowserTab(tapperKeeper);
        rootPage.openNotEmptyTable(tableUrl);

        rootPage.clickOnPaymentButton();
        rootPage.isPaymentUnavailable();

    }

    @Test
    @DisplayName("1.9. Проверяем в другом ресторане, что оплата не будет заглушена")
    public void  isPaymentUnavailableAnotherRestaurant() {

        rootPage.switchBrowserTab(tapperIiko);

        rootPage.clickOnPaymentButton();
        rootPage.isTextContainsInURL(BEST2PAY_NAME);

    }

    @Test
    @DisplayName("2.0. Отключаем все рестораны в админке, очищаем тестовое окружение")
    public void resetAllRestaurants() {

        rootPage.switchBrowserTab(admin);
        lock.resetAllRestaurants();

    }

}
