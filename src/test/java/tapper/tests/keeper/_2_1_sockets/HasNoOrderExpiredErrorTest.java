package tapper.tests.keeper._2_1_sockets;


import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tests.BaseTest;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.Constants.WAIT_UNTIL_TRANSACTION_STILL_ALIVE;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Задержка в оплате, проверка на успешное завершение оплаты")
@DisplayName("Задержка в оплате, проверка на успешное завершение оплаты")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HasNoOrderExpiredErrorTest extends BaseTest {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;

    static double totalPay;

    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();

    @Test
    @Order(1)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.createOrderInKeeper)
    void createAndFillOrder() {
        System.out.println("start");
        nestedTests.createAndFillOrderAndOpenTapperTable(2, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(2)
    @DisplayName("Переходим на транзакцию и ждём пока она не \"протухнет \" за 200сек")
    void goToAcquiringAndWaitTillTransactionExpired() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        nestedTests.goToAcquiringAndWaitTillTransactionExpired(totalPay, WAIT_UNTIL_TRANSACTION_STILL_ALIVE);

    }

    @Test
    @Order(3)
    @DisplayName("Проверяем что получили ошибку")
    void reviewCorrectNegative() {

        reviewPageNestedTests.errorPaymentCorrect(WAIT_UNTIL_TRANSACTION_STILL_ALIVE);

    }

}
