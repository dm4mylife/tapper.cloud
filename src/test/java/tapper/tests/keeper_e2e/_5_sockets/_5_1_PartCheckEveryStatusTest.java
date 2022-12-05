package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.Best2PayPage.transaction_id;

@Order(51)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_1_PartCheckEveryStatusTest extends BaseTest {

    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "6000"));

    }

    @Test
    @DisplayName("1.1. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
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
        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPage.setAnotherGuestCookie();

    }

    @Test
    @DisplayName("1.4. Проверяем что блюда заблокированы")
    public void checkDisabledDishes() {

        rootPage.checkIfDishesDisabledEarlier(chosenDishes);

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {

        rootPage.switchOnAnotherGuest(0);

    }

    @Test
    @DisplayName("1.6. Переключаемся на первого гостя")
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

        rootPage.switchOnAnotherGuest(1);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }

    @Test
    @DisplayName("2.0. Переключаемся на первого гостя, проверяем что суммы оплачены")
    public void switchTo1stdGuestAndCheckPaidDishes() {

        rootPage.switchOnAnotherGuest(0);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("2.1. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
