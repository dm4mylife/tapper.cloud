package tapper_admin;

import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;


import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.SelectorsTapperAdmin.AuthorizationPage.*;
import static constants.SelectorsTapperAdmin.RegistrationPage.copyright;
import static constants.SelectorsTapperAdmin.RegistrationPage.logoAtBottom;

public class AuthorizationPage extends BaseActions {

    @Test
    @Step("Авторизуемся на под админом ресторана")
    public void authorizationUser(String login, String password) {

        isFormContainerCorrect();
        authorizeUser(login,password);
        isTextContainsInURL(R_KEEPER_ADMIN_PROFILE_STAGE_URL);

    }

    @Step("Проверка отображения всех элементов на странице авторизации официанта")
    public void isFormContainerCorrect() {

        isElementVisible(welcomeHeading);
        isElementVisible(titleHeading);
        isElementVisible(emailInput);
        isElementVisible(passwordInput);
        isElementVisible(forgotPasswordLink);
        isElementVisible(logInButton);
        isElementVisible(registrationLink);
        isElementVisible(logoAtBottom);
        isElementVisible(copyright);

    }

    @Step("Авторизация пользователя")
    public void authorizeUser(String login, String password) {

        sendKeys(emailInput,login);
        sendKeys(passwordInput, password);
        click(logInButton);

    }

    @Step("Проверяем что авторизация полностью успешна")
    public void gg() {


    }

}
