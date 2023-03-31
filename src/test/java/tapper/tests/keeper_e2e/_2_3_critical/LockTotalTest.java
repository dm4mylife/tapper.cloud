package tapper.tests.keeper_e2e._2_3_critical;

import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.Constants.TestData.TapperTable.*;


@Epic("Личный кабинет техподдержки")
@Feature("Заглушка")
@Story("Проверка всех элементов заглушки, заглушаем все рестораны, сперва весь сервис, потом только оплату")
@DisplayName("Проверка всех элементов заглушки, заглушаем все рестораны, сперва весь сервис, потом только оплату")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LockTotalTest extends PersonalAccountTest {

    static String guid;
    static int amountDishesForFillingOrder = 7;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Lock lock = new Lock();

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @DisplayName("1. Создание заказа в r_keeper и авторизация в админке")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на категорию заглушки, проверка всех элементов")
    public void goToLock() {

        lock.goToLockCategory();
        lock.isLockCorrect();

    }

    @Test
    @DisplayName("1.3. Выбираем все рестораны")
    public void activateAllRestaurants() {

        lock.choseAllRestaurantToLockOption();

    }

    @Test
    @DisplayName("1.4. Выбираем заглушить сервис полностью")
    public void choseWholeServiceToLockOption() {

        lock.choseWholeServiceToLockOption();

    }

    @Test
    @DisplayName("1.5. Проверяем на выбранном столе, что есть предупреждение")
    public void checkOnTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_222);
        rootPage.isServiceUnavailable();
        rootPage.switchBrowserTab(0);

    }

    @Test
    @DisplayName("1.6. Выбираем в админке заглушить только оплату")
    public void choseOnlyPaymentToLockOption() {

        lock.choseOnlyPaymentToLockOption();

    }

    @Test
    @DisplayName("1.7. Проверяем на выбранном столе, что оплата заглушена")
    public void isPaymentUnavailable() {

        rootPage.switchBrowserTab(1);
        rootPage.refreshPage();

        rootPage.clickOnPaymentButton();
        rootPage.isPaymentUnavailable();

    }

    @Test
    @DisplayName("1.8. Отключаем все рестораны в админке, очищаем тестовое окружение")
    public void resetAllRestaurants() {

        rootPage.switchBrowserTab(0);
        lock.resetAllRestaurants();

    }

}
