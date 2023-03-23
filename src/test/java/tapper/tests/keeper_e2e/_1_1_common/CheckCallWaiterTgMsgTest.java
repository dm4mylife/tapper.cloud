package tapper.tests.keeper_e2e._1_1_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;


import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.ROBOCOP_WAITER;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_CALL_WAITER;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_REVIEW;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("RKeeper")
@Feature("Общие")
@Story("tapper - проверка сообщений вызова официанта при пустом столе и в заказе")
@DisplayName("tapper - проверка сообщений вызова официанта при пустом столе и в заказе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CheckCallWaiterTgMsgTest extends BaseTest {

    static String guid;
    static String tapperTable;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static int amountDishesForFillingOrder = 4;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.0. Открытие пустого стола")
    public void openAndCheck() {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI),
                "На столе был прошлый заказ, его не удалось закрыть");
        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("1.2. Отправка сообщения в вызов официанта")
    public void sendCallWaiter() {

        rootPage.isElementVisible(appHeader);
        rootPage.openCallWaiterForm();
        rootPage.sendWaiterComment();
        rootPage.isSendSuccessful();

    }

    @Test
    @DisplayName("1.3. Сбор данных со стола и сообщения тг")
    public void getTgAndTapperMsgData() {

        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber,"\\D+");

        telegramDataForTgMsg = rootPage.getCallWaiterTgMsgData(tapperTable, WAIT_FOR_TELEGRAM_MESSAGE_CALL_WAITER);
        tapperDataForTgMsg = rootPage.getTapperDataForTgCallWaiterMsg
                (UNKNOWN_WAITER,TEST_WAITER_COMMENT,tapperTable);

    }

    @Test
    @DisplayName("1.4. Проверка что сообщение корректное")
    public void matchTgMsgDataAndTapperData() {

        Assertions.assertEquals(telegramDataForTgMsg,tapperDataForTgMsg,
                "Сообщение в телеграмме не корректное");

    }

    @Test
    @DisplayName("1.5. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.6. Открытие стола уже с заказом, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("1.7. Отправка сообщения в вызов официанта")
    public void sendCallWaiterOnceMore() {

        sendCallWaiter();

    }

    @Test
    @DisplayName("1.8. Сбор данных со стола и сообщения тг")
    public void getTgAndTapperMsgDataAgain() {

        telegramDataForTgMsg = rootPage.getCallWaiterTgMsgData(tapperTable, WAIT_FOR_TELEGRAM_MESSAGE_REVIEW);
        tapperDataForTgMsg = rootPage.getTapperDataForTgCallWaiterMsg
                (ROBOCOP_WAITER,TEST_WAITER_COMMENT,tapperTable);

    }

    @Test
    @DisplayName("1.9. Проверка что сообщение корректное")
    public void matchTgMsgDataAndTapperDataAgain() {

        matchTgMsgDataAndTapperData();

    }

    @Test
    @DisplayName("2.0. Закрываем заказ")
    public void clearDataAndChoseAgain() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,guid,AUTO_API_URI);

    }

}
