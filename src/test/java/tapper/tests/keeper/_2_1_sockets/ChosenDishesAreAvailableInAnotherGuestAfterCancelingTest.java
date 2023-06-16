package tapper.tests.keeper._2_1_sockets;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.TwoBrowsers;

import java.time.Duration;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Открытие стола у двух гостей, второй гость выбирает и отменяет позиции у первого не должно быть заблокировано" +
        " для оплаты")
@DisplayName("Открытие стола у двух гостей, второй гость выбирает и отменяет позиции у первого не должно быть " +
        "заблокировано для оплаты")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChosenDishesAreAvailableInAnotherGuestAfterCancelingTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 4;


    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(createOrderInKeeper + isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {


        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO, restaurantName, tableCode,
                waiter, apiUri, tableId);

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
    @DisplayName("Выбираем рандомно блюда")
    void chooseDishesByAnotherGuest() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);

        });

    }

    @Test
    @Order(4)
    @DisplayName("Отменяем их")
    void cancelCertainAmountChosenDishes() {

        using(firstBrowser, () -> rootPage.cancelCertainAmountChosenDishes(amountDishesToBeChosen));

    }

    @Test
    @Order(5)
    @DisplayName("Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    void switchToFirstGuest() {

        using(secondBrowser, () -> {

            rootPage.isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);
            allNonPaidAndNonDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldNotHave(disabled);

        });

    }

    @Test
    @Order(6)
    @DisplayName("Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid);

    }

}
