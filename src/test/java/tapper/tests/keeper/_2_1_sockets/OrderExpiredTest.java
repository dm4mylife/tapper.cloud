package tapper.tests.keeper._2_1_sockets;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Вывод ошибки OrderExpired")
@DisplayName("Вывод ошибки OrderExpired")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderExpiredTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static double totalPay;
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String,Double> tableData;
    static LinkedHashMap<Integer, Map<String,Double>> dishList;
    static String transactionId;
    static int amountDishesForFillingOrder = 2;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @Order(1)
    @DisplayName("Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurantName, tableCode,waiter,
                apiUri,dishesForFillingOrder,tableUrl,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @Order(2)
    @DisplayName("Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    void payAndGoToReviewPage() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tableData = rootPage.getTableData();
        dishList = rootPage.getDishList(allNonPaidAndNonDisabledDishes);

    }

    @Test
    @Order(3)
    @DisplayName("Переходим на транзакцию и ждём пока она не \"протухнет \"")
    void goToAcquiringAndWaitTillTransactionExpired() {

        transactionId = nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_EXPIRED);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем что получили ошибку")
    void reviewCorrectNegative() {

        reviewPageNestedTests.errorPaymentCorrect(WAIT_UNTIL_TRANSACTION_EXPIRED);

    }

    @Test
    @Order(5)
    @DisplayName("Проверяем что суммы,блюда не изменились")
    void isCurrentTableDataCorrectAfterErrorPayment() {

       rootPage.isCurrentTableDataCorrectAfterErrorPayment(allNonPaidAndNonDisabledDishes,dishList,tableData);

    }
    @Test
    @Order(6)
    @DisplayName("Проверяем что сообщение об ошибки пришло в телеграмм")
    void getPaymentErrorTgMsgData() {

       Assertions.assertNotNull(rootPage.getPaymentErrorTgMsgData("Order expired"));

    }

    @Test
    @Order(7)
    @DisplayName("Закрываем заказ")
    void closedOrderByApi() {

       apiRKeeper.closedOrderByApi(restaurantName,tableId,guid);

    }

}
