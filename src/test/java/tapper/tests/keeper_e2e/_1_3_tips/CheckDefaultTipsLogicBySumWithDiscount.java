package tapper.tests.keeper_e2e._1_3_tips;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;


@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа со скидкой, ввод кастомных чаевых, ошибки суммы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CheckDefaultTipsLogicBySumWithDiscount extends BaseTest {

    static String guid;
    static double tapperTotalPay;
    static double b2pTotalPay;
    static double discount;
    static int amountDishesForFillingOrder = 1;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.0. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_111,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,"10000");
        apiRKeeper.createDiscountByIdObject(discounts, DISCOUNT_BY_ID);

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора, нельзя поделиться счетом т.к. одно блюдо")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @DisplayName("1.3. Проверяем скидку")
    public void setScAndCheckTips() {

        discount = rootPageNestedTests.getTotalDiscount(TABLE_AUTO_111_ID);
        rootPageNestedTests.checkIsDiscountPresent(discount);

    }

    @Test
    @DisplayName("1.4. Добавляем еще одно блюдо в заказ")
    public void addDishes() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, BARNOE_PIVO, 2);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishes));
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        if (!divideCheckSliderActive.isDisplayed()) {
            rootPage.activateDivideCheckSliderIfDeactivated();
        }

    }

    @Test
    @DisplayName("1.5. Разделяем счёт чтобы выбрать позиции")
    public void activateDivideCheckSliderIfDeactivated() {

        rootPage.activateDivideCheckSliderIfDeactivated();

    }

    @Test
    @DisplayName("1.6. Проверяем вторую опцию чаевых")
    public void setScAndCheckTipsWith2ndOption() {

        rootPage.chooseAllNonPaidDishes();
        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @DisplayName("1.7. Добавляем еще одно блюдо в заказ")
    public void addDishesWith3rdOption() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, SOLYANKA, 5);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishes));
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        if (!divideCheckSliderActive.isDisplayed()) {
            rootPage.activateDivideCheckSliderIfDeactivated();
        }

    }

    @Test
    @DisplayName("1.8. Проверяем 3 опцию чаевых")
    public void setScAndCheckTipsWith3rdOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @DisplayName("1.9. Добавляем еще одно блюдо в заказ")
    public void addDishesWith4thOption() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, BARNOE_PIVO, 1);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishes));
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        if (!divideCheckSliderActive.isDisplayed()) {
            rootPage.activateDivideCheckSliderIfDeactivated();
        }

    }

    @Test
    @DisplayName("2.0. Проверяем 4 опцию чаевых")
    public void setScAndCheckTipsWith4thOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @DisplayName("2.1. Добавляем еще одно блюдо в заказ")
    public void addDishesWith5thOption() {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.createDishObject(dishes, SOLYANKA, 5);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishes));
        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        if (!divideCheckSliderActive.isDisplayed()) {
            rootPage.activateDivideCheckSliderIfDeactivated();
        }

    }

    @Test
    @DisplayName("2.2. Проверяем 5 опцию чаевых")
    public void setScAndCheckTipsWith5thOption() {

        setScAndCheckTipsWith2ndOption();

    }

    @Test
    @DisplayName("2.3. Установка кастомных чаевых и проверка суммы")
    public void setCustomTips() {

        rootPage.setCustomTips("534");
        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPage.showPaymentOptionsAndTapBar();

        rootPageNestedTests.clickPayment();
        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        rootPage.returnToPreviousPage();

    }

    @Test
    @DisplayName("2.4. Выбираем все блюда и по одному отщелкиваем, проверяя как выставляются чаевые")
    public void checkTipsLogicByRemovingPositions() {

        allNonPaidAndNonDisabledDishes.asDynamicIterable().stream().forEach(element -> {

           rootPage.click(element.$(dishNameSelector));
           rootPage.isDefaultTipsBySumLogicCorrect();

        });

    }

    @Test
    @DisplayName("2.5. Закрываем заказ")
    public void payAndGoToAcquiringAgain() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,guid,AUTO_API_URI);

    }

}
