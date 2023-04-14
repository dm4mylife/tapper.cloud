package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.TwoBrowsers;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Selenide.using;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Проверка на деактивацию кнопки оплатить, на втором устройстве")
@DisplayName("Проверка на деактивацию кнопки оплатить, на втором устройстве")


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DisabledPaymentTwoGuestTest extends TwoBrowsers {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_222;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_222;
    protected final String tableId = TABLE_AUTO_222_ID;
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

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.isPaymentDisabled();
            rootPage.deactivateDivideCheckSliderIfActivated();
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

        apiRKeeper.closedOrderByApi(restaurantName, tableId, guid, apiUri);

    }


}
