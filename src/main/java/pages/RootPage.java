package pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import constants.Selectors;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.junit.Assert;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.*;
import static constants.Selectors.RootPage.*;
import static constants.Selectors.RootPage.totalTipsSum;


public class RootPage extends BaseActions {

    BaseActions baseActions = new BaseActions();
    Best2PayPage best2PayPage = new Best2PayPage();


    public void openTapperLink() {

        baseActions.openPage(TEST_ROOT_URL);

    }

    @Step("Первичная заставка\\лого\\анимация при открытии страницы")
    @Description("Установлено и прогружается в соответствии с натройками в админке")
    public void isStartScreenShown() {

        baseActions.isElementVisibleDuringLongTime(startScreenLogo,30);

    }

    public void isWaitingForOrder() {

        baseActions.isElementVisible(emptyOrderHeading);

    }
    @Step("Кнопка поделиться счётом. Кнопка есть, но не активна, т.к. 1 позиция в заказе")
    @Description("Если в меню один заказ, то кнопка разделить счёт не кликабельна")
    public void isDivideCheckSliderWith1Dish() {

        baseActions.isElementVisible(divideCheckSlider);
        System.out.println(allDishesInMenu);
        System.out.println(allDishesInMenu.size());

        if (allDishesInMenu.size() == 1) {

            divideCheckSlider.click();
            dishListsItemsWithSharing.shouldHave(CollectionCondition.size(0));

        }


    }

    @Step("Кнопка поделиться счётом. В заказе более одной позиции")
    @Description("Не установлено никаких скидок в заказе и в заказе только одна позиция")
    public void isDivideCheckSliderWithMoreThan1Dishes() {
        baseActions.isElementInvisible(divideCheckSlider);
    }

    @Step("Кнопка поделиться счётом. Кнопка не присутствует из-за того что в заказе есть скидки")
    @Description("Не установлено никаких скидок в заказе")
    public void isDivideCheckSliderDisabled() {
        baseActions.isElementInvisible(divideCheckSlider);
    }




    @Step("Меню корректно отображается")
    public void isDishListVisible() {

        dishListContainer.shouldBe(visible, Duration.ofSeconds(30));
    }

    public void isTipsContainerShown() {

        baseActions.isElementVisible(tipsContainer);

    }

    public void isTipsContainerCorrect() {

        baseActions.isElementVisible(tipsSum);
        baseActions.isElementVisible(tipsWaiter);
        baseActions.isElementVisible(tipsWaiterImage);
    }


    public void checkIfAllTipsOptionsAreCorrectWithTotalSumWithServiceCharge() {


        activateServiceCharge();

        double totalSum = countAllPricesInMenu();

        for (SelenideElement tipsOption: tipsPercentsOptionsWithoutDisabled) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));

            double serviceChargeSum = baseActions.convertSelectorTextIntoDoubleByRgx(serviceCharge,"[^\\d\\.]+");
            serviceChargeSum = Math.floor(serviceChargeSum*100) /100.0;

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");

            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSum,"\\s₽");

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(totalTipsSum.getValue()));

            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter,"\\s₽");

            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;

                double cleanTips = totalSum * percentForCalculation;
                cleanTips = Math.round(cleanTips);

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(cleanTips + " чаевые посчитанные по общей сумме за блюда (чистые)");
                System.out.println(totalSum+cleanTips + " общая сумма за блюда c чаевым, но без сервисного сбора\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent + "\n");
                System.out.println(totalSum+cleanTips+serviceChargeSum + " общая сумма за блюда c чаевым и сервисным сбором\n");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом чаевых и сервисного сбора\n");



                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.00001);
                System.out.println("total tips sum in middle = clean tips");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.00001);
                System.out.println("tips sum in check = clean tips");
                Assert.assertEquals(totalSum+cleanTips+serviceChargeSum,totalPaySum, 0.01);
                System.out.println("total sum dishes = totalPaysum + clean tips + service charge");


                baseActions.click(resetTipsButton);

            } else {

                System.out.println(totalSum + " общая сумма за блюда без чаевых и без сервисного сбора");
                System.out.println(totalSum+serviceChargeSum + " общая сумма за блюда c сервисным сбором");
                System.out.println(totalPaySum + " общая сумма в графе 'Итого к оплате' c учетом скидки и чаевых\n");
                System.out.println(serviceChargeSum + " сервисный сбор по текущей проценту чаевых: " + percent);

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.00001);
                Assert.assertEquals(tipsSumInCheck,0, 0.00001);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.00001);
                Assert.assertEquals(totalSum+serviceChargeSum,totalPaySum, 0.00001);

            }

        }

    }



    public void checkIfAllTipsOptionsAreCorrectWithTotalSumWithoutServiceCharge() {


        cancelServiceCharge();

        double totalSum = countAllPricesInMenu();

        String result = "";

        for (SelenideElement tipsOption: tipsPercentsOptionsWithoutDisabled) {

            baseActions.click(tipsOption);
            tipsOption.shouldHave(attributeMatching("class","tips__list-item active"));

            int percent = baseActions.convertSelectorTextIntoIntByRgx(tipsOption,"\\D+");
            System.out.println("\n" + percent + " current active percent");

            double totalPaySum = baseActions.convertSelectorTextIntoDoubleByRgx(totalPay,"\\s₽");
            System.out.println(totalPaySum + " total pay sum in check");

            double tipsSumInCheck= baseActions.convertSelectorTextIntoDoubleByRgx(checkTipsSum,"\\s₽");
            System.out.println(tipsSumInCheck + " tips sum in check");

            int totalTipsSumInMiddle = Integer.parseInt(Objects.requireNonNull(totalTipsSum.getValue()));
            System.out.println(totalTipsSumInMiddle + " total tips sum in middle");

            double currentSumInWallet = baseActions.convertSelectorTextIntoDoubleByRgx(totalSumInWalletCounter,"\\s₽");
            System.out.println(currentSumInWallet + " sum in wallet\n");



            result += totalTipsSumInMiddle + " total tips sum in middle\n";




            if (percent != 0) {

                double percentForCalculation = (double) (percent) / 100;

                double cleanTips = totalSum * percentForCalculation;
                System.out.println(cleanTips + " clean tips");

                cleanTips = Math.round(cleanTips);
                System.out.println(cleanTips + " rounded floor");

                Assert.assertEquals(totalTipsSumInMiddle,cleanTips, 0.00001);
                System.out.println("total tips sum in middle = clean tips");
                Assert.assertEquals(tipsSumInCheck,cleanTips, 0.00001);
                System.out.println("tips sum in check = clean tips");
                Assert.assertEquals(totalSum+cleanTips,totalPaySum, 0.00001);
                System.out.println("total sum dishes = totalPaysum + clean tips");

                baseActions.click(resetTipsButton);

            } else {

                Assert.assertEquals(percent,0);
                Assert.assertEquals(totalTipsSumInMiddle, 0, 0.00001);
                Assert.assertEquals(tipsSumInCheck,0, 0.00001);
                Assert.assertEquals(totalPaySum,currentSumInWallet, 0.00001);

            }

        }

        Allure.addAttachment("тест",result); // toDO доделать вывод

    }

    public void cancelTipsAndServiceCharge() {

        if (tipsSum.exists()) {

            baseActions.click(Tips0);

        }

        if (serviceChargeInput.exists()) {

            baseActions.scrollByJS(bodyJS);
            baseActions.clickByJS(serviceChargeJS);

        }

    }

    @Step("Считаем сумму всех позиций")
    public double countAllPricesInMenu() {

        double totalSumInMenu = 0;
        int testCounter = 0;

        for (SelenideElement element : allDishesInMenu) {

            String price = element.$(".sum").getText().replaceAll("\\s₽","");
            String dishName = element.$(".dish__checkbox-text").getText();

            double cleanPrice = Double.parseDouble(price);

            totalSumInMenu += cleanPrice;
            testCounter++;

            System.out.println(dishName + " - " + cleanPrice + " --- цена текущей позиции");
            System.out.println(testCounter + ": Общая сумма " + totalSumInMenu);

        }

        return totalSumInMenu;

    }

    @Step("Проверям что сумма всех блюд совпадает с итоговой без чаевыех и СБ")
    public void checkIfTotalSumInDishesMatchWithTotalPay(double totalSumInMenu) {

        cancelTipsAndServiceCharge();

        String totalPayString = totalPay.getText().replaceAll("\\s₽","");
        double totalPay = Double.parseDouble(totalPayString);
        System.out.println(totalPay);
        System.out.println(totalSumInMenu);

        Assert.assertEquals(totalPay,totalSumInMenu, 0.00001);

    }

    @Step("Проверям что сумма 'Итого к оплате' совпадает с суммой счетчика в иконке кошелька")
    public void isPriceInWalletCorrectWithTotalPay( ) {

        String totalPaySum = totalPay.getText().replaceAll("\\s₽","");
        String currentSumInWallet = totalSumInWalletCounter.getText().replaceAll("\\s₽","");

        Assert.assertEquals(totalPaySum,currentSumInWallet);

    }

    @Step("Проверям что процент установленных по умолчанию чаевыех рассчитывается корректно без СБ")
    public void checkIfTipsPercentCorrectWithTotalSumWithoutServiceCharge(double totalSumWithoutTips) {

        if (tipsSum.exists()) {

            String tipPercentString = activeTipsButton.getText();
            tipPercentString = tipPercentString.replaceAll("\\D+","");
            double tipPercent = Double.parseDouble(tipPercentString);
            System.out.println(tipPercentString + " --- tips");

            if (serviceChargeInput.isSelected()) {

                cancelServiceCharge();

            }

            String totalPayString = totalPay.getText().replaceAll("\\s₽","");
            double totalPay = Double.parseDouble(totalPayString);

            double totalSumWithTips = totalSumWithoutTips + (totalSumWithoutTips * tipPercent);

            Assert.assertEquals(totalSumWithTips,totalPay, 0.00001);


        }

    }

    @Step("Проверям что процент установленных по умолчанию чаевыех рассчитывается корректно вместе с СБ")
    public void checkIfTipsPercentCorrectWithTotalSumWithServiceCharge(double totalSumWithoutTips) {

        if (tipsSum.exists()) {

            if (!serviceChargeInput.isSelected()) {

                baseActions.clickByJS(serviceChargeJS);

            }

            String tipPercentString = activeTipsButton.getText();
            tipPercentString = tipPercentString.replaceAll("\\D+","");
            double tipPercent = Double.parseDouble(tipPercentString);
            System.out.println(tipPercentString + " --- tips");

            cancelServiceCharge();

            String totalPayString = totalPay.getText().replaceAll("\\s₽","");
            double totalPay = Double.parseDouble(totalPayString);

            double totalSumWithTips = totalSumWithoutTips + (totalSumWithoutTips * tipPercent);

            Assert.assertEquals(totalSumWithTips,totalPay, 0.00001);


        }

    }

    public void clickOnPaymentButton() {

        baseActions.scrollByJS(bodyJS);
        baseActions.click(paymentButton);


    }

    public void isPageLoaderShown() {
        baseActions.isElementVisibleDuringLongTime(pagePreLoader,6);
    }

    public void serviceChargeIsDisabled() {

        baseActions.isElementInvisible(serviceCharge);
    }

    public void clickOnServiceCharge() {

        baseActions.scrollByJS(bodyJS);
        baseActions.moveMouseToElement(serviceChargeInput);
        baseActions.click(serviceChargeInput);

    }

    public void isEmptyOrder() {

        baseActions.isElementVisible(emptyOrderHeading);


    }

    public void isDivideSliderShown() {

        baseActions.isElementVisibleDuringLongTime(divideCheckSlider,15);
        baseActions.click(divideCheckSlider);
        baseActions.isElementsListVisible(dishListsItemsWithSharing);

        baseActions.click(divideCheckSlider);
        baseActions.isElementsListInVisible(dishListsItemsWithSharing);
    }

    public void isTapBarShown() {

        baseActions.scroll(tabBar);
        baseActions.isElementVisible(tabBar);
        baseActions.isElementVisibleAndClickable(tabBarMenuIcon);
        baseActions.isElementVisibleAndClickable(tabBarWalletIcon);

        baseActions.click(tabBarMenuIcon);
        baseActions.isElementVisible(menuDishesContainer);

        baseActions.isElementInvisible(dishListContainer);

        baseActions.click(tabBarWalletIcon);

        baseActions.isElementVisible(dishListContainer);
        baseActions.isElementInvisible(menuDishesContainer);

    }

    public void isCallWaiterCorrect() {

        baseActions.scroll(callWaiterButton);
        baseActions.isElementVisibleAndClickable(callWaiterButton);
        baseActions.click(callWaiterButton);

        baseActions.isElementVisible(callWaiterContainer);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
        baseActions.isElementVisible(callWaiterCommentArea);

        baseActions.sendHumanKeys(callWaiterCommentArea,TEST_WAITER_COMMENT);
        callWaiterCommentArea.shouldHave(value(TEST_WAITER_COMMENT));

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterButtonCancel);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    public void isPaymentButtonShown() {

        baseActions.scrollByJS("body");

        baseActions.isElementVisibleDuringLongTime(paymentButton,15);

    }

    public void isShareButtonShown() {

        baseActions.isElementVisibleAndClickable(shareButton);

    }

    public void isServiceChargeShown() {

        if (serviceChargeInput.exists()) {

            baseActions.isElementVisible(serviceCharge);

        }

    }

    public void activateServiceCharge() {

        baseActions.scrollTillBottom();

        if (serviceCharge.exists() && !serviceChargeInput.isSelected()) {
            System.out.println("wasnt activated, clicked");
            baseActions.click(serviceCharge);

        }
    }

    public void isConfPolicyShown() {

        baseActions.isElementVisible(confPolicy);

    } // toDO доделать

    public void cancelServiceCharge() {


        if (serviceCharge.exists() && serviceChargeInput.isSelected()) {
            System.out.println("clicked and canceled service charge");
            baseActions.clickByJS(serviceChargeJS);

        }
    }



}


