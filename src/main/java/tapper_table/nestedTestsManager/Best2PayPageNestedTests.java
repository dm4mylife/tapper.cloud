package tapper_table.nestedTestsManager;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import tapper_table.Best2PayPage;

import java.time.Duration;

import static data.selectors.TapperTable.Best2PayPage.paymentContainer;

public class Best2PayPageNestedTests extends Best2PayPage {

    Best2PayPage best2PayPage = new Best2PayPage();


    @Step("Проверка перехода на б2п, общей суммы, способов оплаты, ввод данных для оплаты")
    public void checkPayMethodsAndTypeAllCreditCardData(double totalPay) {

        paymentContainer.shouldBe(Condition.exist, Duration.ofSeconds(300));
        best2PayPage.isTotalPayInTapperMatchTotalPayB2B(totalPay);
        typeDataAndPay();

    }

    @Step("Ввод данных и оплата")
    public void typeDataAndPay() {

        best2PayPage.checkPayMethods();
        best2PayPage.typeCardNumber();
        best2PayPage.typeDateExpire();
        best2PayPage.typeCVV();
        best2PayPage.typeEmail();

    }
    @Step("Ввод данных и оплата")
    public void typeDataAndPay(int timeout) {

        best2PayPage.checkPayMethods();
        forceWait(timeout);
        best2PayPage.typeCardNumber();
        best2PayPage.typeDateExpire();
        best2PayPage.typeCVV();
        best2PayPage.typeEmail();

    }

}
