package pages.nestedTestsManager;

import api.ApiRKeeper;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static constants.Selectors.Best2PayPage.transaction_id;
import static constants.Selectors.RootPage.DishList.*;
import static constants.Selectors.RootPage.TipsAndCheck.discountField;


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
       // matchTapperOrderWithOrderInKeeper(orderInKeeper);

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

        scrollTillBottom();
        clickOnPaymentButton();
        isPageLoaderShown();
        dishesSumChangedHeading.shouldNotHave(Condition.visible, Duration.ofSeconds(2));
        best2PayPage.isTestBest2PayUrl();
        best2PayPage.isPaymentContainerAndVpnShown();

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmountWithTipsWithSCVerifiedNonCard(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditionsWithNoWaiterCard(cleanTotalSum);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmount(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditions(cleanTotalSum);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки без чаевых, без СБ")
    public void chooseDishesWithRandomAmountWithNoTipsNoSc(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditions(cleanTotalSum);

        deactivateTipsAndDeactivateSc();

    }

    @Step("Выбираем по позиционно все блюда, проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseAllDishesWithTipsWithSC() {

        clickDivideCheckSlider();
        chooseAllNonPaidDishes();

        double cleanTotalSum = countAllChosenDishesDivided();
        checkSumWithAllConditions(cleanTotalSum);

        setRandomTipsOption();

    }

    @Step("Выбираем все блюда, проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseAllDishesWithTipsWithSCNotDivided() {

        countAllDishesNotDivided();

        double cleanTotalSum = countAllDishesNotDivided();
        checkSumWithAllConditions(cleanTotalSum);

        setRandomTipsOption();

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
    public void checkSumWithAllConditionsWithNoVerifiedWaiter(double cleanDishesSum) {

        checkCleanSumMatchWithTotalPay(cleanDishesSum);
        checkIsNoTipsElementsIfNonVerifiedNonCard();
        checkScLogic(cleanDishesSum);

    }

    @Step("Очищаем все данные, рефрешим, передаём список ранее выбранных блюд, " +
            "проверяем что выбранные блюда в статусе ожидаются после разделения чека")
    public void clearDataAndCheckDisabledDishes(HashMap<Integer, Map<String, Double>> chosenDishes) {

        clearAllSiteData();

        isDishListNotEmptyAndVisible();
        isStylesCorrectToDisabledDishes();
        dishesAreDisabledInDishList(chosenDishes);

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
        activateServiceChargeIfDeactivated();

    }

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ")
    public void checkAllDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();
        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        areTipsOptionsCorrect(cleanTotalSum);

        cancelTipsAndActivateSC(cleanTotalSum);

    }

    @Step("Проверяем что все варианты чаевых корректны")
    public void areTipsOptionsCorrect(double cleanTotalSum) {

        isActiveTipPercentCorrectWithTotalSumAndSC(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Оплачиваем по {amountDishesPayFor1Time} позиции до тех пор пока весь заказ не будет закрыт")
    public void payTillFullSuccessPayment(int amountDishes) {

        isDishListNotEmptyAndVisible();

        while (!allNonPaidAndNonDisabledDishes.isEmpty()) {

            System.out.println(allNonPaidAndNonDisabledDishes.size() + " кол-во не оплаченных блюд");

            if (allNonPaidAndNonDisabledDishes.size() != amountDishes) {

                clearAllSiteData();
                chooseDishesWithRandomAmount(amountDishes);
                isAnotherGuestSumCorrect();

                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.partialPaymentCorrect();
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

            } else {

                System.out.println("Последняя оплата");
                double totalPay = saveTotalPayForMatchWithAcquiring();
                HashMap<String, Integer> paymentDataKeeper = rootPage.savePaymentDataTapperForB2b();

                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                String transactionId = transaction_id.getValue();
                best2PayPage.clickPayButton();

                reviewPageNestedTests.fullPaymentCorrect();
                reviewPageNestedTests.getTransactionAndMatchSums(transactionId, paymentDataKeeper);

            }

            reviewPage.clickOnFinishButton();
            forceWait(4000); // toDO есть предположение что предоплата не успевает доходить до кассы

        }

    }

    @Step("Закрываем заказ полностью") // toDo костыльное решение по закрытию заказа пока пишется api
    public void closeOrder() {

        if (divideCheckSliderInput.isSelected()) {

            divideCheckSlider.click();

        }

        clickPayment();

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isEmptyOrderAfterClosing();
        forceWait(2000);

    }

    @Step("Сохранение в дату заказа со всеми типа модификаторов, чтобы потом сравнить в таппере")
    public HashMap<Integer, Map<String, Double>> saveOrderDataWithAllModi(Response rs) {

        HashMap<Integer, Map<String, Double>> allDishesInfo = new HashMap<>();

        int totalDishIndex = 0;
        String currentDishName = null;

        int sessionDishSize = rs.jsonPath().getList("Session.Dish").size();
        System.out.println(sessionDishSize + " количество типов блюд\n");

        for (int currentDishIndex = 0; currentDishIndex <sessionDishSize; currentDishIndex++ ) {

            Map<String, Double> temporaryMap = new HashMap<>();

            double dishPrice = 0;
            int modificatorTypeSize = 0;

            if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") != null) {

                if (rs.path("Session.Dish["+ currentDishIndex +"].Modi") instanceof LinkedHashMap) {

                    modificatorTypeSize = 1;

                } else {

                    modificatorTypeSize = rs.jsonPath().getList("Session.Dish["+ currentDishIndex +"].Modi").size();

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

                dishPrice = currentDishPrice + modificatorTotalPrice ;

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

                dishPrice = currentDishPrice + modificatorTotalPrice ;

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

}
