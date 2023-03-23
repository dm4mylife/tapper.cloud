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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная попытка оплаты всего счета с 2х устройств. У второго юзера будет предупреждение что сумма изменилась")
@DisplayName("Одновременная попытка оплаты всего счета с 2х устройств. У второго юзера будет предупреждение что сумма изменилась")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SimultaneouslyTwoGuestFullPayTest extends BaseTestTwoBrowsers {

    static String guid;
    static int amountDishesForFillingOrder = 6;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    public void openTables() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isTableHasOrder();

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isTableHasOrder();

        });

    }

    @Test
    @DisplayName("1.3. Жмём кнопку Оплатить, переходим в эквайринг")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> rootPage.clickOnPaymentButton());

    }

    @Test
    @DisplayName("1.4. Проверяем что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.clickOnPaymentButton();
            rootPage.isElementVisibleDuringLongTime(dishesSumChangedHeading, 7);
            paymentButton.shouldBe(disabled);
            dishesSumChangedHeading.shouldHave(visible, matchText(SUM_CHANGED_ALERT_TEXT));

        });

    }

    @Test
    @DisplayName("1.5. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

    }

}
