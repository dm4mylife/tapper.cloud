package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.config_notifications.ConfigNotifications;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Настройка уведомлений")
@DisplayName("Настройка уведомлений")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigNotificationsTest extends ScreenDesktopTest {

    static TakeOrCompareScreenshots annotation =
            ConfigNotificationsTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = TableData.Keeper.Table_666.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_666.tableCode;
    protected final String waiter = TableData.Keeper.Table_666.waiter;
    protected final String apiUri = TableData.Keeper.Table_666.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_666.tableUrl;
    protected final String tableId = TableData.Keeper.Table_666.tableId;


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

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                        ScreenLayout.AdminPersonalAccount.configNotifications,diffPercent,imagePixelSize);

    }

}
