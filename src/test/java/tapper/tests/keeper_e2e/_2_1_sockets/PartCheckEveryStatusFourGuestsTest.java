package tapper.tests.keeper_e2e._2_1_sockets;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import tapper_table.Best2PayPage;
import tapper_table.RootPage;
import tapper_table.nestedTestsManager.Best2PayPageNestedTests;
import tapper_table.nestedTestsManager.NestedTests;
import tapper_table.nestedTestsManager.RootPageNestedTests;
import tests.FourBrowsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.OrderData.*;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.using;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.STAGE_RKEEPER_TABLE_222;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;
import static data.selectors.TapperTable.RootPage.DishList.*;


@Epic("RKeeper")
@Feature("Сокеты")
@Story("Четыре пользователя. Частичная оплата на 1одном устройстве, и проверка этих позиций на других")
@DisplayName("Четыре пользователя. Частичная оплата на 1одном устройстве, и проверка этих позиций на других")


@TestMethodOrder(MethodOrderer.DisplayName.class)
class PartCheckEveryStatusFourGuestsTest extends FourBrowsers {

    protected final String restaurantName = TableData.Keeper.Table_222.restaurantName;
    protected final String tableCode = TableData.Keeper.Table_222.tableCode;
    protected final String waiter = TableData.Keeper.Table_222.waiter;
    protected final String apiUri = TableData.Keeper.Table_222.apiUri;
    protected final String tableUrl = TableData.Keeper.Table_222.tableUrl;
    protected final String tableId = TableData.Keeper.Table_222.tableId;
    static String guid;
    static int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 8;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static LinkedHashMap<String, String> tapperDataForTgMsg;
    static LinkedHashMap<String, String> telegramDataForTgMsg;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
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

        Response rs = rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri,
                dishesForFillingOrder,tableId);

        guid = apiRKeeper.getGuidFromCreateOrder(rs);
    }

    @Test
    @DisplayName("1.2. Открываем стол на четырёх разных устройствах")
    public void openTables() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(thirdBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(fourthBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда первым гостем")
    public void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("1.4. Проверяем у всех гостей, блюда в статусе Оплачиваются")
    public void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("1.5. Сохраняем платежные данные для эквайринга и тг")
    public void savePaymentData1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

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

        using(firstBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверяем сообщение в телеграмме")
    public void checkTgMessage1Guest() {

        using(firstBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("1.9. Проверяем у всех гостей, что выбранные ранее блюда первым гостем в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid1Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen));

        });
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


        });

    }

    @Test
    @DisplayName("2.1. Проверяем у всех гостей, что блюда в статусе Оплачиваются, которые второй гость выбрал")
    public void checkDisabledDishes2Guest() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });
        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.2. Сохраняем платежную информацию для эквайринга и тг")
    public void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

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

        using(secondBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.5. Проверяем сообщение в телеграмме")
    public void checkTgMessage2Guest() {

        using(secondBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("2.6. Проверяем у всех гостей, что выбранные ранее блюда вторым гостем в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid2Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen * 2);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2));

        });
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


        });

    }

    @Test
    @DisplayName("2.8. Проверяем у всех гостей что выбранные ранее блюда в статусе Оплачено")
    public void checkDisabledDishes3Guest() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });
        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.9. Сохраняем платежную информацию для эквайринга и тг")
    public void savePaymentData3Guest() {

        using(thirdBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

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

        using(thirdBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.2. Проверка сообщения в телеграмме")
    public void checkTgMessage3Guest() {

        using(thirdBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,orderType);
            rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg, tapperDataForTgMsg);

        });

    }

    @Test
    @DisplayName("3.3. Проверяем у всех гостей, что выбранные ранее блюда в статусе Оплачено")
    public void checkIfDishesDisabledAtAnotherGuestArePaid3Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen* 3);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3));

        });
        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));

    }

    @Test
    @DisplayName("3.4.Сохраняем платежные данные и переходим в оплату последним гостем")
    public void chooseDishesAndCheckAfterDivided4Guest() {

        using(fourthBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId, "keeper");

            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @DisplayName("3.5. Проверяем у всех гостей, что блюда выбранные четвертым гостем в статусе Оплачиваются")
    public void checkDisabledDishesWhen4GuestChosen() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });
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

        using(fourthBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.8. Проверка сообщения в телеграмме")
    public void checkTgMessage4Guest() {

        using(fourthBrowser, () -> {

            telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,"full");
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
