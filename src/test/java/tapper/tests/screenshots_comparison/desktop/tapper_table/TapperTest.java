package tapper.tests.screenshots_comparison.desktop.tapper_table;

import api.ApiRKeeper;
import data.ScreenLayout;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenShotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.TakeOrCompareScreenshots;
import tests.ScreenMobileTest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.PayBlock.*;


@Epic("Тесты по верстке проекта")
@Feature("Стол таппера")
@Story("Стол таппера")
@DisplayName("Стол таппера")

@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TapperTest extends ScreenMobileTest {

    static TakeOrCompareScreenshots annotation =
            TapperTest.class.getAnnotation(TakeOrCompareScreenshots.class);

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;

    public static boolean isScreenShot = annotation.isTakeScreenshot() ;
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
    @DisplayName("Пустой стол")
    void emptyTable() throws IOException {

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openPage(tableUrl);
        rootPage.isEmptyOrderAfterClosing();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize);

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, 2);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter,
                apiUri, dishesForFillingOrder, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(2)
    @DisplayName("Стол с заказом")
    void tableWithOrder() throws IOException {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();
        rootPage.deactivateDivideCheckSliderIfActivated();
        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, tapperTableWithOrdinaryOrder, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName("Стол с заказом когда поделились")
    void tableWithOrderDivided() throws IOException {

        rootPage.activateDivideCheckSliderIfDeactivated();
        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, tapperTableWithOrdinaryOrderDivide, diffPercent, imagePixelSize);

    }


    @Test
    @Order(4)
    @DisplayName("Вызов официанта")
    void callWaiter() throws IOException {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.openCallWaiterForm();
        rootPage.isCallContainerWaiterCorrect();
        rootPage.sendWaiterComment();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, openedCallWaiterWithReceiptMessage, diffPercent, imagePixelSize);

        rootPage.closeCallWaiterFormByCloseButton();

    }

    @Test
    @Order(5)
    @DisplayName("Оплата картой")
    void openedPaymentByCreditCardModal() throws IOException {

        rootPage.deactivateDivideCheckSliderIfActivated();
        rootPage.changePaymentTypeOnSBP();
        rootPage.click(paymentButton);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, openedPaymentByCreditCardModal, diffPercent, imagePixelSize);

        rootPage.click(paymentOverlay);
        rootPage.click(cancelProcessPayingContainerSaveBtn);

    }

    @Test
    @Order(6)
    @DisplayName("Форма выбора способа оплаты")
    void openedPaymentOptionsModal() throws IOException {

        rootPage.click(paymentOptionsContainer);

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, openedPaymentOptionsModal, diffPercent, imagePixelSize);

        rootPage.click(paymentOverlay);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);
        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(7)
    @DisplayName("Производится оплата")
    void paymentInProcess() throws IOException {

        rootPage.isElementVisibleDuringLongTime(paymentProcessContainer, 30);

        rootPage.isElementVisible(paymentProcessGifProcessing);
        rootPage.isElementVisible(paymentProcessStatus);
        rootPage.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, paymentInProcess, diffPercent, imagePixelSize);

    }

    @Test
    @Order(8)
    @DisplayName("Оплата прошла успешно")
    void paymentSuccess() throws IOException {

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        rootPage.isElementVisible(paymentProcessGifSuccess);

        ScreenShotComparison.isScreenOrDiff(browserTypeSize,isScreenShot, paymentSuccess, diffPercent, imagePixelSize);
    }

    @Test
    @Order(9)
    @DisplayName("Отзыв")
    void isReviewBlockCorrect() throws IOException {

        reviewPage.isReviewBlockCorrect();

        ScreenShotComparison.isScreenOrDiff
                (browserTypeSize,isScreenShot, ScreenLayout.Tapper.reviewPage, diffPercent, imagePixelSize);

    }

}
