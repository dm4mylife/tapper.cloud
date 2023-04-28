package tapper.tests.keeper_e2e._1_1_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.OrderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;


@Epic("RKeeper")
@Feature("Общие")
@Story("Переход на пустой стол, создание заказа на кассе, появление заказа в таппере")
@DisplayName("Переход на пустой стол, создание заказа на кассе, появление заказа в таппере")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TapperOrderAppearsWhenCreatedInKeeperTest extends BaseTest {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

    static String guid;
    static int amountDishesForFillingOrder = 3;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.openEmptyTapperTable)
    public void openAndCheck() {

      nestedTests.clearTableAndOpenEmptyTable(restaurantName,tableId,apiUri,tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Проверка элементов при пустом столе")
    public void isEmptyTableCorrect() {

        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.createOrderInKeeper)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder,BARNOE_PIVO,
                restaurantName,tableCode,waiter,apiUri,tableUrl,tableId);

    }

    @Test
    @Order(4)
    @DisplayName("Обновляем страницу, проверяем что стол не пустой на кассе совпадают с позициями в таппере")
    void refreshAndCheck() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем что позиции на кассе совпадают с позициями в таппере")
    void isOrderInKeeperCorrectWithTapper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.closedOrder)
    void finishOrder() {

      apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
