package pages.nestedTestsManager;

import api.ApiRKeeper;
import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.*;
import io.restassured.response.Response;
import pages.Best2PayPage;
import pages.ReviewPage;
import pages.RootPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static api.ApiData.QueryParams.rqParamsOrderGet;
import static api.ApiData.orderData.R_KEEPER_RESTAURANT;
import static api.ApiData.orderData.TABLE_3_ID;
import static constants.Selectors.RootPage.DishList.allNonPaidAndNonDisabledDishes;
import static constants.Selectors.RootPage.DishList.disabledDishes;



public class RootPageNestedTests extends RootPage {

    Best2PayPage best2PayPage = new Best2PayPage();
    ReviewPage reviewPage = new ReviewPage();
    Best2PayPageNestedTests best2PayPageNestedTests = new Best2PayPageNestedTests();
    ReviewPageNestedTests reviewPageNestedTests = new ReviewPageNestedTests();
    ApiRKeeper apiRKeeper = new ApiRKeeper();


    @Step("Проверка что меню на кассе одинаково с меню в таппере")
    public void isOrderInKeeperCorrectWithTapper() {

        forceWait(2500L); // toDO не успевают ранее скрипты загрузиться для executeJavascript

        HashMap<String,String> cookie = getCookieSessionAndGuest();

        Response rs =
                apiRKeeper.getOrder(rqParamsOrderGet(TABLE_3_ID, R_KEEPER_RESTAURANT,
                        cookie.get("guest"), cookie.get("session") ));

        HashMap<Integer, Map<String, Double>> orderInKeeper = getOrderInfoFromKeeperAndConvToHashMap(rs);

        isDishListNotEmptyAndVisible();
        matchTapperOrderWithOrderInKeeeper(orderInKeeper);

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

        clickOnPaymentButton();
        isPageLoaderShown();
        best2PayPage.isPaymentContainerAndVpnShown();
        best2PayPage.isTestBest2PayUrl();

    }


    @Step("Выбрать рандомные блюда и отдать эту же ссылку другому юзеру (не через кнопку 'Поделиться счётом'")
    public void chooseDishesAndShareLinkToAnotherUser(int amountDishes) {

        chooseCertainAmountDishes(amountDishes);
        clearAllSiteData();

    }

    public void areChosenDishesDisabledBySharingCheck(int amountDishes) {

        isDishListNotEmptyAndVisible();
        disabledDishes.shouldBe(CollectionCondition.size(amountDishes));

    }


    @Step("Выбираем рандомное число блюд ({amountDishes}) и считаем их сумму")
    public void chooseDishesWithRandomAmount(int amountDishes) {

        clickDivideCheckSlider();
        chooseCertainAmountDishes(amountDishes);

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, проводим все проверки с чаевыми и СБ")
    public void chooseDishesWithRandomAmountAndCheckAllSumsConditions(int amountDishes) {

        chooseDishesWithRandomAmount(amountDishes);
        checkChosenDishesSumsWithAllConditionsWhenDivided();

    }

    @Step("Выбираем рандомное число блюд ({amountDishes}), проверяем сумму, без чаевых, с сервисным сбором")
    public void chooseDishesWithRandomAmountNoTipsWithSC(int amountDishes) {

        chooseDishesWithRandomAmount(amountDishes);
        checkChosenDishesSumsNoTipsWithSC();

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
    public HashMap<String,String> getCookieSessionAndGuest() {

        String guest = getCookieGuest();
        String session = getCookieSession();

        HashMap<String,String> cookie = new HashMap<>();
        cookie.put("guest",guest);
        cookie.put("session",session);

        return cookie;

    }


    @Step("Очищаем все данные, рефрешим, передаём список ранее выбранных блюд, " +
            "проверяем что выбранные блюда в статусе ожидаются после разделения чека")
    public void clearDataAndCheckDisabledDishes(HashMap<Integer, Map<String,Double>> chosenDishes) {

        clearAllSiteData();

        isDishListNotEmptyAndVisible();
        isStylesCorrectToDisabledDishes();
        dishesAreDisabledInDishList(chosenDishes);

    }

    @Step("Проверям сумму не оплаченного заказа со всеми доп. условиями с разделенными позициями")
    public void checkChosenDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllChosenDishes();

        setTipsBy0AndCancelServiceCharge();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isActiveTipPercentCorrectWithTotalSum(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Проверям сумму всего не оплаченного заказа. С чаевыми, с СБ")
    public void checkAllDishesSumsWithAllConditions() { //

        double cleanTotalSum = countAllNonPaidDishesInOrder();

        setTipsBy0AndCancelServiceCharge();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isActiveTipPercentCorrectWithTotalSum(cleanTotalSum);
        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Проверям сумму выбранных позиций заказа. Без чаевых, с СБ")
    public void checkChosenDishesSumsNoTipsWithSC() { //

        double cleanTotalSum = countAllChosenDishes();

        cancelTipsAndActivateSC(cleanTotalSum);

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

    }

    @Step("Проверям сумму выбранных позиций заказа. Без чаевых и СБ")
    public void checkChosenDishesSumsNoTipsNoSC() { //

        double cleanTotalSum = countAllChosenDishes();

        cancelTipsAndDisableSC(cleanTotalSum);

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

    }



    @Step("Проверям сумму выбранных позиций заказа с разделенными позициями. Без чаевых, без СБ")
    public void checkChosenDishesSumsWithAllConditionsWhenDivided() { //

        double cleanTotalSum = countAllChosenDishes();

        setTipsBy0AndCancelServiceCharge();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isAllTipsOptionsAreCorrectWithTotalSumAndSC(cleanTotalSum);

    }

    @Step("Оплачиваем по {amountDishesPayFor1Time} позиции до тех пор пока весь заказ не будет закрыт")
    public void payTillFullSuccessPayment(int amountDishesPayFor1Time) {

        isDishListNotEmptyAndVisible();

        while(!allNonPaidAndNonDisabledDishes.isEmpty()) {

            System.out.println(allNonPaidAndNonDisabledDishes.size() + " кол-во не оплаченных блюд");

            if (allNonPaidAndNonDisabledDishes.size() != 1) {

                clearAllSiteData();
                chooseDishesWithRandomAmountAndCheckAllSumsConditions(amountDishesPayFor1Time);
                isAnotherGuestSumCorrect();

                double totalPay = saveTotalPayForMatchWithAcquiring();
                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                best2PayPage.clickPayButton();

                reviewPageNestedTests.partialPaymentCorrect();

                reviewPage.clickOnFinishButton();

            } else {

                double totalPay = saveTotalPayForMatchWithAcquiring();
                clickPayment();

                best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
                best2PayPage.clickPayButton();

                reviewPageNestedTests.fullPaymentCorrect();
                reviewPageNestedTests.reviewCorrectNegative();

            }

        }

    }

    @Step("Делимся счётом и оплачиваем позиции")
    public void divideCheckAndPayTheRestDishes() {

        clearAllSiteData();
        clickDivideCheckSlider();
        chooseAllNonPaidDishes();
        countAllNonPaidDishesInOrder();
        checkAllDishesSumsWithAllConditions();

        double totalPay = saveTotalPayForMatchWithAcquiring();
        clickPayment();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

    }


    @Step("Закрываем заказ полностью") // toDo костыльное решение по закрытию заказа пока пишется api
    public void closeOrder() {


        double totalPay = saveTotalPayForMatchWithAcquiring();
        clickPayment();

        best2PayPageNestedTests.checkPayMethodsAndTypeAllCreditCardData(totalPay);
        best2PayPage.clickPayButton();

        reviewPageNestedTests.fullPaymentCorrect();
        reviewPage.clickOnFinishButton();

    }



}
