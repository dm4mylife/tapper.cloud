package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.AdminPersonalAccount.Profile.telegramItemsInput;
import static data.selectors.TapperTable.Common.wiFiIcon;
import static data.selectors.TapperTable.Common.wiFiIconBy;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Вызов официанта")
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CallWaiterTest extends ScreenMobileTest {
    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            PaymentErrorTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();
    Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    RootPage rootPage = new RootPage();


    @Test
    @Order(1)
    @DisplayName("Вызов официанта")
    void callWaiter() throws IOException {

        rootPage.openPage(tableUrl);
        rootPage.ignoreWifiIcon();
        rootPage.openCallWaiterForm();

        rootPage.isCallContainerWaiterCorrect();

        rootPage.sendWaiterComment();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartOne, diffPercent,
                        imagePixelSize,ignoredElements);

        rootPage.typeTextToGetSpecialMessage();

        rootPage.isSendSuccessful();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedCallWaiterWithReceiptMessagePartTwo, diffPercent,
                        imagePixelSize,ignoredElements);

    }

}
