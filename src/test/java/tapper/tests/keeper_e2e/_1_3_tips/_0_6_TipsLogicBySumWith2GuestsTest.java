package tapper.tests.keeper_e2e._1_3_tips;


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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;

@Order(6)
@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_6_TipsLogicBySumWith2GuestsTest extends BaseTest {

    static String guid;
    static int amountDishesToBeChosen = 3;
    static int amountDishesForFillingOrder = 5;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.2. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_111,TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

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

        rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);

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

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_111, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

    }

    @Test
    @DisplayName("1.7. Проверяем что логика чаевых по сумме всех позиций и в б2п корректна у второго гостя")
    public void checkDishesByDefaultBySecondGuest() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @DisplayName("1.8. Закрываем заказ")
    public void openTableByFirstGuest() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,guid,AUTO_API_URI);

    }

}
