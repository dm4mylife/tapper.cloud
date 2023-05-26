package tapper.tests.keeper_e2e._3_3_part_payment;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.TapperTable.*;

@Epic("RKeeper")
@Feature("Частичная оплата")
@Story("Частичная оплата - +чай -сбор")
@DisplayName("Частичная оплата - +чай -сбор")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class TipsNoScTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_333.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_333.tableCode;
    protected final String waiter = TableData.Keeper.Table_333.waiter;
    protected final String apiUri = TableData.Keeper.Table_333.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_333.tableUrl;
    protected final String tableId = TableData.Keeper.Table_333.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 7;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode,waiter, apiUri,
                dishesForFillingOrder, tableUrl,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(2)
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, с чаем но без СБ")
    void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPageNestedTests.activateRandomTipsAndDeactivateSc();

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка сообщения в телеграмме")
    void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Переход на эквайринг, ввод данных, оплата")
    void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();
        payAndGoToAcquiring();
        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,"full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
