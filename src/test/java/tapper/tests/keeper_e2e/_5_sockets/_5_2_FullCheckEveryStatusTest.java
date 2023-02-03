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
import tests.BaseTestTwoBrowsers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;

@Order(51)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Полная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', и затем пустой стол у второго устройства")
@DisplayName("Полная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', и затем пустой стол у второго устройства")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_2_FullCheckEveryStatusTest extends BaseTestTwoBrowsers {

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
    @Disabled
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
    @DisplayName("1.2. Выбираем рандомно блюда, проверяем все суммы и условия, сохраняем данные для след.теста")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
            rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

        });

        using(secondBrowser, () -> rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111));

        using(firstBrowser, () -> {

            rootPageNestedTests.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseAllNonPaidDishes();
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWait(1500);
        });

    }

    @Test
    @DisplayName("1.3. Проверяем что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> rootPage.checkIfDishesDisabledEarlier(chosenDishes));

    }

    @Test
    @DisplayName("1.4. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

        });

    }

    @Test
    @DisplayName("1.5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.7. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.8. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.isEmptyOrderAfterClosing();

        });

    }

}
