package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class TapperAdminSelectors {

    public static class AuthorizationPage {

        public static final SelenideElement welcomeHeading = $(".sign__container .sign__welcome");
        public static final SelenideElement titleHeading = $(".sign__container .sign__title");
        public static final SelenideElement emailInput = $("#email");
        public static final SelenideElement passwordInput = $("#password");
        public static final SelenideElement forgotPasswordLink = $(".sign__container .sign__forget-pass");
        public static final SelenideElement logInButton = $("a+.vButton");
        public static final SelenideElement registrationLink = $("a[href='/users/registration']");
        public static final SelenideElement errorMsgLoginOrPassword =
            $x("//*[contains(@class,'vLandingInput')][.//*[@id=\"email\"]]//*[@class='vLandingInput__err']");

    }

    public static class RegistrationPage {

        public static final SelenideElement heading = $(".sign__title");
        public static final SelenideElement signInField = $(".sign__container p.sign__text");
        public static final SelenideElement signInLink = $(".sign__container p.sign__text a");
        public static final SelenideElement nameField = $("#name");
        public static final SelenideElement phoneField = $("#phone");
        public static final SelenideElement emailField = $("#email");
        public static final SelenideElement passwordField = $("#password");
        public static final SelenideElement passwordConfirmationField = $("#confirmation");
        public static final SelenideElement restaurantNameField = $("#name_shop");
        public static final SelenideElement captchaContainer = $("[title=\"reCAPTCHA\"]");
        public static final SelenideElement registrationButton = $(".vButton[type=\"submit\"]");
        public static final SelenideElement confPolicyField = $(".sign__conditions");
        public static final SelenideElement confPolicyLink= $(".sign__conditions a");
        public static final SelenideElement logoAtBottom= $("a[href='/']");
        public static final SelenideElement copyright= $(".sign__copyright");

    }

    public static class RKeeperAdmin {

        public static final SelenideElement mainMenuIcon = $(".vProfileMenu__info");
        public static final SelenideElement exitFromAdmin = $(".vProfileMenu__exit");
        public static final ElementsCollection leftMenuCategory =
            $$x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')]");
        public static final SelenideElement waiterMenuCategory =
            $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')][.//*[text()='Официанты']]");
        public static final SelenideElement tablesAndQrCodesCategory =
            $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')][.//*[text()='Столики и QR-коды']]");
        public static final SelenideElement menuCategory =
                $x("//*[@class='vProfileMenu__list']/*[contains(@class,'VMenuProfileLink')][.//*[text()='Меню']]");
        public static final SelenideElement authPassword = $x("//td[*[contains(text(),'Пароль')]]");
        public static final SelenideElement configuration =
            $(".VMenuProfileLink:nth-of-type(2) .VMenuProfileLink__clickArea");
        public static final SelenideElement optionTabTips =
            $x("//*[@class='logsPage-tabs__btn'][text()='Чаевые']");
        public static final SelenideElement tipsDisabled = $("#DISABLED");
        public static final SelenideElement pageHeading = $(".vProfileHeader__breadcrumbs-item");

        public static class Waiters {

            public static final SelenideElement waiterListHeading =
                    $(".vSectionWaitersection .section-profile__title");
            public static final SelenideElement refreshListButton =
                    $(".vSectionWaitersection__btn");
            public static final ElementsCollection waiterPaginationList =
                    $$(".vPagination .vPagination__item");
            public static final SelenideElement pagePreloader = $(".vLightPreloader");
            public static final SelenideElement errorMsgEmailWasApplied =
                    $x("//*[@class='section-profile__group'][.//*[contains(text(),'Пригласить официанта в систему')]]//*[@class='vLandingInput__err']");
            public static final SelenideElement waiterContainer = $(".vSectionWaitersection__container");
            public static final SelenideElement searchField = $(".employeeSearch #search");
            public static final SelenideElement searchError = $(".vSectionWaitersection__empty-search");
            public static final SelenideElement searchResetButton =
                    $(".vSectionWaitersection__top .employeeSearch__btn-reset");
            public static final ElementsCollection waiterList =
                    $$(".vSectionWaitersection__container .vWaiterItem");
            public static final SelenideElement waiterCardName =
                    $(".vSectionWaitersection__container .vWaiterItem .vWaiterItem__waiterName");
            public static final SelenideElement inviteButton =
                    $(".vSectionWaitersection__container .vButton");
            public static final SelenideElement cancelInvitationButton =
                    $(".vSectionWaitersection__container .vButtonRed");
            public static final SelenideElement saveButton =
                    $x("//*[@class='vButton'][contains(text(),\"Сохранить\")]");
            public static final SelenideElement enterEmailFieldWrapper = $x("//*[@class='vLandingInput__wrapper'][..//*[input[@id='email']]]");
            public static final SelenideElement enterEmailField = $(".section-profile__group #email");
            public static final SelenideElement waiterTelegramLogin = $("#loginTelegram");
            public static final SelenideElement waiterTelegramID = $("#idTelegram");
            public static final SelenideElement unlinkMailConfirmPopup = $(".vModalUnlinkEmail__content");
            public static final SelenideElement confirmUnlinkEmailButton =
                    $x("//*[@class='vModalUnlinkEmail__content']//button[@class='vButton']");
            public static final SelenideElement cancelMailConfirmPopup = $(".vModalCancelInvitation__content");
            public static final SelenideElement confirmCancelingEmailButton =
                    $x("//*[@class='vModalCancelInvitation__content']//button[@class='vButton']");
            public static final SelenideElement backToPreviousPage = $(".vSectionWaitersection__back");
            public static final SelenideElement waiterStatusInCard =
                    $(".vSectionWaitersection__container .waiter-status");
            public static final SelenideElement waiterStatusInPreview =
                    $(".vSectionWaitersection__container .vWaiterItem__waiterStatus");
            public static final SelenideElement waiterNameInCashDesk = $(".section-profile__group #name");
            public static final SelenideElement waiterName = $(".section-profile__group #display_name");
            public static final SelenideElement successSendingInvitation = $(".vAuthentication.active");
            public static final SelenideElement addPaymentCard =
                    $x("//*[@class='vButton'][contains(text(),\"Привязать карту\")]");

        }

        public static class TableAndQrCodes {

            public static final SelenideElement tableHeading =
                    $(".vSectionQr__header");
            public static final SelenideElement tablesAndQrCodesContainer =
                    $(".vSectionQr");
            public static final SelenideElement tableListHeading =
                    $(".vSectionQr .section-profile__title");
            public static final SelenideElement tableSearchFrom =
                    $("[class=\"vSectionQr__input\"][placeholder*=\"С\"]");
            public static final SelenideElement tableSearchTo =
                    $("[class=\"vSectionQr__input\"][placeholder*=\"По\"]");
            public static final SelenideElement findTableButton =
                    $("[class=\"vSectionQr__btn\"]");
            public static final SelenideElement resetTableButton =
                    $("[class=\"vSectionQr__btn vSectionQr__btn--red\"]");
            public static final ElementsCollection tableListItem =
                    $$(".vSectionQr__list .vSectionQr__item");
            public static final ElementsCollection paginationPages =
                    $$(".vPagination__list .vPagination__item");
            public static final SelenideElement backToTableList =
                    $(".vSectionQr__back");
            public static final SelenideElement tableItem =
                    $(".vSectionQr__item");
            public static final SelenideElement tableItemLink =
                    $(".vSectionQr__item a");
            public static final SelenideElement qrBlockWhite =
                    $("[class='vSectionQr__qr']");
            public static final SelenideElement qrBlockBlack =
                    $(".vSectionQr__qr--black");
            public static final SelenideElement qrDownloadImageWhite =
                    $("[class='vSectionQr__qr'] [class='vSectionQr__download']");
            public static final String qrWhiteImage =
                    "[class='vSectionQr__qr'] [class='vSectionQr__download'] img";
            public static final SelenideElement qrDownloadImageBlack =
                    $("[class='vSectionQr__qr vSectionQr__qr--black'] [class='vSectionQr__download']");
            public static final String qrBlackImage =
                    "[class='vSectionQr__qr vSectionQr__qr--black'] [class='vSectionQr__download'] img";

        }

        public static class Menu {

            public static final SelenideElement menuPagePreLoader = $(".vPreloader");
            public static final SelenideElement menuContainer = $(".vSectionMenuAdmin__container");
            public static final SelenideElement refreshMenuButton = $(".vAdminMenuAside .vButton");
            public static final SelenideElement categoryTitle = $(".vAdminMenuAside__title");
            public static final SelenideElement enableMenuForVisitorsContainer = $(".vAdminMenuVisitors");
            public static final SelenideElement enableMenuForVisitorsButton = $(".vAdminMenuVisitors__slider");
            public static final SelenideElement enableMenuForVisitorsInput = $(".vAdminMenuVisitors__switch input");

            public static final SelenideElement categoryNameContainer = $(".vAdminMenuCategoryShield");
            public static final SelenideElement categoryNameInGuest =
                    $(".vAdminMenuCategoryShield__el:nth-child(2) .vAdminMenuCategoryShield__el-name");

            public static final SelenideElement viewSwitcherContainer = $(".vAdminMenuViews");
            public static final SelenideElement menuTableHeader = $(".vAdmiMenuTable__header");




            public static final ElementsCollection menuDishItems=
                    $$(".vAdminMenuMainAside__body .vAdmiMenuTable__item");


            public static final ElementsCollection menuDishItemsEditButtons =
                    $$(".vAdmiMenuTable__item-gramm+.vAdmiMenuTable__item-btn");
            public static final ElementsCollection menuDishItemsEyeIcons =
                    $$(".vAdmiMenuTable__item-btn+.vAdmiMenuTable__item-btn .vAdminMenuBtn");
            public static final ElementsCollection menuDishItemsEyeIconsActive =
                    $$(".vAdmiMenuTable__item-btn+.vAdmiMenuTable__item-btn .vAdminMenuBtn.active");
            public static final ElementsCollection menuDishItemsEditNames =
                    $$(".vAdmiMenuTable__item-name+.vAdmiMenuTable__item-name");
            public static final ElementsCollection menuDishItemsNames =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img+.vAdmiMenuTable__item-name");
            public static final ElementsCollection menuDishItemsPrices =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-price");
            public static final ElementsCollection menuDishItemsImageContainer =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img");

            public static final ElementsCollection menuDishItemsImage =
                    $$(".vAdmiMenuTable__item .vAdmiMenuTable__item-img img");



            public static final SelenideElement editDishNameInput = $(".vAdmiMenuTable__input");
            public static final SelenideElement editDishNameOkButton =
                    $(".vAdmiMenuTable__ok");
            public static final SelenideElement dishPreloader =
                    $(".vSectionMenuAdmin__main-side .vLightPreloader");



            public static final SelenideElement helpAdminContainer = $(".vAdminMenuHelp__description");
            public static final SelenideElement showActivePositionsForGuestContainer =
                    $(".vAdminMenuAside__active");
            public static final SelenideElement showActivePositionsForGuestCheckbox = $("#showActive");



            public static final SelenideElement categoryDishContainer = $(".vAdminMenuAside__list");
            public static final ElementsCollection categoryDishItems =
                    $$("[class='vAdminMenuAside__category'] [class='vAdminMenuCategoryItem']");
            public static final ElementsCollection categoryDishItemsNames =
                    $$("[class='vAdminMenuAside__category'] [class='vAdminMenuCategoryItem'] .vAdminMenuCategoryItem__info-bottom");
            public static final ElementsCollection categoryDishItemsSize =
                    $$(".vAdminMenuCategoryItem__info-top");




            public static final ElementsCollection categoryDishEditButton =
                    $$(".vAdminMenuCategoryItem .vAdminMenuCategoryItem__btn:nth-child(2)");
            public static final ElementsCollection categoryDishEyeIcons =
                    $$("[class='vAdminMenuAside__category'] .vAdminMenuCategoryItem__btn:nth-child(3)");
            public static final SelenideElement editCategoryContainer = $(".vAdminMenuEditModal__content");
            public static final SelenideElement categoryNameForGuest = $("#ourName");
            public static final SelenideElement saveEditedCategoryNameButton =
                    $(".vAdminMenuEditModal__btn button");
            public static final SelenideElement cancelEditedCategoryNameButton =
                    $(".vAdminMenuEditModal__content .vButton--gray\n");
            public static final SelenideElement categoryNameInCashDesk = $(".vAdminMenuEditModal__name");

        }

    }

    public static class YandexMail {

        public static final SelenideElement yandexLogin = $("#passp-field-login");
        public static final SelenideElement enterByEmailButton = $("[data-type=\"login\"]");
        public static final SelenideElement signInButton = $("[id=\"passp:sign-in\"]");
        public static final SelenideElement yandexFormTitle = $("[class=\"passp-title\"]");
        public static final SelenideElement yandexTapperAccount =
                $x("//*[@class='AuthAccountListItem-block'][..//*[contains(text(),'autotests@tapper.cloud')]]");
        public static final SelenideElement yandexPassword = $("#passp-field-passwd");
        public static final SelenideElement tapperMail =
                $x("//*[@class='mail-MessageSnippet-Content'][.//*[contains(text(),'Вас приветствует команда Tapper')]]");
        public static final SelenideElement tapperMailCheckbox =
                $x("//*[@class='mail-MessageSnippet-Content'][.//*[contains(text(),'Вас приветствует команда Tapper')]]//*[@class='_nb-checkbox-flag _nb-checkbox-normal-flag']");
        public static final SelenideElement deleteMailButton = $("[accesskey='Delete']");
        public static final SelenideElement tapperConfirmAuthInMail = $("a[href=\"https://tapper.staging.zedform.ru/profile/\"]");
        public static final SelenideElement orderHeading =
                $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Кассовый чек']]");
        public static final SelenideElement successPaymentHeading =
                $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Успешная оплата']]");
        public static final SelenideElement dropdownListOrder =
                $(".mail-MessagesList div:first-child .mail-MessageSnippet-Content");

    }

}
