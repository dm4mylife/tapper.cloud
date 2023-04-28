package tapper.tests.keeper_e2e._2_3_critical;

import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.PersonalAccountTest;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;


@Epic("Личный кабинет техподдержки")
@Feature("Логи/доступы")
@Story("Чаевые")
@DisplayName("Выключение чаевых")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisableTipsTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    String guid;
    int amountDishesForFillingOrder = 3;
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();
    NestedTests nestedTests = new NestedTests();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете, выбираем ресторан кипера")
    void goToLogsAndPermissionsKeeperRestaurant() {

        logsAndPermissions.goToLogsAndPermissionsKeeperRestaurant();

    }

    @Test
    @Order(2)
    @DisplayName("Переход и проверка элементов в разделе Чаевые")
    void goToTipsTab() {

        logsAndPermissions.isTipsTabCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Отключаем чаевые")
    void disableTips() {

        logsAndPermissions.disableTips();

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем на столе чаевые")
    void checkOnTable() {

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_555);
        rootPage.isTableHasOrder();

        nestedTests.payOrder(tableId,"full",guid);

    }

    @Test
    @Order(5)
    @DisplayName("Включаем обратно чаевые")
    void activateTips() {

        rootPage.switchTab(0);
        logsAndPermissions.activateTips();

    }

}
