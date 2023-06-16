package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.customization.Customization;
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
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AdminPersonalAccount.Customization.reviewTab;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Кастомизация")
@DisplayName("Кастомизация")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomizationTest extends ScreenDesktopTest {

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;
    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
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

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.customization, diffPercent, imagePixelSize);

    }
    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.wiFiInformation)
    void wiFiInformation() throws IOException {

        BaseActions.click(wifiTab);

        customization.isWiFiTabCorrect();
        customization.activateWifiIfDeactivated();
        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
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

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, ScreenLayout.AdminPersonalAccount.reviews,
                diffPercent, imagePixelSize);

    }

}
