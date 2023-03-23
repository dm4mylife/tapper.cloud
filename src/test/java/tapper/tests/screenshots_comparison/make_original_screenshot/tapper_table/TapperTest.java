package tapper.tests.screenshots_comparison.make_original_screenshot.tapper_table;

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
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.MakeOriginalScreenshotBaseTest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static data.AnnotationAndStepNaming.DisplayName.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_666;
import static data.Constants.WAIT_FOR_DELETE_ARTEFACT_BEFORE_SCREEN;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.PayBlock.*;


@Epic("Тесты по верстке проекта")
@Feature("Стол таппера")
@Story("Оригинал - Пустой стол")
@DisplayName("Оригинал - Пустой стол")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TapperTest extends MakeOriginalScreenshotBaseTest {

    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_666;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_666;
    protected final String tableId = TABLE_AUTO_666_ID;


    boolean isScreenShot = MakeOriginalScreenshotBaseTest.isScreenShot;
    double diffPercent = MakeOriginalScreenshotBaseTest.diffScreenPercent;
    int imagePixelSize = MakeOriginalScreenshotBaseTest.imagePixelSize;


    static HashMap<String, Integer> paymentDataKeeper;
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

        ScreenShotComparison.isScreenOrDiff(isScreenShot, tapperTableEmpty, diffPercent, imagePixelSize);

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
        ScreenShotComparison.isScreenOrDiff(isScreenShot, tapperTableWithOrdinaryOrder, diffPercent, imagePixelSize);

    }

    @Test
    @Order(3)
    @DisplayName("Вызов официанта")
    void callWaiter() throws IOException {

        rootPage.openCallWaiterForm();
        rootPage.isCallContainerWaiterCorrect();
        rootPage.sendWaiterComment();

        ScreenShotComparison.isScreenOrDiff
                (isScreenShot, openedCallWaiterWithReceiptMessage, diffPercent, imagePixelSize);

        rootPage.closeCallWaiterFormByCloseButton();

    }

    @Test
    @Order(4)
    @DisplayName("Оплата картой")
    void openedPaymentByCreditCardModal() throws IOException {

        rootPage.deactivateDivideCheckSliderIfActivated();
        rootPage.changePaymentTypeOnSBP();
        rootPage.click(paymentButton);

        ScreenShotComparison.isScreenOrDiff(isScreenShot, openedPaymentByCreditCardModal, diffPercent, imagePixelSize);

        rootPage.click(paymentOverlay);
        rootPage.click(cancelProcessPayingContainerSaveBtn);

    }

    @Test
    @Order(5)
    @DisplayName("Форма выбора способа оплаты")
    void openedPaymentOptionsModal() throws IOException {

        rootPage.click(paymentOptionsContainer);

        ScreenShotComparison.isScreenOrDiff(isScreenShot, openedPaymentOptionsModal, diffPercent, imagePixelSize);

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

        rootPage.isElementVisible(paymentProcessGifProcessing);
        rootPage.isElementVisible(paymentProcessStatus);
        rootPage.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        ScreenShotComparison.isScreenOrDiff(isScreenShot, paymentInProcess, diffPercent, imagePixelSize);

    }

    @Test
    @Order(7)
    @DisplayName("Оплата прошла успешно")
    void paymentSuccess() throws IOException {

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        rootPage.isElementVisible(paymentProcessGifSuccess);

        ScreenShotComparison.isScreenOrDiff(isScreenShot, paymentSuccess, diffPercent, imagePixelSize);
    }

    @Test
    @Order(8)
    @DisplayName("Отзыв")
    void isReviewBlockCorrect() throws IOException {

        reviewPage.isReviewBlockCorrect();

        ScreenShotComparison.isScreenOrDiff(isScreenShot, ScreenLayout.Tapper.reviewPage, diffPercent, imagePixelSize);

    }

}
