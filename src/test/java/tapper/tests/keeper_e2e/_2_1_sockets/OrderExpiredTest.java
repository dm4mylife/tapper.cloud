package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
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

import static api.ApiData.OrderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Вывод ошибки OrderExpired")
@DisplayName("Вывод ошибки OrderExpired")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderExpiredTest extends BaseTest {

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

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_222,TABLE_AUTO_222_ID);

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
    @DisplayName("Закрываем заказ")
    void closedOrderByApi() {

       apiRKeeper.closedOrderByApi(R_KEEPER_RESTAURANT,TABLE_AUTO_222_ID,guid);

    }

}
