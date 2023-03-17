package tapper.tests.keeper_e2e._3_2_modificator;


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

@Order(82)
@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Частичная оплата позиции с ценой 0 и платным модификатором ")
@DisplayName("Частичная оплата позиции с ценой 0 и платным модификатором ")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_2_PartPayZeroPriceAndPaidModiTest extends BaseTest {
    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = rootPageNestedTests.createOrder(R_KEEPER_RESTAURANT, TABLE_CODE_333,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,TABLE_AUTO_333_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(ABISTA_ZERO_PRICE,3, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(ABISTA_PAID_NECESSART_MODI,1));
                    }
                }));
            }
        };

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(R_KEEPER_RESTAURANT,guid, modifiers));

        rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_333);
        rootPage.isDishListNotEmptyAndVisible();


    }

    @Test
    @DisplayName("2. Выбираем обычное блюдо и с модификатором")
    public void checkSumTipsSC() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.choseFirstAndLastDishes(allNonPaidAndNonDisabledDishesName);

        double cleanDishesSum = rootPage.countAllChosenDishesDivided();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsOption();
        rootPage.isModificatorTextCorrect();

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
