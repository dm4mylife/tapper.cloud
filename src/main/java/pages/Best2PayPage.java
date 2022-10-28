package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import common.BaseActions;

import static constants.Constant.TestData.*;
import static constants.Selectors.Best2PayPage.*;
import static constants.Selectors.RootPage.paymentProcessContainer;


public class Best2PayPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    public void isTestBest2PayUrl() {

        baseActions.isTextContainsInURL(TEST_BEST2PAY_URL);


    }

    public void isVpnPopUpShown() {

        vpnPopup.shouldHave(Condition.cssValue("display","flex"));
    }

    public void isPaymentContainerAndVpnShown() {

        isVpnPopUpShown();
        baseActions.isElementVisibleDuringLongTime(paymentContainer,10);
    }

    public void typeCardNumber() {

        baseActions.sendHumanKeys(cardNumber,TEST_PAYMENT_CARD_NUMBER);

    }

    public void typeDateExpire() {

        baseActions.sendHumanKeys(dateExpire,TEST_PAYMENT_CARD_EXPIRE_MONTH);
        baseActions.sendHumanKeys(dateExpire,TEST_PAYMENT_CARD_EXPIRE_YEAR);

    }

    public void typeCVV() {

        baseActions.sendHumanKeys(cvv,TEST_PAYMENT_CARD_CVV);

    }

    public void typeEmail() {

        baseActions.click(sendCheckByEmail);
        baseActions.sendHumanKeys(email,TEST_EMAIL);

    }

    public void clickPayButton() {

        baseActions.click(payButton);

    }

    public void checkPayMethods() {

        baseActions.isElementVisible(payMethodCard);
        baseActions.isElementVisible(payMethodYandexPay);
        baseActions.isElementVisible(payMethodSBP);

    }


    public void checkPayMethodsAndTypeAllCreditCardData() {

        checkPayMethods();
        typeCardNumber();
        typeDateExpire();
        typeCVV();
        typeEmail();

    }


}
