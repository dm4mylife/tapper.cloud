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

@Order(55)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная частичная оплата с 2х устройств")
@DisplayName("Одновременная частичная оплата с 2х устройств")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_5_SimultaneouslyTwoGuestPartPayTest extends BaseTestTwoBrowsers {

    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishesByFirstGuest;
    static HashMap<Integer, Map<String, Double>> chosenDishesBySecondGuest;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 6;
    static int amountDishesToBeChosen = 2;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    public void openTables() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда, проверяем все суммы и условия")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishesByFirstGuest = rootPage.getChosenDishesAndSetCollection();

        });
    }

    @Test
    @DisplayName("1.4. Открываем второго гостя, проверяем что блюда в статусе Оплачиваются, которые выбрал первый гость")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.checkIfDishesDisabledEarlier(chosenDishesByFirstGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.5. Выбираем вторым гостем рандомные блюда")
    public void chooseDishesBySecondGuest() {

        using(secondBrowser, () -> {

            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishesBySecondGuest = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWaitingForSocketChangePositions(WAIT_FOR_SOCKETS_RECEIVED_REQUEST);

        });

    }

    @Test
    @DisplayName("1.6. Проверяем у первого гостя что блюда в статусе Оплачиваются, которые выбрал второй гость")
    public void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            rootPage.checkIfDishesDisabledEarlier(chosenDishesBySecondGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.7. Сохраняем данные по оплате первого гостя")
    public void savePaymentData() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

        });

    }

    @Test
    @DisplayName("1.8. Переходим на эквайринг первым гостем, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.9. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.0. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.1. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishesByFirstGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("2.2. Сохраняем данные по оплате первого гостя")
    public void savePaymentDataSecondGuest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

        });

    }

    @Test
    @DisplayName("2.3. Переходим на эквайринг вторым гостем, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiringSecondGuest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPaymentSecondGuest() {

        using(secondBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.5. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.6. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

    }

}
