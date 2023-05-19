package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TwoBrowsers;

import java.time.Duration;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.selectors.TapperTable.Best2PayPage.paymentContainer;
import static data.selectors.TapperTable.RootPage.DishList.allDisabledDishes;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Открытие стола у двух гостей, первый гость уходит в оплату, у второго должны быть блюда заблокированы." +
        "После первый гость выходит из оплаты, и у второго они должны разблокироваться")
@DisplayName("Открытие стола у двух гостей, первый гость уходит в оплату, у второго должны быть блюда заблокированы." +
        "После первый гость выходит из оплаты, и у второго они должны разблокироваться")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisabledDishesWhenInPaymentTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    int amountDishesForFillingOrder = 6;
    static String guid;
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
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
    @DisplayName("Переходим в оплату у первого гостя")
    void chooseDishesByAnotherGuest() {

        using(firstBrowser, () -> {

            rootPageNestedTests.clickPayment();
            paymentContainer.shouldBe(Condition.exist, Duration.ofSeconds(300));

        });

    }

    @Test
    @Order(4)
    @DisplayName("Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    void switchToFirstGuest() {

        using(secondBrowser, () -> {

            rootPage.isElementsCollectionVisible(allDisabledDishes,10);
            allDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldHave(disabled);

        });

    }

    @Test
    @Order(5)
    @DisplayName("Возвращаемся обратно на стол из оплаты, чтобы блюда не были заблокированы")
    void backToTable() {

        using(firstBrowser, () -> {

            Selenide.back();
            rootPage.isTableHasOrder();
            rootPage.isElementsCollectionVisible(allNonPaidAndNonDisabledDishes);
            rootPage.cancelAllChosenDishes();

        });

    }

    @Test
    @Order(6)
    @DisplayName("Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    void checkNonDisabledDishes() {

        using(secondBrowser, () -> {

            allNonPaidAndNonDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldNotHave(disabled);

        });

    }


    @Test
    @Order(7)
    @DisplayName(TapperTable.closedOrder)
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
