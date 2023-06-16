package data.selectors;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.*;

public class AuthAndRegistrationPage {

    public static class RootTapperPage {

        public static final SelenideElement signInButton = $(".vLandingHeader__login-link");

    }

    public static class AuthorizationPage {

        public static final SelenideElement pagePreloader = $(".vPreloader");
        public static final SelenideElement welcomeHeading = $(".sign__container .sign__welcome");
        public static final SelenideElement titleHeading = $(".sign__container .sign__title");
        public static final SelenideElement emailInput = $("#email");
        public static final SelenideElement wrongEmailError =
                $x("//*[@class='vLandingInput err']/*[@class='vLandingInput__err'" +
                        " and text()=' Введите корректный E-mail']");
        public static final SelenideElement notExistingEmailError =
                $x("//*[@class='vLandingInput err']/*[@class='vLandingInput__err'" +
                        " and text()='Неверный E-mail или пароль ']");
        public static final SelenideElement emptyEmailInputError =
                $x("//*[@class='vLandingInput err']/*[@class='vLandingInput__err'" +
                        " and text()=' Поле не может быть пустым!']");

        public static final SelenideElement nonExistingEmailInputError =
                $x("//*[@class='vLandingInput err']/*[@class='vLandingInput__err' " +
                        "and text()='Такого емейл не существует! ']");
        public static final SelenideElement passwordInput = $("#password");
        public static final SelenideElement passwordError =
                $x("//*[@class='vLandingInput err']/*[@class='vLandingInput__err'" +
                        " and text()=' Введите минимум 6 символов']");
        public static final SelenideElement passwordEyeIcon =
                $x("//*[@class='vLandingInput__wrapper']/*[name()='svg']");
        public static final SelenideElement forgotPasswordLink = $(".sign__container .sign__forget-pass");
        public static final SelenideElement logInButton = $("a+.vButton");
        public static final SelenideElement registrationLink = $("a[href='/users/registration']");
        public static final SelenideElement errorMsgLoginOrPassword =
                $x("//*[contains(@class,'vLandingInput')]" +
                        "[.//*[@id=\"email\"]]//*[@class='vLandingInput__err']");

    }

    public static class RegistrationPage {

        public static final SelenideElement signInField = $(".sign__container p.sign__text");
        public static final SelenideElement signInLink = $(".sign__container p.sign__text a");
        public static final SelenideElement nameField = $("#name");
        public static final SelenideElement phoneField = $("#phone");
        public static final SelenideElement emailField = $("#email");
        public static final SelenideElement emailFieldError = $("[class='vLandingInput err']");
        public static final SelenideElement passwordField = $("#password");
        public static final SelenideElement passwordConfirmationField = $("#confirmation");
        public static final SelenideElement restaurantNameField = $("#name_shop");
        public static final SelenideElement applyButton = $(".vButton[type=\"submit\"]");
        public static final SelenideElement successRegistrationModal = $(".vModalSuccess");
        public static final SelenideElement successRegistrationModalDescription =
                $(".vModalSuccess .vModalSuccess__description");
        public static final String successRegistrationImageSelector = ".vModalSuccess img";
        public static final SelenideElement confPolicyFieldError = $(".vLandingFeedback__error-text");
        public static final SelenideElement confPolicyInput = $(".sign__conditions input");
        public static final ElementsCollection allNecessaryInputsForFilling =
                $$(".sign__container .vLandingInput__wrapper");
        public static final SelenideElement confPolicyField = $(".sign__conditions");
        public static final SelenideElement confPolicyLink = $(".sign__conditions a");
        public static final SelenideElement logoAtBottom = $("a[href='/']");
        public static final SelenideElement copyright = $(".sign__copyright");
        public static final SelenideElement confPolicyContainer = $(".vLandingPoliticModal");
        public static final SelenideElement recoveryButton = applyButton;
        public static final SelenideElement saveButton = applyButton;

    }

}
