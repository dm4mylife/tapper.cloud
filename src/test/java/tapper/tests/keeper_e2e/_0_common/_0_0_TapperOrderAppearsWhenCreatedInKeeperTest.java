package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;

@Order(0)
@Epic("RKeeper")
@Feature("Общие")
@Story("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_0_TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    static String visit;
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие стола")
    public void openAndCheck() {

        rootPage.openPage(STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @DisplayName("2. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), AUTO_API_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

    }

    @Test
    @DisplayName("3. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.openPage(STAGE_RKEEPER_TABLE_111);
        rootPage.isDishListNotEmptyAndVisible();
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("4. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {
        rootPage.isDishListNotEmptyAndVisible();
    }

    @Test
    @DisplayName("5. Закрываем заказ")
    public void finishOrder() {
       rootPage.closeOrderByAPI(guid);
    }

}
