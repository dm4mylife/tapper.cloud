package tapper.tests.keeper_e2e._3_2_modificator;


import api.ApiRKeeper;
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
import java.util.Map;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_333;

@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Частичная оплата со скидкой")
@DisplayName("Частичная оплата со скидкой")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class PartPayDiscountTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    int amountDishesToBeChosen = 3;
    static double discount;
    static HashMap<Integer, Map<String, Double>> orderInKeeper;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола")
    public void createAndFillOrder() {

        Response rs = rootPageNestedTests.createOrder(R_KEEPER_RESTAURANT, TABLE_CODE_333,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,TABLE_AUTO_333_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

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

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(R_KEEPER_RESTAURANT,guid, modifiers));

        ArrayList<LinkedHashMap<String, Object>> discounts = new ArrayList<>();

        apiRKeeper.createDiscountWithCustomSumObject(discounts, DISCOUNT_WITH_CUSTOM_SUM,"10000");

        Map<String, Object> rsBodyCreateDiscount = apiRKeeper.rqBodyAddDiscount(R_KEEPER_RESTAURANT,guid,discounts);
        apiRKeeper.createDiscount(rsBodyCreateDiscount);

        rootPage.openNotEmptyTable(STAGE_RKEEPER_TABLE_333);
        rootPage.isTableHasOrder();


    }

    @Test
    @DisplayName("2. Проверка суммы, чаевых, сервисного сбора. Устанавливаем сервисный сбор")
    public void checkSumTipsSC() {

        rootPageNestedTests.chooseDishesWithRandomAmount(amountDishesToBeChosen);

        discount = rootPageNestedTests.getDiscount(TABLE_AUTO_333_ID);
        rootPageNestedTests.checkIsDiscountPresent(discount);

        rootPage.isModificatorTextCorrect();
        rootPage.activateServiceChargeIfDeactivated();

    }

    @Test
    @DisplayName("3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_333_ID);

    }

    @Test
    @DisplayName("4. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("5. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("6. Проверка сообщения в телеграмме")
    public void matchTgMsgDataAndTapperData() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("7. Закрываем заказ")
    public void closeOrderByAPI() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_333_ID,guid,AUTO_API_URI);

    }

}
