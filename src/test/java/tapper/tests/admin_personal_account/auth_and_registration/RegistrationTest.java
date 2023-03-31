package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.RegistrationPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.PersonalAccountTest;



@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Регистрация администратора ресторана")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationTest extends PersonalAccountTest {
    RegistrationPage registrationPage = new RegistrationPage();

    @Test
    @Order(1)
    @DisplayName("Переход на регистрации")
    void goToAuthPageFromRoot() {

        registrationPage.goToRegistrationPage();

    }
    @Test
    @Order(2)
    @DisplayName("Проверка всех элементов регистрации")
    void isRegistrationFormCorrect() {

        registrationPage.isRegistrationFormCorrect();

    }
    @Test
    @Order(3)
    @DisplayName("Заполняем всем поля для регистрации")
    void typeDataInFields() {

        registrationPage.fillRegistrationForm();
        registrationPage.isReadyForRegistration();

    }

}
