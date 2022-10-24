package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class Selectors {

    public static class RootPage {

        public static final SelenideElement tableNumber = $(".vOrderHeader__table");
        public static final SelenideElement paymentButton = $("button.payment-btn.main-btn");
        public static final SelenideElement pagePreLoader = $(".vLightPreloader");
        public static final SelenideElement tabBar = $(".menu");
        public static final SelenideElement currentTips = $(".tips__sum input");
        public static final SelenideElement Tips0 = $x("//*[contains(@class,'tips__list-item')][text()=' 0% ']");
        public static final SelenideElement Tips10 = $x("//*[contains(@class,'tips__list-item')][text()=' 10% ']");
        public static final SelenideElement Tips15 = $x("//*[contains(@class,'tips__list-item')][text()=' 15% ']");
        public static final SelenideElement Tips20 = $x("//*[contains(@class,'tips__list-item')][text()=' 20% ']");
        public static final SelenideElement Tips25 = $x("//*[contains(@class,'tips__list-item')][text()=' 25% ']");
        public static final SelenideElement activeTipsButton = $x("//*[@class='tips__list-item active']");
        public static final SelenideElement tipsSum = $(".tips__sum input");
        public static final SelenideElement totalPay = $(".check__totalPay-heading+span");
        public static final SelenideElement serviceCharge = $(".payment__conditions:first-of-type");
        public static final SelenideElement confPolicy = $(".payment__conditions:last-of-type");
        public static final SelenideElement paymentStatusAfterPay = $(".review__status");
        public static final SelenideElement paymentStatusBeforeNewOrder = $(".vEmptyOrderModal__heading");
        public static final SelenideElement totalSumInWalletCounter = $(".priceCard>span");
        public static final ElementsCollection sumsOfAllPositionsInMenu = $$(".dishList__item .sum");
        public static final ElementsCollection dishListsItemsWithSharing = $$(".dishList__item.active");
        public static final SelenideElement dishListContainer = $(".dishList");
        public static final SelenideElement tabBarMenuIcon = $(".div .menuOpen");
        public static final SelenideElement tabBarWalletIcon = $(".div .creditCardOpen");
        public static final SelenideElement emptyOrderHeading = $(".vEmptyOrderModal__heading");
        public static final SelenideElement divideCheckSlider = $(".divide__check span.slider");
        public static final SelenideElement callWaiterButton = $(".callWaiter");
        public static final SelenideElement callWaiterContainer = $(".callWaiter-modal");
        public static final SelenideElement callWaiterButtonSend = $(".callWaiter-btn.send");
        public static final SelenideElement callWaiterButtonCancel = $(".callWaiter-btn.cancel");
        public static final SelenideElement callWaiterCommentArea = $(".callWaiter__textarea");
        public static final SelenideElement callWaiterOverlay = $(".modal__overlay");
        public static final SelenideElement menuDishesContainer = $(".menuDishes");


        public static final SelenideElement shareButton = $(".share-btn main-btn");














    }

    public static class Best2PayPage {



        public static final SelenideElement paymentContainer = $(".div.container");
        public static final SelenideElement cardNumber = $(".input.input--pan input");
        public static final SelenideElement dateExpire = $(".input.input--date .input__field--date");
        public static final SelenideElement cvv = $(".input--cvv .input__field--cvv");
        public static final SelenideElement email = $("#email");
        public static final SelenideElement payButton = $("#submitButton");
    }


}
