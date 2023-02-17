package tapper.tests.keeper_e2e._0_common;


import admin_personal_account.operations_history.OperationsHistory;
import api.ApiRKeeper;
import data.Constants;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;


@Order(8)
@Epic("RKeeper")
@Feature("Общие")
@Story("keeper - комплекс оплат для проверки на странице истории операций в админке")
@DisplayName("keeper - комплекс оплат для проверки на странице истории операций в админке")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_8_OperationsHistoryTest extends BaseTest {

    static double totalPay;
    static String guid;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String orderType = "part";
    static int amountDishesToBeChosen = 2;
    static HashMap<Integer, HashMap<String, String>> tapperOrderData;
    static HashMap<Integer, HashMap<String, String>> adminOrderData;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static int amountDishesForFillingOrder = 4;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    OperationsHistory operationsHistory = new OperationsHistory();
    AuthorizationPage authorizationPage = new AuthorizationPage();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                TABLE_AUTO_111_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("1.2. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPageNestedTests.activateRandomTipsAndActivateSc();
        rootPage.setCustomTips(String.valueOf(rootPage.generateRandomNumber(100, 1000)));

    }

    @Test
    @DisplayName("1.3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        tapperOrderData = rootPage.saveOrderDataForOperationsHistoryInAdmin();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("1.4. Переходим на эквайринг, оплачиваем там")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.6. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }


    @Test
    @DisplayName("1.7. Открываем историю операций, проверяем что платёж есть и корректный")
    public void openAdminOperationsHistory() {

        rootPage.openNewTabAndSwitchTo(ADMIN_AUTHORIZATION_STAGE_URL);
        authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

        operationsHistory.goToOperationsHistoryCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData, adminOrderData);

    }

    @Test
    @DisplayName("1.8. Делаем полную оплату на столе")
    public void clearDataAndChoseAgain() {

        rootPage.switchBrowserTab(0);
        savePaymentDataForAcquiring();

    }

    @Test
    @DisplayName("1.9. Производим полную оплату")
    public void payAndGoToAcquiringAgain() {

        payAndGoToAcquiring();
        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("2.0. Переход на эквайринг, ввод данных, полная оплата")
    public void checkFullPayInAdmin() {

        rootPage.switchBrowserTab(1);

        rootPage.refreshPage();
        operationsHistory.goToOperationsHistoryCategory();
        operationsHistory.isHistoryOperationsCorrect();

        adminOrderData = operationsHistory.saveAdminOrderData();
        rootPage.matchTapperOrderDataWithAdminOrderData(tapperOrderData, adminOrderData);

    }

}
