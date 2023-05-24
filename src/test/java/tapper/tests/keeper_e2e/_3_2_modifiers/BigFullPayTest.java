package tapper.tests.keeper_e2e._3_2_modifiers;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.*;
import static api.ApiData.QueryParams.allTypesModificatorList;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;

@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Все вариации модификаторов. Полная оплата без чаевых")
@DisplayName("Все вариации модификаторов. Полная оплата без чаевых")

@TestMethodOrder(MethodOrderer.DisplayName.class)
class BigFullPayTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_333.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_333.tableCode;
    protected final String waiter = TableData.Keeper.Table_333.waiter;
    protected final String apiUri = TableData.Keeper.Table_333.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_333.tableUrl;
    protected final String tableId = TableData.Keeper.Table_333.tableId;

    protected static String guid;
    protected static double totalPay;
    protected static String orderType = "full";
    protected static HashMap<String, String> paymentDataKeeper;
    protected static LinkedHashMap<String, String> tapperDataForTgMsg;
    protected static LinkedHashMap<String, String> telegramDataForTgMsg;
    protected static String transactionId;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = rootPageNestedTests.createAndFillOrderOnlyWithModifiers
                (restaurantName, tableCode,waiter, apiUri,allTypesModificatorList, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @DisplayName("2. Проверка что заказ с кассы совпадает со столом")
    public void matchTapperOrderWithOrderInKeeper() {

        //rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId); toDo тест падает, т.к. в меню есть забагованная позиция в цене. на кассе одна цена в таппере другая

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();
        rootPage.isModificatorTextCorrect();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("7. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
