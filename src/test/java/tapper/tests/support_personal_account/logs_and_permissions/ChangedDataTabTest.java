package tapper.tests.support_personal_account.logs_and_permissions;

import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.RootPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static data.Constants.LOADER_GIF_PATH;
import static data.Constants.OLD_LOADER_GIF_PATH;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.tabPreloader;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdTab;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.permissionsTab.permissionsTab;
import static data.selectors.TapperTable.Common.startScreenLogoContainerImageNotSelenide;


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
