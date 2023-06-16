package tapper.tests.personal_account.admin.tips;

import admin_personal_account.tips.Tips;
import api.ApiRKeeper;
import data.AnnotationAndStepNaming;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static api.ApiData.OrderData.TORT;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_LOGIN_EMAIL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_RESTAURANT_PASSWORD;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.tips10;


@Epic("Личный кабинет администратора ресторана")
@Feature("Чаевые")
@DisplayName("Отправка чаевых официанту и кальянщику, чаевые для кухни выключены")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WaiterAndHookahTips extends PersonalAccountTest {

    protected final String restaurantName = TableData.Keeper.Table_555.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_555.tableCode;
    protected final String waiterName = TableData.Keeper.Table_555.waiter;
    protected final String apiUri = TableData.Keeper.Table_555.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_555.tableUrl;
    protected final String tableId = TableData.Keeper.Table_555.tableId;

    String hookahRole = "hookah";
    String kitchenRole = "kitchen";
    static double waiterTips;
    static double hookahTips;
    static double kitchenTips;
    static double totalTips = 0;
    static HashMap<String, String> paymentDataKeeper;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static String transactionId;
    static String guid;
    static double totalPay;
    static String orderType = "full";

    AuthorizationPage authorizationPage = new AuthorizationPage();
    RootPage rootPage = new RootPage();
    NestedTests nestedTests = new NestedTests();
    Tips tips = new Tips();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();


    @Test
    @Order(1)
    @DisplayName("Создание заказа на кассе.Авторизация под администратором в личном кабинете")
    void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

        apiRKeeper.createDishObject(dishesForFillingOrder, TORT, 6);

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiterName, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_LOGIN_EMAIL, ADMIN_RESTAURANT_PASSWORD);

    }

    @Test
    @Order(2)
    @DisplayName("Переход на страницу чаевых")
    void goToTipsCategory() {

        tips.goToTipsCategory();
        tips.isServiceWorkerRoleTipsTabCorrect(hookahRole);

    }
    @Test
    @Order(3)
    @DisplayName("Активируем чаевые у кальянщик и кухни")
    void isTipsHookahTabCorrect() {

        tips.activateTips();
        tips.goToKitchenTipsCategory();
        tips.isServiceWorkerRoleTipsTabCorrect(kitchenRole);
        tips.deactivateTips();

    }

    @Test
    @Order(4)
    @DisplayName("Открываем таппер")
    void isServiceWorkerAvatarCorrect() {

        rootPage.openNewTabAndSwitchTo(tableUrl);
        rootPage.isTableHasOrder();

    }
    @Test
    @Order(5)
    @DisplayName("Выставляем чаевые официанту")
    void setWaiterTips() {

       totalTips += rootPage.setCertainTipsOption("waiter",tips10);
       waiterTips = rootPage.getCurrentTipsSum();

    }
    @Test
    @Order(6)
    @DisplayName("Выставляем чаевые кальянщику")
    void setHookahTips() {

        totalTips += rootPage.setCertainTipsOption(hookahRole,tips10);
        hookahTips = rootPage.getCurrentTipsSum();

    }

    @Test
    @Order(8)
    @DisplayName("Проверяем чаевые за все роли")
    void isTotalTipsCorrect() {

        Assertions.assertEquals(totalTips, waiterTips + hookahTips,
                "Чаевые за все роли не совпали по сумме");

    }


    @Test
    @Order(9)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.savePaymentData)
    void savePaymentDataForAcquiring() {

        totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
        paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
        tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

    }

    @Test
    @Order(10)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.goToAcquiringAndPayOrder)
    void payAndGoToAcquiring() {

        transactionId = nestedTests.acquiringPayment(totalPay);

    }

    @Test
    @Order(11)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.isPaymentCorrect)
    void checkPayment() {

        nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper);

    }

    @Test
    @Order(12)
    @DisplayName(AnnotationAndStepNaming.DisplayName.TapperTable.isTelegramMessageCorrect)
    void matchTgMsgDataAndTapperData() {

        nestedTests.matchTgMsgDataAndTapperData(guid, tapperDataForTgMsg, orderType);

    }

}
