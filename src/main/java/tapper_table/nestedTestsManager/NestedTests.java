package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;


import java.util.HashMap;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.*;
import static constants.SelectorsTapperTable.Best2PayPage.transaction_id;
import static constants.SelectorsTapperTable.RootPage.DishList.*;
import static constants.SelectorsTapperTable.RootPage.PayBlock.serviceCharge;
import static constants.SelectorsTapperTable.RootPage.TipsAndCheck.totalPay;
import static constants.SelectorsTapperTable.RootPage.TipsAndCheck.totalTipsSumInMiddle;
import static constants.SelectorsTapperTable.YandexMail.dropdownListOrder;


public class NestedTests extends RootPage {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Step("Оплачиваем эквайринг, проверяем процессы оплаты, сверяем транзакцию с б2п, оплата полная")
    public void payAcquiringMatchB2PFullPay() {

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        HashMap<String, Integer> paymentData = rootPage.savePaymentDataTapperForB2b();

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);

        String trans_id = transaction_id.getValue();

        best2PayPage.clickPayButton();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(trans_id, paymentData);

    }

    @Step("Оплачиваем эквайринг, проверяем процессы оплаты, сверяем транзакцию с б2п, оплата частичная")
    public void payAcquiringMatchB2PPartPay() {

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        HashMap<String, Integer> paymentData = rootPage.savePaymentDataTapperForB2b();

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);

        String trans_id = transaction_id.getValue();

        best2PayPage.clickPayButton();

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(trans_id, paymentData);

    }

    @Step("Проверяем чек на почте яндекса")
    public void isCheckAcquiringReceived() {

        openPage(YANDEX_MAIL_URL);
        sendKeys($("#passp-field-login"), TEST_YANDEX_LOGIN_EMAIL);
        click($("#passp:sign-in"));
        sendKeys($("#passp-field-passwd"), TEST_YANDEX_PASSWORD_MAIL);
        click($("#passp:sign-in"));
        click(dropdownListOrder);

    }

    @Step("Проверка суммы что суммы изменились после того как изменился заказ на кассе и в таппере нажали оплатить")
    public void checkIfSumsChangedAfterEditingOrder() {

        double totalPaySum = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        rootPage.clickOnPaymentButton();
        rootPage.isElementVisibleDuringLongTime(dishesSumChangedHeading, 5);
        dishesSumChangedHeading.shouldHave(Condition.text("Сумма заказа изменилась"));
        rootPage.forceWait(2000); // toDo меню не успевает обновится после ошибки изменения суммы оплаты
        double totalPaySumAfterChanging = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        System.out.println(totalPaySum + " сумма до изменения заказа");
        System.out.println(totalPaySumAfterChanging + " сумма после изменения заказа");
        Assertions.assertNotEquals(totalPaySum, totalPaySumAfterChanging,
                "После изменения заказа на кассе, заказ в таппере не поменялся");


    }


    @Step("Проверяем установку чаевых по умолчанию по сумме, формирование СБ от чаевых," +
            " и корректность суммы оплаты в таппере и б2п")
    public void checkDefaultTipsBySumAndScLogicBySumAndB2P(double tapperTotalPay, double b2pTotalPay) {

        if (divideCheckSliderInput.isSelected()) {

            divideCheckSlider.click();

        }

        rootPage.isDefaultTipsBySumLogicCorrect();

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        double tipsSumInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));

        double serviceChargeInField = rootPage.convertSelectorTextIntoDoubleByRgx(serviceCharge, "[^\\d\\.]+");
        serviceChargeInField = convertDouble(serviceChargeInField);

        double serviceChargeSumClear = convertDouble(cleanDishesSum * (SERVICE_PRICE_PERCENT_FROM_TOTAL_SUM / 100));
        System.out.println(serviceChargeSumClear + " сервисный сбор от суммы");

        double serviceChargeTipsClear = convertDouble(tipsSumInTheMiddle * (SERVICE_PRICE_PERCENT_FROM_TIPS / 100));
        System.out.println(serviceChargeTipsClear + " сервисный сбор от чаевых\n");

        double cleanServiceCharge = serviceChargeSumClear + serviceChargeTipsClear;

        Assertions.assertEquals(cleanServiceCharge, serviceChargeInField, 0.1,
                "Сервисный сбор считается не корректно");
        System.out.println("Сервисный сбор считается корректно от суммы и чаевых");

        rootPage.deactivateServiceChargeIfActivated();
        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

        rootPageNestedTests.clickPayment();
        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay,b2pTotalPay,
                "Сумма итого к оплате не совпадает с суммой в таппере");
        System.out.println("Сумма итого к оплате (с СБ) в таппере " + tapperTotalPay +
                " совпадает с суммой в б2п " + b2pTotalPay);

        Selenide.back();

        rootPage.activateServiceChargeIfDeactivated();
        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

        rootPageNestedTests.clickPayment();
        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay,b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");
        System.out.println("Сумма итого к оплате (бес СБ) в таппере " + tapperTotalPay +
                " совпадает с суммой в б2п " + b2pTotalPay);

        Selenide.back();

    }

}
