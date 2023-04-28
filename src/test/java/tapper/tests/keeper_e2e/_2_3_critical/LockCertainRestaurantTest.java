package tapper.tests.keeper_e2e._2_3_critical;

import api.ApiRKeeper;
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
public class LockCertainRestaurantTest extends PersonalAccountTest {

    static String guid;
    static int amountDishesForFillingOrder = 3;


    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();


    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Lock lock = new Lock();

    @Test
    @DisplayName("1.0 Оплачиваем заказ на столе чтобы была хоть одна транзакция в истории операций")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_222,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.1. Авторизация в аккаунте техподдержке")
    public void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

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

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_222);
        rootPage.isServiceUnavailable();

    }

    @Test
    @DisplayName("1.6. Проверяем в другом ресторане, что нет предупреждения")
    public void checkOnTableAnotherRestaurant() {

        rootPage.openPage(STAGE_IIKO_TABLE_3);
        serviceUnavailabilityContainer.shouldHave(disappear);
        rootPage.switchBrowserTab(0);

    }

    @Test
    @DisplayName("1.7. Выбираем в админке заглушить только оплату")
    public void choseOnlyPaymentToLockOption() {

        lock.choseOnlyPaymentToLockOption();

    }

    @Test
    @DisplayName("1.8. Проверяем на выбранном столе, что оплата заглушена")
    public void isPaymentUnavailable() {

        rootPage.switchBrowserTab(1);
        rootPage.openNotEmptyTable(STAGE_RKEEPER_TABLE_222);
        rootPage.isTableHasOrder();

        rootPage.clickOnPaymentButton();
        rootPage.isPaymentUnavailable();

    }

    @Test
    @DisplayName("1.9. Проверяем в другом ресторане, что оплата не будет заглушена")
    public void  isPaymentUnavailableAnotherRestaurant() {

        rootPage.switchBrowserTab(1);
        rootPage.openPage(STAGE_IIKO_TABLE_3);
        rootPage.isTableHasOrder();

        rootPage.clickOnPaymentButton();
        rootPage.isTextContainsInURL(BEST2PAY_NAME);

    }

    @Test
    @DisplayName("2.0. Отключаем все рестораны в админке, очищаем тестовое окружение")
    public void resetAllRestaurants() {

        rootPage.switchBrowserTab(0);
        lock.resetAllRestaurants();

    }

}
