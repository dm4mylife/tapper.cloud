package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.RegistrationPage;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static api.ApiData.orderData.*;
import static data.AnnotationAndStepNaming.DisplayName.AuthorizationAndRegistrationAdminPage.restorePasswordPage;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.ScreenLayout.Tapper.authorizePage;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.forgotPasswordLink;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Страница авторизации,регистрации,восстановления администратора ресторана")
@DisplayName("Страница авторизации,регистрации,восстановления администратора ресторана")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthRestorePasswordRegistrationPageTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            AuthRestorePasswordRegistrationPageTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;

    boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();


    RegistrationPage registrationPage = new RegistrationPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();


    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AuthorizationAndRegistrationAdminPage.authorizationPage)
    void goToAuthorizationPage() throws IOException {

        authorizationPage.goToAuthorizationPage();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, authorizePage,diffPercent,imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(restorePasswordPage)
    void goToRestorePasswordPage() throws IOException {

        rootPage.click(forgotPasswordLink);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.Tapper.restorePasswordPage,diffPercent,imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AuthorizationAndRegistrationAdminPage.registrationPage)
    void goToRegistrationPage() throws IOException {

        registrationPage.goToRegistrationPage();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.Tapper.registrationPage,diffPercent,imagePixelSize);

    }

}
