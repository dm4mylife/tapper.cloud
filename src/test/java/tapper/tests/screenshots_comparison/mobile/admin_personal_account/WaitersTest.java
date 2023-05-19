package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.waiters.Waiters;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.TestData.AdminPersonalAccount.*;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Официанты")
@DisplayName("Официанты")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WaitersTest extends ScreenMobileTest {

    SixTableData data = WaitersTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            WaitersTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.AdminPersonalAccount.waiters, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.waitersSearch)
    void search() throws IOException {

        waiters.searchWaiter(ROBOCOP_WAITER);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.waitersSearch, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.waitersSearchNegative)
    void searchNegative() throws IOException {

        waiters.searchWaiterNegativeWithoutReset();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.waitersSearchNegative, diffPercent, imagePixelSize);

        waiters.resetSearchResult();

    }

    @Test
    @Order(4)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.detailWaiterCard)
    void detailWaiterCard() throws IOException {

        waiters.goToCertainWaiterDetailCard(IRON_MAN_WAITER);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.detailWaiterCard, diffPercent, imagePixelSize);

    }

}
