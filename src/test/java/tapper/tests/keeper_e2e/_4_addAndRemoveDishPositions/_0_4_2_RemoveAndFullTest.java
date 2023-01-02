package tapper.tests.keeper_e2e._4_addAndRemoveDishPositions;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(42)
@Epic("RKeeper")
@Feature("Добавление и удаление позиций из заказа")
@Story("Удаление позиции, полная оплата")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_4_2_RemoveAndFullTest extends BaseTest {


    static String visit;
    static String guid;
    static String uni;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        Response rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));
        uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish['@attributes'].uni");

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("6. Удаляем одну позицию")
    public void deletePosition() {

        apiRKeeper.deletePosition(rqParamsDeletePosition(R_KEEPER_RESTAURANT,guid,uni,1000),API_STAGE_URI);

    }

    @Test
    @DisplayName("7. Пытаемся снова оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterDeleting() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }


    @Test
    @DisplayName("8. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
