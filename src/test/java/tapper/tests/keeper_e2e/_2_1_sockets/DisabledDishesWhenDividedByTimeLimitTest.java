package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import com.codeborne.selenide.SelenideElement;
import com.google.common.base.Stopwatch;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TwoBrowsers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.allDishesStatuses;



@Epic("RKeeper")
@Feature("Сокеты")
@Story("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")
@DisplayName("Выбор позиций в раздельной оплате на 1-м устройстве, позиции в статусе “Оплачивается” на 2-м устройстве")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisabledDishesWhenDividedByTimeLimitTest extends TwoBrowsers {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_222;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_222;
    protected final String tableId = TABLE_AUTO_222_ID;

    int amountDishesToBeChosen = 3;
    int amountDishesForFillingOrder = 6;
    static String guid;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

    }

    @Test
    @Order(2)
    @DisplayName(TapperTable.choseRandomDishesAncCheckSums)
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

        using(firstBrowser, () -> rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen));

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем что позиции закрыты для выбора и в статусе ожидается")
    void savePaymentDataForAcquiring() {

        using(secondBrowser, () -> {

            Stopwatch stopwatch = Stopwatch.createStarted();

            for (SelenideElement element : allDishesStatuses) {

                element.shouldNotBe(text(DISH_STATUS_IS_PAYING), Duration.ofSeconds(300));

            }

            stopwatch.stop();
            long millis = stopwatch.elapsed(TimeUnit.SECONDS);

            Assertions.assertTrue(millis >= 200,
                    "Время ожидания разделенных позиций меньше 4 мин (" + millis + ")");

            Allure.addAttachment
                    ("Время ожидания разделенных позиций в секундах", "text/plain", String.valueOf(millis));

        });

    }

    @Test
    @Order(4)
    @DisplayName("Закрываем заказ, очищаем кассу")
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
