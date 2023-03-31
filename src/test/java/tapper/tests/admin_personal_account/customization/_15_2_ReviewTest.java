package tapper.tests.admin_personal_account.customization;

import admin_personal_account.customization.Customization;
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
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.AdminPersonalAccount.Customization.reviewTab;
import static data.selectors.AdminPersonalAccount.Profile.pagePreloader;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.ReviewPage.review5Stars;

@Order(152)
@Epic("Личный кабинет администратора ресторана")
@Feature("Кастомизация")
@Story("Проверка Отзывы на внешних сервисах")
@DisplayName("Проверка Отзывы на внешних сервисах")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _15_2_ReviewTest extends PersonalAccountTest {

    static String guid;
    static int amountDishesForFillingOrder = 3;
    static HashMap<String, String> paymentDataKeeper;
    static String orderType = "full";
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
    @DisplayName("1.1. Авторизация под администратором в личном кабинете")
    public void authorizeUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @DisplayName("1.2. Переход на страницу кастомизации, проверка всех элементов")
    public void goToCustomizationCategory() {

        customization.goToCustomizationCategory();
        customization.isCustomizationCategoryCorrect();

    }

    @Test
    @DisplayName("1.3. Переходим на Отзывы на внешних сервисах, проверяем формы")
    public void goToReviewTab() {

        rootPage.click(reviewTab);
        pagePreloader.shouldBe(hidden, Duration.ofSeconds(5));
        customization.isReviewCorrect();

    }

    @Test
    @DisplayName("1.4. Вводим некорректную ссылку, чтобы получить ошибку")
    public void clearAllForms() {

        customization.clearAllForms();
        customization.typeIncorrectLink();

    }

    @Test
    @DisplayName("1.5. Пытаемся сохранить пустую ссылку, чтобы получить ошибку")
    public void saveWithEmptyLinks() {

        customization.saveWithEmptyLinks();

    }

    @Test
    @DisplayName("1.6. Активируем форму")
    public void fillReviewLinks() {

        customization.fillReviewLinks();

    }

    @Test
    @DisplayName("1.7. Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_555,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_555,TABLE_AUTO_555_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.8. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void payAndGoToReviewPage() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        transactionId = nestedTests.acquiringPayment(totalPay);
        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(15));
        reviewPageNestedTests.paymentCorrect(orderType = "full");
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.9. Проверяем 'Отзывы на внешних сервисах'")
    public void isThanksForReviewCorrect() {

        rootPage.click(review5Stars);
        reviewPage.isThanksForReviewCorrect();

    }

}
