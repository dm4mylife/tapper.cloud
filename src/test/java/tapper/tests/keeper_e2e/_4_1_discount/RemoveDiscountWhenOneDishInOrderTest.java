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
import static data.Constants.RegexPattern.TapperTable.dishPriceRegex;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление скидки когда осталась одна позиция ")
@DisplayName("Удаление скидки когда осталась одна позиция ")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class RemoveDiscountWhenOneDishInOrderTest extends BaseTest {

    static String uni;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static String discountAmount = "10000";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishes = 5;
    static int firstUni = 0;
    static int amountDishesForFillingOrder = 6;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();
    static Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount = new HashMap<>();
    static Map<Integer,Map<String,Double>> dishesAfterAddingDiscount = new HashMap<>();

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

        uni = rootPageNestedTests.getOrderUni(TABLE_AUTO_444_ID,AUTO_API_URI).get(firstUni);

        rootPage.openNotEmptyTable(STAGE_RKEEPER_TABLE_444);
        rootPage.isTableHasOrder();

    }

    @Test
    @DisplayName("1.1. Проверяем скидку на столе")
    public void isDiscountCorrectOnTable() {

        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.2. Выбираем все позиции, кроме одной")
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
    @DisplayName("1.7. Сохраняем данным по не оплаченной позиции")
    public void saveDishPricesInCollection() {

        dishesBeforeAddingDiscount =
                rootPage.saveDishPricesInCollection(allNonPaidAndNonDisabledDishes,dishPriceWithDiscountSelector,
                        dishPriceWithoutDiscountSelector,dishPriceRegex);

    }

    @Test
    @DisplayName("1.8. Удаляем скидку из заказу")
    public void addDiscountAndCheckSums() {

        apiRKeeper.deleteDiscount(apiRKeeper.rqBodyDeleteDiscount(R_KEEPER_RESTAURANT, guid, uni), AUTO_API_URI);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }
    @Test
    @DisplayName("1.9. Проверяем что скидка удалилась на столе")
    public void isDiscountCorrectOnTableAfterPay() {

        dishesAfterAddingDiscount =
                rootPage.saveDishPricesInCollection(allNonPaidAndNonDisabledDishes,dishPriceWithDiscountSelector,
                        dishPriceWithoutDiscountSelector,dishPriceRegex);

        rootPageNestedTests.isNonPaidDishesHasMarkupAfterRemovingDiscount
                (dishesBeforeAddingDiscount,dishesAfterAddingDiscount);

    }

    @Test
    @DisplayName("2.0. Оплачиваем остатки")
    public void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_444, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
