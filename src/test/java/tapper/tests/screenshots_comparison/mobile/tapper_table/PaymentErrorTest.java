package tapper.tests.screenshots_comparison.mobile.tapper_table;


import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.selectors.TapperTable;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.TapperTable.PAYMENT_ERROR_TEXT;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Оплата с ошибкой")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentErrorTest extends ScreenMobileTest {
    static TakeOrCompareScreenshots annotation =
            PaymentErrorTest.class.getAnnotation(TakeOrCompareScreenshots.class);
    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot();
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

        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber,tableNumberRegex);
        waiterName = TapperTable.RootPage.TipsAndCheck.waiterName.getText();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        transactionId =
                nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_EXPIRED);

        paymentProcessText.shouldHave(matchText(PAYMENT_ERROR_ORDER_EXPIRED), Duration.ofSeconds(40));
        rootPage.isElementVisible(paymentProcessGifError);
        paymentProcessStatus.shouldHave(matchText(PAYMENT_ERROR_TEXT));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.tapperTableOrderExpired,diffPercent,imagePixelSize);

    }

}
