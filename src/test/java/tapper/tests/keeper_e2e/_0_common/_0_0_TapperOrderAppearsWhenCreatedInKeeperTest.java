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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsOrderPay;
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

    static String guid;
    static int amountDishesForFillingOrder = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие пустого стола")
    public void openAndCheck() {

        if (!apiRKeeper.isClosedOrder(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI)) {

            System.out.println("На кассе есть прошлый заказ, закрываем его");
            String guid = apiRKeeper.getGuidFromOrderInfo(TABLE_AUTO_111_ID,AUTO_API_URI);

            apiRKeeper.orderPay(rqParamsOrderPay(R_KEEPER_RESTAURANT, guid), AUTO_API_URI);

            boolean isOrderClosed = apiRKeeper.isClosedOrder(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI);

            Assertions.assertTrue(isOrderClosed, "Заказ не закрылся на кассе");
            System.out.println("\nЗаказ закрылся на кассе\n");

        }

        rootPage.openPage(STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("2. Проверка элементов при пустом столе")
    public void isEmptyTableCorrect() {

        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @DisplayName("3. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                TABLE_AUTO_111_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("4. Обновляем страницу, проверяем что стол не пустой на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isDishListNotEmptyAndVisible();

    }

    @Test
    @DisplayName("5. Проверяем что позиции на кассе совпадают с позициями в таппере")
    public void isOrderInKeeperCorrectWithTapper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("6. Закрываем заказ")
    public void finishOrder() {

        rootPage.closeOrderByAPI(guid,R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI);

    }

}
