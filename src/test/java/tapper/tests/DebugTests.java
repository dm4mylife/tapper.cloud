package tapper.tests;


import api.ApiRKeeper;
import common.ConfigDriver;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import pages.*;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;


import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.*;

@Disabled
@Epic("E2E")
@DisplayName("E2E")
@ConfigDriver(type="desktop")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DebugTests extends BaseTest {


    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    //  <---------- Tests ---------->




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
