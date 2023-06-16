package tapper.tests.keeper._1_3_tips;


import api.ApiRKeeper;
import common.BaseActions;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.RegexPattern.TapperTable.totalPayRegex;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.DishList.dishNameSelector;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;


@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CheckDefaultTipsLogicBySumWithDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static double tapperTotalPay;
    int amountDishesForFillingOrder = 1;
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,"5000");
        apiRKeeper.createDiscountByIdObject(discounts, DISCOUNT_BY_ID);

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName(TapperTable.isDefaultTipsLogicCorrect)
    void setScAndCheckTips() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.addOneMoreDishToOrder)
    void addDishes() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, SOLYANKA, 3);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishes));
        rootPage.refreshTableWithOrder();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isSecondTipsOptionCorrect)
    void setScAndCheckTipsWith2ndOption() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseAllNonPaidDishes();
        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrderIgnoreDiscount();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.addOneMoreDishToOrder)
    void addDishesWith3rdOption() {

        addDishes();

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.isThirdTTipsOptionCorrect)
    void setScAndCheckTipsWith3rdOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.addOneMoreDishToOrder)
    void addDishesWith4thOption() {

        addDishes();

    }

    @Test
    @Order(9)
    @DisplayName(TapperTable.isFourthTipsOptionCorrect)
    void setScAndCheckTipsWith4thOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @Order(10)
    @DisplayName(TapperTable.addOneMoreDishToOrder)
    void addDishesWith5thOption() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, SOLYANKA, 5);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishes));
        rootPage.refreshTableWithOrder();

    }

    @Test
    @Order(11)
    @DisplayName(TapperTable.isFifthTipsOptionCorrect)
    void setScAndCheckTipsWith5thOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @Order(12)
    @DisplayName(TapperTable.setCustomTips)
    void setCustomTips() {

        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(100, 200)));
        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        nestedTests.checkTotalPayInB2P(tapperTotalPay);

    }

    @Test
    @Order(13)
    @DisplayName("Выбираем все блюда и по одному отщелкиваем, проверяя как выставляются чаевые")
    void checkTipsLogicByRemovingPositions() {

        rootPage.hidePaymentOptionsAndTapBar();

        allNonPaidAndNonDisabledDishes.asDynamicIterable().stream().forEach(element -> {

            BaseActions.click(element.$(dishNameSelector));
            double cleanDishesSum = rootPage.countOnlyAllChosenWithFullPriceDishesDivided();
            rootPage.isDefaultTipsBySumLogicCorrect(cleanDishesSum);

        });

    }

    @Test
    @Order(14)
    @DisplayName(TapperTable.closedOrder)
    void payAndGoToAcquiringAgain() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid);

    }

}
