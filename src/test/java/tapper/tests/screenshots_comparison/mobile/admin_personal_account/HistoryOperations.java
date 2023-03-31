package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenMobileTest;
import tests.SixTableData;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static admin_personal_account.operations_history.HistoryOperations.ignoredArraySelectorsInHistoryOperations;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("История операций")
@DisplayName("История операций")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HistoryOperations extends ScreenMobileTest {

    SixTableData data = HistoryOperations.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            HistoryOperations.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    admin_personal_account.operations_history.HistoryOperations historyOperations =
            new admin_personal_account.operations_history.HistoryOperations();
    

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperations)
    void historyOperations() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        historyOperations.goToHistoryOperationsCategory();
        historyOperations.isHistoryOperationsCorrect();

        ScreenShotComparison.isScreenOrDiff(browserSizeType,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperations, diffPercent, imagePixelSize,
                ignoredArraySelectorsInHistoryOperations);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperationsWeekFilter)
    void weekFilter() throws IOException {

        historyOperations.isWeekPeriodCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperationsWeekFilter, diffPercent, imagePixelSize,
                ignoredArraySelectorsInHistoryOperations);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.historyOperationsMonthFilter)
    void tableDetailCard() throws IOException {

        historyOperations.isMonthPeriodCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.historyOperationsMonthFilter, diffPercent, imagePixelSize,
                ignoredArraySelectorsInHistoryOperations);

    }

}
