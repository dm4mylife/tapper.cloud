package tapper.tests.keeper._1_1_common;


import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.SafariTest;

import java.util.LinkedHashMap;

import static api.ApiData.OrderData.BARNOE_PIVO;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;


@Epic("RKeeper")
@Feature("Общие")
@Story("Общая функциональность таппера - Safari")
@DisplayName("Общая функциональность таппера - Safari")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllElementsSafariTest extends SafariTest {

    protected final String restaurantName = TableData.Keeper.Table_111.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_111.tableCode;
    protected final String waiter = TableData.Keeper.Table_111.waiter;
    protected final String apiUri = TableData.Keeper.Table_111.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_111.tableUrl;
    protected final String tableId = TableData.Keeper.Table_111.tableId;

    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String guid;
    static double totalPay;
    int amountDishesForFillingOrder = 3;

    RootPage rootPage = new RootPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    NestedTests nestedTests = new NestedTests();

    @Test
    @Order(1)
    @DisplayName(TapperTable.openEmptyTapperTable)
    void openEmptyTapperTable() {

        nestedTests.openEmptyTapperTable(restaurantName, tableId, apiUri, tableUrl);

    }

    @Test
    @Order(2)
    @DisplayName(TapperTable.checkEmptyTable)
    void checkEmptyTable() {

        rootPage.isStartScreenShown();
        rootPageNestedTests.isEmptyTableCorrect();
        rootPageNestedTests.isRefreshButtonCorrect();

    }

    @Test
    @Order(3)
    @DisplayName(TapperTable.createOrderInKeeper + TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void createAndFillOrder() {

        guid = nestedTests.createAndFillOrderAndOpenTapperTable(amountDishesForFillingOrder, BARNOE_PIVO,
                restaurantName, tableCode, waiter, apiUri, tableUrl, tableId);

    }

    @Test
    @Order(4)
    @DisplayName(TapperTable.refreshPage)
    void refreshPage() {

        rootPage.refreshPage();

    }

    @Test
    @Order(5)
    @DisplayName(TapperTable.isStartScreenRestaurantLogoCorrect)
    void isStartScreenShown() {

        rootPageNestedTests.isStartScreenShown();

    }

    @Test
    @Order(6)
    @DisplayName(TapperTable.isDishesCorrectInCashDeskAndTapperTable)
    void newIsOrderInKeeperCorrectWithTapper() {

        rootPageNestedTests.newIsOrderInKeeperCorrectWithTapper(tableId);

    }

    @Test
    @Order(7)
    @DisplayName(TapperTable.isTableHasOrder)
    void isDishListNotEmptyAndVisible() {

        rootPage.isTableHasOrder();

    }

    @Test
    @Order(8)
    @DisplayName(TapperTable.isDivideCheckButtonCorrect)
    void isDivideSliderCorrect() {

        rootPage.isDivideSliderCorrect();

    }

    @Test
    @Order(9)
    @DisplayName(TapperTable.isTipsContainerCorrect)
    void isTipsContainerCorrect() {

        rootPage.isTipsContainerCorrect();

    }

    @Test
    @Order(10)
    @DisplayName(TapperTable.isCheckContainerCorrect)
    void isCheckContainerShown() {

        rootPage.isCheckContainerShown();

    }

    @Test
    @Order(11)
    @DisplayName(TapperTable.isPaymentButtonCorrect)
    void isPaymentButtonShown() {

        rootPage.isPaymentButtonShown();

    }

    @Test
    @Order(12)
    @DisplayName(TapperTable.isShareButtonCorrect)
    void isShareButtonShown() {

        rootPage.isShareButtonShown();

    }

    @Test
    @Order(13)
    @DisplayName(TapperTable.isServiceChargeCorrect)
    void isServiceChargeShown() {

        rootPage.isServiceChargeShown();

    }

    @Test
    @Order(14)
    @DisplayName(TapperTable.isPrivateAndConfPolicyCorrect)
    void isConfPolicyShown() {

        rootPage.isConfPolicyShown();

    }

    @Test
    @Order(15)
    @DisplayName(TapperTable.isAppFooterCorrect)
    void isTapBarShown() {

        rootPage.isTapBarShown();

    }

    @Test
    @Order(16)
    @DisplayName(TapperTable.isCallWaiterCorrect)
    void isCallWaiterCorrect() {

        rootPage.isCallWaiterCorrect();

    }

    @Test
    @Order(17)
    @DisplayName(TapperTable.isPaymentOptionsCorrect)
    void isPaymentOptionsCorrect() {

        rootPage.isPaymentOptionsCorrect();

    }


    @Test
    @Order(18)
    @DisplayName(TapperTable.saveDataGoToAcquiringTypeDataAndPay)
    void payOrder() {

        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");
        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(19)
    @DisplayName(TapperTable.checkPaymentProcess)
    void checkPayment() {

        reviewPageNestedTests.fullPaymentCorrect();

    }

    @Test
    @Order(20)
    @DisplayName(TapperTable.isReviewCorrect)
    void leaveReview() {

        reviewPageNestedTests.reviewCorrectPositive();

    }

    @Test
    @Order(21)
    @DisplayName(TapperTable.isEmptyTableCorrect)
    void isTableEmpty() {

        rootPage.isEmptyOrderAfterClosing();

    }

    @Test
    @Order(22)
    @DisplayName(TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg, "full");

    }

    @Test
    @Order(23)
    @DisplayName(TapperTable.isChatHistorySavedCorrectAfterCloseBrowser)
    public void isHistorySavedByClosingBrowser() {

        rootPage.isHistorySavedByClosingBrowser(tableUrl);

    }

    @Test
    @Order(24)
    @DisplayName("Проверка смены языка")
    void isChangeLanguageCorrect() {

        rootPage.isChangeLanguageCorrect();

    }

}
