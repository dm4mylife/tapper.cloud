package tapper.tests.keeper_e2e._0_common;


import admin_personal_account.menu.Menu;
import api.ApiRKeeper;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.RootPage.DishList.emptyOrderMenuButton;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;


@Order(1)
@Epic("RKeeper")
@Feature("Общие")
@Story("Общая функциональность таппера")
@DisplayName("Общая функциональность таппера")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_1_AllElementsTest extends BaseTest {

    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String guid;

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    AuthorizationPage authorizationPage = new AuthorizationPage();

    Menu menu = new Menu();

    @Test
    @DisplayName("0. Открываем пустой стол")
    public void openEmptyTable() {

        if (!apiRKeeper.isClosedOrder(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI)) {

            System.out.println("На кассе есть прошлый заказ, закрываем его");
            String guid = apiRKeeper.getGuidFromOrderInfo(TABLE_AUTO_111_ID,AUTO_API_URI);

            apiRKeeper.orderPay(rqParamsOrderPay(R_KEEPER_RESTAURANT, guid), AUTO_API_URI);

            boolean isOrderClosed = apiRKeeper.isClosedOrder(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,AUTO_API_URI);

            Assertions.assertTrue(isOrderClosed, "Заказ не закрылся на кассе");
            System.out.println("\nЗаказ закрылся на кассе\n");

        }

        rootPage.openPage(STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("1.0. Проверка заголовка, номера стола, лого часов, подписи и вызов официанта")
    public void isTableNumberShown() {

        rootPageNestedTests.isEmptyTableCorrect();

    }

    @Test
    @DisplayName("1.1 Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder
                (rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD),
                        AUTO_API_URI);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.orderFill(dishes, BARNOE_PIVO, 3);
        apiRKeeper.newFillingOrder(apiRKeeper.rsBodyFillingOrder(R_KEEPER_RESTAURANT, guid, dishes));

    }

    @Test
    @DisplayName("1.2. Открытие стола, проверка что заказ совпадает с кассой")
    public void openTable() {

        rootPage.refreshPage();
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

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

        rootPage.isPaymentOptionsCorrect();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);
        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.typeDataAndPay();
        best2PayPage.clickPayButton();

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

    @Test
    @DisplayName("3.0. Проверяем меню")
    public void isMenuCorrect() {

        rootPage.click(appFooterMenuIcon);

        if (emptyOrderMenuButton.isDisplayed()) {

            System.out.println("Меню не было активировано, активируем");
            rootPage.openNewTabAndSwitchTo(ADMIN_AUTHORIZATION_STAGE_URL);
            authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

            menu.goToMenuCategory();
            menu.isMenuCorrect();
            menu.activateFirstCategoryAndDishInMenu();

            rootPage.switchBrowserTab(0);
            rootPage.refreshPage();

        }

        rootPage.isElementInvisible(emptyOrderMenuButton);

    }

    @Test
    @DisplayName("3.1. Проверяем телеграм сообщение")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("3.2. Проверка что история с вызовом официанта сохранились при закрытии браузера")
    public void isHistorySavedByClosingBrowser() {

        rootPage.isHistorySavedByClosingBrowser(STAGE_RKEEPER_TABLE_111);

    }

}
