package tapper.tests.screenshots_comparison.desktop.waiter_personal_account;


import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tests.ScreenDesktopTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;
import waiter_personal_account.Waiter;

import java.io.IOException;

import static data.AnnotationAndStepNaming.DisplayName.WaiterPersonalAccount.waiterProfile;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.WAITER_PASSWORD;


@Epic("Тесты по верстке проекта")
@Feature("Личный кабинет официанта")
@Story("Профиль")
@DisplayName("Проверка всех элементов, смены имени, телеграмма, пароля, загрузка изображений, сверка со столом")
@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TotalTest extends ScreenDesktopTest {

    SixTableData data = TotalTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            TotalTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    Waiter waiterPage = new Waiter();
    RootPage rootPage = new RootPage();

    @Test
    @DisplayName(waiterProfile)
    void createAndFillOrder() throws IOException {

        authorizationPage.authorizationUser(WAITER_LOGIN_EMAIL, WAITER_PASSWORD);

        rootPage.scrollTillBottom();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize, isScreenShot,
                ScreenLayout.WaiterPersonalAccount.profilePartOne, diffPercent, imagePixelSize);


    }

}
