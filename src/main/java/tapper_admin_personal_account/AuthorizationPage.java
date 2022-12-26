package tapper_admin_personal_account;

import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;


import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.selectors.AuthAndRegistrationPageSelectors.AuthorizationPage.*;
import static constants.selectors.AuthAndRegistrationPageSelectors.RegistrationPage.copyright;
import static constants.selectors.AuthAndRegistrationPageSelectors.RegistrationPage.logoAtBottom;

public class AuthorizationPage extends BaseActions {

    @Test
    @Step("Авторизуемся на под админом ресторана")
    public void authorizationUser(String login, String password) {

        openPage(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        forceWait(2000); // toDo не успевает прогрузиться
        isTextContainsInURL(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        isFormContainerCorrect();
        authorizeUser(login,password);
        isTextContainsInURL(R_KEEPER_ADMIN_PROFILE_STAGE_URL);
        forceWait(2000); // toDo не успевает прогрузиться

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

}
