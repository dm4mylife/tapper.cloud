package tapper.tests.screenshots_comparison.mobile.support_personal_account;

import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
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
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileTest extends ScreenMobileTest {

    SixTableData data = ProfileTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            ProfileTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    boolean isScreenShot = annotation.isTakeScreenshot();
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

        ScreenShotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, ScreenLayout.SupportPersonalAccount.rootPage,
                diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.profilePage)
    void profile() throws IOException {

        profile.goToProfileCategory();

        Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(telegramItemsInput)));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.SupportPersonalAccount.profilePart, diffPercent, imagePixelSize, ignoredElements);

    }

}
