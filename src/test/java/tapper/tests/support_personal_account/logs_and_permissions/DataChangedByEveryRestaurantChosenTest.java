package tapper.tests.support_personal_account.logs_and_permissions;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.*;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка что при выборе ресторана из поиска на основной странице меняются данные")
@DisplayName("Проверка что при выборе ресторана из поиска на основной странице меняются данные")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataChangedByEveryRestaurantChosenTest extends PersonalAccountTest {
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу логов, проверка всех элементов в этом разделе")
    void goToLogsAndPermissions() {

        logsAndPermissions.goToLogsAndPermissionsCategory();

    }

    @Test
    @Order(3)
    @DisplayName("Выбор ресторана кипер")
    void chooseRestaurantKeeper() {

        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(4)
    @DisplayName("Проверка что таб Доступы соответствует этому ресторану")
    void isKeeperPermissionTabCorrect() {

        logsAndPermissions.isKeeperPermissionTabCorrect();

    }

    @Test
    @Order(5)
    @DisplayName("Выбор ресторана iiko")
    void chooseRestaurantIiko() {

        logsAndPermissions.chooseRestaurant(IIKO_RESTAURANT_NAME);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка что таб Доступы соответствует этому ресторану")
    void goToPermissionsTab() {

        logsAndPermissions.isIikoPermissionTabCorrect();

    }

}
