package tapper.tests.screenshots_comparison.desktop.support_personal_account;

import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import support_personal_account.logs_and_permissions.LogsAndPermissions;
import tapper_table.RootPage;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.AnnotationAndStepNaming.DisplayName.SupportPersonalAccount.searchRestaurant;
import static data.Constants.TestData.SupportPersonalAccount.*;
import static data.selectors.SupportPersonalAccount.Common.*;
import static data.selectors.SupportPersonalAccount.LogsAndPermissions.Common.currentChosenRestaurant;


@Epic("Тесты по верстке проекта")
@Feature("Администратор техподдержки")
@Story("Логи/доступы")
@DisplayName("Логи/доступы")

@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LogsAndPermissionsTest extends ScreenDesktopTest {

    SixTableData data = LogsAndPermissionsTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            LogsAndPermissionsTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    LogsAndPermissions logsAndPermissions = new LogsAndPermissions();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName(searchRestaurant)
    void searchRestaurant() throws IOException {

        authorizationPage.authorizationUser(SUPPORT_LOGIN_EMAIL, SUPPORT_PASSWORD);

        BaseActions.click(expandLeftMenuButton);
        rootPage.isElementVisible(openedLeftMenuContainer);

        BaseActions.click(logsAndPermissionsCategoryDropdownButton);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.SupportPersonalAccount.restaurantSearch, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.SupportPersonalAccount.logsAndPermissions)
    void logsAndPermissions() throws IOException {

        rootPage.sendKeys(searchRestaurantInput,restaurantName);

        searchResultList.first().shouldHave(matchText(restaurantName), Duration.ofSeconds(5));
        BaseActions.click(searchResultList.first());

        pagePreloader.shouldNotHave(attributeMatching("style", "background: transparent;")
                , Duration.ofSeconds(10));
        pagePreloader.shouldBe(hidden,Duration.ofSeconds(5));

        BaseActions.click(collapseLeftMenuButton);
        rootPage.isElementInvisible(openedLeftMenuContainer);


        currentChosenRestaurant.shouldHave(visible,text(KEEPER_RESTAURANT_NAME));

        logsAndPermissions.isPermissionsTabCorrect();


        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.SupportPersonalAccount.logsAndPermissions, diffPercent, imagePixelSize);

    }


}
