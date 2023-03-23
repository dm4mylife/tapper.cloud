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
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Проверка на деактивацию кнопки оплатить, на втором устройстве")
@DisplayName("Проверка на деактивацию кнопки оплатить, на втором устройстве")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DisabledPaymentTwoGuestTest extends BaseTestTwoBrowsers {

    static String guid;
    static int amountDishesForFillingOrder = 4;
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
            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.isPaymentDisabled();
            rootPage.deactivateDivideCheckSliderIfActivated();
            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда у первого гостя, сохраняем данные для след.теста")
    public void chooseDishesAndCheckAfterDivided() {

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isTableHasOrder();
            rootPage.isPaymentDisabled();

            apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid,AUTO_API_URI);

        });

    }

}
