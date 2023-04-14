package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import data.ScreenLayout;
import data.table_data_annotation.SixTableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.Common.wiFiIconBy;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tips25;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Общие тесты")
@SixTableData
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends ScreenMobileTest {
    SixTableData data = ThanksReviewTest.class.getAnnotation(SixTableData.class);
    static TakeOrCompareScreenshots annotation =
            TotalTest.class.getAnnotation(TakeOrCompareScreenshots.class);

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

    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    static String guid;
    static double totalPay;

    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ReviewPage reviewPage = new ReviewPage();

    @Test
    @Order(1)
    @DisplayName("Стол с заказом")
    void tableWithOrder() throws IOException {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 2);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter,
                apiUri, dishesForFillingOrder, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openNotEmptyTable(tableUrl);
        rootPage.ignoreWifiIcon();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableWithOrdinaryOrder, diffPercent, imagePixelSize,
                        ignoredElements);

    }

    @Test
    @Order(2)
    @DisplayName("Стол с заказом когда поделились")
    void tableWithOrderDivided() throws IOException {

        rootPage.activateDivideCheckSliderIfDeactivated();
        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableWithOrdinaryOrderDivide, diffPercent, imagePixelSize,
                        ignoredElements);

    }


    @Test
    @Order(3)
    @DisplayName("Выбранные позиции и чаевые")
    void tapperWithTipsAndChosenDishes() throws IOException {

        rootPageNestedTests.chooseAllNonPaidDishes();
        rootPage.click(tips25);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperWithTipsAndChosenDishes, diffPercent, imagePixelSize,
                        ignoredElements);

    }


    @Test
    @Order(4)
    @DisplayName("Оплата картой")
    void openedPaymentByCreditCardModal() throws IOException {

        rootPage.deactivateDivideCheckSliderIfActivated();
        rootPage.changePaymentTypeOnSBP();
        rootPage.click(paymentButton);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedPaymentByCreditCardModal, diffPercent, imagePixelSize,
                        ignoredElements);

        rootPage.click(paymentOverlay);
        rootPage.click(cancelProcessPayingContainerSaveBtn);

    }

    @Test
    @Order(5)
    @DisplayName("Форма выбора способа оплаты")
    void openedPaymentOptionsModal() throws IOException {

        rootPage.click(paymentOptionsContainer);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedPaymentOptionsModal, diffPercent, imagePixelSize,
                        ignoredElements);

        rootPage.click(paymentOverlay);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);
        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(6)
    @DisplayName("Производится оплата")
    void paymentInProcess() throws IOException {

        rootPage.isElementVisibleDuringLongTime(paymentProcessContainer, 30);
        rootPage.ignoreWifiIcon();
        rootPage.isElementVisible(paymentProcessGifProcessing);
        rootPage.isElementVisible(paymentProcessStatus);
        rootPage.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, paymentInProcess, diffPercent,
                imagePixelSize,ignoredElements);

    }

    @Test
    @Order(7)
    @DisplayName("Оплата прошла успешно")
    void paymentSuccess() throws IOException {

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        rootPage.isElementVisible(paymentProcessGifSuccess);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, paymentSuccess, diffPercent, imagePixelSize,
                ignoredElements);
    }

    @Test
    @Order(8)
    @DisplayName("Отзыв")
    void isReviewBlockCorrect() throws IOException {

        reviewPage.isReviewBlockCorrect();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, ScreenLayout.Tapper.reviewPage, diffPercent, imagePixelSize,
                        ignoredElements);

    }

}
