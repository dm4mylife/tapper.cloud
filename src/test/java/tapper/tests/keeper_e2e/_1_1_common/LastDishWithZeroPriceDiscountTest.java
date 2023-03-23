package tapper.tests.keeper_e2e._1_1_common;


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
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;
import static data.AnnotationAndStepNaming.DisplayName.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.*;



@Epic("RKeeper")
@Feature("Общие")
@Story("Полная оплата. Стоимость последней позиции 0 рублей + скидка")
@DisplayName("Полная оплата. Стоимость последней позиции 0 рублей + скидка")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LastDishWithZeroPriceDiscountTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName(1.1 + createOrderInKeeper + isDishesCorrectInCashDeskAndTapperTable + " Добавляем блюдо с нулевой ценой")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 1);
        apiRKeeper.createDishObject(dishesForFillingOrder, ZERO_PRICE_DISH, 1);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT,
                TABLE_CODE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder, TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,"5000");

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);
        rootPage.isTableHasOrder();
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName(1.2 + isTotalPaySumCorrectTipsSc + setRandomTips)
    public void checkSumTipsSC() {

        $$(dishPriceWithDiscountSelector).filter(visible).shouldHave(size(1));

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.click(allNonPaidAndNonDisabledDishesName.first());
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName(1.3 + savePaymentData)
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName(1.4 + goToAcquiringAndPayOrder)
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName(1.5 + isPaymentCorrect)
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName(1.6 + isTelegramMessageCorrect)
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
