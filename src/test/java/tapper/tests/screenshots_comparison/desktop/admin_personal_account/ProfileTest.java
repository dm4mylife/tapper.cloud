package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.AdminAccount;
import admin_personal_account.profile.Profile;
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

import static data.AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.leftMenu;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.ScreenLayout.AdminPersonalAccount.openedLeftMenu;
import static data.selectors.AdminPersonalAccount.Common.closeLeftMenu;
import static data.selectors.AdminPersonalAccount.Profile.telegramItemsInput;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Профиль")
@DisplayName("Профиль")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileTest extends ScreenDesktopTest {

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
    Profile profile = new Profile();
    RootPage rootPage = new RootPage();
    AdminAccount adminAccount = new AdminAccount();


    @Test
    @Order(1)
    @DisplayName(leftMenu)
    void leftMenu() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        profile.isProfileCategoryCorrect();
        adminAccount.openedLeftMenu();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, openedLeftMenu, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePage)
    void profile() throws IOException {

        BaseActions.click(closeLeftMenu);

        Set<By> ignoredElements = ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(telegramItemsInput)));

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.AdminPersonalAccount.profilePartOne, diffPercent, imagePixelSize, ignoredElements);

        rootPage.scrollTillBottom();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.AdminPersonalAccount.profilePartTwo, diffPercent, imagePixelSize);

    }

}
