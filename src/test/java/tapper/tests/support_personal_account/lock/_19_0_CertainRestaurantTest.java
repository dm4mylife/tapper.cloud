package tapper.tests.support_personal_account.lock;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.lock.Lock;
import tapper_table.RootPage;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static com.codeborne.selenide.Condition.disappear;
import static data.Constants.TestData.Best2Pay.BEST2PAY_NAME;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.Common.serviceUnavailabilityContainer;

@Order(190)
@Epic("Личный кабинет техподдержки")
@Feature("Заглушка")
@Story("Заглушаем только один ресторан и проверяем все вариации заглушки")
@DisplayName("Заглушаем только один ресторан и проверяем все вариации заглушки")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _19_0_CertainRestaurantTest extends BaseTest {

    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Lock lock = new Lock();

    @Test
    @DisplayName("1.1. Авторизация в аккаунте техподдержке")
    public void authorizeUser() {

        Configuration.browserSize = "1920x1080";

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

        lock.choseOnlyCertainRestaurants(RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Выбираем заглушить сервис полностью")
    public void choseWholeServiceToLockOption() {

        lock.choseWholeServiceToLockOption();

    }

    @Test
    @DisplayName("1.5. Проверяем на выбранном столе, что есть предупреждение")
    public void checkOnTable() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_333);
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
        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_333);

        rootPage.clickOnPaymentButton();
        rootPage.isPaymentUnavailable();

    }

    @Test
    @DisplayName("1.9. Проверяем в другом ресторане, что оплата не будет заглушена")
    public void isPaymentUnavailableAnotherRestaurant() {

        rootPage.switchBrowserTab(1);
        rootPage.openPage(STAGE_IIKO_TABLE_3);
        rootPage.isDishListNotEmptyAndVisible();

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
