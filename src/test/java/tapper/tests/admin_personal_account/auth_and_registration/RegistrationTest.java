package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.AdminAccount;
import admin_personal_account.RegistrationPage;
import api.ApiRKeeper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static data.Constants.TestData.RegistrationData.EXISTING_EMAIL_ERROR_TEXT;
import static data.Constants.TestData.Yandex.*;
import static data.selectors.AuthAndRegistrationPage.RegistrationPage.*;
import static data.selectors.YandexMail.tapperMail;
import static data.selectors.YandexMail.tapperMailCheckbox;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Регистрация администратора ресторана")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegistrationTest extends PersonalAccountTest {

    static HashMap<String,String> registrationData = new HashMap<>();
    static String password;
    RegistrationPage registrationPage = new RegistrationPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();
    YandexPage yandexPage = new YandexPage();
    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    @Test
    @Order(1)
    @DisplayName("Переход на регистрации")
    void goToAuthPageFromRoot() {

        apiRKeeper.deleteAdmin(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

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
    void fillRegistrationForm() {

        registrationData = registrationPage.fillRegistrationForm(false);

    }

    @Test
    @Order(4)
    @DisplayName("Проверяем что нельзя зарегистрироваться без политики соглашений")
    void isConfPolicyCorrectWhenDeactivated() {

        registrationPage.isConfPolicyCorrectWhenDeactivated();

    }

    @Test
    @Order(5)
    @DisplayName("Регистрируемся")
    void isReadyForRegistration() {

        registrationPage.registrationAdministrator(registrationData);
        adminAccount.logOut();

    }

    @Test
    @Order(6)
    @DisplayName("Проверяем ошибку существующей почты")
    void existingErrorEmail() {

        goToAuthPageFromRoot();
        isRegistrationFormCorrect();
        registrationPage.fillRegistrationForm(true);
        rootPage.click(applyButton);
        emailFieldError.shouldHave(Condition.matchText(EXISTING_EMAIL_ERROR_TEXT));

    }

    @Test
    @Order(7)
    @DisplayName("Авторизация в почте яндекса")
    void yandexAuthorization() {

        yandexPage.yandexAuthorization(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }

    @Test
    @Order(8)
    @DisplayName("Проверка письма по регистрации на почте")
    void checkInvitationMail() {

        password = yandexPage.checkTapperMail();

    }

    @Test
    @Order(9)
    @DisplayName("Удаляем учетную запись администратора ресторана")
    void deleteAccount() {

        apiRKeeper.deleteAdmin(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }
    @Test
    @Order(10)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        yandexPage.deleteMail
                (ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL,tapperMailCheckbox,tapperMail);

    }

}
