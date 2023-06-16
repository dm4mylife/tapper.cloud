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
@Story("Одновременная частичная оплата с 2х устройств")
@DisplayName("Одновременная частичная оплата с 2х устройств")


@TestMethodOrder(MethodOrderer.DisplayName.class)
class SimultaneouslyTwoGuestPartPayTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishesByFirstGuest;
    static HashMap<Integer, Map<String, Double>> chosenDishesBySecondGuest;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 6;
    static int amountDishesToBeChosen = 2;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    void openTables() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда, проверяем все суммы и условия")
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishesByFirstGuest = rootPage.getChosenDishesAndSetCollection();

        });
    }

    @Test
    @DisplayName("1.4. Открываем второго гостя, проверяем что блюда в статусе Оплачиваются, которые выбрал первый гость")
    void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledEarlier(chosenDishesByFirstGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.5. Выбираем вторым гостем рандомные блюда")
    void chooseDishesBySecondGuest() {

        using(secondBrowser, () -> {

            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishesBySecondGuest = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("1.6. Проверяем у первого гостя что блюда в статусе Оплачиваются, которые выбрал второй гость")
    void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledEarlier(chosenDishesBySecondGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.7. Сохраняем данные по оплате первого гостя")
    void savePaymentData() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("1.8. Переходим на эквайринг первым гостем, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.9. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.0. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishesByFirstGuest);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("2.2. Сохраняем данные по оплате первого гостя")
    void savePaymentDataSecondGuest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("2.3. Переходим на эквайринг вторым гостем, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiringSecondGuest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPaymentSecondGuest() {

        using(secondBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.5. Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
