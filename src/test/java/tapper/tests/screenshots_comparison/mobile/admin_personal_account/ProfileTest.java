package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.profile.Profile;
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
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.ScreenLayout.AdminPersonalAccount.rootPageMobile;
import static data.selectors.AdminPersonalAccount.Profile.telegramItemsInput;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Профиль")
@DisplayName("Профиль")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileTest extends ScreenMobileTest {

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



    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePageMobile)
    void rootPage() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        profile.isOpenedLeftMenuCorrect();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, rootPageMobile, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePage)
    void profile() throws IOException {

        profile.goToProfileCategory();

        Set<By> ignoredElements = ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(telegramItemsInput)));

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.AdminPersonalAccount.profilePartOne, diffPercent, imagePixelSize, ignoredElements);

        rootPage.scrollTillBottom();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.AdminPersonalAccount.profilePartTwo, diffPercent, imagePixelSize, ignoredElements);

    }

}
