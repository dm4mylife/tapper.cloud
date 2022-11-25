package tapper.tests.e2e;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.Best2PayPage.transaction_id;

@Order(21)
@Epic("E2E - тесты (полные)")
@Feature("keeper - частичная оплата - обычные позиции - чай+сбор")
@DisplayName("keeper - частичная оплата - обычные позиции - чай+сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _2_1_PartPayTipSсTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentData;
    static String transactionId;
    static String visit;
    static String guid;
    static String uni;
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        visit = rsCreateOrder.jsonPath().getString("result.visit");

        Response rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        guid = rsCreateOrder.jsonPath().getString("result.guid");
        uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish[\"@attributes\"].uni");

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия, оплачиваем")
    public void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmountWithTipsWithSC(3);

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentData = rootPage.savePaymentDataTapperForB2b();
        rootPageNestedTests.clickPayment();

    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentData);
        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("7. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
