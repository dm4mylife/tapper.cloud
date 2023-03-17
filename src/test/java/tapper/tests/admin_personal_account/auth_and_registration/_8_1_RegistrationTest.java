package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.RegistrationPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.AdminBaseTest;
import tests.BaseTest;
import total_personal_account_actions.AuthorizationPage;

import static data.Constants.TestData.TapperTable.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;


@Order(81)
@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Регистрация администратора ресторана")

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class _8_1_RegistrationTest extends AdminBaseTest {
    RegistrationPage registrationPage = new RegistrationPage();

    @Test
    @DisplayName("1.0. Переход на регистрации")
    public void goToAuthPageFromRoot() {

        registrationPage.goToRegistrationPage();

    }
    @Test
    @DisplayName("1.1. Проверка всех элементов регистрации")
    public void isRegistrationFormCorrect() {

        registrationPage.isRegistrationFormCorrect();

    }
    @Test
    @DisplayName("1.2. Заполняем всем поля для регистрации")
    public void typeDataInFields() {

        registrationPage.fillRegistrationForm();
        registrationPage.isReadyForRegistration();

    }

}
