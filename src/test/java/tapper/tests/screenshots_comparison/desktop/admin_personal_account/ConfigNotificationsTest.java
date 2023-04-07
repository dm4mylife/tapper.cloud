package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.config_notifications.ConfigNotifications;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Настройка уведомлений")
@DisplayName("Настройка уведомлений")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigNotificationsTest extends ScreenDesktopTest {

    static TakeOrCompareScreenshots annotation =
            ConfigNotificationsTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;


    public static boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();

    AuthorizationPage authorizationPage = new AuthorizationPage();
    ConfigNotifications configNotifications = new ConfigNotifications();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.configNotifications)
    void configNotifications() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        configNotifications.goToConfigNotificationsCategory();
        configNotifications.isConfigNotificationsCategoryCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.configNotifications,diffPercent,imagePixelSize);

    }

}
