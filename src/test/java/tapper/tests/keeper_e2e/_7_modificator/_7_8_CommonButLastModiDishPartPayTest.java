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

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishesName;

@Order(78)
@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Частичная оплата нескольких позиций последняя с модификатором")
@DisplayName("Частичная оплата нескольких позиций последняя с модификатором")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _7_8_CommonButLastModiDishPartPayTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 3;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_444,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        ArrayList<LinkedHashMap<String, Object>> modificators = new ArrayList<>() {
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

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(R_KEEPER_RESTAURANT,guid, modificators));

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_444);

    }


    @Test
    @DisplayName("3. Выбираем обычное блюдо и с модификатором")
    public void checkSumTipsSC() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.hidePaymentOptionsAndTapBar();
        rootPage.scrollAndClick(allNonPaidAndNonDisabledDishesName.first());
        rootPage.scrollAndClick(allNonPaidAndNonDisabledDishesName.last());
        rootPage.showPaymentOptionsAndTapBar();
        rootPage.forceWait(2500);
        double cleanDishesSum = rootPage.countAllChosenDishesDivided();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsOption();

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

    @Test
    @DisplayName("8. Закрываем заказ")
    public void closeOrderByAPI() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_444_ID,guid,AUTO_API_URI);
    }

}
