package tapper.tests.keeper._2_2_stress;


import api.ApiRKeeper;
import data.TableData;
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

import static api.ApiData.OrderData.*;


@Epic("RKeeper")
@Feature("Стресс\\Нагрузочные тесты")
@Story("Большой счёт,частичная оплата в несколько платежей до конца - +чай +сбор")
@DisplayName("Большой счёт,частичная оплата в несколько платежей до конца - +чай +сбор")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class BigOrderPartPayTillEndTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;
    static String guid;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String orderType = "part";
    static int amountDishesToFillOrder = 4;
    static int amountDishesToBeChosen = 2;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, GLAZUNYA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, SOLYANKA, amountDishesToFillOrder);
        dishesForFillingOrder = apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesToFillOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode,waiter,
                apiUri,dishesForFillingOrder,tableUrl,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(2)
    @DisplayName("Выбираем рандомно блюда, проверяем все суммы и условия, проверяем что после шаринга выбранные позиции в ожидаются")
    void chooseDishesAndCheckAfterDivided() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(4)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName("Разделяем счёт, оплачиваем по позициям и так пока весь заказ не будет оплачен")
    void payTillEnd() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        rootPageNestedTests.payTillFullSuccessPayment
                (amountDishesToBeChosen, guid, tableId, "keeper");

    }

}
