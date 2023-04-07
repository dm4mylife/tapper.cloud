package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.menu.Menu;
import data.AnnotationAndStepNaming;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenDesktopTest;
import data.table_data_annotation.SixTableData;
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
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuTest extends ScreenDesktopTest {

    SixTableData data = MenuTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            MenuTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    boolean isScreenShot = annotation.isTakeScreenshot();
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

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, adminMenu, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName("Тултип в меню")
    void tooltip() throws IOException {

        helpAdminContainer.hover();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, adminMenuNTooltip, diffPercent, imagePixelSize);

    }


}
