package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static constants.Constant.ApiData.*;
import static constants.Constant.ApiData.WAITER_ROBOCOP;
import static constants.Constant.QueryParams.rqParamsCreateOrderBasic;
import static constants.Constant.QueryParams.rqParamsFillingOrderBasic;
import static constants.Constant.TestData.IPHONE12PRO;
import static constants.Constant.TestData.STAGE_RKEEPER_URL;


@Order(2)
@Epic("E2E - тесты (полные)")
@Feature("keeper - частичная оплата - рандом поз без скидки - чай+сбор - карта - отзыв")
@DisplayName("keeper - частичная оплата - рандом поз без скидки - чай+сбор - карта - отзыв")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class PartPayTipSCTest extends BaseTest {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    @Test
    @Order(1)
    @Step("Создание заказа в r_keeper")
    @DisplayName("Создание заказа в r_keeper")
    public void createAndFillOrder() {

        String visit = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT,TABLE_3, WAITER_ROBOCOP));
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,BARNOE_PIVO,"3000"));

    }


    @Test
    @Order(2)
    @Step("Проверка всех элементов")
    @DisplayName("Проверка всех элементов")
    public void openAndCheck() {

        Configuration.browserSize = IPHONE12PRO;
        rootPage.openTapperLink(STAGE_RKEEPER_URL);
        rootPageNestedTests.checkAllElementsAreVisibleAndActive();

    }

    @Test
    @Order(3)
    @Step("Выбираем рандомно блюда, проверяем все суммы и условия, оплачиваем")
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, оплачиваем")
    public void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmountAndCheckAllSumsConditions(1);

    }

    @Test
    @Order(4)
    @Step("Переход на эквайринг, ввод данных, оплата")
    @DisplayName("Переход на эквайринг, ввод данных, оплата")
    public void payAndGoToAcquiring() {

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

    }

    @Test
    @Order(5)
    @Step("Проверка что оплата полностью корректна, все статусы о транзакции поочередно исполняются")
    @DisplayName("Проверка что оплата полностью корректна, все статусы о транзакции поочередно исполняются")
    public void paymentCorrect() {

        reviewPageNestedTests.partialPaymentCorrect();

    }

    @Test
    @Order(6)
    @Step("Оставление 5 отзыва с рандомным пожеланием")
    @DisplayName("Оставление 5 отзыва с рандомным пожеланием")
    public void reviewCorrect() {

        reviewPageNestedTests.reviewCorrectPositive();

    }



}
