package tapper.tests.keeper_e2e._7_modificator;


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
import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;

@Order(80)
@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Все вариации модификаторов. Частичная оплата без чаевых")
@DisplayName("Все вариации модификаторов. Частичная оплата без чаевых")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _8_0_BigPartPayTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static HashMap<Integer, Map<String, Double>> orderInKeeper;
    static int amountDishesToBeChosen = 2;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = apiRKeeper.createOrder
                (rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_222, WAITER_ROBOCOP_VERIFIED_WITH_CARD),
                        AUTO_API_URI);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.addModificatorOrder(apiRKeeper.rsBodyAddModificatorOrder(R_KEEPER_RESTAURANT,guid, allTypesModificatorList));

        Response rsOrderInfo = apiRKeeper.getOrderInfo(TABLE_AUTO_222_ID, AUTO_API_URI);
        orderInKeeper = rootPageNestedTests.saveOrderDataWithAllModi(rsOrderInfo);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);

    }

    @Test
    @DisplayName("2. Проверка что заказ с кассы совпадает со столом")
    public void matchTapperOrderWithOrderInKeeper() {

        rootPage.isDishListNotEmptyAndVisible();
        rootPageNestedTests.matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPage.isModificatorTextCorrect();
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

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

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
