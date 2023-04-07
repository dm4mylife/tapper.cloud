package tapper.tests.keeper_e2e._4_2_addAndRemoveDishPositions;


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

import static api.ApiData.QueryParams.rqParamsDeletePosition;
import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_444;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;


@Epic("RKeeper")
@Feature("Добавление и удаление позиций из заказа")
@Story("Удаление позиции после частичной оплаты")
@DisplayName("Удаление позиции после частичной оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class RemoveAfterPartAndPartTest extends BaseTest {

    static String guid;
    static String uni;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT, TABLE_CODE_444,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_444,
                TABLE_AUTO_444_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
        uni = apiRKeeper.getUniFirstValueFromOrderInfo(TABLE_AUTO_444_ID,AUTO_API_URI);

    }

    @Test
    @DisplayName("1.2. Проверка суммы, чаевых, сервисного сбора")
    public void checkSumTipsSC() {

        rootPageNestedTests.checkAllDishesSumsWithAllConditions();

    }

    @Test
    @DisplayName("1.3. Выбираем последнее блюдо, проверяем все суммы и условия, без чая, но с СБ")
    public void chooseDishesAndCheckAfterDivided() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseLastDish(allNonPaidAndNonDisabledDishes);

        double cleanTotalSum = rootPage.countAllChosenDishesDivided();
        rootPageNestedTests.checkSumWithAllConditions(cleanTotalSum);
        rootPageNestedTests.activateRandomTipsAndActivateSc();

    }

    @Test
    @DisplayName("1.4. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_444_ID);

    }

    @Test
    @DisplayName("1.5 Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @DisplayName("1.6 Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @DisplayName("1.7 Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

    @Test
    @DisplayName("1.8. Удаляем одно блюдо на кассе")
    public void deleteDishInOrder() {

        apiRKeeper.deletePosition(rqParamsDeletePosition(R_KEEPER_RESTAURANT, guid, uni, 1000), AUTO_API_URI);
        rootPage.forceWait(2000); // toDO не успевает прийти запрос на кассу
    }

    @Test
    @DisplayName("1.9. Пытаемся оплатить и получаем ошибку изменения суммы")
    public void checkChangedSumAfterAdding() {

        nestedTests.checkIfSumsChangedAfterEditingOrder();

    }

    @Test
    @DisplayName("2.0. Закрываем заказ, проверяем тг")
    public void closeOrder() {

        savePaymentDataForAcquiring();
        payAndGoToAcquiring();
        nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper);

        telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
        rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

    }

}