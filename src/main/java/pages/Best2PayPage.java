package pages;

import com.codeborne.selenide.Condition;
import common.BaseActions;
import constants.Selectors;
import io.qameta.allure.Step;
import org.junit.Assert;

import java.time.Duration;

import static constants.Constant.TestData.*;
import static constants.Selectors.Best2PayPage.*;
import static constants.Selectors.RootPage.TipsAndCheck.totalPay;


public class Best2PayPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Проверка что мы находимся на странице эквайринга")
    public void isTestBest2PayUrl() {
        baseActions.isTextContainsInURL(TEST_BEST2PAY_URL);
    }

    @Step("Проверка попапа ВПН")
    public void isVpnPopUpShown() {
        vpnPopup.shouldHave(Condition.cssValue("display","flex"), Duration.ofSeconds(10));
    }

    @Step("Проверка попапа ВПН и что форма оплаты появилась")
    public void isPaymentContainerAndVpnShown() {

        isVpnPopUpShown();
        baseActions.isElementVisibleDuringLongTime(paymentContainer,10);
    }

    @Step("Проверка что доступны 3 способа оплаты")
    public void checkPayMethods() {

        baseActions.isElementVisible(payMethodCard);
        baseActions.isElementVisible(payMethodYandexPay);
        baseActions.isElementVisible(payMethodSBP);

    }

    @Step("Ввод номера карты")
    public void typeCardNumber() {
        baseActions.sendHumanKeys(cardNumber,TEST_PAYMENT_CARD_NUMBER);
    }

    @Step("Ввод даты истечения карты")
    public void typeDateExpire() {

        baseActions.sendHumanKeys(dateExpire,TEST_PAYMENT_CARD_EXPIRE_MONTH);
        baseActions.sendHumanKeys(dateExpire,TEST_PAYMENT_CARD_EXPIRE_YEAR);

    }

    @Step("Ввод CVC")
    public void typeCVС() {
        baseActions.sendHumanKeys(cvv,TEST_PAYMENT_CARD_CVV);
    }

    @Step("Клик по кнопке отправить по email и ввод почты")
    public void typeEmail() {

        baseActions.click(sendCheckByEmail);
        baseActions.isElementVisible(email);
        baseActions.sendHumanKeys(email,TEST_EMAIL);


    }

    @Step("Проверка что сумма 'Итого к оплате' в таппере сходится с суммой в эквайринге")
    public void isTotalPayInTapperMatchTotalPayB2B(double totalPayTapper) {

        double totalPayB2BDouble = baseActions.convertSelectorTextIntoDoubleByRgx(totalPayB2B,"\\s");
        System.out.println(totalPayB2BDouble + " total b2b");

        Assert.assertEquals(totalPayTapper,totalPayB2BDouble, 0.01);
        System.out.println("Сумма в тапере " + totalPayTapper + " сходится с суммой в эквайринге " + totalPayB2BDouble);

    }

    @Step("Клик по кнопке оплатить")
    public void clickPayButton() {
        baseActions.click(payButton);
    }


}
