package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.customization.Customization;
import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
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

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Customization.reviewTab;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Кастомизация")
@DisplayName("Кастомизация")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomizationTest extends ScreenMobileTest {

    SixTableData data = CustomizationTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            CustomizationTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    boolean isScreenShot = annotation.isTakeScreenshot();
    String browserTypeSize = getBrowserSizeType();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();

    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();
    RootPage rootPage = new RootPage();


    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.customization)
    void customization() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.customization, diffPercent, imagePixelSize);

    }
    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.wiFiInformation)
    void wiFiInformation() throws IOException {

        BaseActions.click(wifiTab);
        customization.isWiFiTabCorrect();
        rootPage.forceWait(1000);
        customization.activateWifiIfDeactivated();
        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.wifiInformation, diffPercent, imagePixelSize);

    }
    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.reviews)
    void review() throws IOException {

        BaseActions.click(reviewTab);
        customization.clearAllForms();
        customization.fillReviewLinks();
        customization.isReviewCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, ScreenLayout.AdminPersonalAccount.reviews,
                diffPercent, imagePixelSize);

    }

}
