package tapper.tests.keeper._1_4_zero_price_dish;


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
import java.util.Map;

import static api.ApiData.OrderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable.*;

@Epic("RKeeper")
@Feature("Кейс с нулевыми ценами")
@Story("Дозируемы за дозу. Порционная позиция стоимостью 0р и платными модификаторами и со скидкой")
@DisplayName("Дозируемы за дозу. Порционная позиция стоимостью 0р и платными модификаторами и со скидкой")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DoseCommonAndFreeWithPaidModiWithDiscountTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    static String discountAmount = "10000";
    double portionDishQuantity = 0.5;
    String portionDish = DOZA_DISH;

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

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode,
                waiter, apiUri,dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_WEIGHT_BY_PORTION_PAID_MODI_VEG_SALAD,1));

                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                            {
                                add(apiRKeeper.createModificatorObject
                                        (FREE_WEIGHT_BY_PORTION_PAID_MODI_VEG_SALAD,1));

                            }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                            {
                                add(apiRKeeper.createModificatorObject
                                        (FREE_WEIGHT_BY_PORTION_PAID_MODI_KARTOFEL_FRI,2));

                            }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(portionDish, portionDishQuantity, new ArrayList<>(){
                            {
                                add(apiRKeeper.createModificatorObject
                                        (FREE_WEIGHT_BY_PORTION_PAID_MODI_KARTOFEL_FRI,1));
                                add(apiRKeeper.createModificatorObject(FREE_WEIGHT_BY_PORTION_PAID_MODI_SOUS,1));

                            }
                }));

            }
        };

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(restaurantName,guid, modifiers));

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM_ID,discountAmount);
        apiRKeeper.createDiscountByIdObject(discounts, DISCOUNT_BY_ID);

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(restaurantName,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName("Проверка скидки")
    void checkIsDiscountPresent() {

        rootPageNestedTests.checkIsDiscountPresent(tableId, "keeper");

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем суммы, чаевые, сб")
    void checkAllDishesSumsWithAllConditions() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditionsConsideringDiscount();
        rootPage.setRandomTipsOption();

    }

    @Test
    @Order(4)
    @DisplayName(savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(5)
    @DisplayName(goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(6)
    @DisplayName(isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(7)
    @DisplayName(isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
