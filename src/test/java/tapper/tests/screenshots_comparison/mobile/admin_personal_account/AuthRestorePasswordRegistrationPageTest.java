package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.RegistrationPage;
import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.AnnotationAndStepNaming.DisplayName.AuthorizationAndRegistrationAdminPage.restorePasswordPage;
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

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;

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

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, authorizePage,diffPercent,imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(restorePasswordPage)
    void goToRestorePasswordPage() throws IOException {

        BaseActions.click(forgotPasswordLink);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.Tapper.restorePasswordPage,diffPercent,imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AuthorizationAndRegistrationAdminPage.registrationPage)
    void goToRegistrationPage() throws IOException {

        registrationPage.goToRegistrationPage();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.Tapper.registrationPage,diffPercent,imagePixelSize);

    }

}
