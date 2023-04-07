package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.ScreenLayout.Tapper.*;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@DisplayName("Вызов официанта")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CallWaiterTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            CallWaiterTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    static String guid;

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @Order(1)
    @DisplayName("Вызов официанта")
    void callWaiter() throws IOException {

        rootPage.openPage(tableUrl);

        rootPage.openCallWaiterForm();

        rootPage.isCallContainerWaiterCorrect();

        rootPage.sendWaiterComment();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartOne, diffPercent, imagePixelSize);

        rootPage.typeTextToGetSpecialMessage();

        rootPage.isSendSuccessful();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartTwo, diffPercent, imagePixelSize);

    }

}
