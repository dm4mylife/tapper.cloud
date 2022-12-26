package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.divideCheckSlider;

@Order(5)
@Epic("E2E - тесты (полные)")
@Feature("keeper - проверка что установленные чаевые у одного юзера не сбросятся если другой юзер будет выбирать позиции")
@DisplayName("keeper - проверка что установленные чаевые у одного юзера не сбросятся если другой юзер будет выбирать позиции")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_5_NotChangingTipsInAnotherUserTest extends BaseTest {

    static String visit;
    static String guid;
    static int firstUserTable = 0;
    static int secondUserTable = 1;
    static HashMap<String, Double> sumsInfoFirstUser;
    static HashMap<String, Double> sumsInfoSecondUser;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsGetOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
        visit = rsGetOrder.jsonPath().getString("result.visit");
        guid = rsGetOrder.jsonPath().getString("result.guid");

        apiRKeeper.addModificatorOrder(guid, LIMONAD, "2000", "1000111", "1");
        apiRKeeper.addModificatorOrder(guid, LIMONAD, "2000", "1000112", "1");
        apiRKeeper.addModificatorOrder(guid, GOVYADINA_PORTION, "1000", "1000117", "3");
        apiRKeeper.addModificatorOrder(guid, GOVYADINA_PORTION, "1000", "1000118", "1");

    }

    @Test
    @DisplayName("1.1. Открытие стола")
    public void openAndCheck() {

        rootPage.openTapperTable(STAGE_RKEEPER_TABLE_3);

    }

    @Test
    @DisplayName("1.2. Выбираем рандомные блюда")
    public void choseDishes() {

        rootPage.click(divideCheckSlider);

        rootPage.chooseCertainAmountDishes(3);
        rootPage.setRandomTipsOption();

    }

    @Test
    @DisplayName("1.3. Сохраняем информацию по 1 юзеру")
    public void saveSumsFirstUser() {

        sumsInfoFirstUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoFirstUser + " сейвим 1 юзера");
    }

    @Test
    @DisplayName("1.4. Делимся счётом со 2 юзером")
    public void divideCheck() {

        baseActions.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);
        rootPage.setAnotherGuestCookie();

    }

    @Test
    @DisplayName("1.5. Выбираем рандомные блюда")
    public void choseDishesAndCheckSumsSecondUser() {

        rootPage.click(divideCheckSlider);

        rootPage.chooseCertainAmountDishes(3);
        rootPage.setRandomTipsOption();

    }

    @Test
    @DisplayName("1.6. Сохраняем информацию по 2 юзеру")
    public void saveSumsSecondUser() {

        sumsInfoSecondUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoSecondUser + " сейвим 2 юзера");

    }

    @Test
    @DisplayName("1.7. Переходим к 1 юзеру, и проверяем что чаевые не сбились")
    public void returnFirstUserAndMatchSums() {

        Selenide.switchTo().window(firstUserTable);
        rootPage.forceWait(2000);

        HashMap<String, Double> currentSumsInfoFirstUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoFirstUser, sumsInfoFirstUser,
                "Сумма и чаевые не совпадают или не сохранились после того как второй юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как второй юзер отметил позиции");

    }

    @Test
    @DisplayName("1.8. Отменяем позиции чтобы убедиться что у 2 юзера не сбросились чаевые")
    public void cancelDishes() {

        rootPage.cancelCertainAmountChosenDishes(3);

    }

    @Test
    @DisplayName("1.9. Переходим ко 2 юзеру, и проверяем что чаевые не сбились и потом устанавливаем кастомные чаевые")
    public void returnSecondUserAndMatchSums() {

        Selenide.switchTo().window(secondUserTable);
        rootPage.forceWait(2000);

        HashMap<String, Double> currentSumsInfoSecondUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoSecondUser, sumsInfoSecondUser,
                "Сумма и чаевые не совпадают или не сохранились после того как первый юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как первый юзер отменил позиции");

        rootPage.setCustomTips("350");

        sumsInfoSecondUser = rootPage.saveSumsInCheck();
        System.out.println(sumsInfoSecondUser + " сейвим еще раз 2 юзера");

    }

    @Test
    @DisplayName("2.0. Переходим к 1 юзеру отменяем снова позиции, чтобы убедиться что кастомные чаевые у 2 не слетели")
    public void returnFirstUserAndSetCustomTips() {

        Selenide.switchTo().window(firstUserTable);
        rootPage.forceWait(2000);

        rootPage.cancelCertainAmountChosenDishes(3);

        Selenide.switchTo().window(secondUserTable);
        rootPage.forceWait(2000);

        HashMap<String, Double> currentSumsInfoSecondUser = rootPage.saveSumsInCheck();

        Assertions.assertEquals(currentSumsInfoSecondUser, sumsInfoSecondUser,
                "Сумма и чаевые не совпадают или не сохранились после того как первый юзер отметил позиции");
        System.out.println("Сумма и чаевые совпадают после того как первый юзер отменил позиции");

    }

    @Test
    @DisplayName("2.1. Закрываем заказ")
    public void finishOrder() {

        Selenide.switchTo().window(firstUserTable);

        rootPage.cancelCertainAmountChosenDishes(3);

        rootPageNestedTests.closeOrder();

    }

}
