package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.integrations.Integrations;
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

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.WAIT_FOR_FULL_LOAD_PAGE;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Интеграции")
@DisplayName("Интеграции")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Integration extends ScreenMobileTest {

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
    Integrations integrations = new Integrations();
    RootPage rootPage = new RootPage();
    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.integrations)
    void integrations() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        rootPage.forceWait(WAIT_FOR_FULL_LOAD_PAGE);
        integrations.goToIntegrationsCategory();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.integrations, diffPercent, imagePixelSize);

    }

}
