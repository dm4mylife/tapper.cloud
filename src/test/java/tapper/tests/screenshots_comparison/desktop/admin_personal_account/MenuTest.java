package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.menu.Menu;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.ScreenLayout.AdminPersonalAccount.adminMenu;
import static data.ScreenLayout.AdminPersonalAccount.adminMenuNTooltip;
import static data.selectors.AdminPersonalAccount.Menu.helpAdminContainer;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Меню")
@DisplayName("Меню")
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
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Menu menu = new Menu();


    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.menu)
    void menu() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        menu.goToMenuCategory();
        menu.isMenuCategoryCorrect();
        menu.activateOnlyAllAutoCategoryAndDishes();
        menu.activateShowActiveForGuest();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, adminMenu, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName("Тултип в меню")
    void tooltip() throws IOException {

        helpAdminContainer.hover();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, adminMenuNTooltip, diffPercent, imagePixelSize);

    }


}
