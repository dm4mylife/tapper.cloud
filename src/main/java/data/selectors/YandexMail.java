package data.selectors;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class YandexMail {

        public static final SelenideElement yandexLogin = $("#passp-field-login");
        public static final SelenideElement enterByEmailButton = $("[data-type=\"login\"]");
        public static final SelenideElement signInButton = $("[id=\"passp:sign-in\"]");
        public static final SelenideElement yandexFormTitle = $("[class=\"passp-title\"]");
        public static final SelenideElement yandexTapperAccount =
            $x("//*[@class='AuthAccountListItem-block'][..//*[contains(text(),'tapper.cloud')]]");
        public static final SelenideElement yandexPassword = $("#passp-field-passwd");
        public static final SelenideElement skipAddReservePassportContainer = $("[data-t=\"email_skip\"]");
        public static final SelenideElement skipAddReserveEmail = $("h1.passp-title");


        public static final SelenideElement skipButton = $("[data-t='email_skip']>button");
        public static final SelenideElement loadPhotoProfileContainer = $(".passp-auth-content");
        public static final SelenideElement skipLoadPhotoProfileButton = $(".registration__avatar-btn a");


        public static final SelenideElement tapperMail =
            $x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вас приветствует команда Tapper')]]");
        public static final SelenideElement tapperMailCheckbox =
            $x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вас приветствует команда Tapper')]]" +
                    "//*[@class='_nb-checkbox-flag _nb-checkbox-normal-flag']");
        public static final SelenideElement deleteMailButton = $("[accesskey='Delete']");
        public static final SelenideElement tapperConfirmAuthInMail =
            $x("//a[contains(text(),'Перейти к авторизации')]");
        public static final SelenideElement orderHeading =
            $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Кассовый чек']]");
        public static final SelenideElement successPaymentHeading =
            $x("//*[contains(@class,'mail-MessagesList')]/div[.//span[text()='Успешная оплата']]");
        public static final SelenideElement dropdownListOrder =
            $(".mail-MessagesList div:first-child .mail-MessageSnippet-Content");

}
