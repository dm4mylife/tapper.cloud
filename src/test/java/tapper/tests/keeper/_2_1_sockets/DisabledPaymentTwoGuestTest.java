package tapper.tests.keeper._2_1_sockets;


import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.TwoBrowsers;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Проверка на деактивацию кнопки оплатить, на втором устройстве")
@DisplayName("Проверка на деактивацию кнопки оплатить, на втором устройстве")


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisabledPaymentTwoGuestTest extends TwoBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static int amountDishesForFillingOrder = 4;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName(createOrderInKeeper + isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrder(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableId);

    }

    @Test
    @Order(2)
    @DisplayName("Открываем стол на двух разных устройствах, проверяем что не пустые")
    void openTables() {

        using(firstBrowser, () -> {

            rootPage.openNotEmptyTable(tableUrl);
            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем рандомно блюда у первого гостя, сохраняем данные для след.теста")
    void chooseDishesAndCheckAfterDivided() {

        using(secondBrowser, () -> {

            rootPage.openNotEmptyTable(tableUrl);
            rootPage.isPaymentDisabled();

        });

    }

    @Test
    @Order(4)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.closedOrder)
    void closeOrder() {

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid);

    }


}
