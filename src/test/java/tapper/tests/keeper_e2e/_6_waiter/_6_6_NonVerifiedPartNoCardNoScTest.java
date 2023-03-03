package tapper.tests.keeper_e2e._6_waiter;


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
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_444;

@Order(66)
@Epic("RKeeper")
@Feature("Официант")
@Story("Официант не верифицирован, без привязанной карты, частичная оплата  -СБ")
@DisplayName("Официант не верифицирован, без привязанной карты, частичная оплата  -СБ")

@TestMethodOrder(MethodOrderer.DisplayName.class)

public class _6_6_NonVerifiedPartNoCardNoScTest extends BaseTest {

    static String visit;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishes = 3;
    static ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
    static int amountDishesForFillingOrder = 6;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_444,WAITER_IRONMAN_NON_VERIFIED_NON_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_444,TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда")
    public void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPageNestedTests.chooseCertainAmountDishes(amountDishes);

    }

    @Test
    @DisplayName("3. Проверяем все суммы и условия")
    public void checkSumWithAllConditionsWithNonVerifiedWaiter() {

        double cleanDishesSum = rootPage.countAllNonPaidDAndChosenDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditionsWithNonVerifiedWaiter(cleanDishesSum);

    }

    @Test
    @DisplayName("4. Выключаем сервисный сбор")
    public void deactivateServiceChargeIfActivated() {

        rootPage.deactivateServiceChargeIfActivated();

    }

    @Test
    @DisplayName("5. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("8. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("9. Делимся ссылкой и оплачиваем остальную часть заказа")
    public void clearDataAndChoseAgain() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_444_ID,guid,AUTO_API_URI);

    }

}
