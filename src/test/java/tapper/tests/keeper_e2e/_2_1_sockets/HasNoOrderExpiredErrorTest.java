package tapper.tests.keeper_e2e._2_1_sockets;


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
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.Constants.WAIT_UNTIL_TRANSACTION_STILL_ALIVE;
import static data.selectors.TapperTable.RootPage.DishList.allNonPaidAndNonDisabledDishes;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Задержка в оплате, проверка на успешное завершение оплаты")
@DisplayName("Задержка в оплате, проверка на успешное завершение оплаты")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class HasNoOrderExpiredErrorTest extends BaseTest {

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
    @DisplayName("1.0. Создание заказа в r_keeper и открытие стола, " +
            "проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(R_KEEPER_RESTAURANT,
                TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,STAGE_RKEEPER_TABLE_222,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

    }

    @Test
    @DisplayName("1.1. Сохраняем данные по оплате для проверки их корректности на эквайринге, и транзакции б2п")
    public void payAndGoToReviewPage() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tableData = rootPage.getTableData();
        dishList = rootPage.getDishList(allNonPaidAndNonDisabledDishes);

    }

    @Test
    @DisplayName("1.2. Переходим на транзакцию и ждём пока она не \"протухнет \" за 200сек")
    public void goToAcquiringAndWaitTillTransactionExpired() {

        transactionId =
                nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_STILL_ALIVE);

    }

    @Test
    @DisplayName("1.3. Проверяем что получили ошибку")
    public void reviewCorrectNegative() {

        reviewPageNestedTests.errorPaymentCorrect(WAIT_UNTIL_TRANSACTION_STILL_ALIVE);

    }

}
