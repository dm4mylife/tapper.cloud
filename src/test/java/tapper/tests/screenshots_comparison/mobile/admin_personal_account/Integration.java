package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.integrations.Integrations;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tests.ScreenMobileTest;
import data.table_data_annotation.SixTableData;
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
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Integration extends ScreenMobileTest {

    SixTableData data = Integration.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            Integration.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    Integrations integrations = new Integrations();
    RootPage rootPage = new RootPage();
    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.integrations)
    void integrations() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        rootPage.forceWait(WAIT_FOR_FULL_LOAD_PAGE);
        integrations.goToIntegrationsCategory();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.integrations, diffPercent, imagePixelSize);

    }

}
