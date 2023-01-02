package tapper.tests.keeper_e2e._3_tips;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
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

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(6)
@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_6_TipsLogicBySumWith2GuestsTest extends BaseTest {

    static String visit;
    static String guid;
    static double tapperTotalPay;
    static double b2pTotalPay;

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "7000"));

    }

    @Test
    @DisplayName("1.2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.3. Проверка суммы, чаевых, сервисного сбора, нельзя поделиться счетом т.к. одно блюдо")
    public void checkSumTipsSC() {

        rootPage.disableDivideCheckSliderWithOneDish();

    }

    @Test
    @DisplayName("1.4. Проверяем что логика чаевых по сумме корректна,выбираем блюда гостем, проверяем чаевые")
    public void choseDishesAndCheckTips() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P();
        rootPageNestedTests.chooseCertainAmountDishes(3);
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P();

    }

    @Test
    @DisplayName("1.5. Делимся счётом со 2 юзером")
    public void divideCheck() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPage.setAnotherGuestCookie();

    }

    @Test
    @DisplayName("1.6. Проверяем что логика чаевых по сумме корректна,выбираем блюда вторым гостем, проверяем чаевые")
    public void choseDishesAndCheckTipsBy2ndGuest() {

        choseDishesAndCheckTips();
        rootPageNestedTests.cancelCertainAmountChosenDishes(3);
        Selenide.switchTo().window(0);

    }

    @Test
    @DisplayName("1.7. Закрываем заказ")
    public void payAndGoToAcquiringAgain() {

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isEmptyOrderAfterClosing();

    }

}
