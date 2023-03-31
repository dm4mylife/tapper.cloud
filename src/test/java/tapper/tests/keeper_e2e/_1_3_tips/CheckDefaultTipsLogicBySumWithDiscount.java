package tapper.tests.keeper_e2e._1_3_tips;


import api.ApiRKeeper;
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

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.*;
import static data.Constants.RegexPattern.TapperTable.totalPayRegex;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;


@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckDefaultTipsLogicBySumWithDiscount extends BaseTest {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;


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

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,"10000");
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
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isSecondTipsOptionCorrect)
    void setScAndCheckTipsWith2ndOption() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseAllNonPaidDishes();
        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
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
        rootPage.refreshPage();

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

        allNonPaidAndNonDisabledDishes.asDynamicIterable().stream().forEach(element -> {

            rootPage.click(element.$(dishNameSelector));
            rootPage.isDefaultTipsBySumLogicCorrect();

        });

        rootPage.deactivateDivideCheckSliderIfActivated();

    }

    @Test
    @Order(14)
    @DisplayName(TapperTable.closedOrder)
    void payAndGoToAcquiringAgain() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid, apiUri);

    }

}
