package tapper.tests.screenshots_comparison.mobile.tapper_table;


import api.ApiRKeeper;
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
import tests.BaseTest;
import tests.ScreenDesktopTest;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.ReviewPage.review1Star;
import static data.selectors.TapperTable.ReviewPage.review5Stars;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Отзыв")
@DisplayName("Рейтинг")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            ReviewTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    static String tapperTable;
    static String waiterName;
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper +
            AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.saveDataGoToAcquiringTypeDataAndPay)
    void payAndGoToReviewPage() {

        tapperTable = rootPage.convertSelectorTextIntoStrByRgx(tableNumber,tableNumberRegex);
        waiterName = TapperTable.RootPage.TipsAndCheck.waiterName.getText();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect(orderType = "full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.isReviewBlockCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Оставляем негативный отзыв")
    void reviewCorrectNegative() throws IOException {

        reviewPageNestedTests.reviewCorrectNegative();

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.reviewNegative,diffPercent,imagePixelSize);

    }

    @Test
    @Order(4)
    @DisplayName("Сохраняем данные")
    void createAndPay() {

        createAndFillOrder();
        payAndGoToReviewPage();

    }

    @Test
    @Order(5)
    @DisplayName("Оставляем позитивный отзыв")
    void checkPayment() throws IOException {

        reviewPage.click(review5Stars);
        reviewPage.skipThanksReview();
        reviewPage.typeReviewComment(TEST_REVIEW_COMMENT_POSITIVE);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.reviewPositive,diffPercent,imagePixelSize);

    }

}
