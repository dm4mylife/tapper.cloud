package tapper.tests.iiko_e2e._6_0_common;


import api.ApiData;
import api.ApiIiko;
import api.ApiRKeeper;
import common.BaseActions;
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

import static api.ApiData.IikoData.Dish.BURGER;
import static api.ApiData.IikoData.Dish.ZERO_PRICE_DISH_TEA;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;


@Epic("Iiko")
@Feature("Общие")
@Story("Частичная оплата. Стоимость последней позиции 0 рублей")
@DisplayName("Частичная оплата. Стоимость последней позиции 0 рублей")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LastDishZeroPricePartPayTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_111.tableUrl;
    protected final String tableId = TableData.Iiko.Table_111.tableId;


    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 1;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ApiIiko apiIiko = new ApiIiko();


    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInIiko + TapperTable.isDishesCorrectInCashDeskAndTapperTable +
            " Добавляем блюдо с нулевой ценой")
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,BURGER.getId(),amountDishesForFillingOrder));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid, ZERO_PRICE_DISH_TEA.getId(),amountDishesForFillingOrder));

        rootPage.openNotEmptyTable(tableUrl);
    }

    @Test
    @Order(2)
    @DisplayName("Выбираем не нулевое блюдо, проверяем все суммы и условия, " +
            "проверяем что после шаринга выбранные позиции в ожидаются")
    void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.choseFirstDish(allNonPaidAndNonDisabledDishesName);
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(4)
    @DisplayName(TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg, "full");

    }

    @Test
    @Order(7)
    @DisplayName("Проверяем что пустой стол")
    void payAndGoToAcquiringAgain() {

        rootPage.isEmptyOrderAfterClosing();

    }

}
