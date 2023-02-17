package tapper.tests.keeper_e2e._8_discount;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;

@Order(82)
@Epic("RKeeper")
@Feature("Скидка")
@Story("Удаление скидки из заказа, полная оплата")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_2_PartAndFullPayAfterRemovingDiscountTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String visit;
    static String guid;
    static String uni;
    static double discount;
    static String orderType;
    static int amountDishes = 1;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), AUTO_API_URI);

        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "2000"));
     //   apiRKeeper.createDiscount(rqParamsAddDiscount(R_KEEPER_RESTAURANT,guid, DISCOUNT_ON_DISH), AUTO_API_URI);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        discount = rootPageNestedTests.getTotalDiscount(TABLE_AUTO_111_ID);
        rootPageNestedTests.isDiscountCorrectOnTable(discount);
        rootPageNestedTests.checkAllDishesSumsWithAllConditions();

    }

    @Test
    @DisplayName("1.3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);

    }

    @Test
    @DisplayName("1.4. Удаляем скидку из заказа и проверяем суммы") //
    public void addDiscountAndCheckSums() {

        rootPage.returnToPreviousPage();

        Response rs = apiRKeeper.getOrderInfo(TABLE_AUTO_111_ID, AUTO_API_URI);
        guid = rs.jsonPath().getString("@attributes.guid");
        uni = rs.jsonPath().getString("Session[1].Discount['@attributes'].uni");
        System.out.println(uni + " uni");

        apiRKeeper.deleteDiscount(rqParamsDeleteDiscount(R_KEEPER_RESTAURANT,guid,uni), AUTO_API_URI);

        rootPage.refreshPage();
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные блюда для частичной оплаты")
    public void getRandomDishes() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.6. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiringAfterDeletedDiscount() {

        savePaymentDataForAcquiring();
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
    }

    @Test
    @DisplayName("1.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterDeletedDiscount() {

        reviewPageNestedTests.paymentCorrect(orderType = "part");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();
        rootPageNestedTests.checkTotalSumCorrectAfterRemovingDiscount();

    }

    @Test
    @DisplayName("1.8. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiringAfterPartialPayment() {


        savePaymentDataForAcquiring();
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("1.9. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentAfterPartialPayment() {

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();

    }

}
