package constants.selectors;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AuthAndRegistrationPageSelectors {

    public static class AuthorizationPage {

        public static final SelenideElement welcomeHeading = $(".sign__container .sign__welcome");
        public static final SelenideElement titleHeading = $(".sign__container .sign__title");
        public static final SelenideElement emailInput = $("#email");
        public static final SelenideElement passwordInput = $("#password");
        public static final SelenideElement forgotPasswordLink = $(".sign__container .sign__forget-pass");
        public static final SelenideElement logInButton = $("a+.vButton");
        public static final SelenideElement registrationLink = $("a[href='/users/registration']");
        public static final SelenideElement errorMsgLoginOrPassword =
            $x("//*[contains(@class,'vLandingInput')]" +
                    "[.//*[@id=\"email\"]]//*[@class='vLandingInput__err']");

    }

    public static class RestorePage {

        public static final SelenideElement restoreEmailField = $("[id=\"email\"]");

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
}
