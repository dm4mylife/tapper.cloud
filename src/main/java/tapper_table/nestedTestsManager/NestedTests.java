package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static constants.Constant.TestData.SERVICE_CHARGE_PERCENT_FROM_TIPS;
import static constants.Constant.TestData.SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.dishesSumChangedHeading;
import static constants.selectors.TapperTableSelectors.RootPage.PayBlock.serviceChargeContainer;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.totalPay;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.totalTipsSumInMiddle;



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

    @Step("Проверка всего процесса оплаты, транзакции, ожидание пустого стола")
    public void checkPaymentAndB2pTransaction(String orderType, String transactionId, HashMap<String, Integer> paymentDataKeeper) {

        System.out.println(orderType + " orderType");
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        if (orderType.equals("part")) {

            apiRKeeper.isPrepaymentSuccess(transactionId);
            System.out.println("Предоплата прошла по кассе");

        }

        reviewPage.clickOnFinishButton();

        if (orderType.equals("full")) {

            rootPage.isEmptyOrderAfterClosing();

        }

    }

    @Step("Проверка суммы что суммы изменились после того как изменился заказ на кассе и в таппере нажали оплатить")
    public void checkIfSumsChangedAfterEditingOrder() {

        double totalPaySum = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        rootPage.clickOnPaymentButton();
        rootPage.isElementVisibleDuringLongTime(dishesSumChangedHeading, 5);
        dishesSumChangedHeading.shouldHave(Condition.text("Состав заказа изменился"));
        rootPage.forceWait(2500); // toDo меню не успевает обновится после ошибки изменения суммы оплаты
        double totalPaySumAfterChanging = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        System.out.println(totalPaySum + " сумма до изменения заказа");
        System.out.println(totalPaySumAfterChanging + " сумма после изменения заказа");
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
        System.out.println(serviceChargeInField + " сервисный сбор в контейнере");
        serviceChargeInField = convertDouble(serviceChargeInField);
        double serviceChargeSumClear = convertDouble
                (cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));
        double serviceChargeTipsClear = convertDouble
                (tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));
        double cleanServiceCharge = serviceChargeSumClear + serviceChargeTipsClear;

        System.out.println(serviceChargeSumClear + " сервисный сбор от суммы");
        System.out.println(serviceChargeTipsClear + " сервисный сбор от чаевых\n");

        Assertions.assertEquals(cleanServiceCharge, serviceChargeInField, 0.1,
                "Сервисный сбор считается не корректно от суммы и чаевых");
        System.out.println("\nСервисный сбор считается корректно от суммы и чаевых\n");

        rootPage.deactivateServiceChargeIfActivated();

        double tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        System.out.println(tapperTotalPay + " таппер без СБ");

        rootPageNestedTests.clickPayment();

        double b2pTotalPay = best2PayPage.getPaymentAmount();
        System.out.println(b2pTotalPay + " б2п");

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay,
                "Сумма итого к оплате не совпадает с суммой в таппере");
        System.out.println("\nСумма итого к оплате (с СБ) в таппере " + tapperTotalPay +
                " совпадает с суммой в б2п " + b2pTotalPay + "\n");

        returnToPreviousPage();

        rootPage.activateServiceChargeIfDeactivated();

        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        System.out.println(tapperTotalPay + " сумма итого к оплате в Таппере вместе с СБ\n");

        rootPageNestedTests.clickPayment();

        b2pTotalPay = best2PayPage.getPaymentAmount();
        System.out.println(b2pTotalPay + " сумма итого к оплате в б2п\n");

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");
        System.out.println("\nСумма итого к оплате (бес СБ) в таппере " + tapperTotalPay +
                " совпадает с суммой в б2п " + b2pTotalPay + "\n");

        returnToPreviousPage();

    }

}
