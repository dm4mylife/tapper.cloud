package tapper.tests.keeper_e2e._1_1_common;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.AdminPersonalAccount.ROBOCOP_WAITER;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("RKeeper")
@Feature("Общие")
@Story("Проверка сообщений вызова официанта при пустом столе и в заказе")
@DisplayName("Проверка сообщений вызова официанта при пустом столе и в заказе")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckCallWaiterTgMsgTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static String tapperTable;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    int amountDishesForFillingOrder = 4;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.openEmptyTapperTable)
    void openAndCheck() {

        nestedTests.clearTableAndOpenEmptyTable(restaurantName, tableId, apiUri, tableUrl);
        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber, tableNumberRegex);

    }

    @Test
    @Order(2)
    @DisplayName(TapperTable.callWaiterAndSendMessage)
    void sendCallWaiter() {

        rootPageNestedTests.callWaiterAndTextMessage();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void getTgAndTapperMsgData() {

        telegramDataForTgMsg = rootPage.getCallWaiterTgMsgData(tapperTable, UNKNOWN_WAITER,TEST_WAITER_COMMENT);
        tapperDataForTgMsg = rootPage.getTapperDataForTgCallWaiterMsg(UNKNOWN_WAITER, TEST_WAITER_COMMENT, tapperTable);

        Assertions.assertEquals(telegramDataForTgMsg, tapperDataForTgMsg,
                "Сообщение в телеграмме не корректное");

    }

    @Test
    @Order(4)
    @DisplayName(TapperTable.createOrderInKeeper)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.refreshPage)
    void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.callWaiterAndSendMessage)
    void sendCallWaiterOnceMore() {

        sendCallWaiter();

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void getTgAndTapperMsgDataAgain() {

        telegramDataForTgMsg = rootPage.getCallWaiterTgMsgData(tapperTable, ROBOCOP_WAITER,TEST_WAITER_COMMENT);
        tapperDataForTgMsg = rootPage.getTapperDataForTgCallWaiterMsg(ROBOCOP_WAITER, TEST_WAITER_COMMENT, tapperTable);

        Assertions.assertEquals(telegramDataForTgMsg, tapperDataForTgMsg,
                "Сообщение в телеграмме не корректное");

    }


    @Test
    @Order(8)
    @DisplayName(TapperTable.closedOrder)
    void closedOrderByApi() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid);

    }

}
