package tapper_table.nestedTestsManager;

import admin_personal_account.menu.Menu;
import api.ApiRKeeper;
import data.selectors.AdminPersonalAccount;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;
import total_personal_account_actions.AuthorizationPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static data.Constants.RegexPattern.TapperTable.totalPayRegex;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_111;
import static data.Constants.WAIT_UNTIL_TRANSACTION_EXPIRED;
import static data.Constants.WAIT_UNTIL_TRANSACTION_STILL_ALIVE;
import static data.selectors.TapperTable.Best2PayPage.*;
import static data.selectors.TapperTable.Common.pagePreLoader;
import static data.selectors.TapperTable.RootPage.DishList.dishesSumChangedHeading;
import static data.selectors.TapperTable.RootPage.DishList.emptyOrderMenuButton;
import static data.selectors.TapperTable.RootPage.PayBlock.serviceChargeContainer;
import static data.selectors.TapperTable.RootPage.TapBar.appFooterMenuIcon;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalPay;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.totalTipsSumInMiddle;



public class NestedTests extends RootPage {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ReviewPage reviewPage = new ReviewPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    AuthorizationPage authorizationPage = new AuthorizationPage();

    Menu menu = new Menu();

    @Step("Переход в эквайринг, ввод данных, оплата и возврат на стол таппер")
    public String acquiringPayment(double totalPay) {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        return transactionId;

    }

    @Step("Переход в эквайринг, ввод данных, ожидание пока транзакция \"протухнет\"")
    public String goToAcquiringAndWaitTillTransactionExpired(double totalPay, int wait) {

        rootPageNestedTests.clickPayment();
        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        forceWait(wait);
        String transactionId = transaction_id.getValue();
        best2PayPage.clickPayButton();
        return transactionId;

    }

    @Step("Проверка всего процесса оплаты, транзакции, ожидание пустого стола")
    public void checkPaymentAndB2pTransaction(String orderType, String transactionId, HashMap<String, Integer> paymentDataKeeper) {

        pagePreLoader.shouldNotBe(visible,Duration.ofSeconds(30));
        reviewPageNestedTests.paymentCorrect(orderType);
        reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

        if (orderType.equals("part")) {

            apiRKeeper.isPrepaymentSuccess(transactionId);

        }

        reviewPage.clickOnFinishButton();

        if (orderType.equals("full")) {

            rootPage.isEmptyOrderAfterClosing();

        }

    }

    @Step("Проверка суммы что суммы изменились после того как изменился заказ на кассе и в таппере нажали оплатить")
    public void checkIfSumsChangedAfterEditingOrder() {

        double totalPaySum = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);
        rootPage.clickOnPaymentButton();

        dishesSumChangedHeading.shouldHave(visible,Duration.ofSeconds(3));
        dishesSumChangedHeading.shouldHave(hidden,Duration.ofSeconds(8));
        pagePreLoader.shouldHave(hidden,Duration.ofSeconds(30));

        double totalPaySumAfterChanging = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, totalPayRegex);

        Assertions.assertNotEquals(totalPaySum, totalPaySumAfterChanging,
                "После изменения заказа на кассе, заказ в таппере не поменялся");

    }

    @Step("Проверяем установку чаевых по умолчанию по сумме, формирование СБ от чаевых," +
            " и корректность суммы оплаты в таппере и б2п")
    public void checkDefaultTipsBySumAndScLogicBySumAndB2P(double cleanDishesSum) {

        rootPage.isDefaultTipsBySumLogicCorrect();

        double tipsSumInTheMiddle = Double.parseDouble(Objects.requireNonNull(totalTipsSumInMiddle.getValue()));
        double serviceChargeInField =
                rootPage.convertSelectorTextIntoDoubleByRgx(serviceChargeContainer, "[^\\d\\.]+");

        serviceChargeInField = updateDoubleByDecimalFormat(serviceChargeInField);
        double serviceChargeSumClear = updateDoubleByDecimalFormat
                (cleanDishesSum * (SERVICE_CHARGE_PERCENT_FROM_TOTAL_SUM / 100));
        double serviceChargeTipsClear = updateDoubleByDecimalFormat
                (tipsSumInTheMiddle * (SERVICE_CHARGE_PERCENT_FROM_TIPS / 100));
        double cleanServiceCharge = serviceChargeSumClear + serviceChargeTipsClear;

        Assertions.assertEquals(cleanServiceCharge, serviceChargeInField, 0.1,
                "Сервисный сбор считается не корректно от суммы и чаевых");

        rootPage.deactivateServiceChargeIfActivated();

        double tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPageNestedTests.clickPayment();
        paymentContainer.shouldBe(exist,Duration.ofSeconds(180));

        double b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        returnToPreviousPage();

        rootPage.activateServiceChargeIfDeactivated();

        tapperTotalPay = rootPage.convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");

        rootPageNestedTests.clickPayment();
        paymentContainer.shouldBe(exist,Duration.ofSeconds(180));

        b2pTotalPay = best2PayPage.getPaymentAmount();

        Assertions.assertEquals(tapperTotalPay, b2pTotalPay, 0.1,
                "Сумма итого к оплате не совпадает с суммой в таппере");

        returnToPreviousPage();

    }




    public String createAndFillOrder(int amountDishesForFillingOrder,String dish,
                                   ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder, String restaurant,
                                     String tableCode, String waiter, String apiUri, String tableUrl, String tableId) {

        apiRKeeper.createDishObject(dishesForFillingOrder, dish, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrderAndOpenTapperTable(restaurant, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableUrl, tableId);

        return apiRKeeper.getGuidFromCreateOrder(rs);

    }

    public void openEmptyTapperTable(String restaurantName, String tableId, String apiUri, String tableUrl) {

        apiRKeeper.isTableEmpty(restaurantName, tableId, apiUri);
        rootPage.openPage(tableUrl);

    }


    public void choseAllNonPaidDishesTipsSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndActivateScIfDeactivated();

    }

    public void choseAllNonPaidDishesOneByOneTipsSc() {

        rootPage.activateDivideCheckSliderIfDeactivated();
        rootPage.chooseAllNonPaidDishes();
        rootPage.setRandomTipsOption();
        rootPage.activateServiceChargeIfDeactivated();

    }

    public void choseAllNonPaidDishesNoTipsNoSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPageNestedTests.deactivateTipsAndDeactivateSc();

    }

    public void choseAllNonPaidDishesNoTipsSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPageNestedTests.deactivateTipsAndActivateSc();

    }

    public void choseAllNonPaidDishesTipsNoSc() {

        double cleanDishesSum = rootPage.countAllNonPaidDishesInOrder();
        rootPageNestedTests.checkSumWithAllConditions(cleanDishesSum);
        rootPage.setRandomTipsAndDeactivateScIfActivated();

    }


    public void matchTgMsgDataAndTapperData(String guid, LinkedHashMap<String, String> tapperDataForTgMsg) {

        rootPage.matchTgMsgDataAndTapperData(rootPage.getPaymentTgMsgData(guid), tapperDataForTgMsg);

    }

    public void isMenuCorrect() {

        rootPage.click(appFooterMenuIcon);

        if (emptyOrderMenuButton.isDisplayed()) {

            rootPage.openNewTabAndSwitchTo(ADMIN_AUTHORIZATION_STAGE_URL);
            authorizationPage.authorizeUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

            menu.goToMenuCategory();
            menu.isMenuCorrect();
            menu.activateFirstCategoryAndDishInMenu();

            rootPage.switchBrowserTab(0);
            rootPage.refreshPage();

        }

        rootPage.isElementInvisible(emptyOrderMenuButton);

    }




}
