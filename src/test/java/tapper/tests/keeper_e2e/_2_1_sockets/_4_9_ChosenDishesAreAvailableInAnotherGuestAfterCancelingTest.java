package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTestTwoBrowsers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;

@Order(49)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Открытие стола у двух гостей, второй гость выбирает и отменяет позиции у первого не должно быть заблокировано для оплаты")
@DisplayName("Открытие стола у двух гостей, второй гость выбирает и отменяет позиции у первого не должно быть заблокировано для оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _4_9_ChosenDishesAreAvailableInAnotherGuestAfterCancelingTest extends BaseTestTwoBrowsers {

    static int amountDishesToBeChosen = 3;
    static int amountDishesForFillingOrder = 6;
    static String guid;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("2. Открываем стол на двух разных устройствах, проверяем что не пустые")
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
    @DisplayName("3. Выбираем рандомно блюда")
    public void chooseDishesByAnotherGuest() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);

        });

    }

    @Test
    @DisplayName("4. Отменяем их")
    public void cancelCertainAmountChosenDishes() {

        using(firstBrowser, () -> rootPage.cancelCertainAmountChosenDishes(amountDishesToBeChosen));

    }

    @Test
    @DisplayName("5. Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    public void switchToFirstGuest() {

        using(secondBrowser, () -> {

            allNonPaidAndNonDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldNotHave(disabled);
            System.out.println("Блюда не заблокированы, кнопка активна для оплаты");

        });

    }

    @Test
    @DisplayName("6. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

    }

}
