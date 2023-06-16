package tapper.tests.critical_tests;

import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import support_personal_account.history_operations.HistoryOperations;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.SupportPersonalAccount.*;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Отключение сервисного сбора")
@DisplayName("Отключение сервисного сбора")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisableServiceChargeTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 4);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiterName,
                apiUri,dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

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
    @DisplayName("Переход и проверка элементов в разделе Кастомизация")
    void goToCustomizationTab() {

        logsAndPermissions.isCustomizationTabCorrect();

    }
    @Test
    @Order(6)
    @DisplayName("Выключаем сервисный сбор")
    void deactivateServiceCharge() {

        logsAndPermissions.deactivateServiceCharge();

    }

    @Test
    @Order(7)
    @DisplayName("Создание заказа в r_keeper и открытие стола")
    void createAndFillOrder() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.isTableHasOrder();

        rootPage.isServiceChargeDeactivatedByDefault();

        nestedTests.payOrder(tableId,"keeper",guid);

    }

    @Test
    @Order(8)
    @DisplayName("Включаем обратно сервисный сбор")
    void activateServiceCharge() {

        rootPage.switchTab(0);
        logsAndPermissions.activateServiceCharge();

    }

}
