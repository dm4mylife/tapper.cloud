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
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TwoBrowsers;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.SUM_CHANGED_ALERT_TEXT;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная попытка оплаты всего счета с 2х устройств. У второго юзера будет предупреждение что сумма изменилась")
@DisplayName("Одновременная попытка оплаты всего счета с 2х устройств. У второго юзера будет предупреждение что сумма изменилась")


@TestMethodOrder(MethodOrderer.DisplayName.class)
class SimultaneouslyTwoGuestFullPayTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static int amountDishesForFillingOrder = 6;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

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
    @DisplayName("1.3. Жмём кнопку Оплатить у обоих столов")
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> rootPage.clickOnPaymentButton());
        using(secondBrowser, () -> rootPage.clickOnPaymentButton());

    }

    @Test
    @DisplayName("1.4. Проверяем что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.isElementVisibleDuringLongTime(dishesSumChangedHeading, 7);
            paymentButton.shouldBe(disabled);
            dishesSumChangedHeading.shouldHave(visible, matchText(SUM_CHANGED_ALERT_TEXT));

        });

    }

    @Test
    @DisplayName("1.5. Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
