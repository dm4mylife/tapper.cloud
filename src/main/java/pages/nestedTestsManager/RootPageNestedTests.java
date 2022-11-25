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
import java.util.Map;

import static constants.Selectors.Best2PayPage.transaction_id;
import static constants.Selectors.RootPage.DishList.*;
import static constants.Selectors.RootPage.TipsAndCheck.discountField;
import static constants.Selectors.RootPage.TipsAndCheck.resetTipsButton;


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

        //   Response rs = apiRKeeper.orderInfo();

        //   HashMap<Integer, Map<String, Double>> orderInKeeper = getOrderInfoFromKeeperAndConvToHashMap(rs);

        isDishListNotEmptyAndVisible();
        //  matchTapperOrderWithOrderInKeeper(orderInKeeper);

    }

    @Step("Загрузочный экран, заказ, блок чаевых и итого к оплате, кнопки, нижнее меню")
    public void checkAllElementsAreVisibleAndActive() {

        //   isStartScreenShown(); // toDO не работала загрузка гифки в самой админке, 500 ошибка при сохранении
        isDishListCorrect();
        isTipsAndCheckCorrect();
        isPayBlockCorrect();
        isTabBarCorrect();

    }

    @Step("Номер стола, кнопка разделить счёт отображаются. Заказ есть и не пустой")
    public void isDishListCorrect() {

        isTableNumberShown();
        isDivideSliderCorrect();
        isDishListNotEmptyAndVisible();

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

    @Step("Выбрать рандомные блюда и отдать эту же ссылку другому юзеру (не через кнопку 'Поделиться счётом'")
    public void chooseDishesAndShareLinkToAnotherUser(int amountDishes) {

        chooseCertainAmountDishes(amountDishes);
        clearAllSiteData();

    }

    @Step("Проверка что выбранные блюда заблокированы для выбора, если их уже выбрал другой пользователь")
    public void areChosenDishesDisabledBySharingCheck(int amountDishes) {

        isDishListNotEmptyAndVisible();
        disabledDishes.shouldBe(CollectionCondition.size(amountDishes));

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}) и считаем их сумму")
    public void chooseDishesWithRandomAmount(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}) и считаем их сумму вместо со скидкой на заказ")
    public void chooseDishesWithRandomAmountWithAmount(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmountWithTipsWithSC(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

        double cleanTotalSum = countAllChosenDishesDivided();
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


    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, без чаевых, с сервисным сбором")
    public void chooseDishesWithRandomAmountNoTipsWithSC(int amountDishes) {

        chooseDishesWithRandomAmount(amountDishes);
        checkChosenDishesSumsNoTipsWithSC();
        click(resetTipsButton);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, без чаевых, без сервисного сбора")
    public void chooseDishesWithRandomAmountNoTipsNoSC(int amountDishes) {

        chooseDishesWithRandomAmount(amountDishes);
        checkChosenDishesSumsNoTipsNoSC();

    }

    @Step("Выбираем все блюда, проверяем сумму, без чаевых но с СБ")
    public void chooseAllDishesToPayNoTipsWithSC() {

        chooseAllNonPaidDishes();
        checkChosenDishesSumsNoTipsWithSC();

    }

    @Step("Забираем из кук session и guest")
    public HashMap<String, String> getCookieSessionAndGuest() {

        String guest = getCookieGuest();
        String session = getCookieSession();

        HashMap<String, String> cookie = new HashMap<>();
        cookie.put("guest", guest);
        cookie.put("session", session);

        return cookie;

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

        disableTipsAndSC(cleanTotalSum);

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);
        isSumInWalletMatchWithTotalPay();

        activateRandomTipsAndSC();

    }

    @Step("Проверяем сумму со скидкой за заказ по всем позициям с 'Итого к оплате' и счётчиком в иконке кошелька без СБ и чаевых ")
    public void checkTotalDishDiscountSumWithTotalPayInCheckAndInWalletCounter(double cleanTotalSum, String id_table) {

        disableTipsAndSC(cleanTotalSum);

        Response rsGetOrder = apiRKeeper.getOrderInfo(id_table);
        double rsDiscount = rsGetOrder.jsonPath().getDouble("Session.Discount['@attributes'].amount");
        System.out.println(rsDiscount + " discount from rs");

        double tapperDiscount = rootPage.convertSelectorTextIntoDoubleByRgx(discountField, "[^\\.\\d]+");
        System.out.println(tapperDiscount + " tapper discount");

        double cleanTotalSumWithDiscount = cleanTotalSum + tapperDiscount;
        System.out.println(cleanTotalSum + " clean total sum without discount");


        Assertions.assertEquals(rsDiscount, tapperDiscount,
                "Скидка на кассе не совпадает со скидкой в таппере в поле 'Скидка'");

        Assertions.assertEquals(cleanTotalSum, cleanTotalSumWithDiscount,
                "Чистая сумма за позиции не совпадает с чистой суммой без скидки в 'Итого к оплате'");

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);
        isSumInWalletMatchWithTotalPay();

        activateRandomTipsAndSC();

    }

    @Step("Оплачиваем все блюда без разделения")
    public void payAllDishesNotDividedCheck() {

        hideTapBar();
        scrollTillBottom();
        rootPage.clickOnPaymentButton();

    }

    @Step("Отключаем чаевые и сервисный сбор")
    public void disableTipsAndSC(double cleanTotalSum) {

        disableServiceChargeIfActivated();
        setTipsToZero(cleanTotalSum);

    }

    @Step("Включаем рандомные чаевые и сервисный сбор")
    public void activateRandomTipsAndSC() {

        activateServiceChargeIfDisabled();
        setRandomTipsOption();

    }

    @Step("Проверяем сумму не оплаченного заказа со всеми доп. условиями с разделенными позициями")
    public void checkChosenDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllChosenDishesDivided();

        setTipsBy0AndCancelServiceCharge();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isActiveTipPercentCorrectWithTotalSumAndSC(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Проверяем сумму выбранных позиций заказа с разделенными позициями с чаевыми и СБ")
    public void checkChosenDishesSumsWithTipsWithSC() { //

        double cleanTotalSum = countAllChosenDishesDivided();
        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        areTipsOptionsCorrect(cleanTotalSum);

    }

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ")
    public void checkAllDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();
        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        areTipsOptionsCorrect(cleanTotalSum);

        cancelTipsAndActivateSC(cleanTotalSum);

    }

    @Step("Проверяем сумму всего не оплаченного заказа c чаевыми и с СБ и со скидкой на весь заказ")
    public void checkAllDishesWithDiscountSumsWithAllConditions(String id_table) { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();


        checkTotalDishDiscountSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum, id_table);

        areTipsOptionsCorrect(cleanTotalSum);

        cancelTipsAndActivateSC(cleanTotalSum);


    }

    @Step("Проверяем сумму выбранных позиций заказа без чаевых с СБ")
    public void checkChosenDishesSumsNoTipsWithSC() { //

        double cleanTotalSum = countAllChosenDishesDivided();
        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

    }

    @Step("Проверяем сумму выбранных позиций заказа без чаевых и без СБ")
    public void checkChosenDishesSumsNoTipsNoSC() { //

        double cleanTotalSum = countAllChosenDishesDivided();
        checkTotalDishSumWithTotalPayInCheckAndInWalletCounter(cleanTotalSum);

        cancelTipsAndDisableSC(cleanTotalSum);

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
                chooseDishesWithRandomAmountWithTipsWithSC(amountDishes);
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

    @Step("Делимся счётом и оплачиваем позиции")
    public void divideCheckAndPayTheRestDishes() {

        clearAllSiteData();
        clickDivideCheckSlider();
        chooseAllNonPaidDishes();
        countAllNonPaidDishesInOrderDivided();
        checkAllDishesSumsWithAllConditions();

        double totalPay = saveTotalPayForMatchWithAcquiring();
        clickPayment();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

    }

    @Step("Закрываем заказ полностью") // toDo костыльное решение по закрытию заказа пока пишется api
    public void closeOrder() {

        clickPayment();

        best2PayPageNestedTests.typeDataAndPay();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();
        rootPage.isEmptyOrder();
        forceWait(2000);

    }

}
