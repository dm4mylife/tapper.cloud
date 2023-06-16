package tapper.tests.screenshots_comparison.mobile.tapper_table;

import admin_personal_account.tips.Tips;
import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static api.ApiData.OrderData.WAITER_IRONMAN_NON_VERIFIED_NON_CARD;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.ScreenLayout.Tapper.tapperTableNonVerified;
import static data.selectors.TapperTable.Common.wiFiIconBy;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Официант не верифицирован")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaiterNonVerifiedTest extends ScreenMobileTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = WAITER_IRONMAN_NON_VERIFIED_NON_CARD;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;

    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    Set<By> ignoredElements = ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    static String guid;
    static int amountDishesForFillingOrder = 6;

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Tips tips = new Tips();
    AuthorizationPage authorizationPage = new AuthorizationPage();

    @Test
    @Order(1)
    @DisplayName("Официант не верифицирован")
    void waiterNoCard() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        tips.goToTipsCategory();
        tips.deactivateTips();
        tips.goToKitchenTipsCategory();
        tips.deactivateTips();

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode, waiter,
                apiUri,dishesForFillingOrder,tableUrl, tableId);
        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.ignoreWifiIcon();
        rootPage.ignoreServiceWorkerRoles();
        rootPage.scrollTillBottom();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableNonVerified, diffPercent, imagePixelSize,ignoredElements);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
