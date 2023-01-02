package tapper.tests.keeper_e2e._7_modificator;


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
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;

@Order(71)
@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Платные модификаторы - частичная оплата")


@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _7_1_PaidPartPayTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static HashMap<Integer, Map<String, Double>> orderInKeeper;
    static String transactionId;
    static int amountDishes = 3;
    static String visit;
    static String guid;

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);

        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        apiRKeeper.fillOrderWithAllModiDishes(guid,API_TEST_URI);

        Response rsOrderInfo = apiRKeeper.getOrderInfo(TABLE_3_ID, API_STAGE_URI);

        orderInKeeper = rootPageNestedTests.saveOrderDataWithAllModi(rsOrderInfo);

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

        rootPage.isModificatorTextCorrect();
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();


    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("7. Делимся ссылкой и оплачиваем остальную часть заказа")
    public void clearDataAndChoseAgain() {

        rootPage.clearAllSiteData();
        savePaymentDataForAcquiring();

    }

    @Test
    @DisplayName("8. Закрываем заказ")
    public void payAndGoToAcquiringAgain() {

        rootPageNestedTests.closeOrder();

    }

}
