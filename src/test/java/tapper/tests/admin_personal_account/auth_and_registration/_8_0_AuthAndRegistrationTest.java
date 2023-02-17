package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.RegistrationPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.TapperTable.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;


@Order(80)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Авторизация, все кейсы")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_0_AuthAndRegistrationTest extends BaseTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    RegistrationPage registrationPage = new RegistrationPage();


    @Test
    @DisplayName("1.0. Переход на страницу авторизации с корневой страницы")
    public void goToAuthPageFromRoot() {

        authorizationPage.goToAuthPageFromRoot();

    }

    @Test
    @DisplayName("1.1. Ввод некорректный данных в емейл и пароль, проверка всех типов ошибок и полей")
    public void checkInputError() {

        authorizationPage.checkInputError(emailInput, WRONG_EMAIL_PATTERN, wrongEmailError);
        authorizationPage.checkInputError(emailInput, EMPTY_LOGIN, emptyEmailInputError);
        authorizationPage.checkInputError(passwordInput, SHORT_PASSWORD, passwordError);

    }

    @Test
    @DisplayName("1.2. Ввод данных не зарегистрированного пользователя")
    public void checkNonExistingUserData() {

        authorizationPage.checkNonExistingUserData(NON_EXISTING_EMAIL, NON_EXISTING_PASSWORD);

    }

    @Test
    @DisplayName("1.4. Проверка раскрытия пароля по значку глаза")
    public void isPasswordEyeIconCorrect() {

        authorizationPage.isPasswordEyeIconCorrect(NON_EXISTING_PASSWORD);

    }

    @Test
    @DisplayName("1.5. Клик по логотипу в подвале и переход на корневую страницу")
    public void clickOnLogo() {

        authorizationPage.clickOnLogo();

    }

    @Test
    @DisplayName("1.6. Из главной страницы переходим в регистрации")
    public void goToRegistrationPageFromRoot() {

        authorizationPage.goToRegistrationPageFromRoot();
        registrationPage.isFormContainerCorrect();

    }

    @Test
    @DisplayName("1.7. Проверка политики конфиденциальности")
    public void isConfPolicyCorrect() {

        registrationPage.isConfPolicyCorrect();

    }

}
