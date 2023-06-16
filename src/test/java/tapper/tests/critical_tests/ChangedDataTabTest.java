package tapper.tests.critical_tests;

import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.permissionsTab;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка категории логов/доступы, редактирование полей")
@DisplayName("Проверка категории логов/доступы, редактирование полей")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChangedDataTabTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);
        logsAndPermissions.goToLogsAndPermissionsCategory();
        logsAndPermissions.chooseRestaurant(IIKO_RESTAURANT_NAME);

    }

    @Test
    @Order(2)
    @DisplayName("Проверяем редактирование полей в Доступах для Айко")
    void isPrivateDateChangedCorrect() {

        logsAndPermissions.isPrivateDateChangedCorrectForIiko();

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем ресторан кипера")
    void chooseRestaurant() {

        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);
        BaseActions.click(licenseIdTab); // toDo убрать это когда пофиксят баг что при смене ресторана вкладка доступы не обновляется
        BaseActions.click(permissionsTab);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем редактирование полей в Доступах для Кипера")
    void isPrivateDateChangedCorrectForKeeper() {

        logsAndPermissions.isPrivateDateChangedCorrectForKeeper();

    }

}
