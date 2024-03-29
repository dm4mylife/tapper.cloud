package tapper.tests.screenshots_comparison.mobile.tapper_table;

import admin_personal_account.customization.Customization;
import api.ApiRKeeper;
import common.BaseActions;
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
import java.time.Duration;

import static com.codeborne.selenide.Condition.hidden;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.ScreenLayout.Tapper.openedWifi;
import static data.ScreenLayout.Tapper.wifiIcon;
import static data.selectors.AdminPersonalAccount.Customization.wifiTab;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.Common.wiFiCloseButton;
import static data.selectors.TapperTable.Common.wiFiIcon;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Проверка вайфая")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WiFiTest extends ScreenMobileTest {


    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;
    int adminTab = 0;
    int tappetTab = 1;
    RootPage rootPage = new RootPage();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    boolean isScreenShot = getClass().getAnnotation(TakeOrCompareScreenshots.class).isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    @Test
    @Order(1)
    @DisplayName("Стол с вайфаем")
    void wifiIcon() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();
        BaseActions.click(wifiTab);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        customization.isWiFiTabCorrect();
        customization.activateWifiIfDeactivated();
        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME, TEST_WIFI_NETWORK_PASSWORD);

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.skipStartScreenLogo();
        rootPage.isElementVisibleAndClickable(wiFiIcon);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, wifiIcon, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName("Открытая форма вайфая")
    void openedWifi() throws IOException {

        BaseActions.click(wiFiIcon);
        rootPage.isWifiContainerCorrect(true);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedWifi, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName("Максимальной длинны пароль от вайфая")
    void setWifiMaxSizePassword() throws IOException {

        rootPage.switchBrowserTab(adminTab);
        customization.setWifiConfiguration(TEST_WIFI_NETWORK_NAME,TEST_WIFI_NETWORK_PASSWORD_MAX_LENGTH);
        rootPage.switchBrowserTab(tappetTab);
        rootPage.refreshPage();
        BaseActions.click(wiFiIcon);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.wifiInformationMaxPassword, diffPercent, imagePixelSize);

    }

    @Test
    @Order(4)
    @DisplayName("Вайфай без пароля")
    void withoutPassword() throws IOException {

        BaseActions.click(wiFiCloseButton);
        rootPage.switchBrowserTab(adminTab);
        customization.setWifiConfigurationWithoutPassword(TEST_WIFI_NETWORK_NAME);
        rootPage.switchBrowserTab(tappetTab);
        rootPage.refreshPage();
        rootPage.checkWiFiOnTapperTableWithoutPassword(TEST_WIFI_NETWORK_NAME);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.wifiInformationWithoutPassword, diffPercent, imagePixelSize);

        rootPage.switchBrowserTab(adminTab);
        customization.deactivateWifiIfActivated();

        rootPage.switchBrowserTab(tappetTab);
        rootPage.refreshPage();
        rootPage.skipStartScreenLogo();

        wiFiIcon.shouldBe(hidden);

    }

}
