package tapper.tests.iiko_e2e._8_1_discount;


import api.ApiIiko;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.IikoData.Dish.CUSTOM_DISCOUNT_ID;
import static api.ApiData.IikoData.Dish.ESPRESSO;
import static data.Constants.RegexPattern.TapperTable.dishPriceRegex;
import static data.Constants.TestData.TapperTable.COOKIE_GUEST_SECOND_USER;
import static data.Constants.TestData.TapperTable.COOKIE_SESSION_SECOND_USER;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("Iiko")
@Feature("Скидка")
@Story("Удаление скидки когда осталась одна позиция ")
@DisplayName("Удаление скидки когда осталась одна позиция ")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RemoveDiscountWhenOneDishInOrderTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_333.tableUrl;
    protected final String tableId = TableData.Iiko.Table_333.tableId;


    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishes = 5;
    static int amountDishesForFillingOrder = 6;
    static Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount = new HashMap<>();
    static Map<Integer,Map<String,Double>> dishesAfterAddingDiscount = new HashMap<>();

    RootPage rootPage = new RootPage();
    ApiIiko apiIiko = new ApiIiko();

    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInIiko)
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,ESPRESSO.getId(),amountDishesForFillingOrder));

        Map<String, Object> rsBodyCreateDiscount =
                apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),200);

        apiIiko.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Проверяем скидку на столе")
    void isDiscountCorrectOnTable() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "iiko");

    }

    @Test
    @Order(3)
    @DisplayName("Выбираем все позиции, кроме одной")
    void checkSumTipsSC() {

        rootPageNestedTests.checkChosenDishesSumsWithAllConditionsConsideringDiscount(amountDishes);

    }

    @Test
    @Order(4)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(5)
    @DisplayName("Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(7)
    @DisplayName("Проверка сообщения в телеграмме")
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(8)
    @DisplayName("Сохраняем данным по не оплаченной позиции")
    void saveDishPricesInCollection() {

        dishesBeforeAddingDiscount =
                rootPage.saveDishPricesInCollection(allNonPaidAndNonDisabledDishes,dishPriceWithDiscountSelector,
                        dishPriceWithoutDiscountSelector,dishPriceRegex);

    }

    @Test
    @Order(9)
    @DisplayName("Удаляем скидку из заказу")
    void addDiscountAndCheckSums() {

        Map<String, Object> rsBodyDeleteDiscount =
                apiIiko.rqBodyDeleteDiscount(guid,CUSTOM_DISCOUNT_ID.getId());

        apiIiko.deleteDiscount(rsBodyDeleteDiscount);

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

    }
    @Test
    @Order(10)
    @DisplayName("Проверяем что скидка удалилась на столе")
    void isDiscountCorrectOnTableAfterPay() {

        dishesAfterAddingDiscount =
                rootPage.saveDishPricesInCollection(allNonPaidAndNonDisabledDishes,dishPriceWithDiscountSelector,
                        dishPriceWithoutDiscountSelector,dishPriceRegex);

        rootPageNestedTests.isNonPaidDishesHasMarkupAfterRemovingDiscount
                (dishesBeforeAddingDiscount,dishesAfterAddingDiscount);

    }

    @Test
    @Order(11)
    @DisplayName("Оплачиваем остатки")
    void payAndGoToAcquiringAgain() {

        rootPage.openTableAndSetGuest(tableUrl, COOKIE_GUEST_SECOND_USER, COOKIE_SESSION_SECOND_USER);

        savePaymentDataForAcquiring();

        payAndGoToAcquiring();

        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
