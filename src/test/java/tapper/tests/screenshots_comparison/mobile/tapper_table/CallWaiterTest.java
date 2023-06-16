package tapper.tests.screenshots_comparison.mobile.tapper_table;

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

import static data.ScreenLayout.Tapper.openedCallWaiterWithReceiptMessagePartOne;
import static data.ScreenLayout.Tapper.openedCallWaiterWithReceiptMessagePartTwo;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Вызов официанта")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CallWaiterTest extends ScreenMobileTest {

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
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName("Вызов официанта")
    void callWaiter() throws IOException {

        nestedTests.clearTableAndOpenEmptyTable(restaurantName,tableId,apiUri,tableUrl);
        rootPage.ignoreAllDynamicsElements();

        rootPage.openCallWaiterForm();
        rootPage.isCallContainerWaiterCorrect();
        rootPage.sendWaiterComment();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartOne, diffPercent, imagePixelSize);

        rootPage.typeTextToGetSpecialMessage();
        rootPage.isSendSuccessful();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartTwo, diffPercent, imagePixelSize);

    }

}
