package tapper.tests.keeper_e2e._1_1_common;


import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.ReviewPageNestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTest;

import java.util.LinkedHashMap;

import static api.ApiData.OrderData.*;
import static data.AnnotationAndStepNaming.DisplayName.TapperTable;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;


@Epic("RKeeper")
@Feature("Общие")
@Story("Общая функциональность таппера - Chrome")
@DisplayName("Общая функциональность таппера - Chrome")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AllElementsTest extends BaseTest {


    protected final String restaurantName = R_KEEPER_RESTAURANT;
    protected final String tableCode = TABLE_CODE_111;
    protected final String waiter = WAITER_ROBOCOP_VERIFIED_WITH_CARD;
    protected final String apiUri = AUTO_API_URI;
    protected final String tableUrl = STAGE_RKEEPER_TABLE_111;
    protected final String tableId = TABLE_AUTO_111_ID;

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
    void isHistorySavedByClosingBrowser() {

        rootPage.isHistorySavedByClosingBrowser(tableUrl);

    }

}
