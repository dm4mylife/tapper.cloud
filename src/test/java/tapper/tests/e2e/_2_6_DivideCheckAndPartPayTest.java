package tapper.tests.e2e;


import api.ApiRKeeper;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;
import pages.nestedTestsManager.Best2PayPageNestedTests;
import pages.nestedTestsManager.ReviewPageNestedTests;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.exist;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.Best2PayPage.transaction_id;
import static constants.Selectors.RootPage.DishList.dishesStatus;

@Order(26)
@Epic("E2E - тесты (полные)")
@Feature("keeper - частичная оплата когда разделили счёт - обычные позиции - чай+сбор")
@DisplayName("keeper - частичная оплата когда разделили счёт - обычные позиции - чай+сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _2_6_DivideCheckAndPartPayTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    HashMap<Integer, Map<String, Double>> chosenDishes;

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP));
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "3000"));

    }

    @Test
    @DisplayName("2. Открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void openAndCheck() {

        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmountWithTipsWithSC(1);

        chosenDishes = rootPage.countDisabledDishesAndSetCollection();

        rootPageNestedTests.clearDataAndCheckDisabledDishes(chosenDishes);

    }

    @Test
    @DisplayName("4. Выбираем рандомно оставшиеся блюда для оплаты, проверяем все суммы, в том числе ожидаемых")
    public void chooseDishesForPay() {

        rootPageNestedTests.chooseDishesWithRandomAmountWithTipsWithSC(1);
        rootPage.isAnotherGuestSumCorrect();

    }

    @Test
    @DisplayName("5. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        rootPageNestedTests.clickPayment();

    }

    @Test
    @DisplayName("6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();

    }

    @Test
    @DisplayName("7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        reviewPageNestedTests.partialPaymentCorrect();
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("8. Проверяем что ранее выбранные позиции все еще в статусе ожидаются, после закрываем заказ, очищаем кассу")
    public void closeOrder() {

        for (SelenideElement element : dishesStatus) {

            System.out.println(element);
            element.shouldNotBe(exist, Duration.ofSeconds(300));

        }
        rootPage.forceWait(1000); // toDO счетчик итого после сброса статуса ожидается у позиций, обновляется медленее чем пойдут другие тесты. подумать
        rootPageNestedTests.closeOrder();

    }

}