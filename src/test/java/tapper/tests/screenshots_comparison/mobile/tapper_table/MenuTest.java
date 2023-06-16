package tapper.tests.screenshots_comparison.mobile.tapper_table;

import admin_personal_account.AdminAccount;
import admin_personal_account.menu.Menu;
import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static data.AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.disabledMenu;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.RootPage.Menu.firstTestDish;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Меню")
@DisplayName("Меню таппера")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuTest extends ScreenDesktopTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;

    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    Set<By> ignoredElements = ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();
    AdminAccount adminAccount = new AdminAccount();


    @Test
    @Order(1)
    @DisplayName(disabledMenu)
    void disableMenu() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        menu.goToMenuCategory();
        menu.isMenuCategoryCorrect();
        menu.deactivateShowGuestSliderIfActivated();
        adminAccount.logOut();

        rootPage.openPage(tableUrl);
        rootPage.changeBrowserSizeDuringTest(400,1020);
        rootPage.clickOnMenuInFooter();

        rootPage.emptyMenuCorrect();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.emptyMenu,diffPercent,imagePixelSize,ignoredElements);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.menu)
    void menu() throws IOException {

        rootPage.changeBrowserSizeDuringTest(1920,1080);
        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        menu.goToMenuCategory();
        menu.isMenuCategoryCorrect();
        menu.activateOnlyAllAutoCategoryAndDishes();

        rootPage.openPage(tableUrl);
        rootPage.changeBrowserSizeDuringTest(400,1020);
        rootPage.clickOnMenuInFooter();
        rootPage.ignoreWifiIcon();
        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, ScreenLayout.Tapper.menu,diffPercent,
                imagePixelSize,ignoredElements);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.menuDetailCard)
    void dishInDetailCard() throws IOException {

        BaseActions.click(firstTestDish.first());

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.detailCardMenu,diffPercent, imagePixelSize,ignoredElements);

    }

}
