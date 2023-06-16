package tapper.tests.screenshots_comparison.mobile.tapper_table;

import api.ApiRKeeper;
import common.BaseActions;
import data.ScreenLayout;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import layout_screen_compare.ScreenshotComparison;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.ScreenMobileTest;
import tests.TakeOrCompareScreenshots;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static data.ScreenLayout.Tapper.*;
import static data.selectors.TapperTable.ReviewPage.*;
import static data.selectors.TapperTable.RootPage.PayBlock.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tips25;


@Epic("Тесты по верстке проекта (Мобильные)")
@Feature("Стол")
@Story("Заказ")
@DisplayName("Общие тесты")
@TakeOrCompareScreenshots()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TotalTest extends ScreenMobileTest {

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
        rootPage.ignoreAllDynamicsElements();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableWithOrdinaryOrder, diffPercent, imagePixelSize);

    }

    @Test
    @Order(2)
    @DisplayName("Стол с заказом когда разделили счёт")
    void tableWithOrderDivided() throws IOException {

        rootPage.activateDivideCheckSliderIfDeactivated();
        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperTableWithOrdinaryOrderDivide, diffPercent, imagePixelSize);

    }


    @Test
    @Order(3)
    @DisplayName("Выбранные позиции и чаевые")
    void tapperWithTipsAndChosenDishes() throws IOException {

        rootPageNestedTests.chooseAllNonPaidDishes();
        BaseActions.click(tips25);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, tapperWithTipsAndChosenDishes, diffPercent, imagePixelSize);

    }


    @Test
    @Order(4)
    @DisplayName("Оплата картой")
    void openedPaymentByCreditCardModal() throws IOException {

        rootPage.deactivateDivideCheckSliderIfActivated();
        rootPage.changePaymentTypeOnSBP();
        BaseActions.click(paymentButton);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedPaymentByCreditCardModal, diffPercent, imagePixelSize);

        BaseActions.click(paymentOverlay);
        BaseActions.click(cancelProcessPayingContainerSaveBtn);

    }

    @Test
    @Order(5)
    @DisplayName("Форма выбора способа оплаты")
    void openedPaymentOptionsModal() throws IOException {

        BaseActions.click(paymentOptionsContainer);

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, openedPaymentOptionsModal, diffPercent, imagePixelSize);

        BaseActions.click(paymentOverlay);

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");
        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(6)
    @DisplayName("Производится оплата")
    void paymentInProcess() throws IOException {

        rootPage.isElementVisibleDuringLongTime(paymentProcessContainer, 30);
        rootPage.ignoreAllDynamicsElements();
        rootPage.isElementVisible(paymentProcessGifProcessing);
        rootPage.isElementVisible(paymentProcessStatus);
        rootPage.isElementVisible(paymentProcessText);
        paymentProcessStatus.shouldHave(matchText("Производится оплата"), Duration.ofSeconds(30));

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, paymentInProcess, diffPercent,
                imagePixelSize);

    }

    @Test
    @Order(7)
    @DisplayName("Оплата прошла успешно")
    void paymentSuccess() throws IOException {

        paymentProcessStatus.shouldNotHave(text("Оплата не прошла"))
                .shouldHave(matchText("Оплата прошла успешно!"), Duration.ofSeconds(30));
        rootPage.isElementVisible(paymentProcessGifSuccess);

        ScreenshotComparison.isScreenOrDiff(browserTypeSize, isScreenShot, paymentSuccess, diffPercent, imagePixelSize);
    }

    @Test
    @Order(8)
    @DisplayName("Отзыв")
    void isReviewBlockCorrect() throws IOException {

        reviewPage.isReviewBlockCorrect();

        ScreenshotComparison.isScreenOrDiff
                (browserTypeSize, isScreenShot, ScreenLayout.Tapper.reviewPage, diffPercent, imagePixelSize);

    }

}
