package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.disabled;
import static data.Constants.TestData.*;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static data.selectors.TapperTable.RootPage.PayBlock.paymentButton;

@Order(7)
@Epic("RKeeper")
@Feature("Общие")
@Story("keeper - открытие стола у двух гостей, второй гость выбирает и отменяет позиции," +
        " у первого не должно быть заблокировано для оплаты")
@DisplayName("keeper - открытие стола у двух гостей, второй гость выбирает и отменяет позиции," +
        " у первого не должно быть заблокировано для оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_7_ChosenDishesAreAvailableInAnotherGuestAfterCancelingTest extends BaseTest {

    static int amountDishes = 4;
    static String guid;
    static String visit;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), TapperTable.AUTO_API_URI);
        visit = rs.jsonPath().getString("result.visit");
        guid = rs.jsonPath().getString("result.guid");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, String.valueOf(amountDishes * 1000)));

        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_FIRST_USER, TapperTable.COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда")
    public void chooseDishesByAnotherGuest() {
        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);
    }

    @Test
    @DisplayName("3. Отменяем их")
    public void cancelCertainAmountChosenDishes() {
        rootPage.cancelCertainAmountChosenDishes(amountDishes);
    }

    @Test
    @DisplayName("4. Подменяем данные, словно новый гость")
    public void setAnotherGuestCookie() {
        rootPage.openTableAndSetGuest(TapperTable.STAGE_RKEEPER_TABLE_111, TapperTable.COOKIE_GUEST_SECOND_USER, TapperTable.COOKIE_SESSION_SECOND_USER);
    }

    @Test
    @DisplayName("5. Переключаемся на первого пользователя, проверяем что блюда не заблокированы")
    public void switchToFirstGuest() {

        allNonPaidAndNonDisabledDishes.shouldHave(CollectionCondition.size(amountDishes), Duration.ofSeconds(10));
        paymentButton.shouldNotHave(disabled);
        System.out.println("Блюда не заблокированы, кнопка активна для оплаты");

    }

    @Test
    @DisplayName("6. Закрываем заказ, очищаем кассу")
    public void closeOrder() {
        rootPageNestedTests.closeOrderByAPI(guid);
    }

}
