package tapper.tests.screenshots_comparison.mobile.waiter_personal_account;


import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import java.io.IOException;

import static data.AnnotationAndStepNaming.DisplayName.WaiterPersonalAccount.waiterProfile;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Личный кабинет официанта")
@Story("Профиль")
@DisplayName("Профиль")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends ScreenMobileTest {

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

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Waiter waiterPage = new Waiter();
    RootPage rootPage = new RootPage();

    @Test
    @DisplayName(waiterProfile + " 1")
    @Order(1)
    void profilePartOne() throws IOException {

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.WaiterPersonalAccount.profilePartOne, diffPercent, imagePixelSize);

    }


    @Test
    @Order(2)
    @DisplayName(waiterProfile + " 2")
    void profilePartTwo() throws IOException {

        rootPage.scrollTillBottom();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.WaiterPersonalAccount.profilePartTwo, diffPercent, imagePixelSize);


    }

}
