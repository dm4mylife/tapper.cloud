package tapper.tests.keeper_e2e._3_2_modificator;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.allTypesModificatorList;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;

@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Все вариации модификаторов. Полная оплата без чаевых")
@DisplayName("Все вариации модификаторов. Полная оплата без чаевых")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class BigFullPayTest extends BaseTest {

    protected static String guid;
    protected static double totalPay;
    protected static String orderType = "full";
    protected static HashMap<String, String> paymentDataKeeper;
    protected static LinkedHashMap<String, String> tapperDataForTgMsg;
    protected static LinkedHashMap<String, String> telegramDataForTgMsg;
    protected static String transactionId;
    protected static HashMap<Integer, Map<String, Double>> orderInKeeper;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = rootPageNestedTests.createAndFillOrderOnlyWithModifiers
                (R_KEEPER_RESTAURANT, TABLE_CODE_333,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,allTypesModificatorList,
                        TABLE_AUTO_333_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        Response rsOrderInfo = apiRKeeper.getOrderInfo(TABLE_AUTO_333_ID, AUTO_API_URI);
        orderInKeeper = rootPageNestedTests.saveOrderDataWithAllModi(rsOrderInfo);

        rootPage.openNotEmptyTable(STAGE_RKEEPER_TABLE_333);
        rootPage.isTableHasOrder();

    }

    @Test
    @DisplayName("2. Проверка что заказ с кассы совпадает со столом")
    public void matchTapperOrderWithOrderInKeeper() {

        rootPage.isTableHasOrder();
        rootPageNestedTests.matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();
        rootPage.isModificatorTextCorrect();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_333_ID);

    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("7. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
