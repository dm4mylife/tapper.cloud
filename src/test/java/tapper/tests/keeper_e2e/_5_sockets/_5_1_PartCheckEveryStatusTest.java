package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import data.Constants;
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
import static data.Constants.TestData.*;

@Order(51)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")
@DisplayName("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_1_PartCheckEveryStatusTest extends BaseTest {

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
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), TapperTable.AUTO_API_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "6000"));

        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
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
        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_SECOND_USER, TapperTable.COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("1.4. Проверяем что блюда заблокированы")
    public void checkDisabledDishes() {
        rootPage.checkIfDishesDisabledEarlier(chosenDishes);
    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {
        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
    }

    @Test
    @DisplayName("1.6. Сохраняем данные")
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

        telegramDataForTgMsg = rootPage.getTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

    }

    @Test
    @DisplayName("2.0. Переключаемся на второго гостя, проверяем что суммы оплачены")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_SECOND_USER, TapperTable.COOKIE_SESSION_SECOND_USER);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }

    @Test
    @DisplayName("2.1. Переключаемся на первого гостя, проверяем что суммы оплачены")
    public void switchTo1stdGuestAndCheckPaidDishes() {

        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
        rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

    }

    @Test
    @DisplayName("2.2. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrderByAPI(guid);

    }

}
