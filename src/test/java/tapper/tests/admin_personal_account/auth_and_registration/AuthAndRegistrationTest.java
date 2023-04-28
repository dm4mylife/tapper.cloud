package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.RegistrationPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.TapperTable.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;



@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@Story("Авторизация")
@DisplayName("Авторизация, все кейсы")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthAndRegistrationTest extends PersonalAccountTest {

    AuthorizationPage authorizationPage = new AuthorizationPage();
    RegistrationPage registrationPage = new RegistrationPage();


    @Test
    @Order(1)
    @DisplayName("Переход на страницу авторизации с корневой страницы")
    void goToAuthPageFromRoot() {

        authorizationPage.goToAuthPageFromRoot();

    }

    @Test
    @Order(2)
    @DisplayName("Ввод некорректный данных в емейл и пароль, проверка всех типов ошибок и полей")
    void checkInputError() {

        authorizationPage.checkInputError(emailInput, WRONG_EMAIL_PATTERN, wrongEmailError);
        authorizationPage.checkInputError(emailInput, EMPTY_LOGIN, emptyEmailInputError);
        authorizationPage.checkInputError(passwordInput, SHORT_PASSWORD, passwordError);

    }

    @Test
    @Order(3)
    @DisplayName("Ввод данных не зарегистрированного пользователя")
    void checkNonExistingUserData() {

        authorizationPage.checkNonExistingUserData(NON_EXISTING_EMAIL, NON_EXISTING_PASSWORD);

    }

    @Test
    @Order(4)
    @DisplayName("Проверка раскрытия пароля по значку глаза")
    void isPasswordEyeIconCorrect() {

        authorizationPage.isPasswordEyeIconCorrect(NON_EXISTING_PASSWORD);

    }

    @Test
    @Order(5)
    @DisplayName("Клик по логотипу в подвале и переход на корневую страницу")
    void clickOnLogo() {

        authorizationPage.clickOnLogo();

    }

    @Test
    @Order(6)
    @DisplayName("Из главной страницы переходим в регистрации")
    void goToRegistrationPageFromRoot() {

        authorizationPage.goToRegistrationPageFromRoot();
        registrationPage.isRegistrationFormCorrect();

    }

    @Test
    @Order(7)
    @DisplayName("Проверка политики конфиденциальности")
    void isConfPolicyCorrect() {

        registrationPage.isConfPolicyCorrect();

    }

}
