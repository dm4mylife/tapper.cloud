package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(1)
@Epic("E2E - тесты (полные)")
@Feature("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_1_TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие стола")
    public void openAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @DisplayName("2. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

    }

    @Test
    @DisplayName("3. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
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
