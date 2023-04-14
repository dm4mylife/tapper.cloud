package tapper.tests.screenshots_comparison.mobile.tapper_table;


import api.ApiRKeeper;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Уведомление об изменении цены")
@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChangedOrderSumNotificationTest extends ScreenMobileTest {
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
    static String guid;
    static int amountDishesForFillingOrder = 1;
    NestedTests nestedTests = new NestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();

    @Test
    @Order(1)
    @DisplayName("Изменение цены")
    void createAndFillOrder() throws IOException {

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);
        rootPage.ignoreWifiIcon();
        apiRKeeper.createDishObject(dishes, BARNOE_PIVO, 1);
        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishes));

        rootPage.clickOnPaymentButton();

        dishesSumChangedHeading.shouldHave(visible);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperChangeDishSum,diffPercent,imagePixelSize,ignoredElements);

        apiRKeeper.closedOrderByApi(restaurantName,tableId,guid,apiUri);

    }

}
