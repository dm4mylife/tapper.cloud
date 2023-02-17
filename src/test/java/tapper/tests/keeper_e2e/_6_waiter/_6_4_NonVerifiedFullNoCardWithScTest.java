package tapper.tests.keeper_e2e._6_waiter;


import api.ApiRKeeper;
import data.Constants;
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

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;

@Order(64)
@Epic("RKeeper")
@Feature("Официант")
@Story("Официант не верифицирован, без привязанной карты, полная оплата + СБ")
@DisplayName("Официант не верифицирован, без привязанной карты, полная оплата + СБ")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _6_4_NonVerifiedFullNoCardWithScTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    static int amountDishesForFillingOrder = 6;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_222,WAITER_IRONMAN_NON_VERIFIED_NON_CARD,
                TABLE_AUTO_222_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_222_ID);

    }

    @Test
    @DisplayName("2. Проверка суммы, отсутствия чаевых, корректности сервисного сбора")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditionsWithNonVerifiedWaiter(cleanDishesSum);
        rootPage.activateServiceChargeIfDeactivated();

    }

    @Test
    @DisplayName("3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

    }

    @Test
    @DisplayName("4. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("6. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
