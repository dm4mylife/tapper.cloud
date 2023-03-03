package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
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
import static data.selectors.TapperTable.RootPage.DishList.allDisabledDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;

@Order(50)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Открытие стола у двух гостей, первый гость уходит в оплату, у второго должны быть блюда заблокированы." +
        "После первый гость выходит из оплаты, и у второго они должны разблокироваться")
@DisplayName("Открытие стола у двух гостей, первый гость уходит в оплату, у второго должны быть блюда заблокированы.\" +\n" +
        "        \"После первый гость выходит из оплаты, и у второго они должны разблокироваться")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_0_DisabledDishesWhenInPaymentTest extends BaseTestTwoBrowsers {

    static int amountDishesForFillingOrder = 6;
    static String guid;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
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
    @DisplayName("3. Переходим в оплату у первого гостя")
    public void chooseDishesByAnotherGuest() {

        using(firstBrowser, () -> {

            rootPageNestedTests.clickPayment();

        });

    }

    @Test
    @DisplayName("4. Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    public void switchToFirstGuest() {

        using(secondBrowser, () -> {

            allDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldHave(disabled);
            System.out.println("Блюда заблокированы, кнопка не активна для оплаты");

        });

    }

    @Test
    @DisplayName("5. Возвращаемся обратно на стол из оплаты, чтобы блюда не были заблокированы")
    public void backToTable() {

        using(firstBrowser, () -> {

            Selenide.back();
            rootPage.isDishListNotEmptyAndVisible();
            rootPage.cancelAllChosenDishes();

        });

    }

    @Test
    @DisplayName("6. Переключаемся на второго пользователя, проверяем что блюда не заблокированы")
    public void checkNonDisabledDishes() {

        using(secondBrowser, () -> {

            allNonPaidAndNonDisabledDishes.shouldHave(size(amountDishesForFillingOrder), Duration.ofSeconds(10));
            paymentButton.shouldNotHave(disabled);
            System.out.println("Блюда не заблокированы, кнопка активна для оплаты");

        });

    }


    @Test
    @DisplayName("7. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

    }

}
