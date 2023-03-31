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
import static com.codeborne.selenide.Selenide.$$;
import static data.Constants.RegexPattern.TapperTable.dishPriceRegex;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_444;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("RKeeper")
@Feature("Скидка")
@Story("Увеличение скидки (Целой)")
@DisplayName("Увеличение скидки (Целой)")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class IncreaseDecimalDiscountTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static String discountAmount = "20050";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 10;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    static Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount = new HashMap<>();
    static Map<Integer,Map<String,Double>> dishesAfterAddingDiscount = new HashMap<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_444,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(STAGE_RKEEPER_TABLE_444);

    }
    @Test
    @DisplayName("2. Проверка суммы, чаевых, сервисного сбора, скидку")
    public void checkSumTipsSC() {

        rootPageNestedTests.checkIsDiscountPresent(TABLE_AUTO_444_ID);
        rootPageNestedTests.hasDiscountPriceOnPaidDishesIfDiscountAppliedAfter();
        rootPageNestedTests.checkAllDishesSumsWithAllConditions();
        rootPage.setRandomTipsOption();

        dishesBeforeAddingDiscount =
                rootPage.saveDishPricesInCollection
                        (allDishesInOrder,dishPriceWithDiscountSelector,dishPriceWithoutDiscountSelector,dishPriceRegex);

    }
    @Test
    @DisplayName("3. Добавляем скидку из заказа, и проверяем суммы")
    public void addDiscountAndCheckSums() {

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,discountAmount);
        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

    }
    @Test
    @DisplayName("4. Пытаемся оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @DisplayName("5. Проверяем как изменилась скидка, сверяем все позиции")
    public void matchDishesDiscountPriceAfterAddingDiscount() {

        dishesAfterAddingDiscount =
                rootPage.saveDishPricesInCollection
                        (allDishesInOrder,dishPriceWithDiscountSelector,dishPriceWithoutDiscountSelector,dishPriceRegex);

        rootPage.matchDishesDiscountPriceAfterAddingDiscount(dishesBeforeAddingDiscount,dishesAfterAddingDiscount);

    }

    @Test
    @DisplayName("6. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("7. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("8. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("9. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
