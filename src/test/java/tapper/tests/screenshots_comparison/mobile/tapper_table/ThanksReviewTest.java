package tapper.tests.screenshots_comparison.mobile.tapper_table;

import admin_personal_account.customization.Customization;
import common.BaseActions;
import data.AnnotationAndStepNaming;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;
import total_personal_account_actions.AuthorizationPage;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.AdminPersonalAccount.Customization.reviewTab;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.ReviewPage.review5Stars;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Отзыв")
@DisplayName("Спасибо за отзыв")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThanksReviewTest extends ScreenMobileTest {

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
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;

    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();



    @Test
    @Order(1)
    @DisplayName("Переход в админку и включение отзывов если были выключены.")
    void customization() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

        BaseActions.click(reviewTab);
        customization.clearAllForms();
        customization.fillReviewLinks();
        customization.isReviewCorrect();

    }

    @Test
    @Order(2)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper +
            AnnotationAndStepNaming.DisplayName.TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);
        rootPage.ignoreAllDynamicsElements();

    }

    @Test
    @Order(3)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.saveDataGoToAcquiringTypeDataAndPay)
    void payAndGoToReviewPage() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect(orderType = "full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.isReviewBlockCorrect();

    }

    @Test
    @Order(5)
    @DisplayName("Оставляем позитивный отзыв.")
    void checkPayment() throws IOException {

        BaseActions.click(review5Stars);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize,isScreenShot,
                ScreenLayout.Tapper.thanksReview,diffPercent,imagePixelSize);

    }

}
