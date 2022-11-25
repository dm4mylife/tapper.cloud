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
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.Best2PayPage.transaction_id;

@Order(2)
@Epic("E2E - тесты (полные)")
@Feature("keeper - полная оплата по кнопке 'Оплатить' - позиции с модификаторами - чай+сбор")
@DisplayName("keeper - полная оплата по кнопке 'Оплатить' - позиции с модификаторами - чай+сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_2_ModificatorTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String visit;
    static String guid;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        visit = rsGetOrder.jsonPath().getString("result.visit");
        guid = rsGetOrder.jsonPath().getString("result.guid");

        apiRKeeper.addModificatorOrder(guid, LIMONAD_MOD, "1000", "1000111", "2");
        apiRKeeper.addModificatorOrder(guid, LIMONAD_MOD, "1000", "1000112", "2");
        apiRKeeper.addModificatorOrder(guid, GOVYADINA_PORTION, "3000", "1000117", "3");
        apiRKeeper.addModificatorOrder(guid, GOVYADINA_PORTION, "1000", "1000118", "1");

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditions();
        rootPage.isModificatorTextCorrect();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
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

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("7. Закрываем заказ")
    public void finishOrder() {

        reviewPage.clickOnFinishButton();

    }


}
