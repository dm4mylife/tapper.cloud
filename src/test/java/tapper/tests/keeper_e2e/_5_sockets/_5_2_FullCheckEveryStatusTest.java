package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;

@Order(52)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Полная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")
@DisplayName("Полная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_2_FullCheckEveryStatusTest extends BaseTest {

    static String visit;
    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями" +
            " в таппере и роверяем все суммы и условия у всех не оплаченных и не заблокированных блюдах")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "6000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111, COOKIE_GUEST_FIRST_USER, COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Проверяем все суммы и условия у всех не оплаченных и не заблокированных блюдах")
    public void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseAllNonPaidDishes();

    }

    @Test
    @DisplayName("1.3. Сохраняем данные для след и переходим в оплату")
    public void switchToAnotherUser() {
        chosenDishes = rootPage.getAllDishesAndSetCollection();
    }

    @Test
    @DisplayName("1.4. Переходим на второго гостя и проверяем что блюда заблокированы и оплачиваются")
    public void checkDisabledDishes() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);
        rootPage.checkIfDishesDisabledEarlier(chosenDishes);

    }

    @Test
    @DisplayName("1.5. Возвращаемся на первого гостя")
    public void switchBackTo1Guest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111, COOKIE_GUEST_FIRST_USER, COOKIE_SESSION_FIRST_USER);
        rootPage.cancelCertainAmountChosenDishes(3);
        chosenDishes = rootPage.getChosenDishesAndSetCollection();

    }

    @Test
    @DisplayName("1.6. Оплачиваем на эквайринге")
    public void savePaymentDataAndGoToAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

    }

    @Test
    @DisplayName("1.7. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {
        transactionId = nestedTests.acquiringPayment(totalPay);
    }

    @Test
    @DisplayName("1.8. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {
        nestedTests.checkPaymentAndB2pTransaction(orderType = "part", transactionId, paymentDataKeeper);
    }

    @Test
    @DisplayName("1.9. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("2.0. Переходим на первого гостя")
    public void returnFirstGuest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_FIRST_USER);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }


}
