package tapper.tests.screenshots_comparison.mobile.tapper_table;


import api.ApiRKeeper;
import common.BaseActions;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.selectors.TapperTable.RootPage.PayBlock.serviceChargeCheckboxButton;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Отмена сервисного сбора и ручной ввод чаевых")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomTipsNoScTest extends ScreenMobileTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;
    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    static String guid;
    static int amountDishesForFillingOrder = 2;
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Test
    @Order(1)
    @DisplayName("Окно сервисного сбора")
    void isServiceChargeCorrect() throws IOException {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);
        rootPage.ignoreAllDynamicsElements();
        rootPage.scrollTillBottom();

        BaseActions.click(serviceChargeCheckboxButton);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableServiceChargePopUp,diffPercent,imagePixelSize);

        rootPage.disableScPopUp();

    }

    @Test
    @Order(2)
    @DisplayName("Отмена сервисного сбора и ручной ввод чаевых")
    void createAndFillOrder() throws IOException {

        rootPage.setCustomTips("155");

        rootPage.deactivateServiceChargeIfActivated();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableCustomTipsAndNoSc,diffPercent,imagePixelSize);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }


}
