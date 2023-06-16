package tapper_table.nestedTestsManager;

import api.ApiData;
import api.ApiIiko;
import api.ApiRKeeper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.RegexPattern.TapperTable.serviceChargeRegex;
import static data.Constants.RegexPattern.TapperTable.totalPayRegex;
import static data.Constants.TestData.TapperTable.SERVICE_CHARGE_PERCENT_FROM_TIPS;
import static data.Constants.TestData.TapperTable.SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM;
import static data.Constants.WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK;
import static data.selectors.TapperTable.Best2PayPage.paymentContainer;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.PayBlock.serviceChargeContainer;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalTipsSumInMiddle;


public class NestedTests extends RootPage {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    ApiIiko apiIiko = new ApiIiko();


    @Step("Переход в эквайринг, ввод данных, оплата и возврат на стол таппер")
    public String acquiringPayment(double totalPay) {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        return transactionId;

    }

    @Step("Переход в эквайринг, ввод данных, ожидание пока транзакция \"протухнет\"")
    public String goToAcquiringAndWaitTillTransactionExpired(double totalPay, int wait) {

        rootPageNestedTests.clickPayment();
        forceWait(wait);
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        return transactionId;

    }

    @Step("Проверка всего процесса оплаты, транзакции, ожидание пустого стола")
    public void checkPaymentAndB2pTransaction(String orderType, String transactionId,
                                              HashMap<String, String> paymentDataKeeper) {

        pagePreLoader.shouldNotBe(visible,Duration.ofSeconds(30));
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        if (orderType.equals("part"))
            apiRKeeper.isPrepaymentSuccess(transactionId,WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK);

        reviewPage.clickOnFinishButton();

        if (orderType.equals("full"))
            rootPage.isEmptyOrderAfterClosing();

    }


    public void payOrder(String tableId, String cashDeskType, String guid) {

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        HashMap<String, String> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        LinkedHashMap<String, String> tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, cashDeskType);
        String transactionId = acquiringPayment(totalPay);
        checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);
        matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg, "full");

    }

    @Step("Проверка суммы что суммы изменились после того как изменился заказ на кассе и в таппере нажали оплатить")
    public void checkIfSumsChangedAfterEditingOrder() {

        double totalPaySum = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
        rootPage.clickOnPaymentButton();

        dishesSumChangedHeading.shouldHave(visible).shouldHave(hidden,Duration.ofSeconds(8));
        pagePreLoader.shouldHave(hidden,Duration.ofSeconds(30));

        double totalPaySumAfterChanging = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        Assertions.assertNotEquals(totalPaySum, totalPaySumAfterChanging,
                "После изменения заказа на кассе, заказ в таппере не поменялся");

    }

    @Step("Проверяем установку чаевых по умолчанию по сумме, формирование СБ от чаевых," +
            " и корректность суммы оплаты в таппере и б2п")
    public void checkDefaultTipsBySumAndScLogicBySumAndB2P(double cleanDishesSum) {

        scrollTillBottom();

        rootPage.isDefaultTipsBySumLogicCorrect(cleanDishesSum);

       // if (discountField.exists())
           // cleanDishesSum += convertSelectorTextIntoDoubleByRgx(discountSum,discountInCheckRegex);

        double tipsSumInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
        double serviceChargeInField =
                rootPage.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, serviceChargeRegex);

        serviceChargeInField = updateDoubleByDecimalFormat(serviceChargeInField);
        double serviceChargeSumClear = updateDoubleByDecimalFormat
                (cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));
        double serviceChargeTipsClear = updateDoubleByDecimalFormat
                (tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));
        double cleanServiceCharge = serviceChargeSumClear + serviceChargeTipsClear;

        Assertions.assertEquals(cleanServiceCharge, serviceChargeInField, 0.1,
                "Сервисный сбор считается не корректно от суммы и чаевых");

        rootPage.deactivateServiceChargeIfActivated();

        totalPay.shouldBe(visible);

        double tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        checkTotalPayInB2P(tapperTotalPay);

        rootPage.activateServiceChargeIfDeactivated();
        totalPay.shouldBe(visible);

        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        checkTotalPayInB2P(tapperTotalPay);

    }


    public void checkTotalPayInB2P(double tapperTotalPay) {

        rootPageNestedTests.clickPayment();
        paymentContainer.shouldBe(exist,Duration.ofSeconds(300));

        double b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        returnToPreviousPage();
        rootPage.isTableHasOrder();

    }

    public String createAndFillOrder(int amountDishesForFillingOrder, String dish, String restaurant, String tableCode,
                                     String waiter, String apiUri, String tableId) {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, dish, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurant, tableCode, waiter, apiUri,
                dishesForFillingOrder, tableId);

        return apiRKeeper.getGuidFromCreateOrder(rs);

    }

    public String createAndFillOrderAndOpenTapperTable(int amountDishesForFillingOrder,String dish,
                                                       String restaurant, String tableCode, String waiter,
                                                       String apiUri, String tableUrl, String tableId) {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, dish, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurant, tableCode, waiter, apiUri,
                dishesForFillingOrder, tableId);

        rootPage.openNotEmptyTable(tableUrl);
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

        return apiRKeeper.getGuidFromCreateOrder(rs);

    }

    public void openEmptyTapperTable(String restaurantName, String tableId, String apiUri, String tableUrl) {

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openPage(tableUrl);

    }


    public void choseAllNonPaidDishesTipsSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();

    }

    public void choseAllNonPaidDishesOneByOneTipsSc() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseAllNonPaidDishes();
        scrollTillBottom();
        rootPage.setRandomTipsOption();
        rootPage.activateServiceChargeIfDeactivated();

    }

    public void choseAllNonPaidDishesNoTipsNoSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPageNestedTests.deactivateTipsAndDeactivateSc();

    }

    public void choseAllNonPaidDishesNoTipsSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPageNestedTests.deactivateTipsAndActivateSc();

    }

    public void choseAllNonPaidDishesTipsNoSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndDeactivateScIfActivated();

    }

    public void matchTgMsgDataAndTapperData(String guid, LinkedHashMap<String, String> tapperDataForTgMsg,
                                            String paymentType) {

        rootPage.matchTgMsgDataAndTapperData(rootPage.getPaymentTgMsgData(guid,paymentType), tapperDataForTgMsg);

    }

    public void clearTableAndOpenEmptyTable(String restaurantName, String tableId, String apiUri, String tableUrl) {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");
        rootPage.openPage(tableUrl);
        rootPage.skipStartScreenLogo();

    }

    public String createAndFillOrderAndOpenTapperTable(String tableId, String tableUrl, ApiData.IikoData.Dish dish, int amountDishesForFillingOrder) {

        apiIiko.closedOrderByApi(tableId);

        String orderId =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(orderId,dish.getId(),amountDishesForFillingOrder));

        rootPage.openNotEmptyTable(tableUrl);

        return orderId;

    }


}
