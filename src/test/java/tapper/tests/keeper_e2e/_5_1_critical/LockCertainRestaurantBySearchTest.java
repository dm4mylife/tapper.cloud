package tapper.tests.keeper_e2e._5_1_critical;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import support_personal_account.lock.Lock;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;


@Epic("Личный кабинет техподдержки")
@Feature("Заглушка")
@Story("Заглушаем только один ресторан, но через поиск, и проверяем все вариации заглушки")
@DisplayName("Заглушаем только один ресторан, но через поиск, и проверяем все вариации заглушки")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LockCertainRestaurantBySearchTest extends PersonalAccountTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Lock lock = new Lock();

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
    public void choseOnlyCertainRestaurantsBySearch() {

        lock.choseOnlyCertainRestaurantsBySearch(KEEPER_RESTAURANT_NAME);

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
    @DisplayName("1.7. Проверяем на выбранном столе что оплата заглушена")
    public void isPaymentUnavailable() {

        rootPage.switchBrowserTab(1);
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

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
