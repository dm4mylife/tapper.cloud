package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static api.ApiData.OrderData.WAITER_IRONMAN_NON_VERIFIED_NON_CARD;
import static data.ScreenLayout.Tapper.tapperTableNonVerified;
import static data.selectors.TapperTable.Common.wiFiIconBy;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Официант не верифицирован")
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaiterNonVerifiedTest extends ScreenMobileTest {
    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            WaiterNonVerifiedTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = WAITER_IRONMAN_NON_VERIFIED_NON_CARD;
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    static String guid;
    static int amountDishesForFillingOrder = 6;

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @Order(1)
    @DisplayName("Официант не верифицирован")
    void waiterNoCard() throws IOException {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri,dishesForFillingOrder,tableUrl, tableId);
        rootPage.ignoreWifiIcon();
        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.scrollTillBottom();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableNonVerified, diffPercent, imagePixelSize,ignoredElements);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
