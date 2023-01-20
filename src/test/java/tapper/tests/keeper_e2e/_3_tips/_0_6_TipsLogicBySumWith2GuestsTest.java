package tapper.tests.keeper_e2e._3_tips;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;

@Order(6)
@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_6_TipsLogicBySumWith2GuestsTest extends BaseTest {

    static String visit;
    static String guid;
    static int neededDishesAmount = 4;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.2. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        guid = rsCreateOrder.jsonPath().getString("result.guid");
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "8000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_FIRST_USER, COOKIE_GUEST_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }

    @Test
    @DisplayName("1.3. Проверяем что логика чаевых по сумме всех позиций и в б2п корректна")
    public void checkDishesByDefault() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);
        rootPage.cancelAllChosenDishes();

    }

    @Test
    @DisplayName("1.4. Выбираем рандомные блюда первым гостем")
    public void chooseCertainAmountDishesByFirstGuest() {
        rootPageNestedTests.chooseCertainAmountDishes(neededDishesAmount);
    }

    @Test
    @DisplayName("1.5. Проверяем что логика чаевых по сумме корректна c выбранными блюдами и в б2п")
    public void checkDishesChosen() {

        double cleanDishesSum = rootPage.countAllNonPaidDAndChosenDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @DisplayName("1.6. Делимся счётом со 2 юзером")
    public void openTableBySecondGuest() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111,COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("1.7. Проверяем что логика чаевых по сумме всех позиций и в б2п корректна у второго гостя")
    public void checkDishesByDefaultBySecondGuest() {
        checkDishesByDefault();
    }

    @Test
    @DisplayName("1.8. Закрываем заказ")
    public void openTableByFirstGuest() {

        rootPage.closeOrderByAPI(guid);

    }

}
