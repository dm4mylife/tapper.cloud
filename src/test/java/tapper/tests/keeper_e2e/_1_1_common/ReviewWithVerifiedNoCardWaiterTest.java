package tapper.tests.keeper_e2e._1_1_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import tests.ThirdTableData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.visible;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.*;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.AdminPersonalAccount.TERMINATOR_WAITER;
import static data.Constants.TestData.TapperTable.UNKNOWN_WAITER;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;

@Epic("RKeeper")
@Feature("Общие")
@Story("Отзыв если официант верифицирован но без привязанной карты")
@DisplayName("Отзыв если официант верифицирован но без привязанной карты")

@ThirdTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewWithVerifiedNoCardWaiterTest extends BaseTest {

    ThirdTableData annotation = ReviewWithVerifiedNoCardWaiterTest.class.getAnnotation(ThirdTableData.class);

    protected final String restaurantName = annotation.restaurantName();
    protected final String tableCode = annotation.tableCode();
    protected final String waiter = WAITER_TERMINATOR_VERIFIED_NON_CARD;
    protected final String apiUri = annotation.apiUri();
    protected final String tableUrl = annotation.tableUrl();
    protected final String tableId = annotation.tableId();

    static String guid;
    static double totalPay;
    static String orderType = "part";
    String reviewType = "positive";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String tapperTable;
    static String transactionId;
    String waiterName = TERMINATOR_WAITER;
    static ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    static int amountDishesForFillingOrder = 6;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @Order(1)
    @DisplayName(createOrderInKeeper)
    void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri, dishesForFillingOrder, tableUrl, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(2)
    @DisplayName(isTotalPaySumCorrectNoTipsSc)
    void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmountVerifiedNonCard(2);
        rootPage.deactivateServiceChargeIfActivated();

    }

    @Test
    @Order(3)
    @DisplayName(savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);
        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber, tableNumberRegex);

    }

    @Test
    @Order(4)
    @DisplayName(goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName(isPaymentCorrect)
    void checkPayment() {

        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName(setPositiveReview)
    void reviewCorrectPositive() {

        reviewPageNestedTests.reviewCorrectPositiveWithFewOptions();

    }

    @Test
    @Order(7)
    @DisplayName(isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        tapperDataForTgMsg = reviewPageNestedTests.saveReviewData(tapperTable, waiterName, reviewType);
        telegramDataForTgMsg = rootPageNestedTests.getReviewTgMsgData(tapperTable);

        Assertions.assertEquals(tapperDataForTgMsg, telegramDataForTgMsg);

    }

}
