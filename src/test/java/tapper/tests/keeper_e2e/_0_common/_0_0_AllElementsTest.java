package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;



@Order(0)
@Epic("E2E - тесты (полные)")
@Feature("Общая функциональность таппера")
@DisplayName("Общая функциональность таппера")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_0_AllElementsTest extends BaseTest {

    static String visit;
    static String guid;
    static String uni;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();


    @Test
    @DisplayName("0. Открываем пустой стол")
    public void openEmptyTable() {
        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
    }

    @Test
    @DisplayName("1.0. Проверка заголовка, номера стола, лого часов, подписи и вызов официанта")
    public void isTableNumberShown() {
        rootPageNestedTests.isEmptyTableCorrect();
    }

    @Test
    @DisplayName("1.1 Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
        visit = rsCreateOrder.jsonPath().getString("result.visit");

        Response rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "10000"));

        guid = rsFillingOrder.jsonPath().getString("result.guid");
        uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish[\"@attributes\"].uni");

    }

    @Test
    @DisplayName("1.2. Открытие стола")
    public void openTable() {
        rootPage.openTapperLink(STAGE_RKEEPER_TABLE_3);
    }

    @Test
    @DisplayName("1.3. Проверяем анимацию\\картинку при загрузке стола")
    public void isKeeperOrderCorrectWithTapper() {
        rootPage.isStartScreenShown();
    }

    @Test
    @DisplayName("1.4.Проверяем что позиции заказа на кассе совпадают с позициями на столе после создания ")
    public void isStartScreenShown() {
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();
    }

    @Test
    @DisplayName("1.5. Проверяем что стол не пустой и содержит заказ")
    public void isDishListNotEmptyAndVisible() {
        rootPage.isDishListNotEmptyAndVisible();
    }

    @Test
    @DisplayName("1.7. Проверяем что кнопка разделить счёт есть и работает корректно")
    public void isDivideSliderCorrect() {
        rootPage.isDivideSliderCorrect();
    }

    @Test
    @DisplayName("1.8. Проверяем что все элементы в блоке чаевых отображаются корректно")
    public void isTipsContainerCorrect() {
        rootPage.isTipsContainerCorrect();
    }

    @Test
    @DisplayName("1.9. Проверяем что все элементы в блоке чека отображаются корректно")
    public void isCheckContainerShown() {
        rootPage.isCheckContainerShown();
    }

    @Test
    @DisplayName("2.0. Проверяем что кнопка оплатить есть и работает корректно")
    public void isPaymentButtonShown() {
        rootPage.isPaymentButtonShown();
    }

    @Test
    @DisplayName("2.1. Проверяем что кнопка поделиться счётом есть и работает корректно")
    public void isShareButtonShown() {
        rootPage.isShareButtonShown();
    }

    @Test
    @DisplayName("2.2. Проверяем что кнопка сервисного сбора есть и работает корректно")
    public void isServiceChargeShown() {
        rootPage.isServiceChargeShown();
    }

    @Test
    @DisplayName("2.3. Проверяем что кнопка политики конфиденциальности есть и работает корректно")
    public void isConfPolicyShown() {
        rootPage.isConfPolicyShown();
    }

    @Test
    @DisplayName("2.4. Проверяем что нижнее навигационное меню есть, корректно работает меню, переходы по табам")
    public void isTapBarShown() {
        rootPage.isTapBarShown();
    }

    @Test
    @DisplayName("2.5. Проверяем функционал вызова официанта")
    public void isCallWaiterCorrect() {
        rootPage.isCallWaiterCorrect();
    }

    @Test
    @DisplayName("2.6. Оплачиваем заказ")
    public void payOrder() {


        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.typeDataAndPay();

    }

    @Test
    @DisplayName("2.7. Проверяем оплату")
    public void checkPayment() {
        reviewPageNestedTests.fullPaymentCorrect();
    }

    @Test
    @DisplayName("2.8. Оставляем отзыв")
    public void leaveReview() {
        reviewPageNestedTests.reviewCorrectPositive();
    }

    @Test
    @DisplayName("2.9. Проверяем что стол освободился")
    public void isTableEmpty() {
        rootPage.isEmptyOrderAfterClosing();
    }

}
