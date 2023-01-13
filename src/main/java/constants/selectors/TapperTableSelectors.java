package constants.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class TapperTableSelectors {

    public static class Common {

        public static final String bodyJS = "body";
        public static final SelenideElement startScreenLogoContainer =
            $("[data-auto=\"startScreenLogoContainer\"]");
        public static final SelenideElement startScreenLogoContainerImage =
                $("[data-auto=\"startScreenLogoContainer\"] img");
        public static final String startScreenLogoContainerImageNotSelenide =
                "[data-auto='startScreenLogoContainer'] img";
        public static final SelenideElement pagePreLoader = $(".mainPage .basePreloader");

    }

    public static class RootPage {

        public static class DishList {

            public static final SelenideElement headerTitleMyOrder =
                $("[data-auto=\"headerTitleMyOrder\"]");
            public static final SelenideElement tableNumber =
                $("[data-auto=\"tableNumber\"]");
            public static final SelenideElement emptyTableLogoClock = $("[data-auto=\"emptyTableLogoClock\"]");
            public static final SelenideElement emptyOrderHeading = $("[data-auto=\"emptyOrderHeading\"]");
            public static final SelenideElement separateOrderHeading =
                $("[data-auto=\"separateOrderHeading\"]");
            public static final SelenideElement dishesSumChangedHeading =
                $("[data-auto=\"dishesSumChangedHeading\"]");
            public static final SelenideElement divideCheckSlider = $("[data-auto=\"divideCheckSlider\"]");
            public static final SelenideElement divideCheckSliderActive =
                $("[data-auto=\"divideCheckSliderActive\"]");
            public static final SelenideElement orderContainer = $("[data-auto=\"menuDishesContainer\"]");
            public static final SelenideElement orderMenuContainer = $("[data-auto=\"orderMenuContainer\"]");
            public static final ElementsCollection allDishesInOrder =
                $$("[data-auto=\"allDishesInOrder\"]");
            public static final ElementsCollection paidDishes =
                $$x("//li[.//*[@data-auto=\"paidDishes\"]]");
            public static final ElementsCollection disabledDishes =
                $$x("//li[.//*[@data-auto=\"disabledDishes\"]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishes =
                $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesCheckbox =
                $$x("//li[.//*[@data-auto=\"nonPaidDishes\"]]//*[@data-auto=\"allDishesCheckboxes\"]" +
                        "//*[@class='iconCheck']");
            public static final ElementsCollection disabledAndPaidDishes =
                $$x("//li[.//*[@data-auto=\"disabledDishes\"]] | //li[.//*[@data-auto=\"paidDishes\"]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesSum =
                $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]" +
                        "//*[@class=\"orderItem__price\"]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesName =
                $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]" +
                        "//*[@class=\"orderItem__name\"]");
            public static final ElementsCollection allDishesCheckboxes =
                $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@data-auto=\"serviceCharge\"]");
            public static final ElementsCollection allDishesStatuses =
                $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@class=\"orderItem__status\"]");

        }

        public static class TipsAndCheck {

            public static final SelenideElement tipsWaiter = $("[data-auto=\"tipsWaiter\"]");
            public static final String waiterImageNotSelenide = ".waiter .orderWaiter__photo";
            public static final SelenideElement waiterImage = $(".waiter .orderWaiter__photo");
            public static final SelenideElement waiterName = $(".waiter .orderWaiter__name");
            public static final SelenideElement tipsInCheckField = $("[data-auto=\"tipsInCheckField\"]");
            public static final SelenideElement tipsInCheckSum =
                $("[data-auto=\"tipsInCheckField\"] span+span");
            public static final SelenideElement overallSum =
                    $(".orderTotal__container .orderTotal__row:nth-child(1) span+span");

            public static final SelenideElement anotherGuestField =
                $("[data-auto=\"anotherGuestSumField\"]");
            public static final SelenideElement anotherGuestSum =
                $("[data-auto=\"anotherGuestSumField\"] span+span");
            public static final SelenideElement markedDishesField = $("[data-auto=\"markedDishesField\"]");
            public static final SelenideElement markedDishesSum =
                $("[data-auto=\"markedDishesField\"] span+span");
            public static final SelenideElement discountField = $("[data-auto=\"discountField\"]");
            public static final SelenideElement discountSum = $("[data-auto=\"discountField\"]+span");
            public static final SelenideElement markupField = $("[data-auto=\"markupField\"]");
            public static final SelenideElement markupSum = $("[data-auto=\"markupField\"]+span]");
            public static final SelenideElement resetTipsButton = $("[data-auto=\"resetTipsButton\"]");
            public static final SelenideElement tipsContainer = $("[data-auto=\"tipsContainer\"]");
            public static final SelenideElement totalTipsSumInMiddle =
                $("[data-auto=\"totalTipsSumInMiddle\"]");
            public static final SelenideElement activeTipsButton = $("[data-auto=\"activeTipsButton\"]");
            public static final ElementsCollection tipsListItem = $$("[data-auto=\"tipsListItem\"]");
            public static final SelenideElement totalPay = $("[data-auto=\"totalPay\"]+span");
            public static final SelenideElement tipsErrorMsg = $("[data-auto=\"tipsErrorMsg\"]");
            public static final SelenideElement tips0 =
                $x("//*[contains(@data-auto,'ips')][text()='0%']");
            public static final SelenideElement tips10 =
                $x("//*[contains(@data-auto,'ips')][text()='10%']");
            public static final SelenideElement tips15 =
                $x("//*[contains(@data-auto,'ips')][text()='15%']");
            public static final SelenideElement tips20 =
                $x("//*[contains(@data-auto,'ips')][text()='20%']");
            public static final SelenideElement tips25 =
                $x("//*[contains(@data-auto,'ips')][text()='25%']");
            public static final ElementsCollection notDisabledTipsPercentOptions =
                $$x("//*[@class='waiter__percent-wrap']//*[not(contains(@class,'disabled'))]");
            public static final ElementsCollection notDisabledAndNotZeroTipsPercentOptions =
                    $$x("//*[@class='waiter__percent-wrap']//*[not(contains(@class,'disabled'))]" +
                            "[not(text()='0%')]");

        }

        public static class PayBlock {

            public static final SelenideElement checkContainer = $("[data-auto=\"checkContainer\"]");
            public static final SelenideElement paymentButton = $("[data-auto=\"paymentButton\"]");
            public static final SelenideElement serviceChargeContainer = $(".serviceFee");
            public static final SelenideElement serviceChargeCheckboxSvg =
                $(".serviceFee [data-auto=\"serviceCharge\"] svg");
            public static final SelenideElement serviceChargeCheckboxButton =
                    $(".serviceFee [data-auto=\"serviceCharge\"]");
            public static final SelenideElement paymentMethodsContainer =
                $("[data-auto=\"paymentMethodsContainer\"]");
            public static final ElementsCollection paymentMethods =
                $$("[data-auto=\"paymentMethodsContainer\"] img");
            public static final SelenideElement confPolicyContainer = $("[data-auto=\"confPolicyContainer\"]");
            public static final SelenideElement confPolicyContent = $("[data-auto=\"confPolicyContent\"]");
            public static final SelenideElement confPolicyLink =
                $(".mainPage .privacyPolicy .privacyPolicy__text");
            public static final SelenideElement shareButton = $("[data-auto=\"shareButton\"]");

        }

        public static class TapBar {

            public static final SelenideElement appFooter = $("[data-auto=\"appFooter\"]");
            public static final SelenideElement appFooterMenuIcon =
                $x("//*[@class='appFooter__btn'][.//*[name()=\"svg\" and contains(@class,\"iconMenu\")]]");
            public static final SelenideElement appFooterWalletIcon =
                $("[data-auto=\"totalSumInWalletCounter\"]");
            public static final SelenideElement callWaiterHeadingPrompt =
                $("[data-auto=\"callWaiterHeading\"]");
            public static final SelenideElement callWaiterHeading =
                $("[data-auto=\"callWaiterContainer\"] .waiterCallModal__header");
            public static final SelenideElement callWaiterButton =
                $("[data-auto=\"callWaiterButton\"]");
            public static final SelenideElement callWaiterContainer =
                $("[class='waiterCallModal'][style=\"\"]");
            public static final SelenideElement callWaiterCloseButton =
                $("[data-auto=\"callWaiterCloseButton\"]");
            public static final SelenideElement callWaiterButtonSend =
                $("[data-auto=\"callWaiterButtonSend\"]");
            public static final SelenideElement callWaiterButtonCancel =
                $("[data-auto=\"callWaiterButtonCancel\"]");
            public static final SelenideElement callWaiterCommentArea =
                $("[data-auto=\"callWaiterCommentArea\"]");
            public static final SelenideElement totalSumInWalletCounter =
                $("[data-auto=\"totalSumInWalletCounter\"]");
            public static final SelenideElement closeCallWaiterFormInSuccessButton =
                $("[data-auto=\"closeCallWaiterText\"] button");
            public static final SelenideElement successCallWaiterHeading =
                $("[data-auto=\"closeCallWaiterText\"] .waiterCallModal__success-text");
            public static final SelenideElement successLogoCallWaiter =
                $("[data-auto=\"closeCallWaiterText\"] img");
            public static final SelenideElement callWaiterFadedBackground =
                $("[data-auto=\"callWaiterFadedBackground\"]");

        }

        public static class Menu {

            public static final ElementsCollection menuCategoryInHeader =
                $$(".orderMenu__header .orderMenuCategorues__item");
            public static final ElementsCollection menuCategoryContainerName =
                $$(".orderMenuList .orderMenuList__title");
            public static final SelenideElement menuDishContainer = $("[data-auto=\"orderMenuContainer\"]");
            public static final ElementsCollection dishMenuItems =
                $$(".orderMenuList__item .orderMenuProduct__name");
            public static final ElementsCollection categoryMenuItems =
                $$(".orderMenu .orderMenuCategorues__item");
            public static final String menuDishPhotosNotSelenide = ".orderMenuProduct__photo img";
            public static final ElementsCollection menuDishPhotos = $$(".orderMenuProduct__photo img");

        }

    }

    public static class Best2PayPage {

        public static final SelenideElement paymentContainer = $(".form__body--payment ");
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
        public static final SelenideElement totalPayB2B = $("#totalAmountValue");
        public static final SelenideElement transaction_id = $("form [name='reference']");

    }

    public static class ReviewPage {


        public static final SelenideElement paymentProcessContainer = $("[data-auto=\"paymentProcessStatus\"]");
        public static final SelenideElement paymentProcessText =
            $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__text");
        public static final SelenideElement paymentProcessStatus =
            $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__status");
        public static final SelenideElement paymentProcessGifProcessing =
            $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__gif img[src*='process_gif']");
        public static final SelenideElement paymentProcessGifSuccess =
            $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__gif img[src*='process_yes']");
        public static final SelenideElement reviewContainer = $("[data-auto=\"reviewContainer\"]");
        public static final SelenideElement paymentLogo = $("[data-auto=\"paymentLogo\"]");
        public static final SelenideElement paymentStatusAfterPay = $("[data-auto=\"paymentStatusAfterPay\"]");
        public static final SelenideElement paymentTime = $("[data-auto=\"paymentTime\"]");
        public static final SelenideElement commentHeading = $("[data-auto=\"commentHeading\"]");
        public static final SelenideElement isAllWasGoodHeading = $("[data-auto=\"isAllWasGoodHeading\"]");
        public static final SelenideElement finishReviewButton = $("[data-auto=\"finishReviewButton\"]");
        public static final SelenideElement reviewTextArea = $("[data-auto=\"reviewTextArea\"]");
        public static final SelenideElement reviewStars = $("[data-auto=\"reviewStars\"]");
        public static final SelenideElement review5Stars = $("[data-auto=\"reviewStars\"]:nth-child(5)");
        public static final SelenideElement review1Stars = $("[data-auto=\"reviewStars\"]:nth-child(1)");
        public static final SelenideElement whatDoULikeList = $("[data-auto=\"whatDoULikeList\"]");
        public static final ElementsCollection whatDoULikeListRandomOption =
            $$("[data-auto=\"whatDoULikeListRandomOption\"]");
        public static final SelenideElement activeWhatDoULikeListRandomOption =
            $("[data-auto=\"activeWhatDoULikeListRandomOption\"]");
        public static final SelenideElement suggestionHeading = $("[data-auto=\"suggestionHeading\"]");
        public static final ElementsCollection suggestionOptions = $$("[data-auto=\"suggestionOptions\"]");

    }

}
