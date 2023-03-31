package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.TwoBrowsers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
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

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_222;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_222;
    protected final String tableId = TABLE_AUTO_222_ID;

    int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 4;

    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(createOrderInKeeper + isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

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

            allNonPaidAndNonDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldNotHave(disabled);

        });

    }

    @Test
    @Order(6)
    @DisplayName("Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid, apiUri);

    }

}
