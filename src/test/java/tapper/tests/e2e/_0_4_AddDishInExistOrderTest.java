package tapper.tests.e2e;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.NestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(4)
@Epic("E2E - тесты (полные)")
@Feature("keeper - добавление и затем удаление позиции уже в созданный заказ' - обычные позиции - чай+сбор")
@DisplayName("keeper - добавление и затем удаление уже в созданный заказ - обычные позиции - чай+сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_4_AddDishInExistOrderTest extends BaseTest {

    static double totalPaySum;
    static String visit;
    static String guid;
    static String uni;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        Response rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));
        uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish['@attributes'].uni");

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditions();

    }

    @Test
    @DisplayName("4. Добавляем еще одно блюдо на кассе")
    public void addOneMoreDishInOrder() {

        apiRKeeper.addModificatorOrder(guid, GOVYADINA_PORTION, "1000", "1000118", "1");

    }

    @Test
    @DisplayName("5. Пытаемся оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }


    @Test
    @DisplayName("6. Удаляем одну позицию")
    public void deletePosition() {

        apiRKeeper.deletePosition(guid, uni, "1000");

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
