package tapper_admin;

import common.BaseActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;


import static constants.Constant.TestDataRKeeperAdmin.*;
import static constants.TapperAdminSelectors.AuthorizationPage.*;
import static constants.TapperAdminSelectors.RegistrationPage.copyright;
import static constants.TapperAdminSelectors.RegistrationPage.logoAtBottom;

public class AuthorizationPage extends BaseActions {

    @Test
    @Step("Авторизуемся на под админом ресторана")
    public void authorizationUser(String login, String password) {

        openPage(R_KEEPER_ADMIN_AUTHORISATION_STAGE_URL);
        forceWait(1000); // toDo не успевает прогрузиться
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
