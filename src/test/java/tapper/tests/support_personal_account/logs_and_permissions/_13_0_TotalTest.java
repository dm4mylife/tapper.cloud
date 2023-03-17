package tapper.tests.support_personal_account.logs_and_permissions;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.logsAndPermissions.LogsAndPermissions;
import tapper_table.RootPage;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.FileNotFoundException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.FileDownloadMode.PROXY;
import static data.Constants.LOADER_GIF_PATH;
import static data.Constants.OLD_LOADER_GIF_PATH;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.tabPreloader;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.licenseTab.licenseIdTab;
import static data.selectors.TapperTable.Common.startScreenLogoContainerImageNotSelenide;

@Order(130)
@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Проверка категории логов/доступы, отображения элементов, всех табов на странице")
@DisplayName("Проверка категории логов/доступы, отображения элементов, всех табов на странице")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _13_0_TotalTest extends AdminBaseTest {
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    @Test
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {
        
        Configuration.fileDownload = FOLDER;

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу логов, проверка всех элементов в этом разделе")
    public void goToLogsAndPermissions() {

        logsAndPermissions.goToLogsAndPermissionsCategory();

    }
    @Test
    @DisplayName("1.3. Выбор ресторана")
    public void chooseRestaurant() {

        logsAndPermissions.chooseRestaurant(RESTAURANT_NAME);

    }

    @Test
    @DisplayName("1.4. Переход и проверка элементов в разделе Логи")
    public void goToLogsTab() {

        logsAndPermissions.isLogsAndPermissionsCategoryCorrect();
        logsAndPermissions.isLogsTabCorrect();

    }

    @Test
    @DisplayName("1.5. Переход и проверка элементов в разделе Доступы")
    public void goToPermissionsTab() {

        logsAndPermissions.isPermissionsTabCorrect();

    }

    @Test
    @DisplayName("1.6. Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML интерфейс для приложения")
    public void goToLicenseTab() {

        rootPage.click(licenseIdTab);
        tabPreloader.shouldNotBe(visible, Duration.ofSeconds(10));
        logsAndPermissions.choseXmlApplicationOption();

    }

    @Test
    @DisplayName("1.7. Переход и проверка элементов в разделе Лицензия R-keeper c опцией XML сохранение заказов")
    public void choseXmlSaveOrderOption() {

        logsAndPermissions.choseXmlSaveOrderOption();

    }

    @Test
    @DisplayName("1.8. Переход и проверка элементов в разделе rkeeper/iiko")
    public void isCashDeskTabCorrect() {

        logsAndPermissions.isCashDeskTabCorrect();

    }

    @Test
    @DisplayName("1.9. Переход и проверка элементов в разделе Операции")
    public void goToOperationTab() {

        logsAndPermissions.isOperationsTabCorrect();

    }

    @Test
    @DisplayName("2.0. Переход и проверка элементов в разделе Эквайринг")
    public void goToAcquiringTab() {

        logsAndPermissions.isAcquiringTabCorrect();
        logsAndPermissions.isAllAcquiringOptionsExists();

    }

    @Test
    @DisplayName("2.1. Смена типа эквайринга на Best2pay")
    public void changeAcquiringToBest2Pay() {

        logsAndPermissions.changeAcquiringToBest2Pay();

    }

    @Test
    @DisplayName("2.2. Возвращаем установленный ранее тип эквайринга")
    public void returnToDefaultAcquiring() {

        logsAndPermissions.returnToDefaultAcquiring();

    }

    @Test
    @DisplayName("2.3. Переход и проверка элементов в разделе Лоадер")
    public void goToLoaderTab() {

        logsAndPermissions.isLoaderTabCorrect();

    }

    @Test
    @DisplayName("2.4. Меняем лоадер и проверяем его на столе")
    public void changeLoader() {

        logsAndPermissions.changeLoader(LOADER_GIF_PATH);
        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.isImageCorrect(startScreenLogoContainerImageNotSelenide,
                "Изображение\\гиф на столе не корректная");

    }

    @Test
    @DisplayName("2.5. Меняем лоадер на дефолтный")
    public void changeLoaderByDefault() {

        rootPage.switchBrowserTab(0);
        logsAndPermissions.changeLoader(OLD_LOADER_GIF_PATH);

    }

    @Test
    @DisplayName("2.6. Переход и проверка элементов в разделе Статистика")
    public void goToStatisticsTab() {

        logsAndPermissions.isStatisticsTabCorrect();

    }

    @Test
    @DisplayName("2.7. Скачивание таблицы и балансов официанта")
    public void downloadStatisticsData() throws FileNotFoundException {

        logsAndPermissions.downloadStatisticsData();

    }

    @Test
    @DisplayName("2.8. Переход и проверка элементов в разделе Чаевые")
    public void goToTipsTab() {

        logsAndPermissions.isTipsTabCorrect();

    }

    @Test
    @DisplayName("2.9. Переход и проверка элементов в разделе Кастомизация")
    public void goToCustomizationTab() {

        logsAndPermissions.isCustomizationTabCorrect();

    }

}
