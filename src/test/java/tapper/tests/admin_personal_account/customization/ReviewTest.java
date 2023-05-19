package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
import api.ApiRKeeper;
import common.BaseActions;
import data.TableData;
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
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_555;
import static data.selectors.AdminPersonalAccount.Customization.reviewTab;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.ReviewPage.review5Stars;

@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка Отзывы на внешних сервисах")
@DisplayName("Проверка Отзывы на внешних сервисах")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewTest extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    static String guid;
    static int amountDishesForFillingOrder = 3;
    static HashMap<String, String> paymentDataKeeper;
    RootPage rootPage = new RootPage();
    static String transactionId;
    static double totalPay;
    AuthorizationPage authorizationPage = new AuthorizationPage();
    Customization customization = new Customization();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();


    @Test
    @Order(1)
    @DisplayName("Авторизация под администратором в личном кабинете")
    void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу кастомизации, проверка всех элементов")
    void goToCustomizationCategory() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Переходим на Отзывы на внешних сервисах, проверяем формы")
    void goToReviewTab() {

        BaseActions.click(reviewTab);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        customization.isReviewCorrect();

    }

    @Test
    @Order(4)
    @DisplayName("Вводим некорректную ссылку, чтобы получить ошибку")
    void clearAllForms() {

        customization.clearAllForms();
        customization.typeIncorrectLink();

    }

    @Test
    @Order(5)
    @DisplayName("Пытаемся сохранить пустую ссылку, чтобы получить ошибку")
    void saveWithEmptyLinks() {

        customization.saveWithEmptyLinks();

    }

    @Test
    @Order(6)
    @DisplayName("Активируем форму")
    void fillReviewLinks() {

        customization.fillReviewLinks();

    }

    @Test
    @Order(7)
    @DisplayName("Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode,waiterName,
                apiUri,dishesForFillingOrder,tableUrl,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(8)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void payAndGoToReviewPage() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect("full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @Order(9)
    @DisplayName("Проверяем 'Отзывы на внешних сервисах'")
    void isThanksForReviewCorrect() {

        BaseActions.click(review5Stars);
        reviewPage.isThanksForReviewCorrect();

    }

}
