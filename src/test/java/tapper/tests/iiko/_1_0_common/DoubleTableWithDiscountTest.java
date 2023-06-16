package tapper.tests.iiko._1_0_common;


import api.ApiIiko;
import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.IikoData.Dish.*;
import static com.codeborne.selenide.Condition.visible;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.allDishesInOrder;


@Epic("Iiko")
@Feature("Общие")
@Story("Оплата заказа с дублем стола")
@DisplayName("Оплата заказа с дублем стола")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoubleTableWithDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Iiko.restaurantName;
    protected final String tableUrl = TableData.Iiko.Table_111.tableUrl;
    protected final String tableId = TableData.Iiko.Table_111.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListOriginalTable;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListDoubleTable;
    static  LinkedHashMap<Integer, Map<String, Double>> dishListSecondDoubleTable;
    int amountDishesForFillingOrder = 1;


    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ApiIiko apiIiko = new ApiIiko();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    ReviewPage reviewPage = new ReviewPage();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();



    @Test
    @Order(1)
    @DisplayName(TapperTable.createOrderInIiko)
    void createAndFillOrder() {

        apiIiko.closedOrderByApi(tableId);

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,BURGER.getId(),amountDishesForFillingOrder));

        apiIiko.createDiscount(apiIiko.rqBodyAddDiscount(guid,CUSTOM_DISCOUNT_ID.getId(),100));

        rootPage.openNotEmptyTable(tableUrl);

        dishListOriginalTable = rootPage.getDishList(allDishesInOrder);

    }
    @Test
    @Order(2)
    @DisplayName("Создание второго дубля стола")
    void createAndFillOrderSecondTable() {

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,SHASHLIK_GOVYADINA.getId(),amountDishesForFillingOrder));

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        dishListDoubleTable = rootPage.getDishList(allDishesInOrder);

    }
    @Test
    @Order(3)
    @DisplayName("Создание третьего дубля стола")
    void createAndFillOrderThirdTable() {

        guid =  apiIiko.createOrder(apiIiko.rqBodyCreateOrder(tableId));
        apiIiko.fillingOrder(apiIiko.rqBodyFillingOrder(guid,SHASHLIK_SVININA.getId(),amountDishesForFillingOrder));

    }

    @Test
    @Order(4)
    @DisplayName("Проверка заказа на столе")
    void isDishCorrect() {

        rootPage.refreshPage();
        rootPage.isTableHasOrder();

        dishListSecondDoubleTable = rootPage.getDishList(allDishesInOrder);
        Assertions.assertNotEquals(dishListOriginalTable,dishListSecondDoubleTable);

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isTotalPaySumCorrectTipsSc + TapperTable.setRandomTips)
    void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "iiko");

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.isPaymentCorrect)
    void checkPayment() {

        pagePreLoader.shouldNotBe(visible, Duration.ofSeconds(30));
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        if (orderType.equals("part"))
            apiRKeeper.isPrepaymentSuccess(transactionId,WAIT_FOR_PREPAYMENT_DELIVERED_TO_CASH_DESK);

        reviewPage.clickOnFinishButton();

    }

    @Test
    @Order(9)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @Order(10)
    @DisplayName("Проверка заказа на столе")
    void isDishCorrectAfterFirstPay() {


        Assertions.assertEquals(dishListDoubleTable,rootPage.getDishList(allDishesInOrder));

    }

    @Test
    @Order(11)
    @DisplayName("Проделываем полную оплату второго дубля")
    void payFirstDouble() {

        checkSumTipsSC();
        savePaymentDataForAcquiring();
        payAndGoToAcquiring();
        checkPayment();

    }

    @Test
    @Order(12)
    @DisplayName("Проверка заказа на столе")
    void isDishCorrectAfterSecondPay() {

        Assertions.assertEquals(dishListOriginalTable,rootPage.getDishList(allDishesInOrder));

    }

    @Test
    @Order(13)
    @DisplayName("Проделываем полную оплату третьего дубля")
    void paySecondDouble() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditionsConsideringDiscount();

        savePaymentDataForAcquiring();
        payAndGoToAcquiring();
        checkPayment();
        rootPage.isEmptyOrderAfterClosing();

    }

}
