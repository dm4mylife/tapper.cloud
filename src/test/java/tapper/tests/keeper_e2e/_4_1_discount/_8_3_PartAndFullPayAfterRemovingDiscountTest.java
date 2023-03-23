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

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_444;

@Order(83)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление скидки из заказа, полная оплата")
@DisplayName("Удаление скидки из заказа, полная оплата")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_3_PartAndFullPayAfterRemovingDiscountTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static String uni;
    static String discountAmount = "10000";
    static String orderType = "part";
    static int amountDishes = 1;
    static int amountDishesForFillingOrder = 4;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT,
                TABLE_CODE_444, WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI, dishesForFillingOrder, TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM, discountAmount);

        Map<String, Object> rqBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT, guid, discounts);
        apiRKeeper.createDiscount(rqBodyCreateDiscount);

        uni = apiRKeeper.getUniDiscountFromCreateOrder(TABLE_AUTO_444_ID, AUTO_API_URI);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);

    }
    @Test
    @DisplayName("1.2. Проверка скидки")
    public void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.3. Проверяем суммы, чаевые, сб")
    public void checkAllDishesSumsWithAllConditions() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditions();

    }

    @Test
    @DisplayName("1.4. Удаляем скидку из заказа и проверяем суммы")
    public void addDiscountAndCheckSums() {

        apiRKeeper.deleteDiscount(apiRKeeper.rqBodyDeleteDiscount(R_KEEPER_RESTAURANT, guid, uni), AUTO_API_URI);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные блюда для частичной оплаты")
    public void getRandomDishes() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.6. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiringPartPay() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.7. Переходим в эквайринг, оплачиваем позиции")
    public void savePaymentDataForAcquiringAfterDeletedDiscount() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.8. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterDeletedDiscount() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @DisplayName("1.9. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperDataPartPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("2.0. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiringFullPay() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("2.1. Переходим в эквайринг, оплачиваем позиции")
    public void savePaymentDataForAcquiringAfterPartialPayment() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("2.2. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterPartialPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("2.3. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperDataFullPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
