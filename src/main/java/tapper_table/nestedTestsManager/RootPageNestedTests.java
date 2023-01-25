package tapper_table.nestedTestsManager;

import api.ApiRKeeper;
import constants.selectors.TapperTableSelectors;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import tapper_table.Best2PayPage;
import tapper_table.ReviewPage;
import tapper_table.RootPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static constants.Constant.TestData.API_STAGE_URI;
import static constants.selectors.TapperTableSelectors.Best2PayPage.transaction_id;
import static constants.selectors.TapperTableSelectors.RootPage.DishList.*;
import static constants.selectors.TapperTableSelectors.RootPage.TapBar.*;
import static constants.selectors.TapperTableSelectors.RootPage.TipsAndCheck.*;


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

    @Step("Проверка пустого стола и всех элементов")
    public void isEmptyTableCorrect() {

        isHintModalCorrect();
        closeHintModal();
        isElementVisible(appHeader);
        isElementVisible(tableNumber);
        emptyOrderHeading.shouldHave(matchText("Ваш заказ появится здесь"));
        isRefreshButtonCorrect();
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

    @Step("Блок с чаевыми отображается")
    public void isTipsAndCheckCorrect() {
        isTipsContainerCorrect();
    }

    @Step("Блок 'Итого к оплате', кнопки оплатить и поделиться счётом, чекбоксы сервисного сбора и политики - всё отображается")
    public void isPayBlockCorrect() {

        isCheckContainerShown();
        isPaymentButtonShown();
        isShareButtonShown();
        isServiceChargeShown();
        isConfPolicyShown();

    }

    @Step("Нижнее навигационное меню отображается корректно. Меню, вызов официанта, переключение - корректно")
    public void isTabBarCorrect() {

        isTapBarShown();
        isCallWaiterCorrect();

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

        if (divideCheckSliderActive.isDisplayed()) {

            markedDishesField.shouldNotBe(visible.because("У не верифицированного официанта и без карты не должно быть чаевых"));

        }

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

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ")
    public void checkAllDishesSumsWithAllConditions(double discountSum) { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();

        if (TapperTableSelectors.RootPage.TipsAndCheck.discountSum.exists()) {

            System.out.println("Обнаружена скидка");
            double discount = convertSelectorTextIntoDoubleByRgx(TapperTableSelectors.RootPage.TipsAndCheck.discountSum, "[^\\d\\.]+");

            Assertions.assertEquals(discountSum,discount,"Скидка с кассы не соответствует скидке на столе");
            System.out.println("Скидка на кассе соответствует скидке на столе");

            checkIsDiscountMatchWithSums(discount);
            cleanTotalSum -= discount;

        } else {

            System.out.println("Должна быть скидка но её нет");
            Assertions.fail("Должно быть поле со скидкой но его нет");

        }

        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        areTipsOptionsCorrect(cleanTotalSum);

        cancelTipsAndActivateSC(cleanTotalSum);

    }

    @Step("Проверка что итого к оплате считается корректно со скидкой, поле со скидкой корректно")
    public void checkIsDiscountMatchWithSums(double discount) {

        double cleanTotalSum = countAllNonPaidDishesInOrder();

        isElementVisible(discountField);

        double discountInCheck = convertSelectorTextIntoDoubleByRgx(discountSum, "[^\\d\\.]+");
        double totalPayInCheck = convertSelectorTextIntoDoubleByRgx(totalPay, "\\s₽");
        double serviceCharge = getCurrentSCSum();
        double tips = getCurrentTipsSum();

        cleanTotalSum += serviceCharge + tips - discount;

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
    public void payTillFullSuccessPayment(int amountDishes,String guid) {

        isDishListNotEmptyAndVisible();

        while (!allNonPaidAndNonDisabledDishes.isEmpty()) {

            System.out.println(allNonPaidAndNonDisabledDishes.size() + " кол-во не оплаченных блюд");

            if (allNonPaidAndNonDisabledDishes.size() != amountDishes) {

                clearAllSiteData();
                closeHintModal();
                chooseDishesWithRandomAmount(amountDishes);
                isAnotherGuestSumCorrect();
                setCustomTips(String.valueOf(generateRandomNumber(150,250)));

                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.paymentCorrect("part");
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg = rootPage.getTgMsgData(guid,10000);
                rootPage.matchTgMsgDataAndTapperData(telegramDataForTgMsg,tapperDataForTgMsg);

            } else {

                System.out.println("Последняя оплата");
                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();
                LinkedHashMap<String, String> tapperDataForTgMsg = rootPage.getTapperDataForTgPaymentMsg();

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.fullPaymentCorrect();
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

                LinkedHashMap<String, String> telegramDataForTgMsg = rootPage.getTgMsgData(guid,15000);
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

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();

        isElementVisible(emptyOrderHeading);

    }

    @Step("Сохранение в дату заказа со всеми типа модификаторов, чтобы потом сравнить в таппере")
    public HashMap<Integer, Map<String, Double>> saveOrderDataWithAllModi(Response rs) {

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName;

        int sessionDishSize = rs.jsonPath().getList("Session.Dish").size();
        System.out.println(sessionDishSize + " количество типов блюд\n");

        for (int currentDishIndex = 0; currentDishIndex < sessionDishSize; currentDishIndex++) {

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

            System.out.println(currentDishName + " имя текущего блюда");
            System.out.println(modificatorTypeSize + " количество типов модификатора у текущего типа блюд\n");

            double modificatorTotalPrice = 0;
            if (modificatorTypeSize == 1) {

                String modificatorName =
                        rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                "].Modi['@attributes'].name");
                System.out.println(modificatorName + " имя модификатора");

                String modificatorCurrentPriceFlag = rs.path(
                        "Session.Dish[" + currentDishIndex + "].Modi['@attributes'].price");

                double modificatorCurrentPrice = 0;

                if (modificatorCurrentPriceFlag != null) {

                    modificatorCurrentPrice = rs.jsonPath().getDouble
                            ("Session.Dish[" + currentDishIndex +
                                    "].Modi['@attributes'].price") / 100;

                }

                System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                int modificatorCurrentCount = rs.jsonPath().getInt
                        ("Session.Dish[" + currentDishIndex + "].Modi['@attributes'].count");
                System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                modificatorTotalPrice = modificatorCurrentPrice * modificatorCurrentCount;

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice;

            } else {

                for (int currentModificatorTypeIndex = 0; currentModificatorTypeIndex < modificatorTypeSize; currentModificatorTypeIndex++) {

                    dishPrice = 0;

                    String modificatorName =
                            rs.jsonPath().getString("Session.Dish[" + currentDishIndex +
                                    "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].name");
                    System.out.println(modificatorName + " имя модификатора");

                    String modificatorCurrentPriceFlag = rs.path(
                            "Session.Dish[" + currentDishIndex + "].Modi["
                                    + currentModificatorTypeIndex + "]['@attributes'].price");

                    double modificatorCurrentPrice = 0;

                    if (modificatorCurrentPriceFlag != null) {

                        modificatorCurrentPrice = rs.jsonPath().getDouble
                                ("Session.Dish[" + currentDishIndex +
                                        "].Modi[" + currentModificatorTypeIndex + "]['@attributes'].price") / 100;

                    }

                    System.out.println(modificatorCurrentPrice + " цена текущего модификатора");

                    int modificatorCurrentCount = rs.jsonPath().getInt
                            ("Session.Dish[" + currentDishIndex + "].Modi["
                                    + currentModificatorTypeIndex + "]['@attributes'].count");
                    System.out.println(modificatorCurrentCount + " текущее количество модификаторов");

                    modificatorTotalPrice += modificatorCurrentPrice * modificatorCurrentCount;
                    System.out.println(modificatorTotalPrice + " цена за количество и типы модификатора");

                }

                double currentDishPrice = rs.jsonPath().getDouble
                        ("Session.Dish[" + currentDishIndex
                                + "]['@attributes'].price") / 100;
                System.out.println(currentDishPrice + " цена за само блюдо");

                dishPrice = currentDishPrice + modificatorTotalPrice;

            }
            System.out.println(dishPrice + " общая цена за блюдо + сумма за его модики");

            int dishQuantity = rs.jsonPath().getInt("Session.Dish[" + currentDishIndex + "]['@attributes'].quantity") / 1000;

            for (int k = 0; k < dishQuantity; k++) {

                System.out.println("\nДобавлено в список под индексом " + totalDishIndex + "\n");
                temporaryMap.put(currentDishName, dishPrice);
                allDishesInfo.put(totalDishIndex, temporaryMap);

                totalDishIndex++;

            }

        }

        System.out.println("Итоговый список\n" + allDishesInfo);
        return allDishesInfo;

    }

    @Step("Забираем скидку из кассы")
    public double getTotalDiscount(String table_id) {

        Response rs = apiRKeeper.getOrderInfo(table_id, API_STAGE_URI);
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

}
