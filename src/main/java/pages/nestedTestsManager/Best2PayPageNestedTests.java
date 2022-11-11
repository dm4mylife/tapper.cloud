package pages.nestedTestsManager;

import io.qameta.allure.Step;
import pages.Best2PayPage;

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

}
