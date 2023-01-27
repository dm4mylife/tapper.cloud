package tapper.tests.keeper_e2e._4_addAndRemoveDishPositions;


import api.ApiRKeeper;
import data.Constants;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.QueryParams.*;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.*;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;

@Order(47)
@Epic("RKeeper")
@Feature("Добавление и удаление позиций из заказа")
@Story("Добавление и удаление позиции после в одном заказе")
@DisplayName("Добавление и удаление позиции после в одном заказе")
@Link("https://tapper.staging.zedform.ru/")
@Link(name = "Tapper", type = "mylink")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _0_4_7_AddAndRemoveFullTest extends BaseTest {

    static String visit;
    static String guid;
    static String uni;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rsCreateOrder = apiRKeeper.createOrder(rqParamsCreateOrderBasic(R_KEEPER_RESTAURANT, TABLE_111, WAITER_ROBOCOP_VERIFIED_WITH_CARD), TapperTable.AUTO_API_URI);
        visit = rsCreateOrder.jsonPath().getString("result.visit");
        guid = rsCreateOrder.jsonPath().getString("result.guid");

        Response rsFillingOrder = apiRKeeper.fillingOrder(rqParamsFillingOrderBasic(R_KEEPER_RESTAURANT, visit, BARNOE_PIVO, "5000"));
        uni = rsFillingOrder.jsonPath().getString("result.Order.Session.Dish['@attributes'].uni");

        rootPage.openUrlAndWaitAfter(TapperTable.STAGE_RKEEPER_TABLE_111);
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

    }
    @Description("Создаём заказ\nДобавляем одну позицию\nПробуем оплатить стол и должны получить ошибку\nПроделываем тоже самое с удалением")
    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPage.openUrlAndWaitAfter("https://stage-ssr.zedform.ru/testrkeeper/1000045");
        rootPageNestedTests.isOrderInKeeperCorrectWithTapper();

        double cleanTotalSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        rootPageNestedTests.areTipsOptionsCorrect(cleanTotalSum);

        rootPageNestedTests.cancelTipsAndActivateSC(cleanTotalSum);

    }

    @Test
    @DisplayName("1.3. Добавляем еще одно блюдо на кассе")    public void addOneMoreDishInOrder() {

        apiRKeeper.addModificatorOrder(
                rqParamsAddModificatorWith1Position(
                        R_KEEPER_RESTAURANT,guid, BORSH, "1000", FREE_NECESSARY_MODI_SALT, "1")
                , TapperTable.AUTO_API_URI);

    }

    @Test
    @DisplayName("1.4. Пытаемся оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterAdding() {
        nestedTests.checkIfSumsChangedAfterEditingOrder();
    }

    @Test
    @DisplayName("1.5. Удаляем одну позицию")
    public void deletePosition() {
        apiRKeeper.deletePosition(rqParamsDeletePosition(R_KEEPER_RESTAURANT,guid,uni,1000), TapperTable.AUTO_API_URI);
    }

    @Test
    @DisplayName("1.6. Пытаемся снова оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterDeleting() {
        nestedTests.checkIfSumsChangedAfterEditingOrder();
    }

    @Test
    @DisplayName("1.7. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

    }

    @Test
    @DisplayName("1.8. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();;

    }

    @Test
    @DisplayName("1.9. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {
        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);
    }

    @Test
    @DisplayName("2.0. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getTgMsgData(guid, Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
