package total_personal_account_actions;

import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.appear;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_AUTHORIZATION_STAGE_URL;
import static data.Constants.TestData.AdminPersonalAccount.ADMIN_PROFILE_STAGE_URL;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.copyright;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.logoAtBottom;

public class AuthorizationPage extends BaseActions {


    @Step("Авторизуемся на под админом ресторана")
    public void authorizationUser(String login, String password) {

        openPage(ADMIN_AUTHORIZATION_STAGE_URL);
        forceWait(2000); // toDo не успевает прогрузиться
        isTextContainsInURL(ADMIN_AUTHORIZATION_STAGE_URL);
        isFormContainerCorrect();
        authorizeUser(login,password);
        isTextContainsInURL(ADMIN_PROFILE_STAGE_URL);
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

        emailInput.shouldBe(appear);
        sendKeys(emailInput,login);
        sendKeys(passwordInput, password);
        click(logInButton);

    }

}
