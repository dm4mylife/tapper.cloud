package admin_personal_account;

import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.checked;
import static data.Constants.TestData.Yandex.TEST_YANDEX_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_LOGIN_EMAIL;
import static data.Constants.TestData.SupportPersonalAccount.SUPPORT_PASSWORD;
import static data.selectors.AdminPersonalAccount.Common;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;


public class RegistrationPage extends BaseActions {

    @Step("Проверка отображения всех элементов на странице регистрации официанта")
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

    }

    @Step("Ввод всех обязательных полей для регистрации")
    public void typeAllUserInfo() {

        nameField.sendKeys("test");
        phoneField.sendKeys("0000000000");
        emailField.sendKeys(TEST_YANDEX_LOGIN_EMAIL);
        passwordField.sendKeys("123123");
        passwordConfirmationField.sendKeys("123123");
        registrationButton.click();

    }

    @Step("Проверка что в админке ресторана отключены чаевые")
    public void isTipsDisabledInAdmin() { // toDo доделать. Пока апи нет, как костыль написан

        sendHumanKeys(emailInput, SUPPORT_LOGIN_EMAIL);
        sendHumanKeys(passwordInput, SUPPORT_PASSWORD);
        click(registrationButton);
        isElementVisibleDuringLongTime(Common.configuration, 5);
        click(Common.configuration);
        isElementVisibleDuringLongTime(Common.optionTabTips, 5);
        click(Common.optionTabTips);
        Common.tipsDisabled.shouldHave(checked);

    }

    @Step("Восстановление пароля")
    public void restoreWaiterPassword() {

        click(forgotPasswordLink);



    }



}
