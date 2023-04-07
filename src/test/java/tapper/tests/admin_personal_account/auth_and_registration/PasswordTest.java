package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.AdminAccount;
import admin_personal_account.RegistrationPage;
import api.ApiRKeeper;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import java.util.HashMap;

import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.Yandex.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;
import static data.selectors.YandexMail.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Восстановление пароля")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PasswordTest extends PersonalAccountTest {

    static HashMap<String,String> registrationData = new HashMap<>();

    RegistrationPage registrationPage = new RegistrationPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();

    YandexPage yandexPage = new YandexPage();
    AdminAccount adminAccount = new AdminAccount();
    AuthorizationPage authorizationPage = new AuthorizationPage();
    @Test
    @Order(1)
    @DisplayName("Создание новой учетной записи администратора ресторана")
    void goToAuthPageFromRoot() {

        apiRKeeper.deleteAdmin(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);
        registrationPage.goToRegistrationPage();
        registrationPage.isRegistrationFormCorrect();
        registrationData = registrationPage.fillRegistrationForm(false);
        registrationPage.registrationAdministrator(registrationData);
        adminAccount.logOut();

        Selenide.refresh(); //toDo убрать эту строку когда пофиксят баг, когда после регистрации у тебя остаётся модальное окно успешной регистрации

    }
    @Test
    @Order(2)
    @DisplayName("Переход на страницу восстановления пароля и проверка всех элементов регистрации")
    void isRegistrationFormCorrect() {

        authorizationPage.goToRestorePasswordFromRootPage();
        authorizationPage.isRecoveryPasswordCorrect();

    }
    @Test
    @Order(3)
    @DisplayName("Проверяем поле емайла на различные ошибки")
    void fillRegistrationForm() {

        authorizationPage.isMailErrorCorrect(" ", emptyEmailInputError);
        authorizationPage.isMailErrorCorrect(NON_EXISTING_EMAIL,nonExistingEmailInputError);
        authorizationPage.isMailErrorCorrect(NON_EXISTING_PASSWORD,wrongEmailError);



    }
    @Test
    @Order(4)
    @DisplayName("Восстанавливаем пароль")
    void isReadyForRegistration() {

        authorizationPage.recoverPassword(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL);

    }

    @Test
    @Order(5)
    @DisplayName("Авторизация в почте яндекса")
    void yandexAuthorization() {

        yandexPage.yandexAuthorization(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка письма по восстановлению пароля и переход по ссылке и " +
            "Переход на авторизацию из письма по созданию нового пароля")
    void checkRecoverMail() {

        yandexPage.checkRecoverMail();

    }

    @Test
    @Order(7)
    @DisplayName("Проверка страницы нового пароля")
    void isNewPasswordCorrect() {

       authorizationPage.isNewPasswordCorrect();

    }

    @Test
    @Order(8)
    @DisplayName("Сохранение нового пароля")
    void setNewPassword() {

        authorizationPage.setNewPassword(ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }

    @Test
    @Order(9)
    @DisplayName("Проверка авторизации с новым паролем")
    void authorizationUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL,ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }

    @Test
    @Order(10)
    @DisplayName("Удаляем учетную запись администратора ресторана")
    void deleteAccount() {

        apiRKeeper.deleteAdmin(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }
    @Test
    @Order(11)
    @DisplayName("Удаляем письмо на почте Яндекса")
    void deleteYandexInviteMail() {

        yandexPage.deleteMail
                (ADMIN_RESTAURANT_TEST_LOGIN_EMAIL, ADMIN_RESTAURANT_TEST_PASSWORD_MAIL,
                        recoveryMailCheckbox,recoveryMail);

    }

}
