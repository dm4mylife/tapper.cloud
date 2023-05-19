package tapper.tests.iiko_e2e._7_1_modifiers;


import api.ApiIiko;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TwoBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.IikoData.Dish.*;
import static com.codeborne.selenide.Selenide.using;
import static data.selectors.TapperTable.RootPage.DishList.allDishesDisabledStatuses;
import static data.selectors.TapperTable.RootPage.DishList.allDishesPayedStatuses;

@Epic("Iiko")
@Feature("Модификаторы")
@Story("Частичная оплата позиций с модификаторами с 1-го устройства и полная оплата со 2-го")
@DisplayName("Частичная оплата позиций с модификаторами с 1-го устройства и полная оплата со 2-го")


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartAndFullPayTwoGuestTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_222.tableUrl;
    protected final String tableId = TableData.Iiko.Table_222.tableId;

    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesToBeChosen = 1;

    RootPage rootPage = new RootPage();
    ApiIiko apiIiko = new ApiIiko();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName("Создание заказа в iiko и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_TOMATO_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_ONION_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_BACON_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, BURGER_CHEESE_PAID_NOT_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, BURGER.getId(), 1, dishesForFillingOrder));

        dishesForFillingOrder.clear();

        apiIiko.createModificatorObject
                (dishesForFillingOrder, HOT_DOG_GORCHIZA_FREE_NECESSARY_MODIFIER.getId(), 1);
        apiIiko.createModificatorObject
                (dishesForFillingOrder, HOT_DOG_SOUS_FREE_NECESSARY_MODIFIER.getId(), 1);

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, HOT_DOG.getId(), 1, dishesForFillingOrder));

    }

    @Test
    @Order(2)
    @DisplayName("Открываем стол на двух разных устройствах, проверяем что не пустые")
    void openTables() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем рандомно блюда у первого гостя, сохраняем данные для след.теста. Сбрасываем чаевые")
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.checkTipsAfterReset();

        });
    }

    @Test
    @Order(4)
    @DisplayName("Проверяем у второго гостя что блюда в статусе Оплачиваются, которые первый гость выбрал")
    void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledEarlier(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @Order(5)
    @DisplayName("Переключаемся на первого гостя, сохраняем платежные данные")
    void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

        });

    }

    @Test
    @Order(6)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @Order(8)
    @DisplayName("Проверка сообщения в телеграмме")
    void clearDataAndChoseAgain() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @Order(9)
    @DisplayName("Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();
            rootPage.deactivateDivideCheckSliderIfActivated();

        });

    }

    @Test
    @Order(10)
    @DisplayName("Сохраняем платежные данные второго гостя")
    void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

        });

    }

    @Test
    @Order(11)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring2Guest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @Order(12)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment2Guest() {

        using(secondBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper));

    }

    @Test
    @Order(13)
    @DisplayName("Проверка сообщения в телеграмме")
    void clearDataAndChoseAgain2Guest() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @Order(14)
    @DisplayName("Проверяем у первого гостя что у него пустой стол")
    void switchTo2GuestAndCheckPaidDishes() {

        using(secondBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }
    @Test
    @Order(15)
    @DisplayName("Проверяем у второго гостя что у него пустой стол")
    void switchTo1GuestAndCheckPaidDishes() {

        using(firstBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }


}
