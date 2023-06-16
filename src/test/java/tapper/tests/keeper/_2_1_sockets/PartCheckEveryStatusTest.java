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
import static data.selectors.TapperTable.RootPage.DishList.allDishesPayedStatuses;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")
@DisplayName("Частичная оплата на 1-м устройстве, позиции в статусе 'Оплачиваются', 'Оплачено' на 2-м устройстве")


@TestMethodOrder(MethodOrderer.DisplayName.class)
class PartCheckEveryStatusTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static int amountDishesToBeChosen = 3;
    static int amountDishesForFillingOrder = 6;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и проверка что позиции на кассе совпадают с позициями в таппере")
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
    @DisplayName("1.3. Выбираем рандомно блюда у первого гостя, проверяем все суммы и условия")
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("1.4. Проверяем у второго гостя, что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledEarlier(chosenDishes);

        });

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя")
    void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("1.6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        using(firstBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen);
            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);

        });

    }

    @Test
    @DisplayName("1.9. Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
