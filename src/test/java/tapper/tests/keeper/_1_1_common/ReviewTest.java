package tapper.tests.keeper._1_1_common;


import data.AnnotationAndStepNaming;
import data.TableData;
import data.selectors.TapperTable;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.RegexPattern.TapperTable.tableNumberRegex;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;


@Epic("RKeeper")
@Feature("Общие")
@Story("Проверка всех вариаций рейтинга")
@DisplayName("Проверка всех вариаций рейтинга")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;

    static String tapperTable;
    static String waiterName;
    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
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
        waiterName = TapperTable.RootPage.TipsAndCheck.serviceWorkerName.getText();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect("full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);
        reviewPage.isReviewBlockCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Проверка что определенное количество звезд отображает корректную форму с опциями")
    void isPositiveAndNegativeOptionsCorrect() {

        reviewPage.isPositiveAndNegativeOptionsCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Оставляем негативный отзыв")
    void reviewCorrectNegative() {

        reviewPageNestedTests.reviewCorrectNegative();

    }

    @Test
    @Order(5)
    @DisplayName("Сохраняем данные")
    void saveReviewDataNegative() {

        tapperDataForTgMsg = reviewPageNestedTests.saveReviewData(tapperTable, waiterName,"negative");

    }

    @Test
    @Order(6)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.isTelegramMessageCorrect)
    void matchTgMsg() {

        telegramDataForTgMsg  = rootPageNestedTests.getReviewTgMsgData(guid,"negative");

        Assertions.assertEquals(tapperDataForTgMsg,telegramDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Сохраняем данные")
    void createAndPay() {

        createAndFillOrder();
        payAndGoToReviewPage();

    }

    @Test
    @Order(8)
    @DisplayName("Оставляем позитивный отзыв")
    void checkPayment() {

        reviewPageNestedTests.reviewCorrectPositive();

    }

    @Test
    @Order(9)
    @DisplayName("Сохраняем данные")
    void saveReviewDataForPositive() {

        tapperDataForTgMsg = reviewPageNestedTests.saveReviewData(tapperTable, waiterName,"positive");

    }

    @Test
    @Order(10)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.isTelegramMessageCorrect)
    void matchTgMsgForPositive() {

        telegramDataForTgMsg  = rootPageNestedTests.getReviewTgMsgData(guid,"positive");

        Assertions.assertEquals(tapperDataForTgMsg,telegramDataForTgMsg);


    }

    @Test
    @Order(11)
    @DisplayName("Создаем еще заказ")
    void createAndPayForFewNegativeOptions() {

        createAndFillOrder();
        payAndGoToReviewPage();

    }

    @Test
    @Order(12)
    @DisplayName("Оставляем негативный отзыв c несколькими вариантами пожеланий")
    void reviewCorrectNegativeFewOptions() {

        reviewPageNestedTests.reviewCorrectNegativeFewOptions();

    }

    @Test
    @Order(13)
    @DisplayName("Сохраняем данные")
    void saveReviewDataForFewNegativeOptions() {

        saveReviewDataNegative();

    }

    @Test
    @Order(14)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgForFewNegativeOptions() {

        matchTgMsg();

    }

    @Test
    @Order(15)
    @DisplayName("Создаем еще заказ")
    void createAndPayForFewPositiveOptions() {

        createAndFillOrder();
        payAndGoToReviewPage();

    }

    @Test
    @Order(16)
    @DisplayName("Оставляем позитивный отзыв c несколькими вариантами пожеланий")
    void reviewCorrectForFewPositiveOptions() {

        reviewPageNestedTests.reviewCorrectPositiveWithFewOptions();

    }

    @Test
    @Order(17)
    @DisplayName("Сохраняем данные")
    void saveReviewDataForFewPositiveOptions() {

        saveReviewDataForPositive();

    }

    @Test
    @Order(18)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgForFewPositiveOptions() {

        matchTgMsgForPositive();

    }

}
