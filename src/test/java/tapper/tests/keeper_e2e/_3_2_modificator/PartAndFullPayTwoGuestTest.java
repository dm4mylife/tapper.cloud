package tapper.tests.keeper_e2e._3_2_modificator;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTestTwoBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;

@Epic("RKeeper")
@Feature("Модификаторы")
@Story("Частичная оплата позиций с модификаторами с 1-го устройства и полная оплата со 2-го")
@DisplayName("Частичная оплата позиций с модификаторами с 1-го устройства и полная оплата со 2-го")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class PartAndFullPayTwoGuestTest extends BaseTestTwoBrowsers {

    static String guid;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    static int amountDishesToBeChosen = 1;
    static HashMap<Integer, Map<String, Double>> orderInKeeper;

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();
    NestedTests nestedTests = new NestedTests();


    @Test
    @DisplayName("1.1. Создание заказа в r_keeper и открытие стола, проверка что позиции на кассе совпадают с позициями в таппере")
    public void createAndFillOrder() {

        Response rs = rootPageNestedTests.createOrder(R_KEEPER_RESTAURANT, TABLE_CODE_333,
                WAITER_ROBOCOP_VERIFIED_WITH_CARD, AUTO_API_URI,TABLE_AUTO_333_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);

        ArrayList<LinkedHashMap<String, Object>> modifiers = new ArrayList<>() {
            {
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_MODI_SOLT_ZERO_PRICE,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_KARTOFEL_FRI,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_VEG_SALAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(GOVYADINA_PORTION,1, new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(PAID_MODI_SOUS,1));
                        add(apiRKeeper.createModificatorObject(PAID_MODI_VEG_SALAD,1));
                    }
                }));
                add(apiRKeeper.rqBodyFillModificatorArrayWithDishes(BORSH,1,new ArrayList<>(){
                    {
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_SALT,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_PEPPER,1));
                        add(apiRKeeper.createModificatorObject(FREE_NECESSARY_MODI_GARLIC,1));
                    }
                }));

            }
        };

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(R_KEEPER_RESTAURANT,guid, modifiers));

        Response rsOrderInfo = apiRKeeper.getOrderInfo(TABLE_AUTO_333_ID, AUTO_API_URI);
        orderInKeeper = rootPageNestedTests.saveOrderDataWithAllModi(rsOrderInfo);

    }

    @Test
    @DisplayName("1.2. Открываем стол на двух разных устройствах, проверяем что не пустые")
    public void openTables() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_333);
            rootPage.isTableHasOrder();

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_333);
            rootPage.isTableHasOrder();

        });

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда у первого гостя, сохраняем данные для след.теста. Сбрасываем чаевые")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPageNestedTests.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.checkTipsAfterReset();

        });
    }

    @Test
    @DisplayName("1.4. Проверяем у второго гостя что блюда в статусе Оплачиваются, которые первый гость выбрал")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.checkIfDishesDisabledEarlier(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();

        });

    }

    @Test
    @DisplayName("1.5. Переключаемся на первого гостя, сохраняем платежные данные")
    public void switchBackTo1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_333_ID);

        });

    }

    @Test
    @DisplayName("1.6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment() {

        using(firstBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.9. Переключаемся на второго гостя, проверяем что выбранные ранее блюда в статусе Оплачено")
    public void switchTo2ndGuestAndCheckPaidDishes() {

        using(secondBrowser, () -> {

            rootPage.checkIfDishesDisabledAtAnotherGuestArePaid(chosenDishes);
            rootPage.checkIfPaidAndDisabledDishesCantBeChosen();
            rootPage.deactivateDivideCheckSliderIfActivated();

        });

    }

    @Test
    @DisplayName("2.0. Сохраняем платежные данные второго гостя")
    public void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_333_ID);

        });

    }

    @Test
    @DisplayName("2.1. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring2Guest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.2. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment2Guest() {

        using(secondBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.3. Проверка сообщения в телеграмме")
    public void clearDataAndChoseAgain2Guest() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.4. Проверяем у первого гостя что у него пустой стол")
    public void switchTo2GuestAndCheckPaidDishes() {

        using(secondBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }
    @Test
    @DisplayName("2.5. Проверяем у второго гостя что у него пустой стол")
    public void switchTo1GuestAndCheckPaidDishes() {

        using(firstBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }


}
