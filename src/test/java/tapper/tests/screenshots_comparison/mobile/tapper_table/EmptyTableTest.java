package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.*;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.DishList.refreshButtonEmptyPage;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tips25;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Пустой стол")
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmptyTableTest extends ScreenMobileTest {
    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            EmptyTableTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));


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
        rootPage.ignoreWifiIcon();
        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize,ignoredElements);

        rootPage.isElementVisible(refreshButtonEmptyPage);
        rootPage.click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText(REFRESH_TABLE_BUTTON_TEXT));

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableRefreshTable, diffPercent, imagePixelSize,ignoredElements);

    }

}
