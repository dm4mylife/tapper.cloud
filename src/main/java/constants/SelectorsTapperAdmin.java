package constants;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class SelectorsTapperAdmin {

    public static class AuthorizationPage {

        public static final SelenideElement welcomeHeading = $(".sign__container .sign__welcome");
        public static final SelenideElement titleHeading = $(".sign__container .sign__title");
        public static final SelenideElement emailInput = $("#email");
        public static final SelenideElement passwordInput = $("#password");
        public static final SelenideElement forgotPasswordLink = $(".sign__container .sign__forget-pass");
        public static final SelenideElement logInButton = $("a+.vButton");
        public static final SelenideElement registrationLink = $("a[href='/users/registration']");

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

        public static final SelenideElement authPassword = $x("//td[*[contains(text(),'Пароль')]]");

        public static class WaiterMenu {

            public static final SelenideElement waiterHeading = $(".vProfileHeader__breadcrumbs-item");
            public static final SelenideElement waiterContainer = $(".vSectionWaitersection__container");
            public static final SelenideElement searchField = $(".employeeSearch #search");
            public static final SelenideElement searchError = $(".vSectionWaitersection__empty-search");
            public static final SelenideElement searchResetButton = $(".vSectionWaitersection__top .employeeSearch__btn-reset");
            public static final ElementsCollection waiterList = $$(".vSectionWaitersection__container .vWaiterItem");
            public static final SelenideElement waiterCardName = $(".vSectionWaitersection__container .vWaiterItem .vWaiterItem__waiterName");
            public static final SelenideElement inviteButton = $(".vSectionWaitersection__container .vButton");
            public static final SelenideElement cancelInvitationButton = $(".vSectionWaitersection__container .vButtonRed");
            public static final SelenideElement enterEmailField = $(".section-profile__group #email");
            public static final SelenideElement backToPreviousPage = $(".vSectionWaitersection__back");
            public static final SelenideElement waiterStatusInCard = $(".vSectionWaitersection__container .waiter-status");
            public static final SelenideElement waiterStatusInPreview = $(".vSectionWaitersection__container .vWaiterItem__waiterStatus");

            public static final SelenideElement waiterNameInCashDesk = $(".section-profile__group #name");
            public static final SelenideElement waiterName = $(".section-profile__group #display_name");
            public static final SelenideElement waiterEmail = $(".section-profile__group #email");
            public static final SelenideElement successSendingInvitation = $(".vAuthentication.active");

        }

        public static final SelenideElement configuration =
                $(".VMenuProfileLink:nth-of-type(2) .VMenuProfileLink__clickArea");
        public static final SelenideElement optionTabTips =
                $x("//*[@class='logsPage-tabs__btn'][text()='Чаевые']");
        public static final SelenideElement tipsDisabled = $("#DISABLED");

    }

    public static class YandexMail {

        public static final SelenideElement yandexLogin = $("#passp-field-login");
        public static final SelenideElement signInButton = $("[id=\"passp:sign-in\"]");
        public static final SelenideElement yandexPassword = $("#passp-field-passwd");
        public static final SelenideElement tapperMail =
                $x("//*[@class='mail-MessageSnippet-Content'][.//*[contains(text(),'Вас приветствует команда Tapper')]]");
        public static final SelenideElement tapperMailCheckbox =
                $x("//*[@class='mail-MessageSnippet-Content'][.//*[contains(text(),'Вас приветствует команда Tapper')]]//*[@class='_nb-checkbox-flag _nb-checkbox-normal-flag']");
        public static final SelenideElement deleteMailButton = $("[accesskey='Delete']");






        public static final SelenideElement tapperConfirmAuthInMail = $("a[href=\"https://tapper.staging.zedform.ru/profile/\"]");



        public static final SelenideElement orderHeading =
                $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Кассовый чек']]");
        public static final SelenideElement succesPaymentHeading =
                $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Успешная оплата']]");
        public static final SelenideElement dropdownListOrder =
                $(".mail-MessagesList div:first-child .mail-MessageSnippet-Content");

    }


}
