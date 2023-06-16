package tapper.tests.keeper._1_4_zero_price_dish;


import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;

@Epic("RKeeper")
@Feature("Кейс с нулевыми ценами")
@Story("Удаление блюда когда на столе останется бесплатное блюдо с бесплатным модификатором")
@DisplayName("Удаление блюда когда на столе останется бесплатное блюдо с бесплатным модификатором")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FreeDishWithFreeModiTipsTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static String uni;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper)
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));}
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(ZERO_PRICE_DISH_WITH_MODI,1,
                        new ArrayList<>(){
                    {add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_ZERO_PRICE_DISH,1));}
                }));
            }
        };

        Response rs = rootPageNestedTests.createAndFillOrderOnlyWithModifiers(restaurantName, tableCode,waiter,
                apiUri,modifiers, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        uni = apiRKeeper.getUniFirstValueFromOrderInfo(tableId,apiUri);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Удаляем платное блюдо")
    void addOneMoreDishInOrder() {

        apiRKeeper.deletePosition(apiRKeeper.rqBodyDeletePosition(guid, uni, 1000));
        uni = apiRKeeper.getUniFirstValueFromOrderInfo(tableId,apiUri);

    }

    @Test
    @Order(3)
    @DisplayName("Пытаемся оплатить и получаем ошибку изменения суммы")
    void checkChangedSumAfterAdding() {

        rootPage.clickOnPaymentButton();

        dishesSumChangedHeading.shouldHave(visible).shouldHave(hidden, Duration.ofSeconds(8));
        pagePreLoader.shouldHave(hidden,Duration.ofSeconds(30));
        rootPage.isEmptyOrderAfterClosing();

    }
    @Test
    @Order(4)
    @DisplayName("Проверяем что стол пустой, бесплатное блюдо удаляется")
    void isEmptyOrderAfterClosing() {

        rootPage.isEmptyOrderAfterClosing();

    }
    @Test
    @Order(5)
    @DisplayName("Принудительно удаляем заказ на кассе")
    void closedOrderByApi() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 1);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishesForFillingOrder));

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
