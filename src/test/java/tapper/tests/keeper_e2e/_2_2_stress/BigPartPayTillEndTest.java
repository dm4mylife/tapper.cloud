package tapper.tests.keeper_e2e._2_2_stress;


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

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY;
import static data.Constants.WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY;


@Epic("RKeeper")
@Feature("Стресс\\Нагрузочные тесты")
@Story("Большой счёт,частичная оплата в несколько платежей до конца - +чай +сбор")
@DisplayName("Большой счёт,частичная оплата в несколько платежей до конца - +чай +сбор")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class BigPartPayTillEndTest extends BaseTest {

    static double totalPay;
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String orderType;
    static int amountDishesToFillOrder = 2;
    static int amountDishesToBeChosen = 2;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(R_KEEPER_RESTAURANT, TABLE_AUTO_222_ID, AUTO_API_URI),
                "На столе был прошлый заказ, его не удалось закрыть");

        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, GLAZUNYA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, SOLYANKA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, GOVYADINA_PORTION, amountDishesToFillOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_222,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

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

        nestedTests.checkPaymentAndB2pTransaction(orderType = "part", transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("7. Разделяем счёт, оплачиваем по позициям и так пока весь заказ не будет оплачен")
    public void payTillEnd() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        rootPageNestedTests.payTillFullSuccessPayment
                (amountDishesToBeChosen, guid,
                        WAIT_FOR_TELEGRAM_MESSAGE_PART_PAY, WAIT_FOR_TELEGRAM_MESSAGE_FULL_PAY, TABLE_AUTO_222_ID);

    }

}
