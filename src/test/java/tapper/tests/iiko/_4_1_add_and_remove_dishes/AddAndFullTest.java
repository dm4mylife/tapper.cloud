package tapper.tests.iiko._4_1_add_and_remove_dishes;


import api.ApiIiko;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.IikoData.Dish.BURGER;


@Epic("Iiko")
@Feature("Добавление и удаление позиций из заказа")
@Story("Добавление позиции, полная оплата")
@DisplayName("Добавление позиции, полная оплата")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddAndFullTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_444.tableUrl;
    protected final String tableId = TableData.Iiko.Table_444.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 5;


    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ApiIiko apiIiko = new ApiIiko();
    @Test
    @Order(1)
    @DisplayName("Создание заказа в iiko и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(tableId,tableUrl, BURGER, amountDishesForFillingOrder);

    }

    @Test
    @Order(2)
    @DisplayName("Проверка суммы, чаевых, сервисного сбора")
    void checkSumTipsSC() {

        nestedTests.choseAllNonPaidDishesTipsNoSc();

    }

    @Test
    @Order(3)
    @DisplayName("Добавляем еще одно блюдо на кассе")
    void addOneMoreDishInOrder() {

        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,BURGER.getId(),1));

    }

    @Test
    @Order(4)
    @DisplayName("Пытаемся оплатить и получаем ошибку изменения суммы")
    void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @Order(5)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(6)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(8)
    @DisplayName("Проверка сообщения в телеграмме")
    void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

    }

}
