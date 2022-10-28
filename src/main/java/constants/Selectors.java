package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;

public class Selectors {

    public static class RootPage {

        public static final String bodyJS = "body";
        public static final SelenideElement startScreenLogo = $x("//div[./div[@class=\"startScreen\"]]");
        public static final SelenideElement tableNumber = $(".vOrderHeader__table");
        public static final SelenideElement paymentButton = $("button.payment-btn.main-btn");
        public static final SelenideElement paymentProcessContainer = $(".payment-process");
        public static final SelenideElement pagePreLoader = $(".vLightPreloader");
        public static final SelenideElement tabBar = $(".menu");

        public static final SelenideElement tipsWaiter = $(".tips__waiter");
        public static final ElementsCollection tipsPercentsOptionsWithoutDisabled = $$x("//*[contains(@class,\"tips__list-item\")][not(@class='tips__list-item disabled')]");
        public static final SelenideElement checkTipsSum = $(".check__tips-sum");
        public static final SelenideElement resetTipsButton = $(".tips__sum-btn[type=\"reset\"]");

        public static final SelenideElement tipsContainer = $(".tips");
        public static final SelenideElement currentTips = $(".tips__sum input");
        public static final SelenideElement totalTipsSum = $(".custom-input input");

        public static final SelenideElement tipsWaiterImage = $(".tips__waiter img");
        public static final SelenideElement Tips0 = $x("//*[contains(@class,'tips__list-item')][text()=' 0% ']");
        public static final SelenideElement Tips10 = $x("//*[contains(@class,'tips__list-item')][text()=' 10% ']");
        public static final SelenideElement Tips15 = $x("//*[contains(@class,'tips__list-item')][text()=' 15% ']");
        public static final SelenideElement Tips20 = $x("//*[contains(@class,'tips__list-item')][text()=' 20% ']");
        public static final SelenideElement Tips25 = $x("//*[contains(@class,'tips__list-item')][text()=' 25% ']");
        public static final SelenideElement activeTipsButton = $x("//*[@class='tips__list-item active']");
        public static final SelenideElement tipsSum = $(".tips__sum input");
        public static final SelenideElement header = $(".vOrderHeader");
        public static final SelenideElement totalPay = $(".check__totalPay-heading+span");
        public static final SelenideElement serviceChargeInput = $(".payment__conditions:first-of-type input");
        public static final SelenideElement serviceCharge = $(".payment__conditions:first-of-type");
        public static final String serviceChargeJS = ".payment__conditions:first-of-type";
        public static final SelenideElement confPolicy = $(".payment__conditions:last-of-type");
        public static final SelenideElement confPolicyInput = $(".payment__conditions:last-of-type input");
        public static final SelenideElement totalSumInWalletCounter = $(".priceCard>span");
        public static final ElementsCollection allDishesInMenu = $$("li .dishList__item");
        public static final ElementsCollection dishListsItemsWithSharing = $$(".dishList__item.active");
        public static final SelenideElement dishListContainer = $(".dishList");
        public static final SelenideElement tabBarMenuIcon = $("div .menuOpen");
        public static final SelenideElement tabBarWalletIcon = $("div .creditCardOpen");
        public static final SelenideElement emptyOrderHeading = $(".vEmptyOrderModal__heading");
        public static final SelenideElement divideCheckSlider = $(".divide__check span.slider");
        public static final SelenideElement callWaiterButton = $(".callWaiter");
        public static final SelenideElement callWaiterContainer = $(".callWaiter-modal");
        public static final SelenideElement callWaiterButtonSend = $(".callWaiter-btn.send");
        public static final SelenideElement callWaiterButtonCancel = $(".callWaiter-btn.cancel");
        public static final SelenideElement callWaiterCommentArea = $(".callWaiter__textarea");
        public static final SelenideElement callWaiterOverlay = $(".modal__overlay");
        public static final SelenideElement menuDishesContainer = $(".menuDishes");
        public static final SelenideElement shareButton = $(".share-btn.main-btn");


    }

    public static class Best2PayPage {


        public static final SelenideElement paymentContainer = $("div.container");
        public static final SelenideElement vpnPopup = $(".pin");

        public static final SelenideElement cardNumber = $(".input.input--pan input");
        public static final SelenideElement dateExpire = $(".input.input--date .input__field--date");
        public static final SelenideElement cvv = $(".input--cvv .input__field--cvv");
        public static final SelenideElement email = $("#email");
        public static final SelenideElement payButton = $("#submitButton");
        public static final SelenideElement sendCheckByEmail = $("#cardEmail+.checkbox__body");
        public static final SelenideElement payMethodCard = $("#paymentTypeCard");
        public static final SelenideElement payMethodYandexPay = $(".yandexpay.payment-method__button");
        public static final SelenideElement payMethodSBP = $("#sbpButton");

    }

    public static class ReviewPage {

        public static final SelenideElement paymentStatusAfterPay = $(".review__status");
        public static final SelenideElement reviewContainer = $(".review .container");
        public static final SelenideElement finishReviewButton = $("button.main-btn");
        public static final SelenideElement reviewTextArea = $(".review__textarea");
        public static final SelenideElement reviewStars = $(".review__stars");
        public static final SelenideElement review5Stars = $(".review__stars .review__stars-item:nth-child(6)");
        public static final SelenideElement currentActiveStar = $(".review__stars .review__stars-item.active");
        public static final SelenideElement whatDoULikeList = $(".review__list");
        public static final SelenideElement whatDoULikeListRandomOption = $x("//li[@class=\"review__list-item\"]");
        public static final SelenideElement activeWhatDoULikeListRandomOption = $x("//li[@class=\"review__list-item active\"]");
        public static final SelenideElement paymentProcessStatus = $(".payment-process__status");

    }


}
