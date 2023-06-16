package tapper.tests.screenshots_comparison.mobile.support_personal_account;

import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import support_personal_account.profile.Profile;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.selectors.AdminPersonalAccount.Profile.telegramItemsInput;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор техподдержки")
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

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Profile profile = new Profile();
    String browserTypeSize = getBrowserSizeType();


    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePageMobile)
    void rootPage() throws IOException {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

        profile.isOpenedLeftMenuCorrect();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, ScreenLayout.SupportPersonalAccount.rootPage,
                diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePage)
    void profile() throws IOException {

        profile.goToProfileCategory();

        Set<By> ignoredElements = ScreenshotComparison.setIgnoredElements(new ArrayList<>(List.of(telegramItemsInput)));

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.SupportPersonalAccount.profilePart, diffPercent, imagePixelSize, ignoredElements);

    }

}
