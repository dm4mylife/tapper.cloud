package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.Configuration;
import io.qameta.allure.Description;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$$;
import static api.ApiData.orderData.*;
import static api.ApiData.orderData.WAITER_ROBOCOP;
import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static constants.Constant.TestData.IPHONE12PRO;
import static constants.Constant.TestData.STAGE_RKEEPER_URL;


@Order(6)
@Epic("E2E - тесты (полные)")
@Feature("keeper - частичная оплата когда разделили счёт - рандомные поз без скидки - чай+сбор - карта - отзыв")
@DisplayName("keeper - частичная оплата когда разделили счёт - рандомные поз без скидки - чай+сбор - карта - отзыв")


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class DivideCheckAndPartPayTest extends BaseTest {

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

        String visit = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT,TABLE_3, WAITER_ROBOCOP));
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT,visit,BARNOE_PIVO,"3000"));

    }

    @Test
    @Order(2)
    @Step("Проверяем работу всех активных элементов на странице, проверка блюд на кассе и в таппере")
    @DisplayName("Проверяем работу всех активных элементов на странице, проверка блюд на кассе и в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_URL);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();
        rootPageNestedTests.checkAllElementsAreVisibleAndActive();

    }

    @Test
    @Order(3)
    @Step("Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmountAndCheckAllSumsConditions(1);

        HashMap<Integer, Map<String,Double>> chosenDishes = rootPage.countDisabledDishesAndSetCollection();

        rootPageNestedTests.clearDataAndCheckDisabledDishes(chosenDishes);

    }

    @Test
    @Order(4)
    @Step("Выбираем рандомно оставшиеся блюда для оплаты, проверяем все суммы, в том числе ожидаемых")
    @DisplayName("Выбираем рандомно оставшиеся блюда для оплаты, проверяем все суммы, в том числе ожидаемых")
    public void chooseDishesForPay() {

        rootPageNestedTests.chooseDishesWithRandomAmountAndCheckAllSumsConditions(1);
        rootPage.isAnotherGuestSumCorrect();

    }

    @Test
    @Order(5)
    @Step("Переход на эквайринг, ввод данных, оплата")
    @DisplayName("Переход на эквайринг, ввод данных, оплата")
    public void payAndGoToAcquiring() {

        double totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

    }

    @Test
    @Order(6)
    @Step("Проверка что оплата полностью корректна, все статусы о транзакции поочередно исполняются")
    @DisplayName("Проверка что оплата полностью корректна, все статусы о транзакции поочередно исполняются")
    public void paymentCorrect() {

        reviewPageNestedTests.partialPaymentCorrect();
    }

    @Test
    @Order(7)
    @Step("Оставление 5 отзыва с рандомным пожеланием")
    @DisplayName("Оставление 5 отзыва с рандомным пожеланием")
    public void reviewCorrect() {

        reviewPageNestedTests.reviewCorrectPositive();

    }

    @Test
    @Order(8)
    @Step("Закрываем заказ, очищаем кассу")
    @DisplayName("Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
