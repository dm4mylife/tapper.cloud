package tapper_table.nestedTestsManager;

import io.qameta.allure.Step;
import tapper_table.Best2PayPage;

public class Best2PayPageNestedTests extends Best2PayPage {

    Best2PayPage best2PayPage = new Best2PayPage();

    @Step("Проверка общей суммы, способов оплаты, ввод данных")
    public void checkPayMethodsAndTypeAllCreditCardData(double totalPay) {

        best2PayPage.isTotalPayInTapperMatchTotalPayB2B(totalPay);
        best2PayPage.checkPayMethods();
        best2PayPage.typeCardNumber();
        best2PayPage.typeDateExpire();
        best2PayPage.typeCVV();
        best2PayPage.typeEmail();

    }

    @Step("Ввод данных и оплатить")
    public void typeDataAndPay() {

        best2PayPage.checkPayMethods();
        best2PayPage.typeCardNumber();
        best2PayPage.typeDateExpire();
        best2PayPage.typeCVV();
        best2PayPage.typeEmail();
        best2PayPage.clickPayButton();

    }

}
