package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import static data.Constants.TestData.TapperTable.TEST_WAITER_COMMENT;
import static data.selectors.TapperTable.ReviewPage.thanksReviewCloseButton;

public class TapperTable {

    public static class Common {

        public static final SelenideElement serviceUnavailabilityContainer = $(".doNotWorking");
        public static final SelenideElement serviceUnavailabilityInfoContainer = $(".doNotWorking__info");
        public static final SelenideElement serviceUnavailabilityAdviceContainer = $(".doNotWorking__advice");
        public static final SelenideElement techWorkContainer = $(".baseModal");
        public static final SelenideElement techWorkInfoContainer = $(".payBlocked__description");
        public static final SelenideElement techWorkCloseButton = $(".baseModal button");
        public static final SelenideElement wiFiIcon = $(".appHeader__wifi");

        public static final By wiFiIconBy = By.cssSelector(".appHeader__wifi");
        public static final SelenideElement wiFiContainer = $(".wifiModal");
        public static final SelenideElement wiFiHeader = $(".wifiModal__header");
        public static final SelenideElement wiFiCloseButton = $(".baseButtonClose.wifiModal__close");
        public static final SelenideElement wiFiName = $(".wifiModal__name");
        public static final SelenideElement wiFiPassword = $(".wifiModal__footer button");
        public static final SelenideElement startScreenLogoContainer =
                $("[data-auto=\"startScreenLogoContainer\"]");
        public static final SelenideElement startScreenLogoContainerImage =
                $("[data-auto=\"startScreenLogoContainer\"] img");
        public static final String startScreenLogoContainerImageNotSelenide =
                "[data-auto='startScreenLogoContainer'] img";
        public static final SelenideElement pagePreLoader = $(".appPreloader");

        public static final SelenideElement orderPageContainer = $(".mainPage__content");


    }

    public static class RootPage {

        public static class DishList {

            public static final SelenideElement changeLanguageButton = $(".applicationTranslator");
            public static final SelenideElement languageContainer = $(".listOfLanguages");
            public static final ElementsCollection languageItems= $$(".listOfLanguages__container");
            public static final ElementsCollection languageItemsIcon= $$(".listOfLanguages__container img");
            public static final SelenideElement refreshButtonEmptyPage =
                    $(".baseButtonUpdate .baseButtonUpdate__container");
            public static final By refreshButtonEmptyPageBy =
                    By.cssSelector(".baseButtonUpdate__container__text");
            public static final SelenideElement modalHintContainer = $(".modalHint");
            public static final SelenideElement modalHintCloseButton = $(".modalHint .baseButtonClose");
            public static final SelenideElement appHeader = $(".appHeader");
            public static final SelenideElement tableNumber = $(".appHeader .appHeader__table");
            public static final SelenideElement emptyTableLogoClock = $("[data-auto=\"emptyTableLogoClock\"]");
            public static final SelenideElement emptyOrderMenuDescription = $(".orderMenuEmpty__text");
            public static final SelenideElement emptyOrderHeading = $("[data-auto=\"emptyOrderHeading\"]");
            public static final SelenideElement emptyOrderMenuButton = $(".orderMenuEmpty__container button");
            public static final SelenideElement thanksFeedBackAlert =
                    $(".orderMenuEmpty__background.backgroundActive");
            public static final SelenideElement separateOrderHeading =
                    $("[data-auto=\"separateOrderHeading\"]");
            public static final SelenideElement dishesSumChangedHeading =
                    $("[data-auto=\"dishesSumChangedHeading\"]");
            public static final SelenideElement divideCheckSlider = $("[data-auto=\"divideCheckSlider\"]");
            public static final SelenideElement divideCheckSliderActive =
                    $("[data-auto=\"divideCheckSliderActive\"]");
            public static final SelenideElement orderContainer = $("[data-auto=\"menuDishesContainer\"]");
            public static final SelenideElement orderMenuContainer = $("[data-auto=\"orderMenuContainer\"]");
            public static final ElementsCollection allDishesInOrder = $$("[data-auto=\"allDishesInOrder\"]");
            public static final String dishModificatorName = ".orderItem__modificator";
            public static final String dishNameSelector = ".orderItem__name";
            public static final String dishPriceTotalSelector = ".orderItem__price:last-child";
            public static final String dishPriceWithDiscountSelector = ".orderItem__price.--new";
            public static final String dishPriceWithoutDiscountSelector = ".orderItem__price:first-child";
            public static final String dishCheckboxSelector = ".iconCheck";
            public static final ElementsCollection allPaidDishes =
                    $$x("//li[.//*[@data-auto=\"paidDishes\"]]");
            public static final ElementsCollection allDisabledDishes =
                    $$x("//li[.//*[@data-auto=\"disabledDishes\"]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishes =
                    $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]");
            public static final ElementsCollection allNonPaidAndDisabledDishes =
                    $$x("//*[@data-auto=\"allDishesInOrder\"]" +
                            "[.//*[@class='orderItem__status' and not(@data-auto=\"paidDishes\")]]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesCheckbox =
                    $$x("//li[.//*[@data-auto=\"nonPaidDishes\"]]//*[@data-auto=\"allDishesCheckboxes\"]" +
                            "//*[@class='iconCheck']");
            public static final ElementsCollection disabledAndPaidDishes =
                    $$x("//li[.//*[@data-auto=\"disabledDishes\"]] | " +
                            "//li[.//*[@data-auto=\"paidDishes\"]]");
            public static final ElementsCollection allChosenDishes =
                    $$x("//li[.//*[@class='iconCheck' and @style='']]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesSum =
                    $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]" +
                            "//*[@class=\"orderItem__price\"] | //li[.//*[@data-auto=\"nonPaidDishes\"]]" +
                            "//*[contains(@class,\"orderItem__price\")][last()]");
            public static final ElementsCollection allNonPaidAndNonDisabledDishesName =
                    $$x("//li[.//*[@data-auto=\"\"]] | //li[.//*[@data-auto=\"nonPaidDishes\"]]" +
                            "//*[@class=\"orderItem__name\"]");
            public static final ElementsCollection allDishesCheckboxes =
                    $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@data-auto=\"serviceCharge\"]");
            public static final ElementsCollection allDishesStatuses =
                    $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@class=\"orderItem__status\"]");

            public static final ElementsCollection allDishesDisabledStatuses =
                    $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@class=\"orderItem__status\" and " +
                            "contains(text(),'Оплачивается')]");
            public static final ElementsCollection allDishesPayedStatuses =
                    $$x("//*[@data-auto=\"menuDishesContainer\"]//*[@class=\"orderItem__status\" and " +
                            "contains(text(),'Оплачено')]");
            public static final String dishOrderStatusSelector = ".orderItem__status";
            public static final String dishPreloaderSpinnerSelector = ".orderItem__preloader";
            public static final By dishPreloaderSpinnerSelectorBy = By.cssSelector(".orderItem__preloader");

        }

        public static class TipsAndCheck {

            public static final SelenideElement tipsWaiter = $(".tipsProfile");
            public static final SelenideElement tipsGoalContainer = $(".tipsProfile__dream");
            public static final ElementsCollection roleButtons = $$(".tipsButton");
            public static final SelenideElement roleButtonsContainer = $(".tipsBlock__buttons");
            public static final SelenideElement waiterRoleButton =
                    $x("//*[contains(@class,'tipsButton') and contains(text(),'Официант')]");
            public static final SelenideElement waiterHeading = $(".tipsProfile__position");
            public static final SelenideElement hookahButton =
                    $x("//*[contains(@class,'tipsButton') and contains(text(),'Кальянщик')]");
            public static final SelenideElement kitchenButton =
                    $x("//*[contains(@class,'tipsButton') and contains(text(),'Кухня')]");
            public static final String serviceWorkerImageSelector = ".tipsProfile__photo";
            public static final SelenideElement serviceWorkerImage = $(".tipsProfile__photo");
            public static final SelenideElement serviceWorkerName = $(".tipsProfile__name");
            public static final SelenideElement tipsInCheckField =
                    $("[data-auto=\"tipsInCheckField\"][style=\"\"]");
            public static final SelenideElement tipsInCheckSum =
                    $x("//*[@data-auto='tipsInCheckField'][.//*[contains(text(),'Чаевые')]]/span[2]");
            public static final SelenideElement markedDishesSum =
                    $("[data-auto=\"markedDishesField\"] span+span");
            public static final SelenideElement discountField = $("[data-auto=\"discountField\"]");
            public static final SelenideElement discountSum = $("[data-auto=\"discountField\"]+span");
            public static final SelenideElement markupSum = $("[data-auto=\"markupField\"]+span");
            public static final SelenideElement resetTipsButton = $("[data-auto=\"resetTipsButton\"]");
            public static final SelenideElement tipsContainer =
                    $x("//*[@class='tipsCustom'] | //*[@class='tipsPercents']");
            public static final SelenideElement totalTipsSumInMiddle =
                    $("[data-auto=\"totalTipsSumInMiddle\"]");
            public static final SelenideElement activeTipsButton = $(".tipsPercents__item.active");
            public static final ElementsCollection tipsListItem = $$(".tipsPercents__item");
            public static final SelenideElement totalPay = $("[data-auto=\"totalPay\"]+span");
            public static final SelenideElement tipsInfo = $(".tipsInfo");
            public static final SelenideElement totalOrderPay = $(".orderTotal__container span+span");
            public static final SelenideElement tipsErrorMsg = $(".tipsBlock__error");
            public static final SelenideElement tips0 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '0%']]");
            public static final SelenideElement tips2AndHalf =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '2.5%']]");
            public static final SelenideElement tips5 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '5%']]");
            public static final SelenideElement tips7andHalf =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '7.5%']]");
            public static final SelenideElement tips10 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '10%']]");
            public static final SelenideElement tips12andHalf =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '12.5%']]");
            public static final SelenideElement tips15 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '15%']]");
            public static final SelenideElement tips20 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '20%']]");
            public static final SelenideElement tips25 =
                    $x("//*[contains(@class,'ips')][text()[normalize-space() = '25%']]");
            public static final ElementsCollection notDisabledTipsPercentOptions =
                    $$("[class='tipsPercents__item']:not([disabled])");
            public static final ElementsCollection notDisabledAndNotZeroTipsPercentOptions =
                    $$x("//*[@class='tipsPercents__list']" +
                            "//*[not(contains(@disabled,'disabled'))][not(text()[normalize-space() = '0%'])]");

        }

        public static class PayBlock {

            public static final SelenideElement checkContainer = $("[data-auto=\"checkContainer\"]");
            public static final SelenideElement paymentButton = $("[data-auto=\"paymentButton\"]");
            public static final SelenideElement serviceChargeContainer = $(".serviceFee");
            public static final SelenideElement serviceChargeCheckboxSvg =
                    $(".serviceFee [data-auto=\"serviceCharge\"] svg");
            public static final SelenideElement serviceChargeCheckboxButton =
                    $(".serviceFee [data-auto=\"serviceCharge\"]");
            public static final SelenideElement serviceChargePopUp = $(".ModalServiceFee__content");
            public static final SelenideElement serviceChargePopUpDisableButton =
                    $(".ModalServiceFee__content button.ModalServiceFee__btn");
            public static final SelenideElement paymentOptionsContainer = $(".payedVariants__header");
            public static final SelenideElement paymentOptionCreditCard =
                    $x("//*[@class='paymentMethod__item' and contains(text(),'Банковская карта')]");
            public static final SelenideElement paymentOptionSBP =
                    $x("//*[@class='paymentMethod__item'][.//*[contains(text(),'Система быстрых')]]");
            public static final SelenideElement paymentContainerSaveButton = $(".paymentMethod button");
            public static final SelenideElement paymentChosenTypeText = $(".payedVariants__subtitle");
            public static final SelenideElement paymentOverlay = $(".modalBottom__overlay");
            public static final SelenideElement paymentSBPContainer = $(".modalBottom");
            public static final ElementsCollection paymentBanksPriorityBanks =
                    $$(".paymentBanksPriority__item");
            public static final SelenideElement paymentBanksAllBanksButton = $(".paymentAllBanksButton");
            public static final SelenideElement searchBankInput = $(".baseSearch__input");
            public static final ElementsCollection bankListItems = $$(".paymentAllBanks-list__inner");
            public static final SelenideElement choseBankCloseModal = thanksReviewCloseButton;
            public static final SelenideElement emptyBankListItems = $(".paymentAllBanks__empty");
            public static final SelenideElement searchBankClearButton = $(".baseSearch__clear");
            public static final SelenideElement paymentBanksDescription = $(".paymentBanks__description");
            public static final SelenideElement paymentBanksReceipt = $(".paymentBanks__check");
            public static final SelenideElement emailReceiptInput = $(".baseInput input");
            public static final SelenideElement emailReceiptErrorMsg = $(".baseInput__error");
            public static final SelenideElement cancelProcessPayingContainer =
                    $(".baseModal .baseModal__content");
            public static final SelenideElement cancelProcessPayingContainerCancelBtn =
                    $(".paymentAlert__buttons .baseButton--blue");
            public static final SelenideElement cancelProcessPayingContainerSaveBtn =
                    $(".paymentAlert__buttons .baseButton--gray");
            public static final SelenideElement termOfUseLink =
                    $("[data-auto=\"confPolicyContainer\"] span:first-child");
            public static final SelenideElement termOfUseContainer = $(".privacyPolicyModal__content");
            public static final SelenideElement termOfUseOverlay = $(".privacyPolicyModal__overlay");
            public static final SelenideElement confPolicyLink =
                    $("[data-auto=\"confPolicyContainer\"] span:last-child");
            public static final SelenideElement confPolicyContainer = $(".privacyPolicyPersonalModal");
            public static final SelenideElement confPolicyCheckbox = $(".privacyPolicy__checkbox");
            public static final SelenideElement confPolicyOverlay = $(".privacyPolicyPersonalModal__overlay");
            public static final SelenideElement shareButton = $(".shareBtn");
            public static final String shareButtonSvgNotSelenide = ".shareBtn img";

        }

        public static class TapBar {

            public static final SelenideElement appFooter = $("[data-auto=\"appFooter\"]");
            public static final SelenideElement appFooterMenuIcon =
                    $x("//*[@class='appFooter__btn'][.//*[name()=\"svg\"" +
                            " and contains(@class,\"iconMenu\")]]");
            public static final SelenideElement callWaiterHeading =
                    $("[data-auto=\"callWaiterContainer\"] .waiterCallModal__header");
            public static final SelenideElement callWaiterButton =
                    $("[data-auto=\"callWaiterButton\"]");
            public static final SelenideElement callWaiterButtonText =
                    $("[data-auto=\"callWaiterButton\"] .baseCallButton__container-text");
            public static final SelenideElement callWaiterContainer = $("[data-auto=\"callWaiterContainer\"]");
            public static final SelenideElement callWaiterCloseButton =
                    $("[data-auto=\"callWaiterCloseButton\"]");
            public static final SelenideElement callWaiterButtonSend = $(".waiterCallModal__form button");
            public static final SelenideElement callWaiterCommentArea = $(".waiterCallModal__textarea");
            public static final SelenideElement totalSumInWalletCounter =
                    $("[data-auto=\"totalSumInWalletCounter\"]");
            public static final SelenideElement callWaiterFadedBackground =
                    $("[data-auto=\"callWaiterFadedBackground\"]");
            public static final SelenideElement callWaiterFirstGreetingsMessage =
                    $x("//*[@class='message__text']/p[contains(text(),'Привет! Я отправлю ')]");
            public static final ElementsCollection callWaiterSecondMessage =
                    $$x("//*[@class='message__text']/p[contains(text(),'Я отправил сообщение ')]");
            public static final SelenideElement callWaiterTapperMessage =
                    $x("//*[@class='message__text']/p[contains(text(),'Таппер создан ')]");
            public static final SelenideElement callWaiterGuestTestComment =
                    $x("//*[@class='message__text']/p[contains(text(),'" + TEST_WAITER_COMMENT + "')]");
            public static final SelenideElement callWaiterTypingMessagePreloader = $(".preloader");

        }

        public static class Menu {

            public static final ElementsCollection menuCategoryInHeader =
                    $$(".orderMenu__header .orderMenuCategorues__item");
            public static final ElementsCollection menuCategoryContainerName =
                    $$(".orderMenuList .orderMenuList__title");
            public static final SelenideElement menuDishContainer = $("[data-auto=\"orderMenuContainer\"]");
            public static final ElementsCollection dishMenuItemsName =
                    $$(".orderMenuList__item .orderMenuProduct__name");
            public static final String dishMenuItemsNameSelector = ".orderMenuProduct__name";
            public static final String dishMenuItemsPriceSelector = ".orderMenuProduct__price";
            public static final String dishMenuPhotoSelector = ".orderMenuProduct__photo img";
            public static final ElementsCollection dishMenuItemsWeightAndCalories =
                    $$(".orderMenuList__item .orderMenuProduct__count");
            public static final ElementsCollection dishMenuItems =
                    $$(".orderMenuList__item .orderMenuProductList__item");
            public static final ElementsCollection dishMenuItemsMark =
                    $$(".orderMenuList__item .orderMenuProduct__label-text");
            public static final SelenideElement dishDetailCardContent = $(".detail__content");
            public static final SelenideElement dishDetailCardCloseButton = $(".detail__close");
            public static final ElementsCollection categoryMenuItems =
                    $$(".orderMenu .orderMenuCategorues__item");
            public static final String menuDishPhotoInDetailCard = ".detail__image-wrap img";
            public static final String menuDishPhotosSelector = ".orderMenuProduct__photo .orderMenuProduct__img";
            public static final SelenideElement menuDishNameInDetailCard = $(".detail .detail__name");
            public static final SelenideElement menuDishAmountDetailCard = $(".detail .detail__weight");
            public static final SelenideElement menuDishIngredientsDetailCard =
                    $(".detail__composition.detail__text");
            public static final ElementsCollection firstTestDish =
                    $$x("//*[@class='orderMenuProduct__name']");


        }

    }

    public static class Best2PayPage {

        public static final SelenideElement paymentContainer = $(".container");
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

        public static final SelenideElement paymentProcessContainer =
                $("[data-auto=\"paymentProcessStatus\"]");
        public static final SelenideElement paymentProcessText =
                $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__text");
        public static final SelenideElement paymentProcessStatus =
                $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__status");
        public static final SelenideElement paymentProcessGifProcessing =
                $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__gif img[src*='process_gif']");
        public static final SelenideElement paymentProcessGifSuccess =
                $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__gif img[src*='process_yes']");
        public static final SelenideElement paymentProcessGifError =
                $("[data-auto=\"paymentProcessStatus\"] .paymentProcess__gif img[src*='process_no']");
        public static final SelenideElement paymentProcessCloseButton = $(".paymentProcess__close");
        public static final SelenideElement reviewContainer = $("[data-auto=\"reviewContainer\"]");
        public static final SelenideElement paymentLogo = $("[data-auto=\"paymentLogo\"]");
        public static final SelenideElement paymentStatusAfterPay = $("[data-auto=\"paymentStatusAfterPay\"]");
        public static final SelenideElement paymentTime = $("[data-auto=\"paymentTime\"]");
        public static final SelenideElement commentHeading = $("[data-auto=\"commentHeading\"]");
        public static final SelenideElement isAllWasGoodHeading = $("[data-auto=\"isAllWasGoodHeading\"]");
        public static final SelenideElement finishReviewButton = $("[data-auto=\"finishReviewButton\"]");
        public static final SelenideElement reviewTextArea = $("[data-auto=\"reviewTextArea\"]");
        public static final String reviewStarsSvgSelector = "svg";
        public static final ElementsCollection reviewStars = $$("[data-auto=\"reviewStars\"]");
        public static final SelenideElement thanksReviewContainer = $(".modalBottom");
        public static final SelenideElement thanksReviewCloseButton = $(".modalBottom .modalBottom__close");
        public static final ElementsCollection reviewLinks = $$(".reviewLinksList__item");
        public static final SelenideElement yandexReviewLink = $(".reviewLinksList__item[href*=yandex]");
        public static final SelenideElement doubleGisReviewLink = $(".reviewLinksList__item[href*=gis]");
        public static final SelenideElement googleReviewLink = $(".reviewLinksList__item[href*=google]");
        public static final SelenideElement reviewNoThanksButton = $(".reviewLinksList__close");
        public static final SelenideElement review5Stars = $("[data-auto=\"reviewStars\"]:nth-child(5)");
        public static final SelenideElement review1Star = $("[data-auto=\"reviewStars\"]:nth-child(1)");
        public static final SelenideElement review3Stars = $("[data-auto=\"reviewStars\"]:nth-child(3)");
        public static final SelenideElement review4Stars = $("[data-auto=\"reviewStars\"]:nth-child(4)");
        public static final SelenideElement whatDoULikeList = $("[data-auto=\"whatDoULikeList\"]");
        public static final ElementsCollection whatDoULikeListRandomOption =
                $$("[data-auto=\"whatDoULikeListRandomOption\"]");
        public static final ElementsCollection activeWhatDoULikeListRandomOption =
                $$("[data-auto=\"activeWhatDoULikeListRandomOption\"]");
        public static final SelenideElement suggestionContainer = $(".reviewModal__suggestions");
        public static final SelenideElement suggestionHeading = $("[data-auto=\"suggestionHeading\"]");
        public static final ElementsCollection suggestionOptions = $$("[data-auto=\"suggestionOptions\"]");
        public static final String suggestionOptionsSvgSelector = "svg";
        public static final ElementsCollection suggestionOptionsSvg =
                $$("[data-auto=\"suggestionOptions\"] svg");

    }

}
