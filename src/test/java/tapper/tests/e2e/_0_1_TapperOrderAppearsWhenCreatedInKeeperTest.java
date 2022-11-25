package tapper.tests.e2e;


import api.ApiRKeeper;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(1)
@Epic("E2E - тесты (полные)")
@Feature("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_1_TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1. Открытие стола")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPage.isEmptyOrder();

    }

    @Test
    @DisplayName("2. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

    }

    @Test
    @DisplayName("3. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPage.isDishListNotEmptyAndVisible();

        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPage.isDishListNotEmptyAndVisible();

    }

    @Test
    @DisplayName("4. Закрываем заказ")
    public void finishOrder() {

        rootPageNestedTests.closeOrder();

    }

}
