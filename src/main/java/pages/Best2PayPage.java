package pages;

import com.codeborne.selenide.Condition;
import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;

import static constants.Constant.TestData.*;
import static constants.Selectors.Best2PayPage.*;


public class Best2PayPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Проверка что мы находимся на странице эквайринга")
    public void isTestBest2PayUrl() {
        baseActions.isTextContainsInURL(TEST_BEST2PAY_URL);
    }

    @Step("Проверка попапа ВПН")
    public void isVpnPopUpShown() {
        vpnPopup.shouldHave(Condition.cssValue("display", "flex"), Duration.ofSeconds(10));
    }

    @Step("Проверка попапа ВПН и что форма оплаты появилась")
    public void isPaymentContainerAndVpnShown() {

        baseActions.isElementVisibleDuringLongTime(paymentContainer, 10);
        isVpnPopUpShown();

    }

    @Step("Проверка что доступны 3 способа оплаты")
    public void checkPayMethods() {

        baseActions.isElementVisible(payMethodCard);
        baseActions.isElementVisible(payMethodYandexPay);
        baseActions.isElementVisible(payMethodSBP);

    }

    @Step("Ввод номера карты")
    public void typeCardNumber() {
        baseActions.sendKeys(cardNumber, TEST_PAYMENT_CARD_NUMBER);
    }

    @Step("Ввод даты истечения карты")
    public void typeDateExpire() {

        baseActions.sendKeys(dateExpire, TEST_PAYMENT_CARD_EXPIRE_MONTH);
        baseActions.sendKeys(dateExpire, TEST_PAYMENT_CARD_EXPIRE_YEAR);

    }

    @Step("Ввод CVV")
    public void typeCVV() {
        baseActions.sendKeys(cvv, TEST_PAYMENT_CARD_CVV);
    }

    @Step("Клик по кнопке отправить по email и ввод почты")
    public void typeEmail() {

        baseActions.click(sendCheckByEmail);
        baseActions.isElementVisible(email);
        baseActions.sendKeys(email, TEST_YANDEX_LOGIN_EMAIL);

    }

    @Step("Проверка что сумма 'Итого к оплате' в таппере сходится с суммой в эквайринге")
    public void isTotalPayInTapperMatchTotalPayB2B(double totalPayTapper) {

        double totalPayB2BDouble = baseActions.convertSelectorTextIntoDoubleByRgx(totalPayB2B, "\\s");

        Assertions.assertEquals(totalPayTapper, totalPayB2BDouble, 0.01);
        System.out.println("Сумма в тапере " + totalPayTapper + " сходится с суммой в эквайринге " + totalPayB2BDouble);

    }

    @Step("Клик по кнопке оплатить")
    public void clickPayButton() {
        baseActions.click(payButton);
    }

}
