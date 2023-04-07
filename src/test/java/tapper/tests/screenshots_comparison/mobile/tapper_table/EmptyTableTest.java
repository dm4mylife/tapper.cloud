package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.*;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.DishList.refreshButtonEmptyPage;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tips25;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@DisplayName("Пустой стол")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmptyTableTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            EmptyTableTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName("Пустой стол")
    void emptyTable() throws IOException {

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openPage(tableUrl);
        rootPage.isEmptyOrderAfterClosing();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize);

        rootPage.isElementVisible(refreshButtonEmptyPage);
        rootPage.click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText(REFRESH_TABLE_BUTTON_TEXT));

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableRefreshTable, diffPercent, imagePixelSize);

    }

}
