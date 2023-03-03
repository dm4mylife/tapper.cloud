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

@Order(84)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Частичная оплата + применение скидки (Частичная оплата, потом скидка, потом полная оплата)")
@DisplayName("Частичная оплата + применение скидки (Частичная оплата, потом скидка, потом полная оплата)")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_4_PartPayAddDiscountTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String orderType = "part";
    static String guid;
    static int amountDishes = 2;
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
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT,
                TABLE_CODE_444,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);
        rootPage.isDishListNotEmptyAndVisible();

    }

    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.4. Переходим на эквайринг, вводим данные, оплачиваем заказ")
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

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("1.7. Добавляем скидку")
    public void addDiscountAndCheckSums() {

        apiRKeeper.createDiscountByIdObject(discounts, DISCOUNT_BY_ID);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.refreshPage();

    }

    @Test
    @DisplayName("1.8. Проверяем суммы и скидку")
    public void checkIsDiscountPresent() {

        rootPage.isDishListNotEmptyAndVisible();
        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);
        rootPageNestedTests.hasNoDiscountPriceOnPaidDishesIfDiscountWasAppliedAfterPayment();
        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.9. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiringAfterAddedDiscount() {

        savePaymentDataForAcquiring();

    }

    @Test
    @DisplayName("2.0. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiringAfterAddedDiscount() {

        payAndGoToAcquiring();

    }

    @Test
    @DisplayName("2.1. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterAddedDiscount() {

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("2.2. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperDataFullPay() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
