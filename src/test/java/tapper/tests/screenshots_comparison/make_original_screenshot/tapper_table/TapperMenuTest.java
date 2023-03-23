package tapper.tests.screenshots_comparison.make_original_screenshot.tapper_table;

import admin_personal_account.menu.Menu;
import api.ApiRKeeper;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.MakeOriginalScreenshotBaseTest;
import tests.MakeOriginalScreenshotOnlyDesktopBaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;


@Epic("Тесты по верстке проекта")
@Feature("Стол таппера")
@Story("Оригинал - Пустой стол")
@DisplayName("Оригинал - Пустой стол")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TapperMenuTest extends MakeOriginalScreenshotOnlyDesktopBaseTest {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;


    boolean isScreenShot = MakeOriginalScreenshotBaseTest.isScreenShot;
    double diffPercent = MakeOriginalScreenshotBaseTest.diffScreenPercent;
    int imagePixelSize = MakeOriginalScreenshotBaseTest.imagePixelSize;

    static String guid;
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();

    @Test
    @Order(1)
    @DisplayName("Меню")
    void emptyTable() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        menu.goToMenuCategory();
        menu.isMenuCorrect();
        menu.activateOnlyAllAutoCategoryAndDishes();


        rootPage.openPage(tableUrl);
        rootPage.changeBrowserSizeDuringTest(400,1020);
        rootPage.clickOnMenuInFooter();

        ScreenShotComparison.isScreenOrDiff(isScreenShot, ScreenLayout.Tapper.menu,diffPercent,imagePixelSize);

        guid = apiRKeeper.getGuidFromGetOrder(apiRKeeper.getOrderInfo(tableId,apiUri));

    }


}
