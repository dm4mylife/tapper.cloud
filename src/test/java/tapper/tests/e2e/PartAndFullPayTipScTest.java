package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.*;
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
import static constants.Constant.TestData.*;


@Order(6) // last DivideCheckAndPayTillEnd
@Epic("E2E - тесты (полные)")
@Feature("keeper - полная оплата, разделяя счёт по позиции в 2 заказа - рандомные поз без скидки - чай+сбор - карта - отзыв")
@DisplayName("keeper - полная оплата, разделяя счёт по позиции в 2 заказа - рандомные поз без скидки - чай+сбор - карта - отзыв")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class PartAndFullPayTipScTest extends BaseTest {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    @Test
    @Link("https://foundarium.testit.software/projects/1/tests/384")
    @TmsLink("https://foundarium.testit.software/projects/1/tests/384")
    @Order(1)
    @Step("Создание заказа в r_keeper")
    @DisplayName("Создание заказа в r_keeper")

    public void createAndFillOrder() {

        String visit = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT,TABLE_3, WAITER_ROBOCOP));
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,BARNOE_PIVO,"4000"));

    }

    @Test
    @Order(2)
    @Step("Проверям работу всех активных элементов на странице")
    @DisplayName("Проверям работу всех активных элементов на странице")
    public void openAndCheck() {

        Configuration.browserSize = IPHONE12PRO;
        rootPage.openTapperLink(STAGE_RKEEPER_URL);
        rootPageNestedTests.checkAllElementsAreVisibleAndActive();

    }

    @Test
    @Order(3)
    @Step("Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

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
        reviewPage.clickOnFinishButton();

    }

    @Test
    @Order(6)
    @Step("Разделяем счёт, оплачиваем оставшиеся позиции")
    @DisplayName("Разделяем счёт, оплачиваем оставшиеся позиции")
    public void payTillEnd() {

      rootPageNestedTests.checkAllDishesSumsWithAllConditions();

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

    }

    @Test
    @Order(7)
    @Step("Оставление отрицательный отзыв с рандомным пожеланием")
    @DisplayName("Оставление отрицательный отзыв с рандомным пожеланием")
    public void reviewCorrect() {

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPageNestedTests.reviewCorrectNegative();

    }




}
