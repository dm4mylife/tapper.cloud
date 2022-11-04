package tapper.tests;


import api.ApiRKeeper;
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

import static constants.Constant.TestData.STAGE_ROOT_URL;


@Order(1)
@Epic("E2E - тесты (полные)")
@Feature("Первичный тест")
@DisplayName("Первичный тест")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class AllFunctionAreCorrect extends BaseTest {

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

        String visit = apiRKeeper.createOrder();
        apiRKeeper.fillingOrder(visit);

    }

    @Test
    @Order(2)
    @Step("Проверка всех элементов")
    @DisplayName("Проверка всех элементов")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_ROOT_URL);
        rootPageNestedTests.checkAllElementsAreVisibleAndActive();

    }

    @Test
    @Order(3)
    @Step("Проверка суммы, чаевых, сервисного сбора")
    @DisplayName("Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        double totalSum = rootPage.single_countAllDishesInOrder();

        rootPageNestedTests.single_isAllTipsOptionsAreCorrectWithTotalSumWithSC(totalSum);
        rootPageNestedTests.single_isAllTipsOptionsAreCorrectWithTotalSumWithoutSC(totalSum);

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

        reviewPageNestedTests.paymentCorrect();

    }

    @Test
    @Order(6)
    @Step("Оставление 5 отзыва с рандомным пожеланием")
    @DisplayName("Оставление 5 отзыва с рандомным пожеланием")
    public void reviewCorrect() {

        reviewPage.rate5Stars();
        reviewPage.chooseRandomWhatDoULike();
        reviewPage.typeReviewComment();
        reviewPage.clickOnFinishButton();

    }

    @Test
    @Order(7)
    @Step("Стол освободился и ожидает заказа")
    @DisplayName("Стол освободился и ожидает заказа")
    public void emptyOrderCorrect() {

        rootPage.forceWait(6000L);


    }



}
