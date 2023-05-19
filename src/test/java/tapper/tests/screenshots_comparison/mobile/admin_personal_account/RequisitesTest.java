package tapper.tests.screenshots_comparison.mobile.admin_personal_account;

import admin_personal_account.company_requisites.CompanyRequisites;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.AnnotationAndStepNaming.DisplayName.AdminPersonalAccount.requisites;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Администратор ресторана")
@Story("Реквизиты")
@DisplayName("Реквизиты")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RequisitesTest extends ScreenMobileTest {

    SixTableData data = RequisitesTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            RequisitesTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    CompanyRequisites companyRequisites = new CompanyRequisites();

    @Test
    @Order(1)
    @DisplayName(requisites)
    void requisites() throws IOException {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        companyRequisites.goToCompanyRequisitesCategory();
        companyRequisites.isCompanyRequisitesCategoryCorrect();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.AdminPersonalAccount.requisites, diffPercent, imagePixelSize);

    }

}
