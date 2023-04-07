package total_personal_account_actions;

import api.ApiRKeeper;
import com.codeborne.selenide.SelenideElement;
import common.BaseActions;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static data.Constants.TestData.AdminPersonalAccount.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;
import static data.selectors.AuthAndRegistrationPage.RootTapperPage.signInButton;

public class AuthorizationPage extends BaseActions {

    ApiRKeeper apiRKeeper = new ApiRKeeper();

    @Step("Переход на страницу авторизации")
    public void goToAuthorizationPage() {

        openPage(ADMIN_AUTHORIZATION_STAGE_URL);
        isFormContainerCorrect();

    }

    @Step("Переход на страницу восстановления пароля")
    public void goToRestorePasswordFromRootPage() {

        openPage(ROOT_TAPPER_STAGE_URL);
        click(signInButton);
        click(forgotPasswordLink);
        isTextContainsInURL("users/forget");

    }
    @Step("Проверка страницы восстановления пароля")
    public void isRecoveryPasswordCorrect() {

        titleHeading.shouldHave(text("Восстановление пароля"));
        isElementVisible(signInLink);
        isElementVisible(recoveryButton);
        recoveryButton.shouldBe(disabled);
        isElementVisible(logoAtBottom);

    }

    @Step("Проверка страницы ввода нового пароля")
    public void isNewPasswordCorrect() {

        titleHeading.shouldHave(text("Новый пароль"));
        isElementVisible(passwordField);
        isElementVisible(passwordConfirmationField);
        isElementVisible(saveButton);
        isElementVisible(logoAtBottom);

    }

    @Step("Проверка на ошибку не существующего в базе емайла")
    public void isMailErrorCorrect(String email, SelenideElement elementError) {

        clearText(emailInput);
        sendKeys(emailInput,email);

        if (elementError == nonExistingEmailInputError)
            click(applyButton);

        isElementVisible(elementError);

    }

    @Step("Ввод нового пароля и сохранение")
    public void setNewPassword(String password) {

        sendKeys(passwordField,password);
        sendKeys(passwordConfirmationField,password);
        applyButton.shouldBe(enabled).click();
        successRegistrationModal.shouldBe(visible);
        welcomeHeading.shouldBe(visible,Duration.ofSeconds(10));

    }


    @Step("Заполняем пароль и отправляем заявку на восстановление пароля")
    public void recoverPassword(String email) {

        clearText(emailInput);
        sendKeys(emailInput,email);
        click(recoveryButton);

        successRegistrationModal.shouldBe(visible,Duration.ofSeconds(10));
        successRegistrationModalDescription.shouldBe(visible);
        isImageCorrect(successRegistrationImageSelector,"Иконка успешной регистрации некорректна");

        isElementVisibleDuringLongTime(welcomeHeading,10);
        isElementVisible(titleHeading);

    }

    @Step("Авторизуемся под админом ресторана")
    public void authorizationUser(String login, String password) {

        goToAuthorizationPage();
        authorizeUser(login, password);
        isTextContainsInURL(ADMIN_PROFILE_STAGE_URL);

    }

    @Step("Авторизуемся под админом ресторана")
    public void authorizationByToken(String url,String login, String password) {

        apiRKeeper.authorizeInPersonalAccount(url, apiRKeeper.rqBodyLoginPersonalAccount(login,password));

    }

    @Step("Проверка отображения всех элементов на странице авторизации официанта")
    public void isFormContainerCorrect() {

        welcomeHeading.shouldBe(visible,Duration.ofSeconds(40));
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

        emailInput.shouldBe(visible,interactable);
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

    }

    @Step("Из главной страницы переходим в форму регистрации")
    public void goToRegistrationPageFromRoot() {

        openPage(ROOT_TAPPER_STAGE_URL);
        isElementVisibleAndClickable(signInButton);
        click(signInButton);
        click(registrationLink);
        isTextContainsInURL(ADMIN_REGISTRATION_STAGE_URL);

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
