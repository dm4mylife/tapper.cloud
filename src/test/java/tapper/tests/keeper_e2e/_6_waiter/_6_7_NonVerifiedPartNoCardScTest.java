package tapper.tests.keeper_e2e._6_waiter;


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

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestData.COOKIE_SESSION_SECOND_USER;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;

@Order(67)
@Epic("RKeeper")
@Feature("Официант")
@Story("Официант не верифицирован, без привязанной карты, частичная оплата  +СБ")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _6_7_NonVerifiedPartNoCardScTest extends BaseTest {

    static String visit;
    static String guid;
    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static int amountDishes = 3;
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_IRONMAN_NON_VERIFIED_NON_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);
        rootPage.checkIsNoTipsElementsIfNonVerifiedNonCard();

    }

    @Test
    @DisplayName("3. Включаем сервисный сбор")
    public void activateServiceChargeIfDeactivated() {

        rootPage.activateServiceChargeIfDeactivated();

    }

    @Test
    @DisplayName("3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

    }

    @Test
    @DisplayName("4. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("6. Делимся ссылкой и оплачиваем остальную часть заказа")
    public void clearDataAndChoseAgain() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);
        rootPageNestedTests.closeOrderByAPI(guid);

    }

}
