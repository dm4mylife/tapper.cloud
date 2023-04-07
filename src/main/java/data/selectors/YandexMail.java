package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

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
    public static final SelenideElement enteredEarlierLogin = $("a.CurrentAccount");
    public static final SelenideElement attachPhoto = $("[aria-label=\"Выбрать фото\"]");
    public static final SelenideElement skipButton = $("[data-t='email_skip']>button");
    public static final SelenideElement skipButtonWhenAddPhoto = $(".registration__avatar-btn span");
    public static final SelenideElement tapperMail =
            $x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вас приветствует команда Tapper')]]");

    public static final ElementsCollection tapperMailCheckbox =
            $$x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вас приветствует команда Tapper')]]" +
                    "//*[@class='_nb-checkbox-flag _nb-checkbox-normal-flag']");

    public static final SelenideElement tapperConfirmAuthInMail =
            $x("//a[contains(text(),'Перейти к авторизации')] | " +
                    "//span[contains(text(),'Ссылка на ваш профиль')]/following-sibling::a[1]");

    public static final SelenideElement recoveryMail =
            $x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вы отправили запрос на')]]");
    public static final SelenideElement recoveryMailLinkInMail =
            $x("//td[contains(text(),'Вы можете задать новый пароль')]/a");


    public static final ElementsCollection recoveryMailCheckbox =
            $$x("//*[@class='mail-MessageSnippet-Content']" +
                    "[.//*[contains(text(),'Вы можете задать новый пароль')]]" +
                    "//*[@class='_nb-checkbox-flag _nb-checkbox-normal-flag']");

    public static final SelenideElement deleteMailButton = $("[accesskey='Delete']");

}
