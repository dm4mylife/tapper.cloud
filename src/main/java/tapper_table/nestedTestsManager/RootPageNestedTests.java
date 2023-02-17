package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import data.selectors.TapperTable;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsCreateOrderBasic;
import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.selectors.TapperTable.Best2PayPage.transaction_id;
import static data.selectors.TapperTable.RootPage.DishList.*;
import static data.selectors.TapperTable.RootPage.TapBar.*;
import static data.selectors.TapperTable.RootPage.TipsAndCheck.*;


public class RootPageNestedTests extends RootPage {

    RootPage rootPage = new RootPage();
    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Disabled
    @Step("Проверка что позиции в заказе на кассе и в таппере одинаковы")
    public void isOrderInKeeperCorrectWithTapper() { // toDO доделать, слишком много разных условий

        isDishListNotEmptyAndVisible();

        if (modalHintContainer.isDisplayed()) {

            click(modalHintCloseButton);
            System.out.println("Закрыли подсказку");

        }

        // matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }


    @Step("Проверка что позиции в заказе на кассе и в таппере одинаковы")
    public void newIsOrderInKeeperCorrectWithTapper(String tableId) { // toDO доделать, слишком много разных условий

        isDishListNotEmptyAndVisible();

        if (modalHintContainer.isDisplayed()) {

            click(modalHintCloseButton);
            System.out.println("Закрыли подсказку");

        }
        HashMap<Integer, Map<String, Double>> cashDeskData = getCashDeskData(tableId);
        matchTapperOrderWithOrderInKeeper(cashDeskData);

    }

    @Step("Проверка пустого стола и всех элементов")
    public void isEmptyTableCorrect() {

        //isHintModalCorrect();
        //closeHintModal();
        isElementVisible(appHeader);
        isElementVisible(tableNumber);
        emptyOrderHeading.shouldHave(matchText("Ваш заказ появится здесь"));
        isElementVisible(appFooter);
        isElementVisible(callWaiterButton);
        isElementVisible(totalSumInWalletCounter);
        totalSumInWalletCounter.shouldHave(matchText("0 ₽"));

    }

    @Step("Проверка кнопки обновления страницы при пустом столе")
    public void isRefreshButtonCorrect() {

        isElementVisible(refreshButtonEmptyPage);
        click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText("Обновлено, но заказ ещё не создан"));
        dishesSumChangedHeading.shouldBe(hidden,Duration.ofSeconds(5));

    }

    @Step("Клик в оплату, появление лоадера и проверка что мы на эквайринге")
    public void clickPayment() {

        clickOnPaymentButton();
        isPageLoaderShown();
        dishesSumChangedHeading.shouldNotHave(visible, Duration.ofSeconds(2));
        best2PayPage.isTestBest2PayUrl();
        best2PayPage.isPaymentContainerAndVpnShown();

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmountVerifiedNonCard(int amountDishes) {

        activateDivideCheckSliderIfDeactivated();

        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditionsWithNoWaiterCard(cleanTotalSum);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmount(int amountDishes) {

        activateDivideCheckSliderIfDeactivated();

        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditions(cleanTotalSum);

    }

    @Step("Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "все опции чаевых корректны с\\без СБ, СБ считается по формуле корректно")
    public void checkSumWithAllConditions(double cleanDishesSum) {

        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkTipsOptionWithSC(cleanDishesSum);
        checkTipsOptionWithoutSC(cleanDishesSum);
        checkScLogic(cleanDishesSum);

    }

    @Step("Официант верифицирован, но без карты. Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "чаевые отключены, СБ считается по формуле корректно и включено")
    public void checkSumWithAllConditionsWithNoWaiterCard(double cleanDishesSum) {

        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkIsNoTipsElementsIfVerifiedNonCard();
        checkScLogic(cleanDishesSum);

    }

    @Step("Официант не верифицирован. Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "чаевые отключены, СБ считается по формуле корректно и включено")
    public void checkSumWithAllConditionsWithNonVerifiedWaiter(double cleanDishesSum) {

        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkIsNoTipsElementsIfNonVerifiedNonCard();
        checkScLogic(cleanDishesSum);

    }

    @Step("Проверяем сумму за сами блюда с 'Итого к оплате' и счётчиком в иконке кошелька без СБ и чаевых")
    public void checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(double cleanTotalSum) {

        deactivateTipsAndDeactivateSc();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);
        isSumInWalletMatchWithTotalPay();

        activateRandomTipsAndActivateSc();

    }

    @Step("Отключаем чаевые и выключаем сервисный сбор")
    public void deactivateTipsAndDeactivateSc() {

        resetTips();
        deactivateServiceChargeIfActivated();

    }

    @Step("Отключаем чаевые и включаем сервисный сбор")
    public void deactivateTipsAndActivateSc() {

        resetTips();
        activateServiceChargeIfDeactivated();

    }

    @Step("Включаем рандомные чаевые и сервисный сбор")
    public void activateRandomTipsAndActivateSc() {

        setRandomTipsOption();
        activateServiceChargeIfDeactivated();

    }

    @Step("Включаем рандомные чаевые и сервисный сбор")
    public void activateRandomTipsAndDeactivateSc() {

        setRandomTipsOption();
        deactivateServiceChargeIfActivated();

    }

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ")
    public void checkAllDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();

        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        areTipsOptionsCorrect(cleanTotalSum);

        cancelTipsAndActivateSC(cleanTotalSum);

    }

    @Step("Проверка соответствует ли скидка на кассе полю 'Скидка' на столе")
    public void isDiscountCorrectOnTable(double discountSumFromDesk) {

        double totalDiscount = convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\d\\.]+");
        System.out.println(totalDiscount + " скидка в поле 'Скидка'");

        Assertions.assertEquals(discountSumFromDesk,totalDiscount,"Скидка с кассы не соответствует скидке на столе");
        System.out.println("Скидка на кассе соответствует скидке на столе");

        double clearDiscountOnDish = totalDiscount / allDishesInOrder.size();;

        allDishesInOrder.asFixedIterable().stream().forEach(element -> {

           double dishPriceNoDiscount = convertSelectorTextIntoDoubleByRgx(element.$(".--old"),"[^\\d\\.]+");
           double dishPriceWithDiscount = convertSelectorTextIntoDoubleByRgx(element.$(".--new"),"[^\\d\\.]+");

           Assertions.assertEquals(dishPriceWithDiscount,dishPriceNoDiscount - clearDiscountOnDish,
                   "Скидка рассчитывается некорректно");
            System.out.println("Скидка по позиции рассчитывается корректно");

        });

    }

    @Step("Проверка что итого к оплате считается корректно со скидкой, поле со скидкой корректно")
    public void checkIsDiscountMatchWithSums(double discount) {

        double cleanTotalSum = countAllNonPaidDishesInOrder();
        System.out.println(discount + " скидка");

        isElementVisible(discountField);

        double discountInCheck = convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\d\\.]+");
        double totalPayInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double serviceCharge = getCurrentSCSum();
        double tips = getCurrentTipsSum();

        cleanTotalSum += serviceCharge + tips;

        Assertions.assertEquals(discountInCheck, discount, 0.1,
                "Сумма скидок не совпадает из кассы с таппером");
        System.out.println("Скидка из кассы " + discount + " совпадает с суммой в блоке 'Скидка' " + discountInCheck);

        Assertions.assertEquals(totalPayInCheck, cleanTotalSum, 0.1,
                "Сумма итого к оплате не совпадает с чистой суммой со скидкой");
        System.out.println("Сумма итого к оплате " + totalPayInCheck +
                " совпадает с чистой суммой со скидкой " + cleanTotalSum);

    }

    @Step("Проверяем что все варианты чаевых корректны")
    public void areTipsOptionsCorrect(double cleanTotalSum) {

        isActiveTipPercentCorrectWithTotalSumAndSC(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Оплачиваем по {amountDishesPayFor1Time} позиции до тех пор пока весь заказ не будет закрыт")
    public void payTillFullSuccessPayment(int amountDishes,String guid, int timeoutPartPay, int timeoutFullPay, String tableId) {

        isDishListNotEmptyAndVisible();

        while (!allNonPaidAndNonDisabledDishes.isEmpty()) {

            System.out.println(allNonPaidAndNonDisabledDishes.size() + " кол-во не оплаченных блюд");

            if (allNonPaidAndNonDisabledDishes.size() != amountDishes) {

                clearAllSiteData();

                chooseDishesWithRandomAmount(amountDishes);
                isAnotherGuestSumCorrect();
                setCustomTips(String.valueOf(generateRandomNumber(150,250)));

                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.paymentCorrect("part");
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,timeoutPartPay);
                rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

            } else {

                System.out.println("Последняя оплата");
                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg(tableId);

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.fullPaymentCorrect();
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg = rootPage.getPaymentTgMsgData(guid,timeoutFullPay);
                rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

            }

            reviewPage.clickOnFinishButton();
            forceWait(2000); // toDO есть предположение что предоплата не успевает доходить до кассы

        }

    }

    @Step("Закрываем заказ полностью") // toDo костыльное решение по закрытию заказа пока пишется api
    public void closeOrder() {

        if (divideCheckSliderActive.exists()) {

            divideCheckSliderActive.click();

        }

        clickPayment();

        best2PayPageNestedTests.typeDataAndPay();
        best2PayPage.clickPayButton();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();

        isElementVisible(emptyOrderHeading);

    }

    @Step("Сохранение в дату заказа со всеми типа модификаторов, чтобы потом сравнить в таппере")
    public HashMap<Integer, Map<String, Double>> saveOrderDataWithAllModi(Response rs) {

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName;

        int sessionSize = rs.jsonPath().getList("Session.Dish").size();

        for (int currentDishIndex = 0; currentDishIndex < sessionSize; currentDishIndex++) {

            Map<String, Double> temporaryMap = new HashMap<>();

            double dishPrice;
            int modificatorTypeSize = 0;

            if (rs.path("Session.Dish[" + currentDishIndex + "].Modi") != null) {

                if (rs.path("Session.Dish[" + currentDishIndex + "].Modi") instanceof LinkedHashMap) {

                    modificatorTypeSize = 1;

                } else {

                    modificatorTypeSize = rs.jsonPath().getList("Session.Dish[" + currentDishIndex + "].Modi").size();

                }

            }

            currentDishName = rs.jsonPath().getString("Session.Dish[" + currentDishIndex + "]['@attributes'].name");

            double modificatorTotalPrice = 0;
            if (modificatorTypeSize == 1) {

                String modificatorName =
                        rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                "].Modi['@attributes'].name");

                String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi['@attributes'].price");

                double modificatorCurrentPrice = 0;

                if (modificatorCurrentPriceFlag != null) {

                    modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                    "].Modi['@attributes'].price") / 100;

                }

                int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi['@attributes'].count");

                modificatorTotalPrice = modificatorCurrentPrice * modificatorCurrentCount;

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;

                dishPrice = currentDishPrice + modificatorTotalPrice;

            } else {

                for (int currentModificatorTypeIndex = 0; currentModificatorTypeIndex < modificatorTypeSize; currentModificatorTypeIndex++) {

                    String modificatorCurrentPriceFlag = rs.path(
                            "Session.Dish[" + currentDishIndex + "].Modi["
                                    + currentModificatorTypeIndex + "]['@attributes'].price");

                    double modificatorCurrentPrice = 0;

                    if (modificatorCurrentPriceFlag != null) {

                        modificatorCurrentPrice = rs.jsonPath().getDouble
                                ("Session.Dish[" + currentDishIndex +
                                        "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].price") / 100;

                    }

                    int modificatorCurrentCount = rs.jsonPath().getInt
                            ("Session.Dish[" + currentDishIndex + "].Modi["
                                    + currentModificatorTypeIndex + "]['@attributes'].count");

                    modificatorTotalPrice += modificatorCurrentPrice * modificatorCurrentCount;

                }

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;

                dishPrice = currentDishPrice + modificatorTotalPrice;

            }

            int dishQuantity = rs.jsonPath().getInt("Session.Dish[" + currentDishIndex + "]['@attributes'].quantity") / 1000;

            for (int k = 0; k < dishQuantity; k++) {

                temporaryMap.put(currentDishName, dishPrice);
                allDishesInfo.put(totalDishIndex, temporaryMap);

                totalDishIndex++;

            }

        }

        System.out.println("Итоговый список\n" + allDishesInfo);
        return allDishesInfo;

    }


    public int getKeySize(Response response,String totalPath) {

        Object keySizeFlag = response.path(totalPath);

        int keySize;

        if (keySizeFlag instanceof LinkedHashMap) {

            keySize = 1;

        } else if (keySizeFlag == null) {

            System.out.println("Нет блюд");
            return -1;

        } else {

            keySize = response.jsonPath().getList(totalPath).size();

        }

        return keySize;

    }

    public String getKeyPath(int keySize, int keyIndex,String keyName) {

        if (keySize != 1) {

            keyName += "[" + keyIndex + "]";

        }

        return keyName;

    }


    public double getDiscountFromResponse(Response response) {

        double discount = 0;

        if (response.path("@attributes.discountSum") != null) {

            discount = Math.abs(response.jsonPath().getDouble("@attributes.discountSum") / 100);

        }

        return discount;

    }

    @Step("Забираем скидку из кассы")
    public double getTotalDiscount(String table_id) {

        Response rs = apiRKeeper.getOrderInfo(table_id, AUTO_API_URI);
        Object sessionSizeFlag = rs.path("Session");

        int sessionSize;
        int sessionIndexCounter = 0;
        int totalDiscountAmount = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("Session").size();

        }

        System.out.println(sessionSize + " количество сессий\n");

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            String session;

            if (sessionSize == 1) {
                session = "Session";
            } else {
                session = "Session" + "[" + sessionIndexCounter + "]";
            }

            System.out.println("\n" + sessionIndexCounter + " текущая сессия");

            Object discountOrderFlag = rs.path(session + ".Discount['@attributes'].amount");

            if (discountOrderFlag != null) {

                String discountOrderPath = ".Discount";
                Object discountFlag = rs.path(session + discountOrderPath);

                int discountSize;
                int discountIndexCounter = 0;

                if (discountFlag instanceof LinkedHashMap) {

                    discountSize = 1;

                } else {

                    discountSize = rs.jsonPath().getList(session + discountOrderPath).size();

                }

                for (; discountIndexCounter < discountSize; discountIndexCounter++) {

                    String discount;

                    if (discountSize == 1) {

                        discount = session + discountOrderPath;

                    } else {

                        discount = session + discountOrderPath + "[" + discountIndexCounter + "]";

                    }

                    double discountOrder = rs.jsonPath().getDouble(discount + "['@attributes'].amount") / 100;
                    System.out.println("\n" + discountOrder + " скидка по заказу");

                    totalDiscountAmount -= discountOrder;

                }

            }

            String dishPath = ".Dish";
            Object dishSizeSizeFlag = rs.path(session + dishPath);

            String dish = ".Dish";
            int dishSize;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else if (dishSizeSizeFlag == null) {

                dishSize = 0;

            } else {

                dishSize = rs.jsonPath().getList(session + dishPath).size();
                dish = dishPath + "[" + dishIndexCounter + "]";

            }

            System.out.println(dishSize + " количество блюд\n");

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {

                Object discountDishFlag = rs.path(session + dish + ".Discount['@attributes'].amount");

                if (discountDishFlag != null) {

                    double discountDish = rs.jsonPath().getDouble(session + dish + ".Discount['@attributes'].amount") / 100;
                    System.out.println(discountDish + " скидка на блюдо");

                    totalDiscountAmount -= discountDish;

                }

            }

        }

        System.out.println("\n" + totalDiscountAmount + " общая скидка");
        return totalDiscountAmount;

    }

    public HashMap<Integer,Map<String,Double>> getCashDeskData(String tableId) {

        Response rs = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);

        String totalPath = "";
        String session = "Session";
        String dish = "Dish";
        String price = "[\"@attributes\"].price";
        String name = "[\"@attributes\"].name";
        String quantity = "[\"@attributes\"].quantity";
        int totalDishIndex = 0;

        int sessionSize = getKeySize(rs,session);

        HashMap<Integer,Map<String,Double>> orderData = new HashMap<>();

        for (int sessionIndex = 0; sessionIndex < sessionSize; sessionIndex++) {

            String sessionPath = getKeyPath(sessionSize,sessionIndex,session);
            double discount = getDiscountFromResponse(rs);

            totalPath = sessionPath + "." + dish;

            int dishSize = getKeySize(rs,totalPath);

            for (int dishIndex = 0; dishIndex < dishSize; dishIndex++) {

                HashMap<String,Double> tempData = new HashMap<>();

                String dishPath = getKeyPath(dishSize,dishIndex,totalPath);

                double dishPrice = rs.jsonPath().getDouble(dishPath + price) / 100;
                String dishName = rs.jsonPath().getString(dishPath + name);

                int dishQuantity = rs.jsonPath().getInt(dishPath + quantity) / 1000;
                //System.out.println(dishQuantity + " quantity");

                if (dishQuantity != 1) {

                    int dishQuantityIndex = 0;

                    for (; dishQuantityIndex < dishQuantity; dishQuantityIndex++) {

                       // System.out.println(totalDishIndex+dishQuantityIndex + " counter");
                        tempData.put(dishName,dishPrice);
                        orderData.put(totalDishIndex + dishQuantityIndex,tempData);
                      //  System.out.println("Имя блюда : " + dishName + "\nЦена блюда : " + dishPrice + "\n");

                    }

                    totalDishIndex += dishQuantityIndex;

                } else {

                    tempData.put(dishName,dishPrice);
                    orderData.put(totalDishIndex,tempData);
                    totalDishIndex++;

                    // System.out.println("Имя блюда : " + dishName + "\nЦена блюда : " + dishPrice + "\n");

                }

            }

            totalDishIndex++;

        }

        System.out.println(orderData);
        return orderData;

    }

    public void createAndFillOrder(String restaurant, String tableCode, String waiter, String apiUri, String dishId,
                                   int quantity, String tableUrl, String tableId) {

        Response rs = apiRKeeper.createOrder
                (rqParamsCreateOrderBasic(restaurant, tableCode, waiter), apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        ArrayList<LinkedHashMap<String, Object>> dishes = new ArrayList<>();

        dishes = apiRKeeper.orderFill(dishes, dishId, quantity);
        apiRKeeper.newFillingOrder(apiRKeeper.rsBodyFillingOrder(restaurant, guid, dishes));

        rootPage.openUrlAndWaitAfter(tableUrl);
        newIsOrderInKeeperCorrectWithTapper(tableId);

    }



}
