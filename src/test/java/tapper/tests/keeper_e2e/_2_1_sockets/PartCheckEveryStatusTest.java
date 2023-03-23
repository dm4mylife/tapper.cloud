package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTestTwoBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_SOCKETS_RECEIVED_REQUEST;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")
@DisplayName("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class PartCheckEveryStatusTest extends BaseTestTwoBrowsers {


    static String guid;
    static int amountDishesToBeChosen = 3;
    static int amountDishesForFillingOrder = 6;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    public void openTables() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isTableHasOrder();

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isTableHasOrder();

        });

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда у первого гостя, проверяем все суммы и условия")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWaitingForSocketChangePositions(WAIT_FOR_SOCKETS_RECEIVED_REQUEST);

        });

    }

    @Test
    @DisplayName("1.4. Проверяем у второго гостя, что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> rootPage.checkIfDishesDisabledEarlier(chosenDishes));

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя")
    public void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

        });

    }

    @Test
    @DisplayName("1.6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        using(firstBrowser, () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.9. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes));

    }

    @Test
    @DisplayName("2.0. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

    }

}
