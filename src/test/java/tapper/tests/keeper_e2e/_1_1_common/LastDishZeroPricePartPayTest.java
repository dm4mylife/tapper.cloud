package tapper.tests.keeper_e2e._1_1_common;


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



@Epic("RKeeper")
@Feature("Общие")
@Story("Частичная оплата. Стоимость последней позиции 0 рублей")
@DisplayName("Частичная оплата. Стоимость последней позиции 0 рублей")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class LastDishZeroPricePartPayTest extends BaseTest {

    static String guid;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 1;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1. Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        apiRKeeper.createDishObject(dishesForFillingOrder, ZERO_PRICE_DISH, 1);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable
                (R_KEEPER_RESTAURANT, TABLE_CODE_111,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                        AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_111,TABLE_AUTO_111_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("2. Выбираем рандомно блюда, проверяем все суммы и условия, " +
            "проверяем что после шаринга выбранные позиции в ожидаются")
    public void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.click(allNonPaidAndNonDisabledDishesName.first());
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("3. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_111_ID);

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
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("7. Проверяем что последняя позиция с нулевой ценой и невозможно оплатить заказ")
    public void payAndGoToAcquiringAgain() {

        rootPage.isPaymentDisabled();

    }

    @Test
    @DisplayName("8. Закрываем пустой заказ")
    public void closedOrderByApi() {

        apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_111_ID,guid,AUTO_API_URI);

    }

}
