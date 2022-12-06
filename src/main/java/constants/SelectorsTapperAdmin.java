package constants;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SelectorsTapperAdmin {

    public static class AuthorizationPage {


        public static final SelenideElement email = $("#email");
        public static final SelenideElement password = $("#password");
        public static final SelenideElement logInButton = $("a+.vButton");
        public static final SelenideElement registrationLink = $("a[href='/users/registration']");

    }


}
