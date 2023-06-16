package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.waiters.Waiters;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
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

import static data.Constants.TestData.AdminPersonalAccount.*;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Официанты")
@DisplayName("Официанты")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaitersTest extends ScreenDesktopTest {

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
    Waiters waiters = new Waiters();
    

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.waiters)
    void integrations() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        waiters.goToWaiterCategory();
        waiters.isWaiterCategoryCorrect();

        //Set<By> ignoredSelectors = ScreenShotComparison.setIgnoredElements
        // (new ArrayList<>(List.of(waiterAvatarBy,waiterCardNameBy)));

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.AdminPersonalAccount.waiters, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.waitersSearch)
    void search() throws IOException {

        waiters.searchWaiter(ROBOCOP_WAITER);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.waitersSearch, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.waitersSearchNegative)
    void searchNegative() throws IOException {

        waiters.searchWaiterNegativeWithoutReset();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.waitersSearchNegative, diffPercent, imagePixelSize);

        waiters.resetSearchResult();

    }

    @Test
    @Order(4)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.detailWaiterCard)
    void detailWaiterCard() throws IOException {

        waiters.goToCertainWaiterDetailCard(IRON_MAN_WAITER);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.detailWaiterCard, diffPercent, imagePixelSize);

    }

}
