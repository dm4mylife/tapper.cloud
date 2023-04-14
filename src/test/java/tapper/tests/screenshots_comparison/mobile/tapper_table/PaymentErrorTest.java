package tapper.tests.screenshots_comparison.mobile.tapper_table;


import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.selectors.TapperTable;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.TapperTable.PAYMENT_ERROR_TEXT;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Оплата с ошибкой")
@TakeOrCompareScreenshots()
@SixTableData
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentErrorTest extends ScreenMobileTest {

    SixTableData data = WiFiTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            PaymentErrorTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = data.restaurantName();
    protected final String tableCode = data.tableCode();
    protected final String waiter = data.waiter();
    protected final String apiUri = data.apiUri();
    protected final String tableUrl = data.tableUrl();
    protected final String tableId = data.tableId();

    public static boolean isScreenShot = annotation.isTakeScreenshot();
    Set<By> ignoredElements = ScreenShotComparison.setIgnoredElements(new ArrayList<>(List.of(wiFiIconBy)));

    double diffPercent = getDiffPercent();
    int imagePixelSize = getImagePixelSize();
    String browserTypeSize = getBrowserSizeType();
    static String guid;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    static String tapperTable;
    static String waiterName;
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Ошибка order has expired")
    void createAndFillOrder() throws IOException {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);
        rootPage.ignoreWifiIcon();
        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber,tableNumberRegex);
        waiterName = TapperTable.RootPage.TipsAndCheck.waiterName.getText();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId =
                nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_EXPIRED);

        rootPage.ignoreWifiIcon();
        paymentProcessText.shouldHave(matchText(PAYMENT_ERROR_ORDER_EXPIRED), Duration.ofSeconds(40));
        rootPage.isElementVisible(paymentProcessGifError);
        paymentProcessStatus.shouldHave(matchText(PAYMENT_ERROR_TEXT));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableOrderExpired,diffPercent,imagePixelSize,ignoredElements);

    }

}
