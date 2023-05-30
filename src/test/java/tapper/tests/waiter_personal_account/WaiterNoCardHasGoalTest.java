package tapper.tests.waiter_personal_account;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static api.ApiData.OrderData.WAITER_NON_CARD_HAS_GOAL;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.serviceWorkerImage;


@Epic("Личный кабинет официант ресторана")
@Feature("Цель заполнена, но карта официанта не привязана (Пользовательская часть)")
@DisplayName("Цель заполнена, но карта официанта не привязана (Пользовательская часть)")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaiterNoCardHasGoalTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = WAITER_NON_CARD_HAS_GOAL;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    static int amountDishesForFillingOrder = 3;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа на кассе")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiterName, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPageNestedTests.openNotEmptyTable(tableUrl);
    }

    @Test
    @Order(2)
    @DisplayName("Проверяем на столе пустую цель накоплений")
    void checkGoalOnTable() {

        rootPage.isGoalCorrect("no_card_waiter",WAITER_GOAL_TEXT);

    }

}
