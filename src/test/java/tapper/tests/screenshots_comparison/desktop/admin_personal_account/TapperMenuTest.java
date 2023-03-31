package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.AdminAccount;
import admin_personal_account.menu.Menu;
import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
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
import java.util.*;

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.disabledMenu;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.selectors.AdminPersonalAccount.OperationsHistory.historyPeriodDateBy;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Меню таппера")
@DisplayName("Меню таппера")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TapperMenuTest extends ScreenDesktopTest {

    static TakeOrCompareScreenshots annotation =
            TapperMenuTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();

    String browserTypeSize = getBrowserSizeType();
    static String guid;
    ApiRKeeper apiRKeeper = new ApiRKeeper();
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
                ScreenLayout.Tapper.emptyMenu,diffPercent,imagePixelSize);

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

        Set<By> ignoredSelectors =
                ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(historyPeriodDateBy)));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                    ScreenLayout.Tapper.menu,diffPercent,imagePixelSize,ignoredSelectors);

        guid = apiRKeeper.getGuidFromGetOrder(apiRKeeper.getOrderInfo(tableId,apiUri));

    }

}
