package tapper.tests.keeper_e2e._8_discount;


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
import static data.Constants.TestData.TapperTable.*;

@Order(91)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Добавление скидки, когда в заказе осталась одна позиция")
@DisplayName("Добавление скидки, когда в заказе осталась одна позиция")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _9_1_AddDiscountWhenOneDishInOrderTest extends BaseTest {

    static String uni;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static String discountAmount = "10000";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static double discount;
    static int amountDishes = 5;
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
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI, dishesForFillingOrder, TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);
        rootPage.isDishListNotEmptyAndVisible();

    }

    @Test
    @DisplayName("1.1. Выбираем все позиции, кроме одной")
    public void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.2. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.3. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.5. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("1.6. Добавляем скидку к заказу")
    public void addDiscountAndCheckSums() {

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM, discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT, guid, discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.refreshPage();
        rootPage.isDishListNotEmptyAndVisible();

    }

    @Test
    @DisplayName("1.7. Проверяем скидку на столе")
    public void isDiscountCorrectOnTableAfterPay() {

        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);
        rootPageNestedTests.hasNoDiscountPriceOnPaidDishesIfDiscountWasAppliedAfterPayment();

    }

    @Test
    @DisplayName("1.8. Оплачиваем остатки")
    public void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_444, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
