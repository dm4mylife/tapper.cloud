package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_10;
import static constants.Selectors.RootPage.TipsAndCheck.tips25;
import static constants.Selectors.RootPage.TipsAndCheck.tipsOptions;

@Order(5)
@Epic("E2E - тесты (полные)")
@Feature("keeper - проверяем логику установки дефолтных чаевых по сумме заказа, сброс чаевых, проверка с разделением чека")
@DisplayName("keeper - проверяем логику установки дефолтных чаевых по сумме заказа, сброс чаевых, проверка с разделением чека")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_6_ChangingTips extends BaseTest {

    static String visit;
    static String guid;
    BaseActions baseActions = new BaseActions();
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_10, WAITER_ROBOCOP));
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, TORT, "5000"));

    }

    @Test
    @DisplayName("1.2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_10);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("2.4. пулл")
    public void gg2() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_10);

        rootPageNestedTests.chooseDishesWithRandomAmount(1);
        rootPage.clickOnPaymentButton();
        best2PayPageNestedTests.typeDataAndPay();
        reviewPageNestedTests.partialPaymentCorrect();
        reviewPage.clickOnFinishButton();

        rootPageNestedTests.chooseDishesWithRandomAmount(3);

        baseActions.scrollTillBottom();
        baseActions.click(tips25);

        rootPage.cancelCertainAmountChosenDishes(1);

        for (SelenideElement element :
                tipsOptions) {

            System.out.println(element.getCssValue("border"));

        }

        tipsOptions.filterBy(Condition.cssValue("border", "1px solid rgb(103, 100, 255)"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));

        System.out.println("один активный");

        baseActions.scrollTillBottom();
        baseActions.click(tips25);


        for (SelenideElement element :
                tipsOptions) {

            System.out.println(element.getCssValue("border"));

        }

        tipsOptions.filterBy(Condition.cssValue("border", "1px solid rgb(103, 100, 255)"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        System.out.println("один активный");

        rootPage.cancelCertainAmountChosenDishes(1);

        baseActions.scrollTillBottom();
        baseActions.click(tips25);

        baseActions.forceWait(2000);

        for (SelenideElement element :
                tipsOptions) {

            System.out.println(element.getCssValue("border"));

        }

        tipsOptions.filterBy(Condition.cssValue("border", "1px solid rgb(103, 100, 255)"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        System.out.println("один активный");

        baseActions.forceWait(2000);

        for (SelenideElement element :
                tipsOptions) {

            System.out.println(element.getCssValue("border"));

        }

        tipsOptions.filterBy(Condition.cssValue("border", "1px solid rgb(103, 100, 255)"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1));
        System.out.println("один активный");


    }


    @Disabled
    @Test
    @DisplayName("2.9. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
