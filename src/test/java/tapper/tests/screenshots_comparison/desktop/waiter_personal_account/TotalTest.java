package tapper.tests.screenshots_comparison.desktop.waiter_personal_account;


import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;

import static data.AnnotationAndStepNaming.DisplayName.WaiterPersonalAccount.waiterProfile;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;


@Epic("Тесты по верстке проекта")
@Feature("Личный кабинет официанта")
@Story("Профиль")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends ScreenDesktopTest {

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

    AuthorizationPage authorizationPage = new AuthorizationPage();

    RootPage rootPage = new RootPage();

    @Test
    @DisplayName(waiterProfile)
    void createAndFillOrder() throws IOException {

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

        rootPage.scrollTillBottom();

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.WaiterPersonalAccount.profilePartOne, diffPercent, imagePixelSize);


    }

}
