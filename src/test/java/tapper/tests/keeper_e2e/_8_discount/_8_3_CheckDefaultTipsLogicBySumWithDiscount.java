package tapper.tests.keeper_e2e._8_discount;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;

@Order(5)
@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа, ввод кастомных чаевых, ошибки суммы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_3_CheckDefaultTipsLogicBySumWithDiscount extends BaseTest {

    static String visit;
    static String guid;
    static double tapperTotalPay;
    static double b2pTotalPay;
    static double discount;

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), AUTO_API_URI);
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));

        apiRKeeper.addDiscount(rqParamsAddCustomDiscount(R_KEEPER_RESTAURANT,guid, CUSTOM_DISCOUNT_ON_ORDER,"5000"), AUTO_API_URI);
        apiRKeeper.addDiscount(rqParamsAddDiscount(R_KEEPER_RESTAURANT,guid, DISCOUNT_ON_DISH), AUTO_API_URI);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.2. Определяем скидку")
    public void getTotalDiscount() {

        discount = rootPageNestedTests.getTotalDiscount(TABLE_AUTO_1_ID);
        rootPage.disableDivideCheckSliderWithOneDish();
    }

    @Test
    @DisplayName("1.3. Проверка суммы, чаевых, сервисного сбора, нельзя поделиться счетом т.к. одно блюдо")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkAllDishesSumsWithAllConditions(discount);
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);


    }

    @Test
    @DisplayName("1.4. Проверяем что логика чаевых по сумме корректна к минимальным чаевым")
    public void setScAndCheckTips() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditions(discount);

    }

    @Test
    @DisplayName("1.5. Добавляем еще одно блюдо в заказ")
    public void addDishes() {

        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, SOLYANKA, "5000"));
        rootPage.refreshPage();

    }

    @Test
    @DisplayName("1.6. Проверяем вторую опцию чаевых")
    public void setScAndCheckTipsWith2ndOption() {

        setScAndCheckTips();

    }

    @Test
    @DisplayName("1.7. Добавляем еще одно блюдо в заказ")
    public void addDishesWith3rdOption() {

        addDishes();

    }

    @Test
    @DisplayName("1.8. Проверяем 3 опцию чаевых")
    public void setScAndCheckTipsWith3rdOption() {

        setScAndCheckTips();

    }

    @Test
    @DisplayName("1.9. Добавляем еще одно блюдо в заказ")
    public void addDishesWith4thOption() {

        addDishes();

    }

    @Test
    @DisplayName("2.0. Проверяем 4 опцию чаевых")
    public void setScAndCheckTipsWith4thOption() {

        setScAndCheckTips();

    }

    @Test
    @DisplayName("2.1. Добавляем еще одно блюдо в заказ")
    public void addDishesWith5thOption() {

        addDishes();

    }

    @Test
    @DisplayName("2.2. Проверяем 5 опцию чаевых")
    public void setScAndCheckTipsWith5thOption() {

        setScAndCheckTips();

    }

    @Test
    @DisplayName("2.3. Установка кастомных чаевых и проверка суммы")
    public void setCustomTips() {

        rootPage.setCustomTips("534");
        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPageNestedTests.clickPayment();
        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");
        System.out.println("Сумма итого к оплате (с СБ) в таппере " + tapperTotalPay +
                " совпадает с суммой в б2п " + b2pTotalPay);

    }

    @Test
    @DisplayName("2.4. Выбираем все блюда и по одному отщелкиваем, проверяя как выставляются чаевые")
    public void checkTipsLogicByRemovingPositions() {

        for (int index = 0; index < allNonPaidAndNonDisabledDishes.size()-1; index++) {

            allNonPaidAndNonDisabledDishesName.get(index).click();
            rootPage.isDefaultTipsBySumLogicCorrect();

        }

        rootPage.deactivateDivideCheckSliderIfActivated();

    }

    @Test
    @DisplayName("2.5. Закрываем заказ")
    public void payAndGoToAcquiringAgain() {

        rootPageNestedTests.clickPayment();

        best2PayPageNestedTests.typeDataAndPay();
        best2PayPage.clickPayButton();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isEmptyOrderAfterClosing();

    }

}
