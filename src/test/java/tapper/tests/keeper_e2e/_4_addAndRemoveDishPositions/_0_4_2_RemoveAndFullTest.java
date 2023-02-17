package tapper.tests.keeper_e2e._4_addAndRemoveDishPositions;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.rqParamsDeletePosition;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;

@Order(42)
@Epic("RKeeper")
@Feature("Добавление и удаление позиций из заказа")
@Story("Удаление позиции, полная оплата")
@DisplayName("Удаление позиции, полная оплата")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_4_2_RemoveAndFullTest extends BaseTest {

    static String guid;
    static String uni;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 5;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                TABLE_AUTO_222_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        uni = apiRKeeper.getUni(TABLE_AUTO_222_ID,AUTO_API_URI);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_222_ID);

    }

    @Test
    @DisplayName("2. Удаляем одну позицию")
    public void deletePosition() {

        apiRKeeper.deletePosition(rqParamsDeletePosition(R_KEEPER_RESTAURANT, guid, uni, 1000), AUTO_API_URI);
        rootPage.forceWait(2000); // toDO не успевает прийти запрос на кассу

    }

    @Test
    @DisplayName("3. Пытаемся снова оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterDeleting() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

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
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
