package total_personal_account_actions;

import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.Constants.WAIT_FOR_FULL_LOAD_PAGE;
import static data.Constants.WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.copyright;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.logoAtBottom;
import static data.selectors.AuthAndRegistrationPage.RootTapperPage.signInButton;

public class AuthorizationPage extends BaseActions {


    @Step("Авторизуемся на под админом ресторана")
    public void authorizationUser(String login, String password) {

        openPage(ADMIN_AUTHORIZATION_STAGE_URL);
        forceWait(WAIT_FOR_FULL_LOAD_PAGE); // toDo не успевает прогрузиться
        isFormContainerCorrect();
        authorizeUser(login, password);
        isTextContainsInURL(ADMIN_PROFILE_STAGE_URL);
        forceWait(WAIT_FOR_FULL_LOAD_PAGE); // toDo не успевает прогрузиться

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
        forceWait(WAIT_FOR_INPUT_IS_FULL_LOAD_ON_PAGE);
        sendKeys(emailInput, login);
        sendKeys(passwordInput, password);
        click(logInButton);

    }

    @Step("Авторизовываемся в админке через главную страницу и кнопку Войти")
    public void goToAuthPageFromRoot() {

        openPage(ROOT_TAPPER_STAGE_URL);
        isElementVisibleAndClickable(signInButton);
        click(signInButton);
        isTextContainsInURL(ADMIN_AUTHORIZATION_STAGE_URL);
        forceWait(WAIT_FOR_FULL_LOAD_PAGE);

    }

    @Step("Из главной страницы переходим в форму регистрации")
    public void goToRegistrationPageFromRoot() {

        openPage(ROOT_TAPPER_STAGE_URL);
        isElementVisibleAndClickable(signInButton);
        click(signInButton);
        click(registrationLink);
        isTextContainsInURL(ADMIN_REGISTRATION_STAGE_URL);
        forceWait(WAIT_FOR_FULL_LOAD_PAGE);

    }

    @Step("Проверка формы ввода ({input}) и её ошибки при негативном тесте")
    public void checkInputError(SelenideElement input, String inputText, SelenideElement errorElement) {

        clearText(input);

        sendKeys(input, inputText);
        errorElement.shouldBe(visible);
        logInButton.shouldBe(disabled);

        clearText(input);

    }

    @Step("Проверка раскрытие пароля через иконку глаза")
    public void isPasswordEyeIconCorrect(String password) {

        clearText(passwordInput);
        sendKeys(passwordInput, password);
        forceWait(1000);
        passwordError.shouldNotBe(visible);
        passwordInput.shouldHave(attribute("type", "password"));
        click(passwordEyeIcon);

        passwordInput.shouldHave(attribute("type", "text"));

        clearText(passwordInput);

    }

    @Step("Ввод несуществующего емейла и логина")
    public void checkNonExistingUserData(String login, String password) {

        clearText(emailInput);
        clearText(passwordInput);

        sendKeys(emailInput, login);
        sendKeys(passwordInput, password);

        click(logInButton);

        isElementVisible(notExistingEmailError);

    }

    @Step("Клик по логу и переход на главную страницу")
    public void clickOnLogo() {

        click(logoAtBottom);
        isTextContainsInURL(ROOT_TAPPER_STAGE_URL);

    }

}
