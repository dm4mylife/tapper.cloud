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

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.tabPreloader;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdTab;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Лицензия R-keeper")
@DisplayName("Проверка категории логов/доступы, отображения элементов, всех табов на странице")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class KeeperLicenseTabTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);
        logsAndPermissions.goToLogsAndPermissionsCategory();
        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);
        BaseActions.click(licenseIdTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));
        logsAndPermissions.choseXmlApplicationOption();

    }

    @Test
    @Order(6)
    @DisplayName("Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML интерфейс для приложения")
    void goToLicenseTab() {



    }

    @Test
    @Order(7)
    @DisplayName("Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML сохранение заказов")
    void choseXmlSaveOrderOption() {

        logsAndPermissions.choseXmlSaveOrderOption();

    }


}
