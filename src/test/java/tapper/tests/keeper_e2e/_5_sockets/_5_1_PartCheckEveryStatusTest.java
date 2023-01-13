package tapper.tests.keeper_e2e._5_sockets;


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
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestData.COOKIE_SESSION_SECOND_USER;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;

@Order(51)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_1_PartCheckEveryStatusTest extends BaseTest {

    static String visit;
    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "6000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Выбираем рандомно блюда, проверяем все суммы и условия, сохраняем данные для след. теста")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(3);

    }

    @Test
    @DisplayName("1.3. Открываем новый стол и меняем гостя")
    public void switchToAnotherUser() {

        chosenDishes = rootPage.getChosenDishesAndSetCollection();
        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("1.4. Проверяем что блюда заблокированы")
    public void checkDisabledDishes() {

        rootPage.checkIfDishesDisabledEarlier(chosenDishes);

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);

    }

    @Test
    @DisplayName("1.6. Сохраняем данные")
    public void savePaymentDataAndGoToAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPage.clickOnPaymentButton();

    }

    @Test
    @DisplayName("1.7. Переходим на эквайринг, оплачиваем")
    public void payOnAcquiring() {

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("1.8. Проверяем статус оплаты")
    public void checkPaymentStatus() {

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.9. Переключаемся на второго гостя, проверяем что суммы оплачены")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }

    @Test
    @DisplayName("2.0. Переключаемся на первого гостя, проверяем что суммы оплачены")
    public void switchTo1stdGuestAndCheckPaidDishes() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }

    @Test
    @DisplayName("2.1. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrderByAPI(guid);

    }

}
