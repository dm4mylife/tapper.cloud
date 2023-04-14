package tapper.tests.admin_personal_account.auth_and_registration;

import admin_personal_account.AdminAccount;
import admin_personal_account.RegistrationPage;
import api.ApiRKeeper;
import api.MailByApi;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tapper_table.RootPage;
import tapper_table.YandexPage;
import tests.PersonalAccountTest;
import total_personal_account_actions.AuthorizationPage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;

import static data.Constants.ADMIN_REGISTRATION_EMAIL;
import static data.Constants.RESTORE_PASSWORD_REGISTRATION_EMAIL;
import static data.Constants.TestData.TapperTable.*;
import static data.Constants.TestData.Yandex.*;
import static data.selectors.AuthAndRegistrationPage.AuthorizationPage.*;


@Epic("Личный кабинет администратора ресторана")
@Feature("Авторизация\\регистрация")
@DisplayName("Восстановление пароля")

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestorePasswordTest extends PersonalAccountTest {

    static HashMap<String,String> registrationData = new HashMap<>();
    static HashMap<String,String> waiterData = new HashMap<>();
    RegistrationPage registrationPage = new RegistrationPage();
    ApiRKeeper apiRKeeper = new ApiRKeeper();
    RootPage rootPage = new RootPage();

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
    void yandexAuthorization() throws MessagingException, IOException {

        waiterData = MailByApi.getMailData("administrator.yandex.mail", "administrator.yandex.password",
                RESTORE_PASSWORD_REGISTRATION_EMAIL);

    }

    @Test
    @Order(6)
    @DisplayName("Проверка страницы нового пароля")
    void isNewPasswordCorrect() {

        rootPage.openPage(waiterData.get("url"));
        authorizationPage.isNewPasswordCorrect();

    }
    @Test
    @Order(7)
    @DisplayName("Сохранение нового пароля")
    void setNewPassword() {

        authorizationPage.setNewPassword(ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

    }

    @Test
    @Order(8)
    @DisplayName("Проверка авторизации с новым паролем")
    void authorizationUser() {

        authorizationPage.authorizationUser(ADMIN_RESTAURANT_TEST_LOGIN_EMAIL,ADMIN_RESTAURANT_TEST_PASSWORD_MAIL);

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

        MailByApi.deleteMailsByApi("administrator.yandex.mail", "administrator.yandex.password",
                RESTORE_PASSWORD_REGISTRATION_EMAIL);

    }

}
