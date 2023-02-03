package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import tests.BaseTestTwoBrowsers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.Best2PayPage.payButton;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.DishList.paidDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;

@Order(53)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная полная оплата с 2х устройств")
@DisplayName("Одновременная полная оплата с 2х устройств")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _5_3_SimultaneouslyPartPayTest extends BaseTestTwoBrowsers {

    static String visit;
    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Disabled
    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), TapperTable.AUTO_API_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "6000"));

        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Выбираем рандомно блюда, проверяем все суммы и условия, сохраняем данные для след.теста")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
            rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

        });

        using(secondBrowser, () -> rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111));

        using(firstBrowser, () -> {


            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @DisplayName("1.3. Проверяем что у него блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.clickOnPaymentButton();
            rootPage.isElementVisibleDuringLongTime(dishesSumChangedHeading,5);
            paymentButton.shouldBe(disabled);

        });

    }

}
