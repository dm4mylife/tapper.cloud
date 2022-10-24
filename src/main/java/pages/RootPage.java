package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import org.junit.Assert;

import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static constants.Constant.TestData.*;
import static constants.Selectors.RootPage.*;


public class RootPage extends BaseActions {

    BaseActions baseActions = new BaseActions();
    Best2PayPage best2PayPage = new Best2PayPage();

    public void openTapperLink() {

        baseActions.openPage(TEST_ROOT_URL);

    }

    public void clickOnPaymentButton() {

        baseActions.scroll($("body"));
        Selenide.sleep(400);
        Selenide.executeJavaScript("window.scrollTo({top: -300, behavior: 'smooth'})");
        paymentButton.click();
        best2PayPage.isPaymentContainerShown();
        best2PayPage.isTestBest2PayUrl();

    }

    public void isDishListVisible() {

        dishListContainer.shouldBe(visible, Duration.ofSeconds(10));
    }

    public int countAllPricesInMenu() {

        int totalSumInMenu = 0;

        for (SelenideElement position : sumsOfAllPositionsInMenu) {

            String currentPositionPrice = position.getText().replaceAll("\\s₽","");

            int cleanPrice = Integer.parseInt(currentPositionPrice);

            totalSumInMenu += cleanPrice;


        }

        return totalSumInMenu;

    }

    public void isCounterInWalletCorrect(int totalSumInMenuNumber) {

        String totalSumInMenu = String.valueOf(totalSumInMenuNumber);
        String currentSumInWallet = totalSumInWalletCounter.getText().replaceAll("\\s₽","");

        Assert.assertEquals(totalSumInMenu,currentSumInWallet);

    }

    public void checkIfTipsPercentCorrectInTotalSum(int totalSumWithoutTips) {

        if (tipsSum.exists()) {

            String tipPercentString = activeTipsButton.getText();
            tipPercentString = tipPercentString.replaceAll("\\d+","");
            int tipPercent = Integer.parseInt(tipPercentString);

            int totalSumWithTips = totalSumWithoutTips + (totalSumWithoutTips * tipPercent);

            baseActions.click(serviceCharge);

            String totalPayString = totalPay.getText().replaceAll("\\s₽","");
            int totalPay = Integer.parseInt(totalPayString);

            Assert.assertEquals(totalSumWithTips,totalPay);

        }


    }

    public void clickOnServiceCharge() {

        baseActions.scroll(tabBar);
        Selenide.sleep(500);
        Selenide.executeJavaScript("window.scrollTo({bottom: 200, behavior: 'smooth'})");
        baseActions.moveMouseToElement(serviceCharge);
        baseActions.click(serviceCharge);

    }

    public void isEmptyOrder() {
        baseActions.isElementVisible(emptyOrderHeading);

    }

    public void isDivideSliderShown() {

        baseActions.isElementVisible(divideCheckSlider);
        baseActions.click(divideCheckSlider);
        baseActions.isElementsListVisible(dishListsItemsWithSharing);

    }

    public void isTapBarShown() {

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

        baseActions.isElementVisibleAndClickable(callWaiterButton);
        baseActions.click(callWaiterButton);

        baseActions.isElementVisible(callWaiterContainer);
        baseActions.isElementVisibleAndClickable(callWaiterButtonSend);
        baseActions.isElementVisibleAndClickable(callWaiterButtonCancel);
        baseActions.isElementVisible(callWaiterCommentArea);

        baseActions.sendHumanKeys(callWaiterCommentArea,TEST_WAITER_COMMENT);
        callWaiterCommentArea.shouldHave(value(TEST_WAITER_COMMENT));

        baseActions.click(callWaiterOverlay);
        baseActions.isElementInvisible(callWaiterContainer);

        baseActions.click(callWaiterButton);
        baseActions.click(callWaiterButtonCancel);
        baseActions.isElementInvisible(callWaiterContainer);

    }

    public void isPaymentButtonShown() {

        baseActions.scroll($("body"));
        Selenide.sleep(400);
        Selenide.executeJavaScript("window.scrollTo({top: -300, behavior: 'smooth'})");

        baseActions.isElementVisibleDuringLongTime(paymentButton,15);
        paymentButton.click();

        baseActions.isElementVisibleDuringLongTime(pagePreLoader,6);

    }

    public void isShareButtonShown() {

        baseActions.scroll($("body"));
        Selenide.sleep(400);
        Selenide.executeJavaScript("window.scrollTo({top: -300, behavior: 'smooth'})");

        baseActions.isElementVisibleAndClickable(shareButton);

    }

    public void isServiceChargeShown() {

        baseActions.isElementVisibleAndClickable(serviceCharge);

    } // toDO доделать

    public void isConfPolicyShown() {

        baseActions.isElementVisibleAndClickable(serviceCharge);

    } // toDO доделать
}


