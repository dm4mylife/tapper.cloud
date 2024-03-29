package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
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

import static com.codeborne.selenide.Condition.*;
import static data.Constants.RegexPattern.TapperTable.discountInCheckRegex;
import static data.Constants.TestData.TapperTable.AUTO_API_URI;
import static data.Constants.TestData.TapperTable.REFRESH_TABLE_BUTTON_TEXT;
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

        isTableHasOrder();

        if (modalHintContainer.isDisplayed()) {

            click(modalHintCloseButton);

        }

        // matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }


    @Step("Проверка что позиции в заказе на кассе и в таппере одинаковы")
    public void newIsOrderInKeeperCorrectWithTapper(String tableId) { // toDO доделать, слишком много разных условий

        HashMap<Integer, Map<String, Double>> cashDeskData = getCashDeskData(tableId);
        matchTapperOrderWithOrderInKeeper(cashDeskData);

    }

    @Step("Проверка пустого стола и всех элементов")
    public void isEmptyTableCorrect() {

        isElementVisible(appHeader);
        isElementVisible(tableNumber);
        emptyOrderHeading.shouldHave(matchText("Ваш заказ появится здесь"));
        isElementVisible(appFooter);
        isElementVisible(callWaiterButton);
        isElementVisible(totalSumInWalletCounter);
        totalSumInWalletCounter.shouldHave(exactText("0 ₽"));

    }

    @Step("Проверка кнопки обновления страницы при пустом столе")
    public void isRefreshButtonCorrect() {

        isElementVisible(refreshButtonEmptyPage);
        click(refreshButtonEmptyPage);
        dishesSumChangedHeading.shouldBe(visible,matchText(REFRESH_TABLE_BUTTON_TEXT));
        dishesSumChangedHeading.shouldBe(hidden,Duration.ofSeconds(5));

    }

    @Step("Клик в оплату, появление лоадера и проверка что мы на эквайринге")
    public void clickPayment() {

        clickOnPaymentButton();
        isPageLoaderShown();
        dishesSumChangedHeading.shouldNotHave(visible, Duration.ofSeconds(2));

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmountVerifiedNonCard(int amountDishes) {

        activateDivideCheckSliderIfDeactivated();

        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countOnlyAllChosenDishesDivided();
        checkSumWithAllConditionsWithNoWaiterCard(cleanTotalSum);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmount(int amountDishes) {

        activateDivideCheckSliderIfDeactivated();

        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countOnlyAllChosenDishesDivided();

        checkSumWithAllConditions(cleanTotalSum);

    }

    @Step("Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "все опции чаевых корректны с\\без СБ, СБ считается по формуле корректно")
    public void checkSumWithAllConditions(double cleanDishesSum) {

        scrollTillBottom();
        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        isServiceChargeTextCorrect();
        checkTipsOptionWithSC(cleanDishesSum);
        checkTipsOptionWithoutSC(cleanDishesSum);
        checkScLogic(cleanDishesSum);

    }

    @Step("Официант верифицирован, но без карты. Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "чаевые отключены, СБ считается по формуле корректно и включено")
    public void checkSumWithAllConditionsWithNoWaiterCard(double cleanDishesSum) {

        scrollTillBottom();
        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkIsNoTipsElementsIfVerifiedNonCard();
        checkScLogic(cleanDishesSum);

    }

    @Step("Официант не верифицирован. Проверка что чистая сумма позиций совпадает с общей суммой в 'Итого к оплате', " +
            "чаевые отключены, СБ считается по формуле корректно и включено")
    public void checkSumWithAllConditionsWithNonVerifiedWaiter(double cleanDishesSum) {

        scrollTillBottom();
        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkIsNoTipsElementsIfNonVerifiedNonCard();
        checkScLogic(cleanDishesSum);

    }

    @Step("Проверяем сумму за сами блюда с 'Итого к оплате' и счётчиком в иконке кошелька без СБ и чаевых")
    public void checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(double cleanTotalSum) {

        deactivateTipsAndDeactivateSc();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum, 0);
        isSumInWalletMatchWithTotalPay();

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

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ и со скидкой")
    public void checkAllDishesSumsWithAllConditionsConsideringDiscount() { //

        double cleanTotalSumWithDiscount = countAllNonPaidDishesInOrder();

        if (markupSum.isDisplayed()) {

            cleanTotalSumWithDiscount += convertSelectorTextIntoDoubleByRgx(markupSum,discountInCheckRegex);

        } else if (discountSum.isDisplayed()) {

            cleanTotalSumWithDiscount -= convertSelectorTextIntoDoubleByRgx(discountSum,discountInCheckRegex);

        }


        double cleanTotalDishAmount = countAllDishes();

        scrollTillBottom();

        isTotalCheckSumCorrect(cleanTotalDishAmount);

        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSumWithDiscount);

        areTipsOptionsCorrect(cleanTotalSumWithDiscount);

    }

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ и со скидкой")
    public void checkChosenDishesSumsWithAllConditionsConsideringDiscount(int amountDishesToBeChosen) { //

        activateDivideCheckSliderIfDeactivated();
        chooseCertainAmountDishes(amountDishesToBeChosen);

        double cleanTotalSumWithDiscount = countOnlyAllChosenDishesDivided();
        double cleanTotalDishAmount = countAllDishes();

        scrollTillBottom();

        isMarkedSumCorrect(cleanTotalSumWithDiscount);
        isTotalCheckSumCorrect(cleanTotalDishAmount);

        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSumWithDiscount);

        areTipsOptionsCorrect(cleanTotalSumWithDiscount);

    }


    @Step("Проверка соответствует ли скидка на кассе полю 'Скидка' на столе")
    public void isDiscountVisible(double discountSumFromDesk) {

        discountSum.shouldBe(visible);

        double totalDiscount = convertSelectorTextIntoDoubleByRgx(discountSum, discountInCheckRegex);

        Assertions.assertEquals(discountSumFromDesk,totalDiscount,
                "Скидка с кассы не соответствует скидке на столе");

    }

    @Step("Проверка соответствует ли наценка на кассе полю 'Наценка' на столе")
    public void isMarkupVisible(double markupSumFromDesk) {

        markupSum.shouldBe(visible);

        double totalMarkup = convertSelectorTextIntoDoubleByRgx(markupSum, discountInCheckRegex);

        Assertions.assertEquals(markupSumFromDesk,totalMarkup,
                "Скидка с кассы не соответствует скидке на столе");

    }

    @Step("Проверяем что у оплаченных позиций с ранее выставленной скидкой должна быть цена также со скидкой")
    public void hasNoDiscountPriceOnPaidDishesIfDiscountWasAppliedAfterPayment() {

        if (divideCheckSliderActive.isDisplayed()) {

            allPaidDishes.asDynamicIterable().stream().forEach(
                    element -> isElementInvisible(element.$(dishPriceWithoutDiscountSelector)));

            allNonPaidAndNonDisabledDishes.asDynamicIterable().stream().forEach(element -> {

                isElementVisible(element.$(dishPriceWithoutDiscountSelector));
                isElementVisible(element.$(dishPriceTotalSelector));

            });

        } else {

            allPaidDishes.asDynamicIterable().stream().forEach(
                    element -> isElementInvisible(element.$(dishPriceWithDiscountSelector)));

        }


    }


    public void isDivideSliderDisabledWhenOneDish() {

        divideCheckSlider.shouldHave(attributeMatching("class",".*disabled.*"));

    }


    @Step("Проверяем что у не оплаченных блюд будет наценка если были оплаченные позиции и скидку удалили")
    public void isNonPaidDishesHasMarkupAfterRemovingDiscount(Map<Integer,Map<String,Double>> dishesBeforeAddingDiscount,
                                                              Map<Integer,Map<String,Double>> dishesAfterAddingDiscount) {

        for (int index = 0; index < dishesBeforeAddingDiscount.size(); index++) {

            double totalPriceBefore = dishesBeforeAddingDiscount.get(index).get("totalPrice") ;
            double totalPriceAfter = dishesAfterAddingDiscount.get(index).get("totalPrice");

            double discountPriceBefore = dishesBeforeAddingDiscount.get(index).get("discountPrice") ;
            double discountPriceAfter = dishesAfterAddingDiscount.get(index).get("discountPrice");

            if (totalPriceBefore != totalPriceAfter )
                Assertions.fail("После добавления еще одной скидки изменилась общая цена");

            if (discountPriceAfter < discountPriceBefore )
                Assertions.fail("Цена должна увеличиться потому что удалили скидку, которая распространялась на " +
                        "оплаченные позиции");

        }

    }


    @Step("Проверяем что у оплаченных позиций с ранее выставленной скидкой должна быть цена также со скидкой")
    public void hasDiscountPriceOnPaidDishesIfDiscountAppliedAfter() {

        allPaidDishes.asDynamicIterable().stream().forEach(element -> {

            isElementVisible(element.$(dishPriceWithoutDiscountSelector));
            isElementVisible(element.$(dishPriceTotalSelector));

        });

        allNonPaidAndNonDisabledDishes.asDynamicIterable().stream().forEach(element -> {

            isElementVisible(element.$(dishPriceWithoutDiscountSelector));
            isElementVisible(element.$(dishPriceTotalSelector));

        });

    }

    @Step("Проверяем что все варианты чаевых корректны")
    public void areTipsOptionsCorrect(double cleanTotalSum) {

        isActiveTipPercentCorrectWithTotalSumAndSC(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Оплачиваем по позициям до тех пор пока весь заказ не будет закрыт")
    public void payTillFullSuccessPayment(int amountDishes, String guid, String
            tableId, String cashDeskType) {

        while (!allNonPaidAndNonDisabledDishes.isEmpty()) {

            System.out.println(allNonPaidAndNonDisabledDishes.size() + " : " + amountDishes);

            if (allNonPaidAndNonDisabledDishes.size() != amountDishes) {

                chooseDishesWithRandomAmount(amountDishes);
                setCustomTips(String.valueOf(generateRandomNumber(150,250)));

                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, String> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg =
                        rootPage.getTapperDataForTgPaymentMsg(tableId, cashDeskType);

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.paymentCorrect("part");
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg =
                        rootPage.getPaymentTgMsgData(guid,"part",tapperDataForTgMsg.get("paySum"));
                rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

            } else {

                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, String> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg =
                        rootPage.getTapperDataForTgPaymentMsg(tableId, cashDeskType);

                System.out.println(activeTipsButton);
                System.out.println(totalTipsSumInMiddle);

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.fullPaymentCorrect();
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg =
                        rootPage.getPaymentTgMsgData(guid,"full",tapperDataForTgMsg.get("paySum"));
                rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

            }

            reviewPage.clickOnFinishButton();

        }

    }

    @Step("Сохранение в дату заказа со всеми типа модификаторов, чтобы потом сравнить в таппере")
    public HashMap<Integer, Map<String, Double>> saveOrderDataWithAllModi(Response rs) {

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName;
        Object sessionSizeFlag = rs.path("result.CommandResult.Order.Session");
        int sessionSize;
        int sessionIndexCounter = 0;

        if (sessionSizeFlag instanceof LinkedHashMap) {

            sessionSize = 1;

        } else {

            sessionSize = rs.jsonPath().getList("result.CommandResult.Order.Session").size();

        }

        for (; sessionIndexCounter < sessionSize; sessionIndexCounter++) {

            String session;

            if (sessionSize == 1) {
                session = "result.CommandResult.Order.Session";
            } else {
                session = "result.CommandResult.Order.Session" + "[" + sessionIndexCounter + "]";
            }

            String dishPath = session + ".Dish";

            Object dishSizeSizeFlag = rs.path(dishPath);


            int dishSize;
            int dishIndexCounter = 0;

            if (dishSizeSizeFlag instanceof LinkedHashMap) {

                dishSize = 1;

            } else if (dishSizeSizeFlag == null) {

                dishSize = 0;

            } else {

                dishSize = rs.jsonPath().getList(dishPath).size();
                dishPath += "[" + dishIndexCounter + "]";

            }

            for (; dishIndexCounter < dishSize; dishIndexCounter++) {


                Map<String, Double> temporaryMap = new HashMap<>();

                double dishPrice;

                int modificatorTypeSize = 0;
                String modiPath = dishPath + ".Modi";

                if (rs.path(modiPath) != null) {

                    if (rs.path(modiPath) instanceof LinkedHashMap) {

                        modificatorTypeSize = 1;

                    } else {

                        modificatorTypeSize = rs.jsonPath().getList(modiPath).size();


                    }

                }

                currentDishName = rs.jsonPath().getString(dishPath + "['@attributes'].name");

                double modificatorTotalPrice = 0;
                if (modificatorTypeSize == 1) {


                    String modificatorCurrentPriceFlag = rs.path(modiPath + "['@attributes'].price");

                    double modificatorCurrentPrice = 0;

                    if (modificatorCurrentPriceFlag != null) {

                        modificatorCurrentPrice = rs.jsonPath().getDouble(modiPath + "['@attributes'].price") / 100;

                    }

                    int modificatorCurrentCount = rs.jsonPath().getInt(modiPath + "['@attributes'].count");

                    modificatorTotalPrice = modificatorCurrentPrice * modificatorCurrentCount;

                    double currentDishPrice = rs.jsonPath().getDouble(dishPath + "['@attributes'].price") / 100;

                    dishPrice = currentDishPrice + modificatorTotalPrice;

                } else {

                    int currentModificatorTypeIndex = 0;
                    modiPath += "[" + currentModificatorTypeIndex + "]";

                    for (; currentModificatorTypeIndex < modificatorTypeSize; currentModificatorTypeIndex++) {

                        String modificatorCurrentPriceFlag = rs.path(modiPath + "['@attributes'].price");

                        double modificatorCurrentPrice = 0;

                        if (modificatorCurrentPriceFlag != null) {

                            modificatorCurrentPrice = rs.jsonPath().getDouble
                                    (modiPath + "['@attributes'].price") / 100;

                        }

                        int modificatorCurrentCount = rs.jsonPath().getInt(modiPath + "['@attributes'].count");

                        modificatorTotalPrice += modificatorCurrentPrice * modificatorCurrentCount;

                    }

                    double currentDishPrice = rs.jsonPath().getDouble(dishPath + "['@attributes'].price") / 100;

                    dishPrice = currentDishPrice + modificatorTotalPrice;

                }

                int dishQuantity = rs.jsonPath().getInt(dishPath + "['@attributes'].quantity") / 1000;

                for (int k = 0; k < dishQuantity; k++) {

                    temporaryMap.put(currentDishName, dishPrice);
                    allDishesInfo.put(totalDishIndex, temporaryMap);

                    totalDishIndex++;

                }

            }

        }

        return allDishesInfo;

    }

    public int getKeySize(Response response,String totalPath) {

        Object keySizeFlag = response.path(totalPath);

        if (keySizeFlag instanceof LinkedHashMap) {

            return 1;

        } else if (keySizeFlag == null) {

            return -1;

        } else {

            return response.jsonPath().getList(totalPath).size();

        }

    }

    public String getKeyPath(int keySize, int keyIndex,String keyName) {

        if (keySize != 1)
            keyName += "[" + keyIndex + "]";

        return keyName;

    }

    public LinkedHashMap<Integer,Map<String,Double>> getCashDeskData(String tableId) {

        Response rs = apiRKeeper.getOrderInfo(tableId, AUTO_API_URI);

        String totalPath = "";
        String session = "result.CommandResult.Order.Session";
        String dish = "Dish";
        String price = "[\"@attributes\"].amount";
        String name = "[\"@attributes\"].name";
        String quantity = "[\"@attributes\"].quantity";
        int totalDishIndex = 0;

        int sessionSize = getKeySize(rs,session);

        LinkedHashMap<Integer,Map<String,Double>> orderData = new LinkedHashMap<>();

        for (int sessionIndex = 0; sessionIndex < sessionSize; sessionIndex++) {

            String sessionPath = getKeyPath(sessionSize,sessionIndex,session);

            totalPath = sessionPath + "." + dish;

            int dishSize = getKeySize(rs,totalPath);

            for (int dishIndex = 0; dishIndex < dishSize; dishIndex++) {

                HashMap<String,Double> tempData = new HashMap<>();

                String dishPath = getKeyPath(dishSize,dishIndex,totalPath);

                double dishPrice = rs.jsonPath().getDouble(dishPath + price) / 100;
                String dishName = rs.jsonPath().getString(dishPath + name);
                double dishQuantity = rs.jsonPath().getDouble(dishPath + quantity) / 1000;

                if (dishQuantity % 1 == 0 && dishQuantity > 1) {

                    int dishQuantityIndex = 0;

                    for (; dishQuantityIndex < dishQuantity; dishQuantityIndex++) {

                        tempData.put(dishName,dishPrice / dishQuantity);
                        orderData.put(totalDishIndex + dishQuantityIndex,tempData);

                    }

                    totalDishIndex += dishQuantityIndex;

                } else {

                    tempData.put(dishName,dishPrice);
                    orderData.put(totalDishIndex,tempData);
                    totalDishIndex++;

                }

            }

            totalDishIndex++;

        }

        return orderData;

    }


    public HashMap<Integer,String> getDiscountUni(String tableId, String apiUri) {

        Response rs = apiRKeeper.getOrderInfo(tableId, apiUri);

        String totalPath;
        String session = "result.CommandResult.Order.Session";
        String dish = "Discount";
        String uni = "[\"@attributes\"].uni";

        int totalUniIndex = 0;
        int sessionSize = getKeySize(rs,session);
        HashMap<Integer,String> uniData = new HashMap<>();

        for (int sessionIndex = 0; sessionIndex < sessionSize; sessionIndex++) {

            String sessionPath = getKeyPath(sessionSize,sessionIndex,session);
            totalPath = sessionPath + "." + dish;

            int discountSize = getKeySize(rs,totalPath);

            for (int discountIndex = 0; discountIndex < discountSize; discountIndex++) {

                String dishPath = getKeyPath(discountSize,discountIndex,totalPath);
                String dishUni = rs.jsonPath().getString(dishPath + uni);

                uniData.put(totalUniIndex,dishUni);
                totalUniIndex++;

            }

        }


        return uniData;

    }






    @Step("Создание заказа, наполнение, открытие стола")
    public Response createAndFillOrderAndOpenTapperTable(String restaurantName, String tableCode, String waiter,
                                                         String apiUri, ArrayList<LinkedHashMap<String, Object>> dishes,
                                                         String tableUrl, String tableId) {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishes));

        rootPage.openNotEmptyTable(tableUrl);
        rootPage.isTableHasOrder();
        newIsOrderInKeeperCorrectWithTapper(tableId);

        return rs;

    }

    @Step("Создание заказа")
    public Response createOrder(String restaurantName, String tableCode, String waiter, String apiUri, String tableId) {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);

        return apiRKeeper.createOrder(rqCreateOrder,apiUri);

    }


    @Step("Создание заказа и наполнение")
    public Response createAndFillOrder(String restaurantName, String tableCode, String waiter, String apiUri,
                                       ArrayList<LinkedHashMap<String, Object>> dishes, String tableId) {


        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);


        apiRKeeper.fillingOrder(apiRKeeper.rqBodyFillingOrder(restaurantName, guid, dishes));

        return rs;

    }

    @Step("Создание заказа, наполнение заказа с модификаторами")
    public Response createAndFillOrderOnlyWithModifiers(String restaurantName, String tableCode, String waiter, String apiUri,
                                                        ArrayList<LinkedHashMap<String, Object>> modifiers, String tableId) {

        Assertions.assertTrue(apiRKeeper.isTableEmpty(restaurantName,tableId,apiUri),
                "На столе был прошлый заказ, его не удалось закрыть");

        LinkedHashMap<String, Object> rqCreateOrder =
                apiRKeeper.rqBodyCreateOrder(restaurantName, tableCode, waiter);
        Response rs = apiRKeeper.createOrder(rqCreateOrder,apiUri);

        String guid = apiRKeeper.getGuidFromCreateOrder(rs);

        apiRKeeper.addModificatorOrder(apiRKeeper.rqBodyAddModificatorOrder(restaurantName,guid, modifiers));

        return rs;

    }



    @Step("Проверка что скидка есть на столе")
    public void checkIsDiscountPresent(String tableId, String cashDeskType) {

        double discount = 0;

        if (cashDeskType.equals("keeper")) {

            discount = getDiscount(tableId);

        } else if (cashDeskType.equals("iiko")) {

            discount = getDiscountFromTable();

        }


        if (divideCheckSliderActive.isDisplayed()) {

            allPaidDishes.asFixedIterable().stream().forEach(
                    element -> isElementVisible(element.$(dishPriceWithoutDiscountSelector)));

        } else {

            allPaidDishes.asFixedIterable().stream().forEach(
                    element -> isElementInvisible(element.$(dishPriceWithDiscountSelector)));

        }

        isDiscountVisible(discount);

    }

    @Step("Проверка что скидка есть на столе")
    public void checkIsMarkupPresent(String tableId, String cashDeskType) {

        double markup = 0;

        if (cashDeskType.equals("keeper")) {

            markup = getDiscount(tableId);

        } else if (cashDeskType.equals("iiko")) {

            markup = convertSelectorTextIntoDoubleByRgx(markupSum,discountInCheckRegex);

        }


        if (divideCheckSliderActive.isDisplayed()) {

            allPaidDishes.asFixedIterable().stream().forEach(
                    element -> isElementVisible(element.$(dishPriceWithoutDiscountSelector)));

        } else {

            allPaidDishes.asFixedIterable().stream().forEach(
                    element -> isElementInvisible(element.$(dishPriceWithDiscountSelector)));

        }

        isMarkupVisible(markup);

    }


    public void callWaiterAndTextMessage() {

        rootPage.isElementVisible(appHeader);
        rootPage.openCallWaiterForm();
        rootPage.sendWaiterComment();
        rootPage.isSendSuccessful();

    }





}
