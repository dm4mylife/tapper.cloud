package tapper.tests.keeper_e2e._7_modificator;


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
import java.util.Map;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;

@Order(70)
@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Полная оплата без чаевых")
@DisplayName("Полная оплата без чаевых")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _7_0_FullPayNoTipsTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "full";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static HashMap<Integer, Map<String, Double>> orderInKeeper;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_KARTOFEL_FRI,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_VEG_SALAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_MODI_KARTOFEL_FRI,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_VEG_SALAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_GARLIC,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));

                    }
                }));
            }
        };

        Response rs = rootPageNestedTests.createAndFillOrderOnlyWithModifiers
                (R_KEEPER_RESTAURANT, TABLE_CODE_444,WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,modifiers,
                        TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        Response rsOrderInfo = apiRKeeper.getOrderInfo(TABLE_AUTO_444_ID, AUTO_API_URI);
        orderInKeeper = rootPageNestedTests.saveOrderDataWithAllModi(rsOrderInfo);

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);

    }

    @Test
    @DisplayName("2. Проверка что заказ с кассы совпадает со столом")
    public void matchTapperOrderWithOrderInKeeper() {

        rootPageNestedTests.matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }

    @Test
    @DisplayName("3. Проверка суммы, чаевых, сервисного сбора. Сбрасываем чаевые")
    public void checkSumTipsSC() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);

        rootPage.isModificatorTextCorrect();
        rootPage.checkTipsAfterReset();

    }

    @Test
    @DisplayName("4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

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
