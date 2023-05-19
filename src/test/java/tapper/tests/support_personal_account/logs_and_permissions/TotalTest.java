package tapper.tests.support_personal_account.logs_and_permissions;

import common.BaseActions;
import data.TableData;
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

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.LOADER_GIF_PATH;
import static data.Constants.OLD_LOADER_GIF_PATH;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.tabPreloader;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdContainer;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdTab;
import static data.selectors.TapperTable.Common.startScreenLogoContainerImageNotSelenide;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка категории логов/доступы, отображения элементов, всех табов на странице")
@DisplayName("Проверка категории логов/доступы, отображения элементов, всех табов на странице")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;
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
    @DisplayName("Выбор ресторана")
    void chooseRestaurant() {

        logsAndPermissions.chooseRestaurant(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(4)
    @DisplayName("Переход и проверка элементов в разделе Доступы")
    void goToPermissionsTab() {

        logsAndPermissions.isPermissionsTabCorrect();

    }


    @Test
    @Order(5)
    @DisplayName("Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML интерфейс для приложения")
    void choseXmlSaveOrderOption() {

        BaseActions.click(licenseIdTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));


        logsAndPermissions.choseXmlApplicationOption();

    }


    @Test
    @Order(6)
    @DisplayName("Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML сохранение заказов")
    void goToLicenseTab() {

        logsAndPermissions.choseXmlSaveOrderOption();

    }

    @Test
    @Order(7)
    @DisplayName("Возвращаем исходные значения и проверяем форму отмены изменения")
    void notSaveChanges() {

        logsAndPermissions.notSaveChangesLicenseTab(KEEPER_RESTAURANT_NAME);

    }


    @Test
    @Order(8)
    @DisplayName("Переход и проверка элементов в разделе rkeeper/iiko")
    void isCashDeskTabCorrect() {

        logsAndPermissions.isCashDeskTabCorrect(KEEPER_RESTAURANT_NAME);

    }

    @Test
    @Order(9)
    @DisplayName("Переход и проверка элементов в разделе Эквайринг")
    void goToAcquiringTab() {

        logsAndPermissions.isAcquiringTabCorrect();
        logsAndPermissions.isAllAcquiringOptionsExists();

    }

    @Test
    @Order(10)
    @DisplayName("Смена типа эквайринга на Best2pay")
    void changeAcquiringToBest2Pay() {

        logsAndPermissions.changeAcquiringToBest2Pay();

    }

    @Test
    @Order(11)
    @DisplayName("Возвращаем установленный ранее тип эквайринга")
    void returnToDefaultAcquiring() {

        logsAndPermissions.returnToDefaultAcquiring();

    }

    @Test
    @Order(12)
    @DisplayName("Переход и проверка элементов в разделе Лоадер")
    void goToLoaderTab() {

        logsAndPermissions.isLoaderTabCorrect();

    }

    @Test
    @Order(13)
    @DisplayName("Меняем лоадер")
    void changeLoader() {

        logsAndPermissions.changeLoader(LOADER_GIF_PATH);
    }

    @Test
    @Order(14)
    @DisplayName("Проверяем его на столе")
    void isLoaderCorrect() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.isImageCorrect(startScreenLogoContainerImageNotSelenide,
                "Изображение\\гиф на столе не корректная");

    }

    @Test
    @Order(15)
    @DisplayName("Меняем лоадер на дефолтный")
    void changeLoaderByDefault() {

        rootPage.switchBrowserTab(0);
        logsAndPermissions.changeLoader(OLD_LOADER_GIF_PATH);

    }

    @Test
    @Order(16)
    @DisplayName("Переход и проверка элементов в разделе Статистика")
    void goToStatisticsTab() {

        logsAndPermissions.isStatisticsTabCorrect();

    }

    @Test
    @Order(17)
    @DisplayName("Скачивание таблицы и балансов официанта")
    void downloadStatisticsData() throws FileNotFoundException {

        logsAndPermissions.downloadStatisticsData();

    }

    @Test
    @Order(18)
    @DisplayName("Переход и проверка элементов в разделе Чаевые")
    void goToTipsTab() {

        logsAndPermissions.isTipsTabCorrect();

    }

    @Test
    @Order(18)
    @DisplayName("Переход и проверка элементов в разделе Кастомизация")
    void goToCustomizationTab() {

        logsAndPermissions.isCustomizationTabCorrect();

    }
    @Test
    @Order(20)
    @DisplayName("Переход и проверка элементов в разделе Система лояльности")
    void goToLoyaltyTab() {

        logsAndPermissions.isLoyaltyTabCorrect();

    }


}
