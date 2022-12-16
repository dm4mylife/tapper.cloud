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
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.TapperTableSelectors.Best2PayPage.transaction_id;

@Order(52)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Полная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_2_FullCheckEveryStatusTest extends BaseTest {

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

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Проверяем все суммы и условия у всех не оплаченных и не заблокированных блюдах")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseAllDishesWithTipsWithSCNotDivided();

    }

    @Test
    @DisplayName("1.3. Сохраняем данные для след и переходим в оплату")
    public void switchToAnotherUser() {

        chosenDishes = rootPage.getAllDishesAndSetCollection();
        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPage.clickOnPaymentButton();

    }

    @Test
    @DisplayName("1.4. Переходим на второго гостя и проверяем что блюда заблокированы и оплачиваются")
    public void checkDisabledDishes() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPage.setAnotherGuestCookie();

        rootPage.checkIfDishesDisabledEarlier(chosenDishes);

    }

    @Test
    @DisplayName("1.5. Возвращаемся на первого гостя")
    public void switchBackTo1Guest() {

        rootPage.switchTab(0);

    }

    @Test
    @DisplayName("1.6. Оплачиваем на эквайринге")
    public void savePaymentDataAndGoToAcquiring() {

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("1.7. Проверяем статус оплаты")
    public void checkPaymentStatus() {

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.8. Переключаемся на второго гостя, проверяем что суммы оплачены и пустой заказ")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        rootPage.switchTab(1);
        rootPage.isEmptyOrderAfterClosing();

    }

}
