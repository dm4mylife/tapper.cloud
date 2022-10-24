package pages;

import common.BaseActions;

import static constants.Constant.TestData.*;
import static constants.Selectors.Best2PayPage.*;


public class Best2PayPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    public void isTestBest2PayUrl() {

        baseActions.isTextContainsInURL(TEST_BEST2PAY_URL);
    }

    public void isPaymentContainerShown() {
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

        baseActions.sendHumanKeys(email,TEST_EMAIL);

    }

    public void clickPayButton() {

        baseActions.click(payButton);

    }

}
