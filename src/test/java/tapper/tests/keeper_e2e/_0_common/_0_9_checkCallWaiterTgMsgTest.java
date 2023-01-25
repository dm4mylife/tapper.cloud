package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.*;

@Order(1)
@Epic("RKeeper")
@Feature("Общие")
@Story("tapper - проверка сообщений вызова официанта при пустом столе и в заказе")
@DisplayName("tapper - проверка сообщений вызова официанта при пустом столе и в заказе")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_9_checkCallWaiterTgMsgTest extends BaseTest {

    static String visit;
    static String guid;
    static String tapperTable;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие стола")
    public void openAndCheck() {

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);

        if (modalHintContainer.isDisplayed()) {

            rootPage.closeHintModal();

        }

        rootPage.isElementVisible(appHeader);

    }

    @Test
    @DisplayName("2. Отправка сообщения в вызов официанта")
    public void sendCallWaiter() {

        rootPage.openCallWaiterForm();
        rootPage.sendWaiterComment();
        rootPage.isSendSuccessful();

    }

    @Test
    @DisplayName("3. Сбор данных со стола и сообщения тг")
    public void getTgAndTapperMsgData() {

        tapperTable = "Номер столика: " + rootPage.convertSelectorTextIntoStrByRgx(tableNumber,"\\D+");

        telegramDataForTgMsg = rootPage.getTgCallWaiterMsgData(tapperTable,WAIT_FOR_TELEGRAM_MESSAGE_REVIEW);
        tapperDataForTgMsg = rootPage.getTapperDataForTgCallWaiterMsg(TEST_WAITER_COMMENT,tapperTable);

    }

    @Test
    @DisplayName("4. Проверка что сообщение корректное")
    public void matchTgMsgDataAndTapperData() {
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);
    }

    @Test
    @DisplayName("5. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

    }

    @Test
    @DisplayName("6. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isDishListNotEmptyAndVisible();
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("7. Сбор данных со стола и сообщения тг")
    public void getTgAndTapperMsgDataAgain() {
        getTgAndTapperMsgData();
    }

    @Test
    @DisplayName("8. Проверка что сообщение корректное")
    public void matchTgMsgDataAndTapperDataAgain() {
        matchTgMsgDataAndTapperData();
    }

    @Test
    @DisplayName("9. Закрываем заказ")
    public void clearDataAndChoseAgain() {
        rootPage.closeOrderByAPI(guid);
    }

}
