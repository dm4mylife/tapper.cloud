package admin_personal_account;

import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;


public class RegistrationPage extends BaseActions {

    @Step("Проверка отображения всех элементов на странице регистрации администратора ресторана")
    public void isFormContainerCorrect() {

        isElementVisible(signInField);
        isElementVisible(signInLink);
        isElementVisible(nameField);
        isElementVisible(phoneField);
        isElementVisible(emailField);
        isElementVisible(passwordField);
        isElementVisible(passwordConfirmationField);
        isElementVisible(restaurantNameField);
        isElementVisible(captchaContainer);
        isElementVisible(registrationButton);
        isElementVisible(confPolicyField);
        isElementVisible(confPolicyLink);
        isElementVisible(logoAtBottom);
        isElementVisible(copyright);
        registrationButton.shouldBe(disabled);

    }

    @Step("Проверяем политику конфиденциальности на странице авторизации")
    public void isConfPolicyCorrect() {

        click(confPolicyLink);
        confPolicyContainer.shouldHave(attribute("style",""));

    }

}
