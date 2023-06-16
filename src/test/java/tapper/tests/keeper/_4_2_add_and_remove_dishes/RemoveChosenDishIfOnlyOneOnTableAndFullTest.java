package tapper.tests.keeper._4_2_add_and_remove_dishes;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static api.ApiData.OrderData.TORT;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;


@Epic("RKeeper")
@Feature("Добавление и удаление позиций из заказа")
@Story("Удаление позиции которая выбрана на столе и в заказе остается только одна позиция c разделенным счетом")
@DisplayName("Удаление позиции которая выбрана на столе и в заказе остается только одна позиция c разделенным счетом")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RemoveChosenDishIfOnlyOneOnTableAndFullTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_444.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_444.tableCode;
    protected final String waiter = TableData.Keeper.Table_444.waiter;
    protected final String apiUri = TableData.Keeper.Table_444.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_444.tableUrl;
    protected final String tableId = TableData.Keeper.Table_444.tableId;

    static String guid;
    static String uni;
    static int amountDishesForFillingOrder = 1;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri,dishesForFillingOrder,tableUrl, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        uni = apiRKeeper.getUniFirstValueFromOrderInfo(tableId,apiUri);

    }
    @Test
    @Order(2)
    @DisplayName("Выбираем позицию которую затем удалим")
    void choseDish() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.choseFirstDish(allNonPaidAndNonDisabledDishesName);


    }
    @Test
    @Order(3)
    @DisplayName("Удаляем эту позицию на кассе")
    void deletePosition() {

        apiRKeeper.deletePosition(apiRKeeper.rqBodyDeletePosition(guid, uni, 1000));

    }

    @Test
    @Order(4)
    @DisplayName("Пытаемся снова оплатить и получаем ошибку изменения суммы")
    void checkChangedSumAfterDeleting() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем что заказ на столе не должен быть заблокирован к оплате")
    void savePaymentDataForAcquiring() {

        rootPage.isPaymentDisabled();

    }

}
