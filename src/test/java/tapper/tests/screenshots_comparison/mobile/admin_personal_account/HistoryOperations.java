package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

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


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("История операций")
@DisplayName("История операций")
@Disabled
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HistoryOperations extends ScreenMobileTest {
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
    RootPage rootPage = new RootPage();

    AuthorizationPage authorizationPage = new AuthorizationPage();
    admin_personal_account.history_operations.HistoryOperations historyOperations =
            new admin_personal_account.history_operations.HistoryOperations();
    

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperations)
    void historyOperations() throws IOException {


        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        historyOperations.goToHistoryOperationsCategory();

        historyOperations.isHistoryOperationsCorrect();

        ScreenshotComparison.isScreenOrDiff(browserSizeType,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperations, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperationsWeekFilter)
    void weekFilter() throws IOException {

        historyOperations.isWeekPeriodCorrect();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperationsWeekFilter, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperationsMonthFilter)
    void tableDetailCard() throws IOException {

        historyOperations.isMonthPeriodCorrect();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperationsMonthFilter, diffPercent, imagePixelSize);

    }

}
