package tapper.tests.screenshots_comparison.mobile.tapper_table;

import admin_personal_account.AdminAccount;
import admin_personal_account.menu.Menu;
import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
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
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuTest extends ScreenDesktopTest {
    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            WaiterNonVerifiedTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));

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

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
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
        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, ScreenLayout.Tapper.menu,diffPercent,
                imagePixelSize,ignoredElements);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.menuDetailCard)
    void dishInDetailCard() throws IOException {

        BaseActions.click(firstTestDish.first());

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.detailCardMenu,diffPercent, imagePixelSize,ignoredElements);

    }

}
