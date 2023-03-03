package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_REVIEW;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.tableNumber;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.waiterName;


@Order(6)
@Epic("RKeeper")
@Feature("Общие")
@Story("Проверка всех вариаций рейтинга")
@DisplayName("Проверка всех вариаций рейтинга")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_6_ReviewTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;

    static String tapperTable;
    static String waiter;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @DisplayName("1.0. Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_111,TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.1. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void payAndGoToReviewPage() {

        tapperTable = "Номер столика: " + rootPage.convertSelectorTextIntoStrByRgx(tableNumber,"\\D+");
        waiter = waiterName.getText();

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect(orderType = "full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.2. Проверка что определенное количество звезд отображает корректную форму с опциями")
    public void isPositiveAndNegativeOptionsCorrect() {

        reviewPage.isReviewBlockCorrect();
        reviewPage.isPositiveAndNegativeOptionsCorrect();

    }

    @Test
    @DisplayName("1.3. Оставляем негативный отзыв")
    public void reviewCorrectNegative() {

        reviewPageNestedTests.reviewCorrectNegative();

    }

    @Test
    @DisplayName("1.4. Сохраняем данные")
    public void saveReviewData() {

        tapperDataForTgMsg = reviewPageNestedTests.saveReviewData(tapperTable, waiter,"negative");

    }

    @Test
    @DisplayName("1.5. Проверка сообщения в телеграмме")
    public void matchTgMsg() {

        telegramDataForTgMsg = rootPage.getTapperDataForReviewMsg
                (tapperTable, waiter,TEST_REVIEW_COMMENT_NEGATIVE,tapperDataForTgMsg.get("stars")
                        ,tapperDataForTgMsg.get("suggestions"));


    }

    @Test
    @DisplayName("1.6. Сохраняем данные")
    public void createAndPay() {

        createAndFillOrder();
        payAndGoToReviewPage();

    }

    @Test
    @DisplayName("1.7. Оставляем позитивный отзыв")
    public void checkPayment() {

        reviewPageNestedTests.reviewCorrectPositive();

    }

    @Test
    @DisplayName("1.8. Сохраняем данные")
    public void saveReviewDataForPositive() {

        tapperDataForTgMsg = reviewPageNestedTests.saveReviewData(tapperTable, waiter,"negative");

    }

    @Test
    @DisplayName("1.9. Проверка сообщения в телеграмме")
    public void matchTgMsgForPositive() {

        telegramDataForTgMsg = rootPage.getTapperDataForReviewMsg
                (tapperTable, waiter,TEST_REVIEW_COMMENT_NEGATIVE,tapperDataForTgMsg.get("stars")
                        ,tapperDataForTgMsg.get("suggestions"));


    }

}
