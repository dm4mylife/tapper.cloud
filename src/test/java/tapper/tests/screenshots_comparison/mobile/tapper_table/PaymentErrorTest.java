package tapper.tests.screenshots_comparison.mobile.tapper_table;


import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.matchText;
import static data.Constants.TestData.TapperTable.PAYMENT_ERROR_ORDER_EXPIRED;
import static data.Constants.TestData.TapperTable.PAYMENT_ERROR_TEXT;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.selectors.TapperTable.ReviewPage.*;

@Disabled
@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Оплата с ошибкой")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentErrorTest extends ScreenMobileTest {

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
    static String guid;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Ошибка order has expired")
    void createAndFillOrder() throws IOException {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);
        rootPage.ignoreAllDynamicsElements();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId =
                nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_EXPIRED);

        rootPage.ignoreAllDynamicsElements();
        paymentProcessText.shouldHave(matchText(PAYMENT_ERROR_ORDER_EXPIRED), Duration.ofSeconds(40));
        rootPage.isElementVisible(paymentProcessGifError);
        paymentProcessStatus.shouldHave(matchText(PAYMENT_ERROR_TEXT));

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableOrderExpired,diffPercent,imagePixelSize);

    }

}
