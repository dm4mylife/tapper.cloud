package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class TapperTableSelectors {

    public static class Common {

        public static final String bodyJS = "body";
        public static final SelenideElement startScreenLogoContainer = $(".mainPage .restaurantPreloader");
        public static final SelenideElement pagePreLoader = $(".mainPage .basePreloader");

    }

    public static class RootPage {

        public static class DishList {

            public static final SelenideElement headerTitleMyOrder =
                $x("//*[contains(@class,'header__item')][contains(text(),'Мой заказ')]");
            public static final SelenideElement tableNumber =
                $x("//*[contains(@class,'header__item')][contains(text(),'Стол')]");
            public static final SelenideElement emptyTableLogoClock = $(".orderEmpty img");
            public static final SelenideElement emptyOrderHeading = $(".orderEmpty .orderEmpty__description");
            public static final SelenideElement separateOrderHeading = $(".mainPage__content .separateOrderText");
            public static final SelenideElement dishesSumChangedHeading = $(".mainPage .baseAlert.active");
            public static final SelenideElement divideCheckSlider = $(".mainPage .BaseSwitch");
            public static final SelenideElement divideCheckSliderActive = $(".mainPage .BaseSwitch.active");
            public static final SelenideElement orderContainer = $(".mainPage__content .orderList");
            public static final SelenideElement orderMenuContainer = $(".mainPage .orderMenu");

            public static final ElementsCollection allDishesInOrder =
                $$(".orderList li.orderItem");
            public static final ElementsCollection paidDishes =
                $$(".orderList .orderItem.payed");
            public static final ElementsCollection disabledDishes =
                $$x("//*[@class='orderItem'][./*[contains(text(),'Оплачивается')]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishes =
                $$x("//*[@class='orderItem'][./*[@class='orderItem__status' and not(contains(text(),'Оплачивается'))]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesCheckbox =
                    $$x("//*[@class='orderItem'][./*[@class='orderItem__status' and not(contains(text(),'Оплачивается'))]]//*[@class='iconCheck']");
            public static final ElementsCollection disabledAndPaidDishes =
                $$x("//*[contains(@class,'orderItem')][./*[@class='orderItem__status' and not(contains(text(),'Не оплачено'))]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesSum =
                $$x("//*[@class='orderItem'][./*[@class='orderItem__status' and not(contains(text(),'Оплачивается'))]]//*[@class='orderItem__price']");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesName =
                $$x("//*[@class='orderItem'][./*[@class='orderItem__status' and not(contains(text(),'Оплачивается'))]]//*[@class='orderItem__name']");
            public static final ElementsCollection allDishesCheckboxes = $$(".orderList .orderItem__checkbox");
            public static final ElementsCollection allDishesStatuses = $$(".orderList .orderItem__status");
            public static final ElementsCollection dishesStatus = $$(".orderItem .orderItem__status");

        }

        public static class TipsAndCheck {

            public static final SelenideElement tipsWaiter = $(".orderWaiter");
            public static final String waiterImage = ".waiter .orderWaiter__photo";
            public static final SelenideElement tipsInCheckField =
                $x("//*[@class='orderTotal']//*[@class='orderTotal__row'][.//*[contains(text(),'Чаевые')]]");
            public static final SelenideElement tipsInCheckSum =
                $x("//*[@class='orderTotal']//*[contains(text(),'Чаевые')]/following-sibling::span");
            public static final SelenideElement anotherGuestSumField =
                $x("//*[@class='orderTotal']//*[@class='orderTotal__row'][.//*[contains(text(),'Другой пользователь')]]");
            public static final SelenideElement anotherGuestSum =
                $x("//*[@class='orderTotal']//*[contains(text(),'Другой пользователь')]/following-sibling::span");
            public static final SelenideElement markedDishesField =
                $x("//*[@class='orderTotal']//*[@class='orderTotal__row'][.//*[contains(text(),'Отмеченные позиции')]]");
            public static final SelenideElement markedDishesSum =
                $x("//*[@class='orderTotal']//*[contains(text(),'Отмеченные позиции')]/following-sibling::span");
            public static final SelenideElement discountField =
                $x("//*[@class='orderTotal']//*[@class='orderTotal__row'][.//*[contains(text(),'Скидка')]]");
            public static final SelenideElement discountSum =
                $x("//*[@class='orderTotal']//*[contains(text(),'Скидка')]/following-sibling::span");
            public static final SelenideElement markupField =
                $x("//*[@class='orderTotal']//*[@class='orderTotal__row'][.//*[contains(text(),'Наценка')]]");
            public static final SelenideElement markupSum =
                $x("//*[@class='orderTotal']//*[contains(text(),'Наценка')]/following-sibling::span");
            public static final SelenideElement resetTipsButton = $(".waiter__sum .baseButtonClose");
            public static final SelenideElement tipsContainer = $(".waiter");
            public static final SelenideElement totalTipsSumInMiddle = $(".waiter__sum input");
            public static final SelenideElement activeTipsButton = $(".waiter__percents .waiterTipsPercent.active");
            public static final ElementsCollection tipsOptions =
                $$(".waiter__percents .waiterTipsPercent");
            public static final SelenideElement totalPay =
                $x("//*[contains(@class,'orderTotal__row')]/span[text()=\"Итого к оплате:\"]/following-sibling::span");
            public static final SelenideElement tipsErrorMsg = $(".waiter__percents .waiter__error");
            public static final SelenideElement tips0 =
                $x("//*[contains(@class,'waiterTipsPercent') and normalize-space(text())='0 %']");
            public static final SelenideElement tips10 =
                $x("//*[contains(@class,'waiterTipsPercent') and normalize-space(text())='10 %']");
            public static final SelenideElement tips15 =
                $x("//*[contains(@class,'waiterTipsPercent') and normalize-space(text())='15 %']");
            public static final SelenideElement tips20 =
                $x("//*[contains(@class,'waiterTipsPercent') and normalize-space(text())='20 %']");
            public static final SelenideElement tips25 =
                $x("//*[contains(@class,'waiterTipsPercent') and normalize-space(text())='25 %']");
            public static final ElementsCollection notDisabledTipsPercentOptions =
                $$x("//*[@class='waiterTipsPercent']");
            public static final ElementsCollection notDisabledAndZeroTipsPercentOptions =
                $$x("//*[@class='waiterTipsPercent' and not(normalize-space(text())='0 %')]");

        }

        public static class PayBlock {

            public static final SelenideElement checkContainer = $(".orderTotal");
            public static final SelenideElement paymentButton = $x("//*[@class='mainPage']//button[text()='Оплатить']");
            public static final SelenideElement serviceChargeContainer = $(".serviceFee");
            public static final SelenideElement serviceChargeCheckbox = $(".serviceFee__checkbox .baseClickedCheckbox svg");
            public static final SelenideElement paymentMethodsContainer = $(".payedVariants");
            public static final ElementsCollection paymentMethods = $$(".payedVariants img");
            public static final SelenideElement confPolicyContainer = $(".mainPage .privacyPolicyModal");
            public static final SelenideElement confPolicyContent = $(".mainPage .privacyPolicyModal__content");
            public static final SelenideElement confPolicyLink = $(".mainPage .privacyPolicy .privacyPolicy__text");
            public static final SelenideElement shareButton = $x("//*[@class='mainPage']//button[text()='Поделиться счетом']");

        }

        public static class TapBar {

            public static final SelenideElement appFooter = $("[class='appFooter']");
            public static final SelenideElement appFooterMenuIcon = $x("//*[@class='appFooter__btn'][.//*[name()=\"svg\" and contains(@class,\"iconMenu\")]]");
            public static final SelenideElement appFooterWalletIcon = $x("//*[@class='appFooter__btn'][.//*[name()=\"svg\" and @class=\"iconCreditCard\"]]");
            public static final SelenideElement callWaiterHeadingPrompt = $(".appFooter .appFooter__callWaiter-text");
            public static final SelenideElement callWaiterHeading = $(".waiterCallModal .waiterCallModal__header");
            public static final SelenideElement callWaiterButton = $(".appFooter .appFooter__callWaiter");
            public static final SelenideElement callWaiterContainer = $(".waiterCallModal .waiterCallModal__content");
            public static final SelenideElement callWaiterCloseButton = $(".baseButtonClose.waiterCallModal__close");
            public static final SelenideElement callWaiterButtonSend = $(".waiterCallModal__btn button[type=\"submit\"]");
            public static final SelenideElement callWaiterButtonCancel = $(".waiterCallModal__btn button[type=\"button\"]");
            public static final SelenideElement callWaiterCommentArea = $(".waiterCallModal__form textarea");
            public static final SelenideElement totalSumInWalletCounter = $(".appFooter .appFooter__priceShield");
            public static final SelenideElement closeCallWaiterFormInSuccess = $(".waiterCallModal__success .waiterCallModal__success-btn");
            public static final SelenideElement successCallWaiterHeading = $(".waiterCallModal__success .waiterCallModal__success-text");
            public static final SelenideElement successLogoCallWaiter = $(".waiterCallModal__content .waiterCallModal__success img");
            public static final SelenideElement callWaiterFadedBackground = $(".waiterCallModal .waiterCallModal__overlay");

        }

        public static class Menu {



            public static final ElementsCollection menuCategoryInHeader =
                    $$(".orderMenu__header .orderMenuCategorues__item");
            public static final ElementsCollection menuCategoryContainerName =
                    $$(".orderMenuList .orderMenuList__title");
            public static final ElementsCollection menuDishNames =
                    $$(".orderMenuList .orderMenuProduct__name");
            public static final ElementsCollection dishesInCategoryContainer =
                    $$x("//*[@class='orderMenuList']//*[@class='orderMenuList__item'][.//*[contains(text(),'новая группа')]]//*[@class='orderMenuProductList__item']");


            public static final SelenideElement menuDishContainer = $(".orderMenu .orderMenuList");
            public static final ElementsCollection dishMenuItems = $$(".orderMenuList__item .orderMenuProduct__name");
            public static final ElementsCollection categoryMenuItems = $$(".orderMenu .orderMenuCategorues__item");
            public static final String menuDishPhotos = ".orderMenuProduct__photo img";
            public static final SelenideElement test = $(".orderMenuProduct__photo img");

        }

    }

    public static class Best2PayPage {

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

        public static final SelenideElement paymentContainer = $(".form__body--payment ");
        public static final SelenideElement paymentProcessContainer = $(".paymentProcess");
        public static final SelenideElement paymentProcessText = $(".paymentProcess .paymentProcess__text");
        public static final SelenideElement paymentProcessStatus = $(".paymentProcess .paymentProcess__status");
        public static final SelenideElement paymentProcessGifProcessing = $(".paymentProcess .paymentProcess__gif img[src*='process_gif']");
        public static final SelenideElement paymentProcessGifSuccess = $(".paymentProcess .paymentProcess__gif img[src*='process_yes']");
        public static final SelenideElement reviewContainer = $(".mainPage .reviewModal");
        public static final SelenideElement paymentLogo = $(".reviewModal__top img");
        public static final SelenideElement paymentStatusAfterPay = $(".reviewModal__top .reviewModal__status");
        public static final SelenideElement paymentTime = $(".reviewModal__top .reviewModal__time");
        public static final SelenideElement commentHeading = $(".reviewModal__comments .reviewModal__comments-title");
        public static final SelenideElement isAllWasGoodHeading = $(".reviewModal__common .reviewModal__title");
        public static final SelenideElement finishReviewButton = $(".reviewModal__btn button");
        public static final SelenideElement reviewTextArea = $(".reviewModal__comments textarea");
        public static final SelenideElement reviewStars = $(".reviewModal__stars .reviewModal__stars-item");
        public static final SelenideElement review5Stars = $(".reviewModal__stars .reviewModal__stars-item:nth-child(5)");
        public static final SelenideElement review1Stars = $(".reviewModal__stars .reviewModal__stars-item:nth-child(1)");
        public static final SelenideElement whatDoULikeList = $(".reviewModal__common .reviewModal__ratng-list");
        public static final ElementsCollection whatDoULikeListRandomOption =
            $$(".reviewModal__common .reviewModal__ratng-list .reviewModal__ratng-item");
        public static final SelenideElement activeWhatDoULikeListRandomOption =
            $(".reviewModal__common .reviewModal__ratng-list .reviewModal__ratng-item.active");
        public static final SelenideElement suggestionHeading = $(".reviewModal__suggestions .reviewModal__suggestions-title");
        public static final ElementsCollection suggestionOptions =
            $$(".reviewModal__suggestions .reviewModal__suggestions-list .reviewModal__suggestions-item");

    }


}
