package tapper.tests.keeper_e2e._0_common;


import api.ApiRKeeper;
import com.beust.ah.A;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_admin_personal_account.AdminAccount;
import tapper_admin_personal_account.AuthorizationPage;
import tapper_admin_personal_account.operations_history.OperationsHistory;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.QueryParams.rqParamsFillingOrderBasic;
import static api.ApiData.orderData.*;
import static constants.Constant.TestData.*;
import static constants.Constant.TestData.COOKIE_SESSION_FIRST_USER;
import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.emptyOrderHeading;


@Order(8)
@Epic("RKeeper")
@Feature("Общие")
@Story("keeper - комплекс оплат для проверки на странице истории операций в админке")
@DisplayName("keeper - комплекс оплат для проверки на странице истории операций в админке")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_8_OperationsHistoryTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static int amountDishes = 1;
    static HashMap<Integer, HashMap<String, String>> tapperOrderData;
    static HashMap<Integer, HashMap<String, String>> adminOrderData;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    ReviewPage reviewPage = new ReviewPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    OperationsHistory operationsHistory = new OperationsHistory();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    AdminAccount adminAccount = new AdminAccount();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_3, WAITER_ROBOCOP_VERIFIED_WITH_CARD), API_STAGE_URI);
        String visit = rs.jsonPath().getString("result.visit");
        apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "4000"));

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();
    }

    @Test
    @DisplayName("1.2. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishes);
        rootPageNestedTests.activateRandomTipsAndActivateSc();
        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(100,1000)));

    }

    @Test
    @DisplayName("1.3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        tapperOrderData = rootPage.saveOrderDataForOperationsHistoryInAdmin();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

    }

    @Test
    @DisplayName("1.4. Переходим на эквайринг, оплачиваем там")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(transactionId, paymentDataKeeper);
        reviewPage.clickOnFinishButton();

    }

    @Test
    @DisplayName("1.6. Открываем историю операций, проверяем что платёж есть и корректный")
    public void openAdminOperationsHistory() {

        rootPage.openPage(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        rootPage.forceWait(2000);
        authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);
        operationsHistory.goToOperationsHistoryCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData,adminOrderData);

        adminAccount.logOut();

    }

    @Test
    @DisplayName("1.7. Делаем полную оплату на столе")
    public void clearDataAndChoseAgain() {

        rootPage.openTableAndSetGuest(STAGE_RKEEPER_TABLE_3,COOKIE_GUEST_FIRST_USER,COOKIE_SESSION_FIRST_USER);

        savePaymentDataForAcquiring();

    }

    @Test
    @DisplayName("1.8. Производим полную оплату")
    public void payAndGoToAcquiringAgain() {

        rootPageNestedTests.clickPayment();

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isElementVisible(emptyOrderHeading);

    }

    @Test
    @DisplayName("1.9. Переход на эквайринг, ввод данных, полная оплата")
    public void checkFullPayInAdmin() {

        openAdminOperationsHistory();

    }

}
