package tapper.tests.keeper_e2e._1_3_tips;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.*;
import static data.Constants.TestData.TapperTable.*;


@Epic("RKeeper")
@Feature("Чаевые")
@Story("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")
@DisplayName("Проверка логики установки дефолтных чаевых от суммы заказа с двумя гостями")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TipsLogicBySumWith2GuestsTest extends BaseTest {


    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

    static String guid;
    static int amountDishesToBeChosen = 3;
    int amountDishesForFillingOrder = 5;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    public void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(2)
    @DisplayName("Проверяем что логика чаевых по сумме всех позиций и в б2п корректна")
    void checkDishesByDefault() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);
        rootPage.cancelAllChosenDishes();

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем рандомные блюда первым гостем")
    void chooseCertainAmountDishesByFirstGuest() {

        rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем что логика чаевых по сумме корректна c выбранными блюдами и в б2п")
    void checkDishesChosen() {

        double cleanDishesSum = rootPage.countAllNonPaidDAndChosenDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @Order(5)
    @DisplayName("Делимся счётом со 2 юзером")
    void openTableBySecondGuest() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);
        rootPage.isTableHasOrder();

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем что логика чаевых по сумме всех позиций и в б2п корректна у второго гостя")
    void checkDishesByDefaultBySecondGuest() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        nestedTests.checkDefaultTipsBySumAndScLogicBySumAndB2P(cleanDishesSum);

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.closedOrder)
    void payAndGoToAcquiringAgain() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid, apiUri);

    }

}
