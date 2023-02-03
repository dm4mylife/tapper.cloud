package tapper.tests.keeper_e2e._0_common;


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

import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable;

@Order(24)
@Epic("RKeeper")
@Feature("Частичная оплата")
@Story("Частичная оплата в несколько платежей до конца - +чай +сбор")
@DisplayName("Частичная оплата в несколько платежей до конца - +чай +сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _0_1_0_BigPartPayTillEndTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static int amountDishes = 3;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String orderType;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Disabled
    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), TapperTable.AUTO_API_URI);
        String visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "60000"));

        rootPage.openUrlAndWaitAfter(TapperTable.STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPage.openUrlAndWaitAfter(TapperTable.STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();
        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

    }

    @Test
    @DisplayName("4. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {
        transactionId = nestedTests.acquiringPayment(totalPay);
    }

    @Test
    @DisplayName("5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {
        nestedTests.checkPaymentAndB2pTransaction(orderType = "part", transactionId, paymentDataKeeper);
    }

    @Test
    @DisplayName("6. Разделяем счёт, оплачиваем по позициям и так пока весь заказ не будет оплачен")
    public void payTillEnd() {

        telegramDataForTgMsg = rootPage.getTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

        rootPageNestedTests.payTillFullSuccessPayment(amountDishes,guid);

    }

}
