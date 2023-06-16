package tapper.tests.keeper._3_4_portions.portions_by_weight;


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
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.*;

@Epic("RKeeper")
@Feature("Порционные по весу")
@Story("Порционная позиция с различными модификаторами и количеством")
@DisplayName("Порционная позиция с различными модификаторами и количеством")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MixModifiersFullPayTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_333.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_333.tableCode;
    protected final String waiter = TableData.Keeper.Table_333.waiter;
    protected final String apiUri = TableData.Keeper.Table_333.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_333.tableUrl;
    protected final String tableId = TableData.Keeper.Table_333.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    double portionDishQuantity = 0.5;
    String portionDish = PORTION_DISH_BY_WEIGHT;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(createOrderInKeeper)
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode,
                waiter, apiUri,dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,1));}
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,2));}
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,2));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,2));}
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,2));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,2));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,2));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(GOVYADINA_FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_KARTOFEL_FRI,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(GOVYADINA_PAID_MODI_VEG_SALAD,1));
                    }
                }));

            }
        };

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(restaurantName,guid, modifiers));

        rootPage.openNotEmptyTable(tableUrl);
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

    }


    @Test
    @Order(2)
    @DisplayName(isTotalPaySumCorrectTipsSc)
    void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsOption();

    }

    @Test
    @Order(3)
    @DisplayName(savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(4)
    @DisplayName(goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(5)
    @DisplayName(isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(6)
    @DisplayName(isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
