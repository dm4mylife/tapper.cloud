package tapper.tests.keeper_e2e._3_2_modifiers;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;


@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Дробные позиции с несколькими модификаторами")
@DisplayName("Дробные позиции с несколькими модификаторами")

@TestMethodOrder(MethodOrderer.DisplayName.class)
class DecimalModifiersTest extends BaseTest {

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


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(PORTION_DISH_BY_WEIGHT,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_KARTOSHKA_FRI,1));
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_NON_NECESSARY_MODI_SALAT,1));
                        add(apiRKeeper.createModificatorObject(FREE_NON_NECESSARY_MODI_SALAT,1));
                    }
                }));
            }
        };

        Response rs = rootPageNestedTests.createAndFillOrderOnlyWithModifiers(restaurantName, tableCode,waiter,
                apiUri,modifiers, tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        rootPage.openNotEmptyTable(tableUrl);
        rootPage.isTableHasOrder();
        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

    }

    @Test
    @DisplayName("2. Проверка что заказ с кассы совпадает со столом")
    public void matchTapperOrderWithOrderInKeeper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора. Отключаем сервисный сбор")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);

        rootPage.isModificatorTextCorrect();
        rootPage.deactivateServiceChargeIfActivated();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @DisplayName("5. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("6. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("7. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}
