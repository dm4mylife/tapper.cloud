package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import pages.RootPage;
import pages.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.disabled;
import static constants.Constant.TestData.STAGE_RKEEPER_TABLE_3;
import static constants.Selectors.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static constants.Selectors.RootPage.PayBlock.paymentButton;

@Order(3)
@Epic("E2E - тесты (полные)")
@Feature("keeper - открытие стола у двух гостей, второй гость выбирает и отменяет позиции," +
        " у первого не должно быть заблокировано для оплаты")
@DisplayName("keeper - открытие стола у двух гостей, второй гость выбирает и отменяет позиции," +
        " у первого не должно быть заблокировано для оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_7_ChosenDishesAreAvailableInAnotherGuestAfterCancelingTest extends BaseTest {

    static int amountDishes = 3;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD));
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
    @DisplayName("3. Открытие стола во второй вкладке под новым юзером")
    public void openNewTab() {

        rootPage.openNewTabAndSwitchTo(STAGE_RKEEPER_TABLE_3);

    }

    @Test
    @DisplayName("4. Подменяем данные, словно новый гость")
    public void setAnotherGuestCookie() {

        rootPage.setAnotherGuestCookie();

    }

    @Test
    @DisplayName("5. Выбираем рандомно блюда и отменяем их")
    public void chooseDishesByAnotherGuest() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);
        rootPage.cancelCertainAmountChosenDishes(3);
        rootPage.forceWait(1000); // toDo тест проходит слишком быстро, принудительно ждем


    }

    @Test
    @DisplayName("6. Переключаемся на первого пользователя, проверяем что блюда не заблокированы")
    public void switchToFirstGuest() {

        Selenide.switchTo().window(0);
        rootPage.forceWait(1000); // toDo тест проходит слишком быстро, принудительно ждем
        allNonPaidAndNonDisabledDishes.shouldHave(CollectionCondition.size(amountDishes), Duration.ofSeconds(5));
        paymentButton.shouldNotHave(disabled);
        System.out.println("Блюда не заблокированы, кнопка активна для полаты");

    }


    @Test
    @DisplayName("7. Закрываем заказ, очищаем кассу")
    public void closeOrder() {

        rootPageNestedTests.closeOrder();

    }

}
