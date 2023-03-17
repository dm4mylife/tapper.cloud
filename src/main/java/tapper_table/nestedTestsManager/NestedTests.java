package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.RegexPattern.TapperTable.totalPayRegex;
import static data.Constants.TestData.TapperTable.SERVICE_CHARGE_PERCENT_FROM_TIPS;
import static data.Constants.TestData.TapperTable.SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.Constants.WAIT_UNTIL_TRANSACTION_STILL_ALIVE;
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
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        forceWait(wait);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        return transactionId;

    }

    @Step("Проверка всего процесса оплаты, транзакции, ожидание пустого стола")
    public void checkPaymentAndB2pTransaction(String orderType, String transactionId, HashMap<String, Integer> paymentDataKeeper) {

        pagePreLoader.shouldNotBe(visible,Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        if (orderType.equals("part")) {

            apiRKeeper.isPrepaymentSuccess(transactionId);

        }

        reviewPage.clickOnFinishButton();

        if (orderType.equals("full")) {

            rootPage.isEmptyOrderAfterClosing();

        }

    }

    @Step("Проверка суммы что суммы изменились после того как изменился заказ на кассе и в таппере нажали оплатить")
    public void checkIfSumsChangedAfterEditingOrder() {

        double totalPaySum = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
        rootPage.clickOnPaymentButton();

        dishesSumChangedHeading.shouldHave(visible,Duration.ofSeconds(3));
        dishesSumChangedHeading.shouldHave(hidden,Duration.ofSeconds(8));
        pagePreLoader.shouldHave(hidden,Duration.ofSeconds(30));

        double totalPaySumAfterChanging = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        Assertions.assertNotEquals(totalPaySum, totalPaySumAfterChanging,
                "После изменения заказа на кассе, заказ в таппере не поменялся");

    }

    @Step("Проверяем установку чаевых по умолчанию по сумме, формирование СБ от чаевых," +
            " и корректность суммы оплаты в таппере и б2п")
    public void checkDefaultTipsBySumAndScLogicBySumAndB2P(double cleanDishesSum) {

        rootPage.isDefaultTipsBySumLogicCorrect();

        double tipsSumInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
        double serviceChargeInField =
                rootPage.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");

        serviceChargeInField = updateDoubleByDecimalFormat(serviceChargeInField);
        double serviceChargeSumClear = updateDoubleByDecimalFormat
                (cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));
        double serviceChargeTipsClear = updateDoubleByDecimalFormat
                (tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));
        double cleanServiceCharge = serviceChargeSumClear + serviceChargeTipsClear;

        Assertions.assertEquals(cleanServiceCharge, serviceChargeInField, 0.1,
                "Сервисный сбор считается не корректно от суммы и чаевых");

        rootPage.deactivateServiceChargeIfActivated();

        double tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPageNestedTests.clickPayment();

        double b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        returnToPreviousPage();

        rootPage.activateServiceChargeIfDeactivated();

        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPageNestedTests.clickPayment();

        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        returnToPreviousPage();

    }

}
