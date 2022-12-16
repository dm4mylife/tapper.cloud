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
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.TapperTableSelectors.RootPage.TipsAndCheck.totalPay;

@Order(5)
@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа, ввод кастомных чаевых, ошибки суммы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_5_CheckDefaultTipsLogicBySumTest extends BaseTest {

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

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));

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
    @DisplayName("1.4. Проверяем что логика чаевых по сумме корректна к минимальным чаевым")
    public void setScAndCheckTips() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(tapperTotalPay, b2pTotalPay);

    }

    @Test
    @DisplayName("1.5. Добавляем еще одно блюдо в заказ")
    public void addDishes() {

        apiRKeeper.addModificatorOrder(guid, LIMONAD, "1000", "1000112", "1");

        //apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));

        Selenide.refresh();


    }

    @Test
    @DisplayName("1.6. Проверяем вторую опцию чаевых")
    public void setScAndCheckTipsWith2ndOption() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(tapperTotalPay, b2pTotalPay);

    }

    @Test
    @DisplayName("1.7. Добавляем еще одно блюдо в заказ")
    public void addDishesWith3rdOption() {

        addDishes();

    }

    @Test
    @DisplayName("1.8. Проверяем 3 опцию чаевых")
    public void setScAndCheckTipsWith3rdOption() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(tapperTotalPay, b2pTotalPay);

    }

    @Test
    @DisplayName("1.9. Добавляем еще одно блюдо в заказ")
    public void addDishesWith4thOption() {

        addDishes();

    }

    @Test
    @DisplayName("2.0. Проверяем 4 опцию чаевых")
    public void setScAndCheckTipsWith4thOption() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(tapperTotalPay, b2pTotalPay);

    }

    @Test
    @DisplayName("2.1. Добавляем еще одно блюдо в заказ")
    public void addDishesWith5thOption() {

        addDishes();

    }

    @Test
    @DisplayName("2.2. Проверяем 5 опцию чаевых")
    public void setScAndCheckTipsWith5thOption() {

        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(tapperTotalPay, b2pTotalPay);

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
    @DisplayName("2.4. Закрываем заказ")
    public void payAndGoToAcquiringAgain() {

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isEmptyOrderAfterClosing();

    }

}
