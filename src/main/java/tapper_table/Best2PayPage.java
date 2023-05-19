package tapper_table;

import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import static data.Constants.TestData.Best2Pay;
import static data.Constants.TestData.Best2Pay.TEST_PAYMENT_CARD_CVV;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.selectors.TapperTable.Best2PayPage.*;


public class Best2PayPage extends BaseActions {

    BaseActions baseActions = new BaseActions();

    @Step("Проверка что доступны 3 способа оплаты")
    public void checkPayMethods() {

        baseActions.isElementVisible(payMethodCard);
        baseActions.isElementVisible(payMethodYandexPay);
        baseActions.isElementVisible(payMethodSBP);

    }

    @Step("Ввод номера карты")
    public void typeCardNumber() {

        baseActions.sendKeys(cardNumber, Best2Pay.TEST_PAYMENT_CARD_NUMBER);

    }

    @Step("Ввод даты истечения карты")
    public void typeDateExpire() {

        baseActions.sendKeys(dateExpire, Best2Pay.TEST_PAYMENT_CARD_EXPIRE_MONTH);
        baseActions.sendKeys(dateExpire, Best2Pay.TEST_PAYMENT_CARD_EXPIRE_YEAR);

    }

    @Step("Ввод CVV")
    public void typeCVV() {

        baseActions.sendKeys(cvv, TEST_PAYMENT_CARD_CVV);

    }

    @Step("Клик по кнопке отправить по email и ввод почты")
    public void typeEmail() {

        click(sendCheckByEmail);

        if(email.isDisplayed()) {

            baseActions.isElementVisible(email);
            baseActions.sendKeys(email, TEST_YANDEX_LOGIN_EMAIL);

        }

    }

    @Step("Проверка что сумма 'Итого к оплате' в таппере сходится с суммой в эквайринге")
    public void isTotalPayInTapperMatchTotalPayB2B(double totalPayTapper) {

        double totalPayB2BDouble = baseActions.convertSelectorTextIntoDoubleByRgx(totalPayB2B, "\\s");

        Assertions.assertEquals(totalPayTapper, totalPayB2BDouble, 0.1,"Сумма в тапере не сходится с б2п");

    }

    @Step("Клик по кнопке оплатить")
    public void clickPayButton() {
        click(payButton);
    }

    @Step("Забрать итоговую сумму для оплаты со страницы транзакции")
    public double getPaymentAmount() {
        return baseActions.convertSelectorTextIntoDoubleByRgx(totalPayB2B, "\\s");
    }

}
