package tapper.tests.screenshots_comparison.desktop.admin_personal_account;

import admin_personal_account.tables_and_qr_codes.TablesAndQrCodes;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenDesktopTest;
import data.table_data_annotation.SixTableData;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Тесты по верстке проекта")
@Feature("Администратор ресторана")
@Story("Столики и QR-коды")
@DisplayName("Столики и QR-коды")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TablesAndQrCodesTest extends ScreenDesktopTest {

    SixTableData data = TablesAndQrCodesTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            TablesAndQrCodesTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    TablesAndQrCodes tablesAndQrCodes = new TablesAndQrCodes();
    

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.tablesAndQRCodes)
    void tablesAndQrCodes() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        tablesAndQrCodes.goToTablesAndQrCodesCategory();
        tablesAndQrCodes.isTableAndQrCodesCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.tablesAndQRCodes, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.tablesAndQRCodesSearch)
    void tableSearch() throws IOException {

        int fromTableSearchValue = 1;
        int toTableSearchValue = 5;

        tablesAndQrCodes.searchTableRangeWithoutReset(fromTableSearchValue, toTableSearchValue);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.tablesAndQRCodesSearch, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.tableDetailCard)
    void tableDetailCard() throws IOException {

        tablesAndQrCodes.goToDetailTableCard();
        tablesAndQrCodes.areTableCardElementCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.tableDetailCard, diffPercent, imagePixelSize);

    }

}
