package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;
import static data.ScreenLayout.Tapper.*;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Официант не верифицирован")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaiterNonVerifiedTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            WaiterNonVerifiedTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_IRONMAN_NON_VERIFIED_NON_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot();
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

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.scrollTillBottom();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableNonVerified, diffPercent, imagePixelSize);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid,apiUri);

    }

}
