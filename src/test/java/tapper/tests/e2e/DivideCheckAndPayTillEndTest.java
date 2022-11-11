package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.junit5.TextReportExtension;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static constants.Constant.ApiData.BARNOE_PIVO;
import static constants.Constant.ApiData.R_KEEPER_RESTAURANT;
import static constants.Constant.RequestBody.rqBodyFillingOrder;
import static constants.Constant.TestData.*;
import static constants.Selectors.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static constants.Selectors.RootPage.DishList.nonPaidAndNonDisabledDishesWhenDivided;


@Order(7)
@Epic("E2E - тесты (полные)")
@Feature("keeper - оплата, разделяя счёт по позиции до самого конца - рандомные поз без скидки - чай+сбор - карта")
@DisplayName("keeper - оплата, разделяя счёт по позиции до самого конца - рандомные поз без скидки - чай+сбор - карта")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DivideCheckAndPayTillEndTest extends BaseTest {

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
    @Description("Ресторан {R_KEEPER_RESTAURANT} - блюдо - {BARNOE_PIVO}")
    public void createAndFillOrder() {

        String visit = apiRKeeper.createOrder();
        apiRKeeper.fillingOrder(rqBodyFillingOrder(R_KEEPER_RESTAURANT,visit,BARNOE_PIVO,"10000"));

    }

    @Test
    @Order(2)
    @Step("Проверям работу всех активных элементов на странице")
    @DisplayName("Проверям работу всех активных элементов на странице")
    public void openAndCheck() {

        //Configuration.browserSize = IPHONE12PRO;

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--auto-open-devtools-for-tabs");

        Configuration.browserCapabilities = options;

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
    @Step("Разделям счёт, оплачиваем одну позицию и так пока весь заказ не будет оплачен")
    @DisplayName("Разделям счёт, оплачиваем одну позицию и так пока весь заказ не будет оплачен")
    public void payTillEnd() {

      rootPageNestedTests.payTillFullSuccessPayment(1);

    }

}
