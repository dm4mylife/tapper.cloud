package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.divideCheckSlider;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.tips20;

@Disabled
@Order(5)
@Epic("RKeeper")
@Feature("Общие")
@Story("keeper - проверка что установленные чаевые у одного юзера не сбросятся если другой юзер будет выбирать позиции")
@DisplayName("keeper - проверка что установленные чаевые у одного юзера не сбросятся если другой юзер будет выбирать позиции")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_5_NotChangingTipsInAnotherUser extends BaseTest {

    static String visit;
    static String guid;
    static int dishesAmountToBeChosen = 2;
    static HashMap<String, Double> sumsInfoFirstUser;
    static HashMap<String, Double> sumsInfoSecondUser;

    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        Response rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        visit = rsGetOrder.jsonPath().getString("result.visit");
        guid = rsGetOrder.jsonPath().getString("result.guid");

        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "4000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);

    }

    @Test
    @DisplayName("1.2. Выбираем рандомные блюда")
    public void choseDishes() {

        rootPage.click(divideCheckSlider);
        rootPage.forceWait(1000);
        rootPage.chooseCertainAmountDishes(dishesAmountToBeChosen);
        rootPage.setCertainTipsOption(tips20);

    }

    @Test
    @DisplayName("1.3. Сохраняем информацию по 1 гостю")
    public void saveSumsFirstUser() {

        sumsInfoFirstUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoFirstUser + " сейвим 1 гостя");

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные блюда у 2 гостя")
    public void choseDishesAndCheckSumsSecondUser() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);

        rootPage.click(divideCheckSlider);
        rootPage.forceWait(1000);
        rootPage.chooseCertainAmountDishes(dishesAmountToBeChosen);
        rootPage.setCertainTipsOption(tips20);

    }

    @Test
    @DisplayName("1.6. Сохраняем информацию по 2 гостю")
    public void saveSumsSecondUser() {

        sumsInfoSecondUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoSecondUser + " сейвим 2 гостя");

    }

    @Test
    @DisplayName("1.7. Переходим к 1 гостю, и проверяем что чаевые не сбились")
    public void returnFirstUserAndMatchSums() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);

        HashMap<String, Double> currentSumsInfoFirstUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoFirstUser, sumsInfoFirstUser,
                "Сумма и чаевые не совпадают или не сохранились после того как второй юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как второй юзер отметил позиции");

        rootPage.scrollTillTop();
        rootPage.forceWait(1000);

    }

    @Test
    @DisplayName("1.8. Отменяем позиции у 1 гостю чтобы убедиться что у 2 гостя не сбросились чаевые")
    public void cancelDishes() {

        rootPage.cancelCertainAmountChosenDishes(dishesAmountToBeChosen);

    }

    @Test
    @DisplayName("1.9. Переходим ко 2 гостю, и проверяем что чаевые не сбились и потом устанавливаем кастомные чаевые")
    public void returnSecondUserAndMatchSums() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_SECOND_USER,COOKIE_SESSION_SECOND_USER);
        rootPage.scrollTillTop();

        HashMap<String, Double> currentSumsInfoSecondUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoSecondUser, sumsInfoSecondUser,
                "Сумма и чаевые не совпадают или не сохранились после того как первый юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как первый юзер отменил позиции");

    }

    @Test
    @DisplayName("2.0 Устанавливаем кастомные чаевые")
    public void setCustomTips() {

        rootPage.setCustomTips("350");

    }

    @Test
    @DisplayName("2.1 Сохраняем информацию по 2 гостю")
    public void saveSumsSecondUserAgain() {

        sumsInfoSecondUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoSecondUser + " сейвим еще раз 2 юзера");

    }

    @Test
    @DisplayName("2.2. Отменяем позиции у 2 гостя")
    public void cancelDishesTwice() {

        rootPage.cancelCertainAmountChosenDishes(dishesAmountToBeChosen);

    }

    @Test
    @DisplayName("2.3. Переходим к 1 гостю, чтобы убедиться что кастомные чаевые у 2 не слетели")
    public void returnFirstUserAndSetCustomTips() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        baseActions.scrollTillTop();
        rootPage.forceWait(1000);

        HashMap<String, Double> currentSumsInfoSecondUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoSecondUser, sumsInfoSecondUser,
                "Сумма и чаевые не совпадают или не сохранились после того как первый юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как первый юзер отменил позиции");

    }

    @Test
    @DisplayName("2.4. Закрываем заказ")
    public void finishOrder() {

        rootPageNestedTests.closeOrder();

    }

}
