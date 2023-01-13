package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.emptyOrderHeading;

@Disabled
@Order(1)
@Epic("RKeeper")
@Feature("Общие")
@Story("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_1_TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    static String visit;
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие стола")
    public void openAndCheck() {

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @DisplayName("2. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));

    }

    @Test
    @DisplayName("3. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_3);
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

       rootPage.closeOrderByAPI(guid);

    }

}
