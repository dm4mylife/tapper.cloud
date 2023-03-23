package tapper.tests.keeper_e2e._4_1_discount;


import api.ApiRKeeper;
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

import static api.ApiData.QueryParams.rqParamsDeletePosition;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_444;

@Order(94)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление позиций после применения скидки")
@DisplayName("Удаление позиций после применения скидки")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_4_RemoveDishAfterDiscountTest extends BaseTest {

    static String uni;
    static String guid;
    static double totalPay;
    static String orderType = "full";
    static String discountAmount = "10000";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static double discount;
    static int firstUni = 0;
    static int amountDishesForFillingOrder = 6;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_444,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        uni =  apiRKeeper.getUniFirstValueFromOrderInfo(TABLE_AUTO_444_ID,AUTO_API_URI);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);
        rootPage.isTableHasOrder();

    }

    @Test
    @DisplayName("1.1. Проверяем скидку на столе")
    public void isDiscountCorrectOnTable() {

        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.2. Удаляем одно блюдо на кассе")
    public void addOneMoreDishInOrder() {

        apiRKeeper.deletePosition(rqParamsDeletePosition(R_KEEPER_RESTAURANT, guid, uni, 1000), AUTO_API_URI);

    }

    @Test
    @DisplayName("1.3. Пытаемся оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @DisplayName("1.4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.6. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
