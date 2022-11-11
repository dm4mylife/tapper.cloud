package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.*;

public class Selectors {

    public static class Common {

        public static final String bodyJS = "body";
        public static final SelenideElement header = $(".vOrderHeader");
        public static final SelenideElement startScreenLogo = $x("//div[./div[@class='startScreen']]");
        public static final SelenideElement pagePreLoader = $(".vLightPreloader");

    }

    public static class RootPage {

        public static class DishList {

            public static final SelenideElement tableNumber = $(".vOrderHeader__table");
            public static final SelenideElement emptyOrderHeading = $(".vEmptyOrderModal__heading");
            public static final SelenideElement separateOrderHeading = $(".vOrderHeader__separate");
            public static final SelenideElement dishesSumChangedHeading = $(".vAuthentication.active");



            public static final SelenideElement divideCheckSlider = $(".divide__check span.slider");

            public static final SelenideElement menuDishesContainer = $(".menuDishes");
            public static final SelenideElement dishListContainerWithDishes = $(".dishList ul.dishList__list");



            public static final ElementsCollection allDishesInOrder = $$("li [class='dishList__item']");
            public static final ElementsCollection paidDishes = $$(".dishList__item.paid");
            public static final ElementsCollection disabledDishes = $$("[class='dishList__item disabled']");
            public static final ElementsCollection allNonPaidAndNonDisabledDishes = $$(".dishList [class=\"dishList__item\"]");



            public static final ElementsCollection allDishesWhenDivided = $$(".dishList__item.active");
            public static final ElementsCollection paidDishesWhenDivided = $$(".dishList__item.active.paid");

            public static final ElementsCollection nonPaidAndNonDisabledDishesWhenDivided =
                    $$x("//*[@class=\"dishList__item active\"][not(@class=\"paid\") and not(@class=\"disabled\")]");
            public static final ElementsCollection nonPaidAndNonDisabledDishesSumWhenDivided =
                    $$x("//*[@class=\"dishList__item active\"][not(@class=\"paid\") and not(@class=\"disabled\")]//*[@class='sum']");
            public static final ElementsCollection nonPaidAndNonDisabledDishesNameWhenDivided =
                    $$x("//*[@class=\"dishList__item active\"][not(@class=\"paid\") and not(@class=\"disabled\")]//*[@class='dish__checkbox-content']");
            public static final ElementsCollection nonPaidAndNonDisabledDishesInputWhenDivided =
                    $$x("//*[@class=\"dishList__item active\"][not(@class=\"paid\") and not(@class=\"disabled\")]//input");

            public static final ElementsCollection disabledDishesWhenDivided =
                    $$("[class='dishList__item active disabled']");

            public static final ElementsCollection disabledAndPainDishesWhenDivided =
                    $$x("//*[@class=\"dishList__list\"]//*[contains(@class,'disabled') or contains(@class,'paid')]");


            public static final ElementsCollection nonPaidDishesWhenDivided =
                    $$(".dishList__list li [class='dishList__item active']");

            public static final ElementsCollection orderPositionsSpinners = $$(".dishList .dishList__list .dishList__item-status img");

        }

        public static class TipsAndCheck {

            public static final SelenideElement tipsWaiter = $(".tips__waiter");
            public static final SelenideElement checkTipsSumWithDivide = $(".check__tips .check__tips-sum");
            public static final ElementsCollection tipsPercentList= $$(".tips__percents-list li");
            public static final SelenideElement tipsInCheck= $(".check .check__tips-heading");
            public static final SelenideElement anotherGuestSum =
                    $x("//*[@class=\"check__tips-heading\"][text()=\"Другой пользователь:\"]/following-sibling::span");
            public static final SelenideElement markedDishes =
                    $x("//*[@class=\"check__tips-heading\"][text()=\"Отмеченные позиции:\"]/following-sibling::span");


            public static final SelenideElement resetTipsButton = $(".tips__sum-btn[type='reset']");
            public static final SelenideElement tipsContainer = $(".tips");
            public static final SelenideElement currentTips = $(".tips__sum input");
            public static final SelenideElement totalTipsSumInMiddle = $(".custom-input input");
            public static final SelenideElement tipsWaiterImage = $(".tips__waiter img");
            public static final SelenideElement activeTipsButton = $x("//*[@class='tips__list-item active']");
            public static final SelenideElement tipsSum = $(".tips__sum input");
            public static final SelenideElement totalPay = $(".check__totalPay-heading+span");

            public static final SelenideElement chosenDishesHeadingInCheck = $x("//*[@class=\"check__marked\"]/*[text()=\"Отмеченные позиции:\"]");

            public static final SelenideElement tipsErrorMsg = $(".tips__error");

            public static final SelenideElement Tips0 =
                $x("//*[contains(@class,'tips__list-item')][text()=' 0% ']");
            public static final SelenideElement Tips10 =
                $x("//*[contains(@class,'tips__list-item')][text()=' 10% ']");
            public static final SelenideElement Tips15 =
                $x("//*[contains(@class,'tips__list-item')][text()=' 15% ']");
            public static final SelenideElement Tips20 =
                $x("//*[contains(@class,'tips__list-item')][text()=' 20% ']");
            public static final SelenideElement Tips25 =
                $x("//*[contains(@class,'tips__list-item')][text()=' 25% ']");
            public static final ElementsCollection notDisabledTipsPercentOptions =
                $$x("//*[contains(@class,'tips__list-item')][not(@class='tips__list-item disabled')]");
            public static final ElementsCollection notActiveTipsPercentOptions =
                    $$("[class='tips__list-item']");
        }

        public static class PayBlock {


            public static final SelenideElement checkContainer = $(".check");
            public static final SelenideElement paymentButton = $("button.payment-btn.main-btn");
            public static final SelenideElement serviceChargeInput = $(".payment__conditions:first-of-type input");
            public static final SelenideElement serviceCharge = $(".payment__conditions:first-of-type");
            public static final String serviceChargeJS = ".payment__conditions:first-of-type";
            public static final SelenideElement confPolicyLink = $(".payment__conditions:last-of-type a");
            public static final SelenideElement confPolicyInput = $(".payment__conditions:last-of-type input");
            public static final SelenideElement confPolicyContainer = $(".vLandingPoliticModal");
            public static final SelenideElement confPolicyContent = $(".vLandingPoliticModal__content");


            public static final SelenideElement shareButton = $(".share-btn.main-btn");

        }

        public static class TapBar {

            public static final SelenideElement tabBar = $(".menu");
            public static final SelenideElement tabBarMenuIcon = $("div .menuOpen");
            public static final SelenideElement tabBarWalletIcon = $("div .creditCardOpen");
            public static final SelenideElement callWaiterButton = $(".callWaiter");
            public static final SelenideElement callWaiterContainer = $(".callWaiter-modal");
            public static final SelenideElement callWaiterButtonSend = $(".callWaiter-btn.send");
            public static final SelenideElement callWaiterButtonCancel = $(".callWaiter-btn.cancel");
            public static final SelenideElement callWaiterCommentArea = $(".callWaiter__textarea");
            public static final SelenideElement callWaiterOverlay = $(".modal__overlay");
            public static final SelenideElement totalSumInWalletCounter = $(".priceCard>span");
            public static final SelenideElement successCallWaiterHeading = $(".callWaiter-modal .successfulSending");
            public static final SelenideElement closeCallWaiterFormInSuccess = $(".callWaiter-modal .successfulSendin__btn");



        }

    }

    public static class Best2PayPage {

        public static final SelenideElement paymentContainer = $("div.container");
        public static final SelenideElement vpnPopup = $(".pin");
        public static final SelenideElement paymentProcessContainer = $(".payment-process");
        public static final SelenideElement cardNumber = $(".input.input--pan input");
        public static final SelenideElement dateExpire = $(".input.input--date .input__field--date");
        public static final SelenideElement cvv = $(".input--cvv .input__field--cvv");
        public static final SelenideElement email = $("#cardEmailField");
        public static final SelenideElement payButton = $("#submitButton");
        public static final SelenideElement sendCheckByEmail = $("#cardEmail+.checkbox__body");
        public static final SelenideElement payMethodCard = $("#paymentTypeCard");
        public static final SelenideElement payMethodYandexPay = $(".yandexpay.payment-method__button");
        public static final SelenideElement payMethodSBP = $("#sbpButton");
        public static final SelenideElement totalPayB2B = $("#totalAmountValue");



    }

    public static class ReviewPage {

        public static final SelenideElement paymentStatusAfterPay = $(".review__status");
        public static final SelenideElement reviewContainer = $(".review .container");
        public static final SelenideElement finishReviewButton = $(".review__common .main-btn");
        public static final SelenideElement reviewTextArea = $(".review__textarea");
        public static final SelenideElement reviewStars = $(".review__stars");
        public static final SelenideElement review5Stars = $(".review__stars .review__stars-item:nth-child(6)");
        public static final SelenideElement currentActiveStar = $(".review__stars .review__stars-item.active");
        public static final SelenideElement whatDoULikeList = $(".review__list");
        public static final ElementsCollection whatDoULikeListRandomOption = $$x("//li[@class='review__list-item']");
        public static final SelenideElement activeWhatDoULikeListRandomOption = $x("//li[@class='review__list-item active']");
        public static final SelenideElement paymentProcessStatus = $(".payment-process__status");
        public static final SelenideElement suggestionHeading = $(".suggestions .suggestions__title");
        public static final ElementsCollection suggestionOptions = $$(".suggestions .suggestions__list-item");

    }

    public static class RKeeperAdmin {

        public static final SelenideElement email = $("#email");
        public static final SelenideElement password = $("#password");
        public static final SelenideElement logInButton = $(".vButton");
        public static final SelenideElement configuration =
            $(".VMenuProfileLink:nth-of-type(2) .VMenuProfileLink__clickArea");
        public static final SelenideElement optionTabTips =
            $x("//*[@class='logsPage-tabs__btn'][text()='Чаевые']");
        public static final SelenideElement tipsDisabled = $("#DISABLED");

    }


}
