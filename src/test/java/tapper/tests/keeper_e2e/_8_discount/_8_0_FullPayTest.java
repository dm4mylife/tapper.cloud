package tapper.tests.keeper_e2e._8_discount;


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
import java.util.Map;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.*;

@Order(80)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Полная оплата с общей скидкой на заказ")
@DisplayName("Полная оплата с общей скидкой на заказ")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_0_FullPayTest extends BaseTest {

    static String visit;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static double discount;

    static int amountDishesForFillingOrder = 10;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                TABLE_AUTO_222_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSum(discounts, DISCOUNT_WITH_CUSTOM_SUM,"10000");
        apiRKeeper.createDiscountById(discounts, DISCOUNT_BY_ID);

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rsBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);


    }

    @Test
    @DisplayName("2. Проверка суммы, чаевых, сервисного сбора, скидку")
    public void checkSumTipsSC() {

        discount = rootPageNestedTests.getDiscount(TABLE_AUTO_222_ID);
        rootPageNestedTests.isDiscountCorrectOnTable(discount);
        rootPageNestedTests.checkAllDishesSumsWithAllConditions();

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
        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);
    }

    @Test
    @DisplayName("6. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {


        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
