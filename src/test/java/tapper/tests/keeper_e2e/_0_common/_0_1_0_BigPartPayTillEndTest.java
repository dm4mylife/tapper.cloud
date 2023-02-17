package tapper.tests.keeper_e2e._0_common;


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

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;

@Order(24)
@Epic("RKeeper")
@Feature("Частичная оплата")
@Story("Частичная оплата в несколько платежей до конца - +чай +сбор")
@DisplayName("Частичная оплата в несколько платежей до конца - +чай +сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _0_1_0_BigPartPayTillEndTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String orderType;
    static int amountDishesToFillOrder = 4;
    static int amountDishesToBeChosen = amountDishesToFillOrder / 2;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        dishesForFillingOrder = apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.orderFill(dishesForFillingOrder, GLAZUNYA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.orderFill(dishesForFillingOrder, SOLYANKA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.orderFill(dishesForFillingOrder, TORT, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.orderFill(dishesForFillingOrder, GOVYADINA_PORTION, amountDishesToFillOrder);

        apiRKeeper.orderFill(dishesForFillingOrder, BARNOE_PIVO, amountDishesToFillOrder);

        Response rs = apiRKeeper.createAndFillOrder(R_KEEPER_RESTAURANT,TABLE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                TABLE_AUTO_111_ID, AUTO_API_URI,dishesForFillingOrder);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);


        rootPage.openUrlAndWaitAfter(TapperTable.STAGE_RKEEPER_TABLE_111);

    }

    @Test
    @DisplayName("2. Проверяем что блюда соответствую кассе")
    public void newIsOrderInKeeperCorrectWithTapper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("3. Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);

    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {
        transactionId = nestedTests.acquiringPayment(totalPay);
    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {
        nestedTests.checkPaymentAndB2pTransaction(orderType = "part", transactionId, paymentDataKeeper);
    }

    @Test
    @DisplayName("7. Разделяем счёт, оплачиваем по позициям и так пока весь заказ не будет оплачен")
    public void payTillEnd() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid, WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

        rootPageNestedTests.payTillFullSuccessPayment
                (amountDishesToBeChosen,guid,
                        WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY,WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY,TABLE_AUTO_111_ID);

    }

}
