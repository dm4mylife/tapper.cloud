package tapper.tests.keeper_e2e._5_sockets;


import api.ApiRKeeper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import tapper_table.Best2PayPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.BaseTestFourBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.orderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.*;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;
import static data.selectors.TapperTable.RootPage.DishList.allDisabledDishes;
import static data.selectors.TapperTable.RootPage.DishList.allPaidDishes;

@Order(63)
@Epic("RKeeper")
@Feature("Сокеты")
@Story("Одновременная частичная  оплата с 4х устройств ")
@DisplayName("Одновременная частичная  оплата с 4х устройств ")


@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _6_3_PartCheckEveryStatusFourGuestsTest extends BaseTestFourBrowsers {

    static String guid;
    static int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 8;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, Integer> paymentDataKeeper;
    static String transactionId;
    ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();

    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();

    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);

        Response rs = rootPageNestedTests.createAndFillOrder(R_KEEPER_RESTAURANT, TABLE_CODE_222,WAITER_ROBOCOP_VERIFIED_WITH_CARD,
                AUTO_API_URI,dishesForFillingOrder,TABLE_AUTO_222_ID);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
    }

    @Test
    @DisplayName("1.2. Открываем стол на четырёх разных устройствах")
    public void openTables() {

        using(firstBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

        using(secondBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

        using(thirdBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

        using(fourthBrowser, () -> {

            rootPage.openUrlAndWaitAfter(STAGE_RKEEPER_TABLE_222);
            rootPage.isDishListNotEmptyAndVisible();

        });

    }


    @Test
    @DisplayName("1.3. Выбираем рандомно блюда первым гостем")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWaitingForSocketChangePositions(2500);

        });

    }

    @Test
    @DisplayName("1.4. Проверяем у всех гостей, блюда в статусе Оплачиваются")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("1.5. Сохраняем платежные данные для эквайринга и тг")
    public void savePaymentData1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

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

        using(firstBrowser, () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверяем сообщение в телеграмме")
    public void checkTgMessage1Guest() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.9. Проверяем у всех гостей, что выбранные ранее блюда первым гостем в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid1Guest() {

        using(secondBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen)));
        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.0. Выбираем рандомно блюда вторым гостем")
    public void chooseDishesAndCheckAfterDivided2ndGuest() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWaitingForSocketChangePositions(2500);

        });

    }

    @Test
    @DisplayName("2.1. Проверяем у всех гостей, что блюда в статусе Оплачиваются, которые второй гость выбрал")
    public void checkDisabledDishes2Guest() {

        using(firstBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.2. Сохраняем платежную информацию для эквайринга и тг")
    public void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

        });

    }

    @Test
    @DisplayName("2.3. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring2Guest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment2Guest() {

        using(secondBrowser, () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.5. Проверяем сообщение в телеграмме")
    public void checkTgMessage2Guest() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.6. Проверяем у всех гостей, что выбранные ранее блюда вторым гостем в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid2Guest() {

        using(secondBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2)));
        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2)));

    }

    @Test
    @DisplayName("2.7. Выбираем рандомно блюда третьим гостем")
    public void chooseDishesAndCheckAfterDivided3dGuest() {

        using(thirdBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();
            rootPage.forceWaitingForSocketChangePositions(2500);

        });

    }

    @Test
    @DisplayName("2.8. Проверяем у всех гостей что выбранные ранее блюда в статусе Оплачено")
    public void checkDisabledDishes3Guest() {

        using(firstBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.9. Сохраняем платежную информацию для эквайринга и тг")
    public void savePaymentData3Guest() {

        using(thirdBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

        });

    }

    @Test
    @DisplayName("3.0. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring3Guest() {

        using(thirdBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("3.1. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment3Guest() {

        using(thirdBrowser, () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.2. Проверка сообщения в телеграмме")
    public void checkTgMessage3Guest() {

        using(thirdBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("3.3. Проверяем у всех гостей, что выбранные ранее блюда в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid3Guest() {

        using(secondBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));
        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));

    }

    @Test
    @DisplayName("3.4.Сохраняем платежные данные и переходим в оплату последним гостем")
    public void chooseDishesAndCheckAfterDivided4Guest() {

        using(fourthBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(TABLE_AUTO_222_ID);

            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @DisplayName("3.5. Проверяем у всех гостей, что блюда выбранные четвертым гостем в статусе Оплачиваются")
    public void checkDisabledDishesWhen4GuestChosen() {

        using(firstBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("3.6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    public void payAndGoToAcquiring4Guest() {

        using(fourthBrowser, () -> {

            best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
            transactionId = transaction_id.getValue();
            best2PayPage.clickPayButton();

        });

    }

    @Test
    @DisplayName("3.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    public void checkPayment4Guest() {

        using(fourthBrowser, () -> nestedTests.checkPaymentAndB2pTransaction(orderType = "full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.8. Проверка сообщения в телеграмме")
    public void checkTgMessage4Guest() {

        using(fourthBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType = "full");
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("3.9. Проверяем у всех гостей что стол пустой")
    public void isEmptyOrder() {

        using(firstBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(secondBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(thirdBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(fourthBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }

}
