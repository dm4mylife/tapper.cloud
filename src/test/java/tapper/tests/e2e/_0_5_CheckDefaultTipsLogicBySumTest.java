package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.RootPage;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;

@Order(5)
@Epic("E2E - тесты (полные)")
@Feature("keeper - проверяем логику установки дефолтных чаевых по сумме заказа, сброс чаевых")
@DisplayName("keeper - проверяем логику установки дефолтных чаевых по сумме заказа, сброс чаевых")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_5_CheckDefaultTipsLogicBySumTest extends BaseTest {

    static String visit;
    static String guid;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "1000"));

    }

    @Test
    @DisplayName("1.2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.3. Проверка суммы, чаевых, сервисного сбора, нельзя поделиться счетом т.к. одно блюдо")
    public void checkSumTipsSC() {

        rootPage.disableDivideCheckSliderWithOneDish();

    }

    @Test
    @DisplayName("1.4. Проверяем что логика чаевых по сумме корректна к минимальным чаевым")
    public void isDefaultTipsBySumLogicCorrect() {

        rootPage.isDefaultTipsBySumLogicCorrect();

    }

    @Test
    @DisplayName("1.5. Добавляем еще одно блюдо в заказ")
    public void addDishes() {

        apiRKeeper.addModificatorOrder(guid, LIMONAD_MOD, "1000", "1000112", "1");
        Selenide.refresh();
        rootPage.forceWait(1500);
    }

    @Test
    @DisplayName("1.6. Проверяем что логика чаевых по сумме корректна к 25%")
    public void isTipsBySumLogicCorrect() {

        isDefaultTipsBySumLogicCorrect();

    }

    @Test
    @DisplayName("1.7. Добавляем еще одно блюдо в заказ")
    public void addDishes2() {

        addDishes();

    }

    @Test
    @DisplayName("1.8. Проверяем что логика чаевых по сумме корректна к 20%")
    public void isTipsBySumLogicCorrect2() {

        isDefaultTipsBySumLogicCorrect();

    }

    @Test
    @DisplayName("1.9. Добавляем еще одно блюдо в заказ")
    public void addDishes3() {

        addDishes();

    }

    @Test
    @DisplayName("2. Проверяем что логика чаевых по сумме корректна к 15%")
    public void isTipsBySumLogicCorrectAgain3() {

        isDefaultTipsBySumLogicCorrect();

    }

    @Test
    @DisplayName("2.1. Добавляем еще одно блюдо в заказ")
    public void addDishes4() {

        apiRKeeper.addModificatorOrder(guid, LIMONAD_MOD, "3000", "1000112", "1");
        Selenide.refresh();
        rootPage.forceWait(1500);

    }

    @Test
    @DisplayName("2.2. Проверяем что логика чаевых по сумме корректна к 10%")
    public void isTipsBySumLogicCorrectAgain4() {

        isDefaultTipsBySumLogicCorrect();

    }

    @Test
    @DisplayName("2.3. Проверка сброса чаевых")
    public void resetTipsCorrect() {

        rootPage.resetTips();

    }

    @Test
    @DisplayName("2.9. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
