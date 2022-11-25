package pages.nestedTestsManager;

import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.*;
import static constants.Selectors.Best2PayPage.transaction_id;
import static constants.Selectors.RootPage.DishList.dishesSumChangedHeading;
import static constants.Selectors.RootPage.TipsAndCheck.totalPay;
import static constants.Selectors.YandexMail.dropdownListOrder;


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


}
