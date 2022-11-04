package pages.nestedTestsManager;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.*;
import pages.Best2PayPage;
import pages.RootPage;

import static constants.Constant.TestData.STAGE_ROOT_URL;
import static constants.Selectors.RootPage.DishList.disabledSharedDishes;


public class RootPageNestedTests extends RootPage {

    Best2PayPage best2PayPage = new Best2PayPage();

    @Step("Загрузочный экран, заказ, блок чаевых и итого к оплате, кнопки, нижнее меню")
    public void checkAllElementsAreVisibleAndActive() {

        isStartScreenShown();
        isDishListCorrect();
        isTipsAndCheckCorrect();
        isPayBlockCorrect();
        isTabBarCorrect();

    }

    @Step("Номер стола, кнопка разделить счёт отображаются. Заказ есть и не пустой")
    public void isDishListCorrect() {

        isTableNumberShown();
        isDivideSliderShown();
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

        chooseCertainAmountDishesAndCountCosts(amountDishes);
        openNewWindow(STAGE_ROOT_URL);




    }

    public void areChosenDishesDisabledBySharingCheck(int amountDishes) {

        isDishListNotEmptyAndVisible();
        disabledSharedDishes.shouldBe(CollectionCondition.size(amountDishes));


    }



    @Step("Выбираем рандомное число блюд ({amountDishes}) и считаем их сумму")
    public double divide_chooseAndCountTotalSumDishesWithRndAmount(int amountDishes) {

        return chooseCertainAmountDishesAndCountCosts(amountDishes);

    }


    @Step("Проверям сумму со всеми доп. условиями с разделенными позициями")
    public void divide_checkDividedOrderSumsWithAllConditions() { //

        double cleanTotalSum = divide_countAllChosenDishes();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isActiveTipPercentCorrectWithTotalSumWithoutSC(cleanTotalSum);

        isActiveTipPercentCorrectWithTotalSumWithSC(cleanTotalSum);

        divide_isAllTipsOptionsAreCorrectWithTotalSumWithoutSC(cleanTotalSum);

        divide_isAllTipsOptionsAreCorrectWithTotalSumWithSC(cleanTotalSum);


    }


    @Step("Проверям сумму со всеми доп. условиями не разделяя чек")
    public void single_checkDividedOrderSumsWithAllConditions() { //

        double cleanTotalSum = single_countAllDishesInOrder();

        isTotalSumInDishesMatchWithTotalPay(cleanTotalSum);

        isSumInWalletMatchWithTotalPay();

        isActiveTipPercentCorrectWithTotalSumWithoutSC(cleanTotalSum);

        isActiveTipPercentCorrectWithTotalSumWithSC(cleanTotalSum);


        single_isAllTipsOptionsAreCorrectWithTotalSumWithoutSC(cleanTotalSum);

        single_isAllTipsOptionsAreCorrectWithTotalSumWithSC(cleanTotalSum);


    }



}
