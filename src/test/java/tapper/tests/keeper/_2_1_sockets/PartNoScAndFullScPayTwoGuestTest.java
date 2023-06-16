package tapper.tests.keeper._2_1_sockets;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TwoBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Selenide.using;
import static data.selectors.TapperTable.RootPage.DishList.allDishesDisabledStatuses;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата с 1-го устройства с комиссией со 2-го без комиссии")
@DisplayName("Частичная оплата с 1-го устройства с комиссией со 2-го без комиссии")


@TestMethodOrder(MethodOrderer.DisplayName.class)
class PartNoScAndFullScPayTwoGuestTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 6;
    static int amountDishesToBeChosen = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    public void openTables() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда у первого гостя, сохраняем данные для след.теста. Включаем сервисный сбор")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.scrollTillBottom();
            rootPage.activateServiceChargeIfDeactivated();

        });
    }

    @Test
    @DisplayName("1.4. Проверяем у второго гостя что блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledEarlier(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя, сохраняем платежные данные")
    public void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

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

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.9. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено. Выключаем сервисный сбор")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();
            rootPage.deactivateDivideCheckSliderIfActivated();
            rootPage.scrollTillBottom();
            rootPage.deactivateServiceChargeIfActivated();

        });

    }

    @Test
    @DisplayName("2.0. Сохраняем платежные данные второго гостя")
    public void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

        });

    }

    @Test
    @DisplayName("2.1. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring2Guest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.2. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment2Guest() {

        using(secondBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.3. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain2Guest() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,"full");
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.4. Проверяем у первого гостя что у него пустой стол")
    public void switchTo2GuestAndCheckPaidDishes() {

        using(secondBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }
    @Test
    @DisplayName("2.5. Проверяем у второго гостя что у него пустой стол")
    public void switchTo1GuestAndCheckPaidDishes() {

        using(firstBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }

}
