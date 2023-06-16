package tapper.tests.keeper._2_1_sockets;


import api.ApiRKeeper;
import data.TableData;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
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

import static api.ApiData.OrderData.BARNOE_PIVO;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.using;
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

    static int amountDishesToBeChosen = 2;
    static int amountDishesForFillingOrder = 8;
    static HashMap<Integer, Map<String, Double>> chosenDishes;
    static double totalPay;
    static String orderType = "part";
    static HashMap<String, String> paymentDataKeeper;
    static String transactionId;


    RootPage rootPage = new RootPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    NestedTests nestedTests = new NestedTests();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    Best2PayPage best2PayPage = new Best2PayPage();

    RootPageNestedTests rootPageNestedTests = new RootPageNestedTests();

    @Test
    @DisplayName("1.1. Создание заказа в r_keeper")
    public void createAndFillOrder() {

        ArrayList<LinkedHashMap<String, Object>> dishesForFillingOrder = new ArrayList<>();
        apiRKeeper.createDishObject(dishesForFillingOrder, BARNOE_PIVO, amountDishesForFillingOrder);
        rootPageNestedTests.createAndFillOrder(restaurantName, tableCode, waiter, apiUri, dishesForFillingOrder,tableId);

    }

    @Test
    @DisplayName("1.2. Открываем стол на четырёх разных устройствах")
    void openTables() {

        using(firstBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(secondBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(thirdBrowser, () -> rootPage.openNotEmptyTable(tableUrl));
        using(fourthBrowser, () -> rootPage.openNotEmptyTable(tableUrl));

    }

    @Test
    @DisplayName("1.3. Выбираем рандомно блюда первым гостем")
    void chooseDishesAndCheckAfterDivided() {

        using(firstBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("1.4. Проверяем у всех гостей, блюда в статусе Оплачиваются")
    void checkDisabledDishes() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("1.5. Сохраняем платежные данные для эквайринга и тг")
    void savePaymentData1Guest() {

        using(firstBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("1.6. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring() {

        using(firstBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("1.7. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment() {

        using(firstBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("1.8. Проверяем у всех гостей, что выбранные ранее блюда первым гостем в статусе Оплачено")
    void checkIfDishesDisabledAtAnotherGuestArePaid1Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("1.9. Выбираем рандомно блюда вторым гостем")
    void chooseDishesAndCheckAfterDivided2ndGuest() {

        using(secondBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("2.0. Проверяем у всех гостей, что блюда в статусе Оплачиваются, которые второй гость выбрал")
    void checkDisabledDishes2Guest() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.1. Сохраняем платежную информацию для эквайринга и тг")
    void savePaymentData2Guest() {

        using(secondBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("2.2. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring2Guest() {

        using(secondBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.3. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment2Guest() {

        using(secondBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("2.4. Проверяем у всех гостей, что выбранные ранее блюда вторым гостем в статусе Оплачено")
    void checkIfDishesDisabledAtAnotherGuestArePaid2Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen * 2);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2));

        });

        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 2)));

    }

    @Test
    @DisplayName("2.5. Выбираем рандомно блюда третьим гостем")
    void chooseDishesAndCheckAfterDivided3dGuest() {

        using(thirdBrowser, () -> {

            rootPage.activateDivideCheckSliderIfDeactivated();
            rootPage.chooseCertainAmountDishes(amountDishesToBeChosen);
            chosenDishes = rootPage.getChosenDishesAndSetCollection();

        });

    }

    @Test
    @DisplayName("2.6. Проверяем у всех гостей что выбранные ранее блюда в статусе Оплачено")
    void checkDisabledDishes3Guest() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(fourthBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("2.7. Сохраняем платежную информацию для эквайринга и тг")
    void savePaymentData3Guest() {

        using(thirdBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

        });

    }

    @Test
    @DisplayName("2.8. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring3Guest() {

        using(thirdBrowser, () -> transactionId = nestedTests.acquiringPayment(totalPay));

    }

    @Test
    @DisplayName("2.9. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment3Guest() {

        using(thirdBrowser,
                () -> nestedTests.checkPaymentAndB2pTransaction(orderType, transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.0. Проверяем у всех гостей, что выбранные ранее блюда в статусе Оплачено")
    void checkIfDishesDisabledAtAnotherGuestArePaid3Guest() {

        using(secondBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesPayedStatuses,amountDishesToBeChosen* 3);
            allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3));

        });

        using(thirdBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));
        using(fourthBrowser, () -> allPaidDishes.shouldHave(size(amountDishesToBeChosen * 3)));

    }

    @Test
    @DisplayName("3.1.Сохраняем платежные данные и переходим в оплату последним гостем")
    void chooseDishesAndCheckAfterDivided4Guest() {

        using(fourthBrowser, () -> {

            totalPay = rootPage.saveTotalPayForMatchWithAcquiring();
            paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
            rootPage.clickOnPaymentButton();

        });

    }

    @Test
    @DisplayName("3.2. Проверяем у всех гостей, что блюда выбранные четвертым гостем в статусе Оплачиваются")
    void checkDisabledDishesWhen4GuestChosen() {

        using(firstBrowser, () -> {

            rootPage.isDishStatusChanged(allDishesDisabledStatuses,amountDishesToBeChosen);
            allDisabledDishes.shouldHave(size(amountDishesToBeChosen));

        });

        using(secondBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));
        using(thirdBrowser, () -> allDisabledDishes.shouldHave(size(amountDishesToBeChosen)));

    }

    @Test
    @DisplayName("3.3. Переходим на эквайринг, вводим данные, оплачиваем заказ")
    void payAndGoToAcquiring4Guest() {

        using(fourthBrowser, () -> {

            best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
            transactionId = transaction_id.getValue();
            best2PayPage.clickPayButton();

        });

    }

    @Test
    @DisplayName("3.4. Проверяем корректность оплаты, проверяем что транзакция в б2п соответствует оплате")
    void checkPayment4Guest() {

        using(fourthBrowser, () ->
                nestedTests.checkPaymentAndB2pTransaction("full", transactionId, paymentDataKeeper));

    }

    @Test
    @DisplayName("3.5. Проверяем у всех гостей что стол пустой")
    void isEmptyOrder() {

        using(firstBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(secondBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(thirdBrowser, () -> rootPage.isEmptyOrderAfterClosing());
        using(fourthBrowser, () -> rootPage.isEmptyOrderAfterClosing());

    }

}
