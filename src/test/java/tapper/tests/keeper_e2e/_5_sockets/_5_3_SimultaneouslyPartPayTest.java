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
import static constants.selectors.TapperTableSelectors.RootPage.DishList.paidDishes;

@Order(53)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная частичная оплата с 2х устройств")
@DisplayName("Одновременная частичная оплата с 2х устройств")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_3_SimultaneouslyPartPayTest extends BaseTest {

    static String visit;
    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes1stGuest;
    static HashMap<Integer, Map<String, Double>> chosenDishes2ndGuest;
    static double totalPay1Guest;
    static String orderType;
    static HashMap<String, Integer> paymentDataKeeper1Guest;
    static double totalPay2Guest;
    static HashMap<String, Integer> paymentDataKeeper2Guest;
    static String transactionId;
    int amountDishes = 1;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "3000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Выбираем рандомные блюда")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.3. Открываем стол у второго гостя")
    public void switchToAnotherUser() {

        chosenDishes1stGuest = rootPage.getChosenDishesAndSetCollection();
        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("1.4. Проверяем что блюда заблокированы от первого гостя. Выбираем блюда чтобы проверить что у первого гостя они будут заблокированы")
    public void checkDisabledDishes() {

        rootPage.checkIfDishesDisabledEarlier(chosenDishes1stGuest);
        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);

    }

    @Test
    @DisplayName("1.5. Сохраняем данные второго гостя")
    public void savePayData2Guest() {

        chosenDishes2ndGuest = rootPage.getChosenDishesAndSetCollection();

    }

    @Test
    @DisplayName("1.6. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);

    }

    @Test
    @DisplayName("1.7. Проверяем что заблокированы блюда")
    public void checkDisabledDishesBy2Guest() {

        rootPage.checkIfDishesDisabledEarlier(chosenDishes2ndGuest);

    }

    @Test
    @DisplayName("1.8. Производим оплату, сверяем суммы и транзакции у первого гостя")
    public void savePaymentDataAndGoToAcquiring() {

        totalPay1Guest = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper1Guest = rootPage.savePaymentDataTapperForB2b();
        rootPage.clickOnPaymentButton();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay1Guest);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        reviewPageNestedTests.paymentCorrect(orderType = "part");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper1Guest);

    }

    @Test
    @DisplayName("1.9. Переключаемся на второго гостя")
    public void switchBackTo2Guest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("2.0. Производим оплату, сверяем суммы и транзакции у второго гостя")
    public void savePaymentDataAndGoToAcquiring2ndGuest() {

        totalPay2Guest = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper2Guest = rootPage.savePaymentDataTapperForB2b();
        rootPage.clickOnPaymentButton();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay2Guest);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        reviewPageNestedTests.paymentCorrect(orderType = "part");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper2Guest);

    }

    @Test
    @DisplayName("2.1. Сверяем количество оплаченных блюд")
    public void payOnAcquiring() {

        reviewPage.clickOnFinishButton();

        Assertions.assertEquals(paidDishes.size(), amountDishes + amountDishes,
                "Не совпадает количество оплаченных блюд");
        System.out.println("Количество оплаченных блюд совпадает");

    }

    @Test
    @DisplayName("2.2. Закрываем заказ, очищаем кассу")
    public void closeOrder() {
        rootPageNestedTests.closeOrderByAPI(guid);
    }

}
