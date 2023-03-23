package tapper.tests.keeper_e2e._1_1_common;


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

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;


@Epic("RKeeper")
@Feature("Общие")
@Story("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("tapper - переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    static String guid;
    static int amountDishesForFillingOrder = 3;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Открытие пустого стола")
    public void openAndCheck() {

        apiRKeeper.isTableEmpty(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI);
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

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_111,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_111,
                TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("4. Обновляем страницу, проверяем что стол не пустой на кассе совпадают с позициями в таппере")
    public void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @DisplayName("5. Проверяем что позиции на кассе совпадают с позициями в таппере")
    public void isOrderInKeeperCorrectWithTapper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("6. Закрываем заказ")
    public void finishOrder() {

      apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,guid,AUTO_API_URI);

    }

}
