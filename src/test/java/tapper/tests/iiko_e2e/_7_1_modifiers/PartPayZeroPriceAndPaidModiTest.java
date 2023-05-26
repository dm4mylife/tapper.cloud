package tapper.tests.iiko_e2e._7_1_modifiers;


import api.ApiIiko;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.IikoData.Dish.ZERO_PRICE_DISH_TEA;
import static api.ApiData.IikoData.Dish.ZERO_PRICE_DISH_TEA_PAID_NOT_NECESSARY_MODIFIER;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;
@Disabled("Пока не починят добавление платного модика к бесплатному блюдо по api iiko")
@Epic("Iiko")
@Feature("Модификаторы")
@Story("Частичная оплата позиции с ценой 0 и платным модификатором ")
@DisplayName("Частичная оплата позиции с ценой 0 и платным модификатором ")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartPayZeroPriceAndPaidModiTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_222.tableUrl;
    protected final String tableId = TableData.Iiko.Table_222.tableId;
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    RootPage rootPage = new RootPage();
    ApiIiko apiIiko = new ApiIiko();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в iiko и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiIiko.createModificatorObject
                (dishesForFillingOrder, ZERO_PRICE_DISH_TEA_PAID_NOT_NECESSARY_MODIFIER.getId(), 3);
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrderWithModifiers
                (guid, ZERO_PRICE_DISH_TEA.getId(), 1, dishesForFillingOrder));

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Выбираем обычное блюдо и с модификатором")
    void checkSumTipsSC() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.choseFirstAndLastDishes(allNonPaidAndNonDisabledDishesName);

        double cleanDishesSum = rootPage.countOnlyAllChosenDishesDivided();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsOption();
        rootPage.isModificatorTextCorrect();

    }

    @Test
    @Order(3)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

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
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(7)
    @DisplayName("Закрываем заказ")
    public void payOrder() {

        apiIiko.closedOrderByApi(tableId);

    }

}
